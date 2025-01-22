from flask import Flask, jsonify
import uiautomator2 as u2
from datetime import datetime
import re

app = Flask(__name__)

# 连接 Waydroid 设备
# 开发环境 
d = u2.connect("localhost:5555")
# 生产环境 localhost换为 waydroid session start后出现的ip
# d = u2.connect("192.168.240.112:5555")

@app.route('/get_price', methods=['GET'])
def get_price():
    try:
        # 获取当前界面 XML
        xml_str = d.dump_hierarchy()

        # print(xml_str)
        
        # 正则表达式提取价格，基于关键字段 "浙商实时金价"
        # pattern = r'<node.*?text="实时金价\(元/克\)".*?<node.*?text="(\d+\.\d{2})'
        pattern = r'text="实时金价(\d+\.\d+)元/克"'

        
        # 搜索匹配
        match = re.search(pattern, xml_str, re.DOTALL)
        
        if match:
            price = match.group(1)
            response = {
                "time": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                "price": price
            }
            return jsonify(response), 200
        else:
            return jsonify({"error": "未找到浙商实时金价"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
