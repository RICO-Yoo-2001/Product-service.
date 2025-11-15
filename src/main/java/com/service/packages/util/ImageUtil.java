package com.service.packages.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Log4j2
public class ImageUtil {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    };

    /**
     * Converts MultipartFile to Base64 encoded string
     * @param file The image file to convert
     * @return Base64 encoded string
     * @throws IOException if file reading fails
     * @throws IllegalArgumentException if file is invalid
     */
    public static String convertToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // Validate file size
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image size exceeds maximum allowed size of 5MB");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new IllegalArgumentException("Invalid image format. Allowed formats: JPEG, PNG, GIF, WEBP");
        }

        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            log.debug("Image converted to Base64 successfully. Size: {} bytes", imageBytes.length);
            return base64Image;
        } catch (IOException e) {
            log.error("Error converting image to Base64: {}", e.getMessage());
            throw new IOException("Failed to process image file", e);
        }
    }

    /**
     * Validates if the content type is allowed
     */
    private static boolean isAllowedContentType(String contentType) {
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates image file
     */
    public static void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Image size exceeds maximum allowed size of 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new IllegalArgumentException("Invalid image format. Allowed formats: JPEG, PNG, GIF, WEBP");
        }
    }
}

