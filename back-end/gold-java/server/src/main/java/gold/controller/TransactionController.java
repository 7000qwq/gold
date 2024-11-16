package gold.controller;

import gold.context.BaseContext;
import gold.dto.TransactionAddDTO;
import gold.dto.TransactionModifyDTO;
import gold.dto.TransactionPageQueryDTO;
import gold.entity.Transaction;
import gold.result.PageResult;
import gold.result.Result;
import gold.service.TransactionService;
import gold.vo.TransactionQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Result insert(@RequestBody TransactionAddDTO transactionAddDTO){

        transactionAddDTO.setUserId(BaseContext.getCurrentId());
        transactionService.insert(transactionAddDTO);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody TransactionModifyDTO transactionModifyDTO){

        transactionService.update(transactionModifyDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TransactionQueryVO> getTransById(@PathVariable Long id){

        Transaction transaction = transactionService.getTransactionById(id);
        TransactionQueryVO transactionQueryVO = new TransactionQueryVO();
        BeanUtils.copyProperties(transaction, transactionQueryVO);
        return Result.success(transactionQueryVO);
    }

    @DeleteMapping("/{id}")
    public Result deleteTransById(@PathVariable Long id){

        transactionService.delete(id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(TransactionPageQueryDTO transactionPageQueryDTO){

        if (transactionPageQueryDTO.getStringBeginTime() != "") {
            transactionPageQueryDTO.setBeginTime(LocalDateTime.parse(transactionPageQueryDTO.getStringBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
        if (transactionPageQueryDTO.getStringEndTime() != "") {
            transactionPageQueryDTO.setEndTime(LocalDateTime.parse(transactionPageQueryDTO.getStringEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }


        transactionPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageResult pageResult = transactionService.page(transactionPageQueryDTO);
        return Result.success(pageResult);
    }

    // todo
    @GetMapping("/downloadExcel")
    public void downloadExcel(HttpServletResponse response) throws IOException {

        Long userID = BaseContext.getCurrentId();

        // 创建一个新的 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transaction History");

        // 示例数据，可以用数据库数据替换
        List<Transaction> data = transactionService.getExcelByUserId(userID);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Time", "Type", "Gold Price", "Amount", "Weight", "Commission", "Note"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // 填充表格数据
        int rowNum = 1;
        for (Transaction rowData : data) {
            Row row = sheet.createRow(rowNum++);

            // 填充每列数据
            row.createCell(0).setCellValue(
                    rowData.getTime() != null ? rowData.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : ""
            );

            row.createCell(1).setCellValue(
                    rowData.getType() != null ? (rowData.getType() == 0 ? "Buy" : "Sell") : ""
            );

            row.createCell(2).setCellValue(
                    rowData.getGoldPrice() != null ? rowData.getGoldPrice().toPlainString() : ""
            );

            row.createCell(3).setCellValue(
                    rowData.getAmount() != null ? rowData.getAmount().toPlainString() : ""
            );

            row.createCell(4).setCellValue(
                    rowData.getWeight() != null ? rowData.getWeight().toPlainString() : ""
            );

            row.createCell(5).setCellValue(
                    rowData.getCommission() != null ? rowData.getCommission().toPlainString() : ""
            );

            row.createCell(6).setCellValue(
                    rowData.getNote() != null ? rowData.getNote() : ""
            );
        }

        // 设置 HTTP 响应头，准备下载文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Transaction_History.xlsx");

        // 将工作簿写入响应输出流
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
