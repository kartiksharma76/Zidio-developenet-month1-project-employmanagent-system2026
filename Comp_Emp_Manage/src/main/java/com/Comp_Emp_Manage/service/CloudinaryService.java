package com.Comp_Emp_Manage.service;

import com.Comp_Emp_Manage.dto.CloudinaryUploadResponse;
import com.Comp_Emp_Manage.entity.CloudinaryImage;
import com.Comp_Emp_Manage.repository.CloudinaryImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class handling upload, deletion, and updating of images from Cloudinary.
 * Also synchronizes the uploaded image metadata with the MySQL database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final CloudinaryImageRepository imageRepository;

    // Allowed content types: images, PDFs, Word docs
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    // Maximum allowed file size in bytes (5 MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * Validates file size and file format before uploading to Cloudinary.
     * Throws IllegalArgumentException if validation checks fail.
     *
     * @param file The multipart image file to validate
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty and cannot be uploaded");
        }

        // Validate file size limit
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of 5 MB");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Allowed formats: JPG, JPEG, PNG, WEBP, PDF, DOC, DOCX");
        }

        // Secondary validation based on file extension
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "pdf", "doc", "docx");
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("Invalid file extension. Allowed formats: JPG, JPEG, PNG, WEBP, PDF, DOC, DOCX");
            }
        }
    }

    /**
     * Public upload endpoint mapping to requirement #4: uploadImage(MultipartFile file).
     * Automatically assigns a "PUBLIC" scope by default.
     *
     * @param file The multipart file to upload
     * @return Upload response containing imageUrl, publicId, and originalFilename
     */
    @Transactional
    public CloudinaryUploadResponse uploadImage(MultipartFile file) {
        return uploadImageWithScope(file, "PUBLIC");
    }

    /**
     * Uploads the image to Cloudinary and saves its reference in MySQL under the specified role scope.
     *
     * @param file The multipart file to upload
     * @param roleScope Scope of access (EMPLOYEE, MANAGER, ADMIN, PUBLIC, or AUTHENTICATED)
     * @return Upload response containing imageUrl, publicId, and originalFilename
     */
    @Transactional
    public CloudinaryUploadResponse uploadImageWithScope(MultipartFile file, String roleScope) {
        // Validate image constraints
        validateFile(file);

        // Fetch currently authenticated username/email
        String currentUsername = getCurrentUsername();

        try {
            // Define upload properties
            Map<?, ?> uploadParams = ObjectUtils.asMap(
                    "folder", "employee_profiles",
                    "use_filename", true,
                    "unique_filename", true,
                    "resource_type", "auto" // Auto-detect resource type (image, raw, etc.)
            );

            log.info("Uploading file to Cloudinary folder 'employee_profiles' for user: {}, scope: {}", currentUsername, roleScope);
            
            // Execute Cloudinary upload
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            String originalFilename = file.getOriginalFilename();
            String resourceType = (String) uploadResult.get("resource_type");

            // Save reference in MySQL database
            CloudinaryImage cloudinaryImage = CloudinaryImage.builder()
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .originalFilename(originalFilename)
                    .uploadedByUsername(currentUsername)
                    .roleScope(roleScope)
                    .resourceType(resourceType)
                    .build();

            imageRepository.save(cloudinaryImage);
            log.info("Saved file details to database. Public ID: {}, Resource Type: {}", publicId, resourceType);

            return new CloudinaryUploadResponse(imageUrl, publicId, originalFilename);

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new RuntimeException("Image upload failed due to I/O error: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes the image from Cloudinary using publicId and removes its database entry in MySQL.
     * Mapping to requirement #4: deleteImage(String publicId).
     *
     * @param publicId Cloudinary public ID of the image to delete
     */
    @Transactional
    public void deleteImage(String publicId) {
        // Find existing record in the database
        CloudinaryImage existingImage = imageRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Image metadata not found in database for publicId: " + publicId));

        try {
            String resourceType = existingImage.getResourceType() != null ? existingImage.getResourceType() : "image";
            log.info("Deleting file from Cloudinary (type: {}) for publicId: {}", resourceType, publicId);
            
            // Delete from Cloudinary
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", resourceType));
            String result = (String) deleteResult.get("result");
            
            log.info("Cloudinary destroy result: {}", result);

            // Even if the image is already deleted on Cloudinary, we should remove the record from our DB
            imageRepository.delete(existingImage);
            log.info("Deleted file metadata from database. Public ID: {}", publicId);

        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary", e);
            throw new RuntimeException("Image deletion failed due to I/O error: " + e.getMessage(), e);
        }
    }

    /**
     * Overwrites an existing image in Cloudinary and updates its database record.
     * Mapping to requirement #4: updateImage(String publicId, MultipartFile file).
     *
     * @param publicId The Cloudinary public ID of the image to replace
     * @param file The new image multipart file
     * @return Updated upload response containing new imageUrl, publicId, and originalFilename
     */
    @Transactional
    public CloudinaryUploadResponse updateImage(String publicId, MultipartFile file) {
        // Validate constraints
        validateFile(file);

        // Retrieve existing database metadata
        CloudinaryImage existingImage = imageRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Image metadata not found in database for publicId: " + publicId));

        try {
            // Re-upload using the same publicId (which overwrites the existing file on Cloudinary)
            Map<?, ?> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true,
                    "invalidate", true, // Invalidates CDN cached copies
                    "resource_type", "auto" // Auto-detect resource type
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            String imageUrl = (String) uploadResult.get("secure_url");
            String originalFilename = file.getOriginalFilename();
            String resourceType = (String) uploadResult.get("resource_type");

            // Update local database record
            existingImage.setImageUrl(imageUrl);
            existingImage.setOriginalFilename(originalFilename);
            existingImage.setResourceType(resourceType);
            existingImage.setUploadTime(java.time.LocalDateTime.now());
            imageRepository.save(existingImage);
            
            log.info("Successfully updated file metadata in database for publicId: {}", publicId);

            return new CloudinaryUploadResponse(imageUrl, publicId, originalFilename);

        } catch (IOException e) {
            log.error("Failed to update image on Cloudinary", e);
            throw new RuntimeException("Image update failed due to I/O error: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches all publicly scoped images.
     *
     * @return List of public images
     */
    public List<CloudinaryImage> getPublicImages() {
        return imageRepository.findByRoleScope("PUBLIC");
    }

    /**
     * Fetches details of an image by its publicId.
     *
     * @param publicId Public ID to search
     * @return Optional containing the record if found
     */
    public Optional<CloudinaryImage> getImageByPublicId(String publicId) {
        return imageRepository.findByPublicId(publicId);
    }

    /**
     * Helper method to extract the username/email of the current authenticated user.
     * Falls back to "anonymousUser" if no authentication context exists.
     *
     * @return Authenticated user email/username
     */
    private String getCurrentUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "anonymousUser";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            return principal.toString();
        }
        return "anonymousUser";
    }
}
