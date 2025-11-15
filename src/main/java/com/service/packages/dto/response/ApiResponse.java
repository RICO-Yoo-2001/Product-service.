package com.service.packages.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private int statusCode;
    private String statusMessage;
    private String description;
    private Object payload;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /** Success response without payload */
    public static ApiResponse success(HttpStatus httpStatus, String description) {
        return ApiResponse.builder()
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Success response with payload */
    public static ApiResponse success(HttpStatus httpStatus, String description, Object payload) {
        return ApiResponse.builder()
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .description(description)
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Error response */
    public static ApiResponse error(HttpStatus httpStatus, String description) {
        return ApiResponse.builder()
                .statusCode(httpStatus.value())
                .statusMessage(httpStatus.getReasonPhrase())
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
