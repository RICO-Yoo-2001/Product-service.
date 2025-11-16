package com.service.packages.service;

import com.service.packages.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    byte[] generateProductReportPDF(List<Product> products, LocalDateTime startDate, LocalDateTime endDate);
}

