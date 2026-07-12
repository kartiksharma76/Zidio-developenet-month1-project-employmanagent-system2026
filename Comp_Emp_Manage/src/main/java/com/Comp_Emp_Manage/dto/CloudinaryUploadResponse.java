package com.Comp_Emp_Manage.dto;

/**
 * Data Transfer Object representing the details of a successful image upload to Cloudinary.
 * Designed as a Java 21 Record for clean, concise, and immutable data modeling.
 *
 * @param imageUrl The secure URL for retrieving the uploaded image
 * @param publicId The unique identifier of the image in Cloudinary
 * @param originalFilename The original filename of the uploaded image
 */
public record CloudinaryUploadResponse(
    String imageUrl,
    String publicId,
    String originalFilename
) {}
