package com.service.packages.service.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.service.packages.entity.Product;
import com.service.packages.service.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Log4j2
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public byte[] generateProductReportPDF(List<Product> products, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating PDF report for {} products", products.size());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph("Product Report")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Add date range
            Paragraph dateRange = new Paragraph(
                    String.format("Period: %s to %s",
                            startDate != null ? startDate.format(DATE_FORMATTER) : "All Time",
                            endDate != null ? endDate.format(DATE_FORMATTER) : "All Time"))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(dateRange);

            // Add summary
            Paragraph summary = new Paragraph(
                    String.format("Total Products: %d", products.size()))
                    .setFontSize(12)
                    .setMarginBottom(20);
            document.add(summary);

            // Create table
            float[] columnWidths = {1, 2, 2, 3, 2, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            // Add header row
            addTableHeader(table, "ID");
            addTableHeader(table, "Product Code");
            addTableHeader(table, "Product Name");
            addTableHeader(table, "Description");
            addTableHeader(table, "Cost");
            addTableHeader(table, "Status");
            addTableHeader(table, "Created At");

            // Add data rows
            BigDecimal totalCost = BigDecimal.ZERO;
            for (Product product : products) {
                table.addCell(createCell(String.valueOf(product.getProdId())));
                table.addCell(createCell(product.getProdCode()));
                table.addCell(createCell(product.getProdName()));
                table.addCell(createCell(product.getProdDescription() != null ? 
                        (product.getProdDescription().length() > 50 ? 
                                product.getProdDescription().substring(0, 50) + "..." : 
                                product.getProdDescription()) : ""));
                table.addCell(createCell(product.getProdCost() != null ? 
                        product.getProdCost().toString() : "0.00"));
                table.addCell(createCell(product.getStatus()));
                table.addCell(createCell(product.getCreatedAt() != null ? 
                        product.getCreatedAt().format(DATE_FORMATTER) : ""));

                if (product.getProdCost() != null) {
                    totalCost = totalCost.add(product.getProdCost());
                }
            }

            document.add(table);

            // Add total cost
            Paragraph total = new Paragraph(
                    String.format("Total Cost: %s", totalCost.toString()))
                    .setFontSize(12)
                    .setBold()
                    .setMarginTop(20);
            document.add(total);

            // Add footer
            Paragraph footer = new Paragraph(
                    String.format("Report Generated: %s", LocalDateTime.now().format(DATE_FORMATTER)))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(footer);

            document.close();

            log.info("PDF report generated successfully. Size: {} bytes", baos.size());
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error generating PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addTableHeader(Table table, String headerText) {
        Cell headerCell = new Cell()
                .add(new Paragraph(headerText))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(headerCell);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text != null ? text : ""))
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT);
    }
}

