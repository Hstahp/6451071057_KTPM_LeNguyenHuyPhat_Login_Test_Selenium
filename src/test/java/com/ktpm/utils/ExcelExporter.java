package com.ktpm.utils;

import com.ktpm.model.TestCaseResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tiện ích xuất kết quả kiểm thử tự động ra file Excel (.xlsx) chuyên nghiệp
 */
public class ExcelExporter {

    public static void exportResultsToExcel(List<TestCaseResult> results, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("KetQuaKiemThu");

            // --- 1. Tạo Fonts ---
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            Font passFont = workbook.createFont();
            passFont.setBold(true);
            passFont.setColor(IndexedColors.DARK_GREEN.getIndex());

            Font failFont = workbook.createFont();
            failFont.setBold(true);
            failFont.setColor(IndexedColors.RED.getIndex());

            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            // --- 2. Tạo Cell Styles ---
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(headerStyle);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(dataStyle);

            CellStyle centerDataStyle = workbook.createCellStyle();
            centerDataStyle.setAlignment(HorizontalAlignment.CENTER);
            centerDataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(centerDataStyle);

            CellStyle passStyle = workbook.createCellStyle();
            passStyle.setFont(passFont);
            passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            passStyle.setAlignment(HorizontalAlignment.CENTER);
            passStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(passStyle);

            CellStyle failStyle = workbook.createCellStyle();
            failStyle.setFont(failFont);
            failStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            failStyle.setAlignment(HorizontalAlignment.CENTER);
            failStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(failStyle);

            CellStyle summaryStyle = workbook.createCellStyle();
            summaryStyle.setFont(boldFont);
            summaryStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(summaryStyle);

            // --- 3. Ghi Tiêu đề báo cáo ---
            int rowIndex = 0;
            Row titleRow = sheet.createRow(rowIndex++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO KẾT QUẢ KIỂM THỬ TỰ ĐỘNG - E2E LOGIN TEST");
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            Row infoRow1 = sheet.createRow(rowIndex++);
            infoRow1.createCell(0).setCellValue("Hệ thống: Văn phòng điện tử UTC (vanphongdientu.utc.edu.vn)");

            Row infoRow2 = sheet.createRow(rowIndex++);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            infoRow2.createCell(0).setCellValue("Mã SV: 6451071057 | Thời gian thực hiện: " + currentTime);

            rowIndex++; // Dòng trống

            // --- 4. Ghi Headers của bảng ---
            String[] headers = {
                    "STT / Mã TC",
                    "Tên Kịch Bản Kiểm Thử",
                    "Dữ Liệu Đầu Vào",
                    "Kết Quả Mong Đợi",
                    "Kết Quả Thực Tế",
                    "Trạng Thái",
                    "Thời Gian Chạy"
            };

            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.setHeightInPoints(28);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- 5. Ghi dữ liệu từng Test Case ---
            int passCount = 0;
            int failCount = 0;

            for (TestCaseResult result : results) {
                Row row = sheet.createRow(rowIndex++);
                row.setHeightInPoints(22);

                Cell c0 = row.createCell(0);
                c0.setCellValue(result.getTestCaseId());
                c0.setCellStyle(centerDataStyle);

                Cell c1 = row.createCell(1);
                c1.setCellValue(result.getTestName());
                c1.setCellStyle(dataStyle);

                Cell c2 = row.createCell(2);
                c2.setCellValue(result.getInputData());
                c2.setCellStyle(dataStyle);

                Cell c3 = row.createCell(3);
                c3.setCellValue(result.getExpectedResult());
                c3.setCellStyle(dataStyle);

                Cell c4 = row.createCell(4);
                c4.setCellValue(result.getActualResult());
                c4.setCellStyle(dataStyle);

                Cell c5 = row.createCell(5);
                c5.setCellValue(result.getStatus());
                if ("PASS".equalsIgnoreCase(result.getStatus())) {
                    c5.setCellStyle(passStyle);
                    passCount++;
                } else {
                    c5.setCellStyle(failStyle);
                    failCount++;
                }

                Cell c6 = row.createCell(6);
                c6.setCellValue(result.getExecutionTime());
                c6.setCellStyle(centerDataStyle);
            }

            // --- 6. Ghi dòng Thống kê kết quả ---
            rowIndex++;
            Row summaryRow1 = sheet.createRow(rowIndex++);
            Cell s1 = summaryRow1.createCell(1);
            s1.setCellValue("TỔNG SỐ TEST CASE: " + results.size());
            s1.setCellStyle(summaryStyle);

            Row summaryRow2 = sheet.createRow(rowIndex++);
            Cell s2 = summaryRow2.createCell(1);
            s2.setCellValue(String.format("KẾT QUẢ: PASS = %d | FAIL = %d (Tỷ lệ đạt: %.1f%%)",
                    passCount, failCount, (results.isEmpty() ? 0 : (passCount * 100.0 / results.size()))));
            s2.setCellStyle(summaryStyle);

            // --- 7. Tự động điều chỉnh độ rộng cột ---
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int calcWidth = sheet.getColumnWidth(i) + 1200;
                int finalWidth = Math.min(Math.max(calcWidth, 4000), 250 * 256);
                sheet.setColumnWidth(i, finalWidth);
            }

            // Ghi file ra ổ đĩa
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("=================================================");
            System.out.println("✔ ĐÃ XUẤT THÀNH CÔNG BÁO CÁO EXCEL TẠI: " + filePath);
            System.out.println("=================================================");

        } catch (IOException e) {
            System.err.println("❌ Lỗi khi xuất file Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
