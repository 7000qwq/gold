from flask import Flask, request, jsonify, send_file, send_from_directory
import io
import uuid
import openpyxl


app = Flask(__name__)

# 简单的缓存，存储 { token: BytesIO对象 }
generated_files = {}


@app.route('/')
def index():
    return send_from_directory('.', 'frontend.html')

@app.route('/<path:path>')
def static_files(path):
    return send_from_directory('.', path)

import pymysql
# 创建数据库连接
connection = pymysql.connect(
    host='localhost',
    port=3306,            # MySQL 默认端口
    user='root',
    password='11Hope17!',
    database='gold_emulator',
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)


@app.route('/computeAndDownload', methods=['POST'])
def compute_and_download():
    """
    接收前端传来的 JSON 数据：
    {
      "startTime": "2022-05-01 23:22:45",
      "endTime": "2022-05-01 23:22:45",
      "amount": 100000,
      "singleAmount": 1000,
      "difference": 10,
      "commission": 0.3
    }
    返回：
    {
      "code": 200,
      "income": 1234.56,
      "downloadUrl": "/downloadHistory?token=xxxxxx"
    }
    """

    # 1. 获取请求参数
    data = request.get_json()
    if not data:
        return jsonify({"code": 400, "message": "Bad Request: no JSON data"}), 400

    start_time = data.get("startTime")
    end_time = data.get("endTime")
    amount = data.get("amount")
    single_amount = data.get("singleAmount")
    difference = data.get("difference")
    commission = data.get("commission")

    print(start_time, end_time, amount, single_amount, difference, commission)
    # 2. 校验参数 (此处仅做简单示例)
    if not (start_time and end_time and amount and single_amount and difference and commission is not None):
        return jsonify({"code": 400, "message": "Missing required parameters"}), 400

    # 2. 从数据库查询历史金价（假设minute_price表中有字段time和price）
    try:
        with connection.cursor() as cursor:
            sql = "SELECT time, gold_price FROM minute_price WHERE time >= %s AND time <= %s ORDER BY time ASC"
            cursor.execute(sql, (start_time, end_time))
            minute_data = cursor.fetchall()
    except Exception as e:
        return jsonify({"code": 500, "message": "Database query error", "error": str(e)}), 500

    # 3. 初始化模拟参数
    principal = float(amount)  # 初始本金
    open_positions = {}  # 持仓记录：键为整数价格，如390，值为列表，每个元素为{'price': x, 'time': ..., 'note': ...}
    transaction_records = []  # 记录格式：[time, type, price, quantity, amount, note, principal, commission]
    final_price = None  # 用于记录最后的金价

    for row in minute_data:
        current_time = row['time']
        # 当前金价，保留两位小数
        x = round(float(row['gold_price']), 2)
        final_price = x  # 每次更新，循环结束时即为最终金价
        current_int = int(x)
        # 目标价格为当前整数部分减去差价
        x_diff_key = current_int - int(difference)

        # 从持仓记录中取出当前价位和目标价位的所有持仓（以整数价格为key）
        pos_at_x = open_positions.get(current_int, [])
        pos_at_x_diff = open_positions.get(x_diff_key, [])

        # 情况1：如果同时存在当前价位和目标价位的持仓，则对目标价位所有持仓进行卖出
        if pos_at_x and pos_at_x_diff:
            for holding in pos_at_x_diff:
                weight = holding.get('weight', 0)
                sale_price = x
                sale_value = weight * sale_price
                commission_fee = sale_value * (commission / 100.0)
                net_sale = sale_value - commission_fee
                principal += net_sale
                transaction_records.append([
                    current_time, "SELL", sale_price, weight, sale_value,
                    "sold", principal, commission_fee
                ])
            # 卖出后清空目标价位的所有持仓
            open_positions.pop(x_diff_key, None)

        # 情况2：如果当前价位没有持仓，但目标价位有持仓，则对目标价位所有持仓进行“卖出并买入”
        elif (not pos_at_x) and pos_at_x_diff:
            for holding in pos_at_x_diff:
                weight = holding.get('weight', 0)
                sale_price = x
                sale_value = weight * sale_price
                # 根据描述，此处卖出记录不收手续费
                principal += sale_value
                transaction_records.append([
                    current_time, "SELL", sale_price, weight, sale_value,
                    "soldAndBuy", principal, 0
                ])
                open_positions.setdefault(current_int, []).append({
                    "price": x, "weight": weight
                })
                principal -= sale_value
                transaction_records.append([
                    current_time, "BUY", sale_price, weight, sale_value,
                    "soldAndBuy", principal, 0
                ])
            # 处理完后清除目标价位的持仓
            open_positions.pop(x_diff_key, None)

        # 情况3：如果当前价位有持仓但目标价位没有持仓，则不操作
        elif pos_at_x and (not pos_at_x_diff):
            pass

        # 情况4：如果两个价位都没有持仓，则如果本金足够，则买入一笔
        elif (not pos_at_x) and (not pos_at_x_diff):
            if principal >= single_amount:
                principal -= single_amount
                weight_new = single_amount / x
                open_positions.setdefault(current_int, []).append({
                    "price": x, "weight": weight_new
                })
                transaction_records.append([
                    current_time, "BUY", x, weight_new, single_amount,
                    "bought", principal, 0
                ])

    # 模拟结束后，计算最终收益：剩余本金 + 所有未平仓持仓的市值
    total_position_value = 0
    if final_price is None:
        final_price = 0
    for pos_list in open_positions.values():
        for holding in pos_list:
            weight = holding.get('weight', 0)
            total_position_value += weight * final_price

    income = principal + total_position_value - amount

    # 4. 生成 XLSX 文件，写入交易记录（包含新字段principal和commission）
    wb = openpyxl.Workbook()
    ws = wb.active
    ws.title = "Transaction History"
    # 写表头
    ws.append(["Time", "Type", "Price", "Quantity", "Amount", "Note", "Principal", "Commission"])
    for record in transaction_records:
        ws.append(record)


    # 5. 将 XLSX 文件保存到内存（BytesIO）
    xlsx_data = io.BytesIO()
    wb.save(xlsx_data)
    xlsx_data.seek(0)  # 指针回到起始位置

    # 6. 生成一个唯一 token 并把内存数据缓存起来
    token = str(uuid.uuid4())
    generated_files[token] = xlsx_data

    # 7. 返回 JSON，包括 income 和下载链接
    download_url = f"/downloadHistory?token={token}"
    response = {
        "code": 200,
        "income": income,
        "downloadUrl": download_url
    }
    return jsonify(response)

@app.route('/downloadHistory', methods=['GET'])
def download_history():
    """
    接收 token 参数，查找对应的 XLSX 文件并返回给用户下载
    """
    token = request.args.get("token")
    if not token or token not in generated_files:
        return jsonify({"code": 404, "message": "File not found"}), 404

    # 取出内存中的 XLSX 数据
    xlsx_data = generated_files[token]

    # 也可以选择下载后删除缓存，防止堆积：
    del generated_files[token]

    # 发送文件
    # attachment_filename 适配 Flask < 2.2；Flask >= 2.2 用 download_name 替换
    return send_file(
        xlsx_data,
        as_attachment=True,
        download_name="transaction_history.xlsx",
        mimetype="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    )

if __name__ == '__main__':
    app.run(debug=True)
