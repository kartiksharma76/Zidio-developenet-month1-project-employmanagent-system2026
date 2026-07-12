package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.dto.CloudinaryUploadResponse;
import com.Comp_Emp_Manage.entity.CloudinaryImage;
import com.Comp_Emp_Manage.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class exposing REST endpoints for uploading, updating, and deleting images from Cloudinary.
 * Follows a clean, layered architecture: Controller -> Service -> Configuration.
 */
@RestController
@RequiredArgsConstructor
public class CloudinaryImageController {

    private final CloudinaryService cloudinaryService;

    // =========================================================================
    // 1. General / Authenticated APIs (Requirement 7)
    // =========================================================================

    /**
     * Upload an image to Cloudinary (Authenticated, default scope is AUTHENTICATED).
     * POST /api/images/upload
     */
    @PostMapping("/api/images/upload")
    public ResponseEntity<CloudinaryUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImageWithScope(file, "AUTHENTICATED");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update/Overwrite an existing image using its publicId.
     * PUT /api/images/update/{publicId}
     * Supports nested paths in the publicId (e.g. employee_profiles/name) using wildcard pattern {*publicId}
     */
    @PutMapping("/api/images/update/{*publicId}")
    public ResponseEntity<CloudinaryUploadResponse> updateImage(
            @PathVariable("publicId") String publicId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.updateImage(publicId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an image from Cloudinary and database metadata by its publicId.
     * DELETE /api/images/delete/{publicId}
     * Supports nested paths in the publicId (e.g. employee_profiles/name) using wildcard pattern {*publicId}
     */
    @DeleteMapping("/api/images/delete/{*publicId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("publicId") String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // 2. Public APIs (saath me ek banana jo publickliy hr koi deekh sake)
    // =========================================================================

    /**
     * Public upload endpoint. Anyone can upload images without authentication.
     * POST /api/public/images/upload
     */
    @PostMapping("/api/public/images/upload")
    public ResponseEntity<CloudinaryUploadResponse> uploadPublicImage(@RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImageWithScope(file, "PUBLIC");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Public update/overwrite endpoint for publicly accessible images.
     * PUT /api/public/images/update/{publicId}
     */
    @PutMapping("/api/public/images/update/{*publicId}")
    public ResponseEntity<CloudinaryUploadResponse> updatePublicImage(
            @PathVariable("publicId") String publicId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryImage img = cloudinaryService.getImageByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Public image metadata not found"));
        if (!"PUBLIC".equals(img.getRoleScope())) {
            throw new IllegalArgumentException("Access Denied: The target image is not a public image.");
        }
        CloudinaryUploadResponse response = cloudinaryService.updateImage(publicId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Public deletion endpoint for publicly accessible images.
     * DELETE /api/public/images/delete/{publicId}
     */
    @DeleteMapping("/api/public/images/delete/{*publicId}")
    public ResponseEntity<Void> deletePublicImage(@PathVariable("publicId") String publicId) {
        CloudinaryImage img = cloudinaryService.getImageByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Public image metadata not found"));
        if (!"PUBLIC".equals(img.getRoleScope())) {
            throw new IllegalArgumentException("Access Denied: The target image is not a public image.");
        }
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all images with "PUBLIC" scope. Accessible to everyone.
     * GET /api/public/images
     */
    @GetMapping("/api/public/images")
    public ResponseEntity<List<CloudinaryImage>> getPublicImages() {
        return ResponseEntity.ok(cloudinaryService.getPublicImages());
    }

    /**
     * Retrieve a specific public image's metadata by its public ID. Accessible to everyone.
     * GET /api/public/images/{publicId}
     */
    @GetMapping("/api/public/images/{*publicId}")
    public ResponseEntity<CloudinaryImage> getPublicImageByPublicId(@PathVariable("publicId") String publicId) {
        CloudinaryImage img = cloudinaryService.getImageByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Public image not found"));
        if (!"PUBLIC".equals(img.getRoleScope())) {
            throw new IllegalArgumentException("Access Denied: The target image is not a public image.");
        }
        return ResponseEntity.ok(img);
    }

    // =========================================================================
    // 3. Employee Dedicated APIs (Restricted to ROLE_EMPLOYEE)
    // =========================================================================

    /**
     * Upload an image scoped for Employees.
     * POST /api/employee/images/upload
     */
    @PostMapping("/api/employee/images/upload")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<CloudinaryUploadResponse> uploadEmployeeImage(@RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImageWithScope(file, "EMPLOYEE");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update/overwrite an image scoped for Employees.
     * PUT /api/employee/images/update/{publicId}
     */
    @PutMapping("/api/employee/images/update/{*publicId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<CloudinaryUploadResponse> updateEmployeeImage(
            @PathVariable("publicId") String publicId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.updateImage(publicId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an image scoped for Employees.
     * DELETE /api/employee/images/delete/{publicId}
     */
    @DeleteMapping("/api/employee/images/delete/{*publicId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deleteEmployeeImage(@PathVariable("publicId") String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // 4. Manager/HR Dedicated APIs (Restricted to ROLE_MANAGER or ROLE_HR)
    // =========================================================================

    /**
     * Upload an image scoped for Managers.
     * POST /api/manager/images/upload
     */
    @PostMapping("/api/manager/images/upload")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<CloudinaryUploadResponse> uploadManagerImage(@RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImageWithScope(file, "MANAGER");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update/overwrite an image scoped for Managers.
     * PUT /api/manager/images/update/{publicId}
     */
    @PutMapping("/api/manager/images/update/{*publicId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<CloudinaryUploadResponse> updateManagerImage(
            @PathVariable("publicId") String publicId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.updateImage(publicId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an image scoped for Managers.
     * DELETE /api/manager/images/delete/{publicId}
     */
    @DeleteMapping("/api/manager/images/delete/{*publicId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR')")
    public ResponseEntity<Void> deleteManagerImage(@PathVariable("publicId") String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // 5. Admin Dedicated APIs (Restricted to ROLE_ADMIN)
    // =========================================================================

    /**
     * Upload an image scoped for Admins.
     * POST /api/admin/images/upload
     */
    @PostMapping("/api/admin/images/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudinaryUploadResponse> uploadAdminImage(@RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.uploadImageWithScope(file, "ADMIN");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update/overwrite an image scoped for Admins.
     * PUT /api/admin/images/update/{publicId}
     */
    @PutMapping("/api/admin/images/update/{*publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudinaryUploadResponse> updateAdminImage(
            @PathVariable("publicId") String publicId,
            @RequestParam("file") MultipartFile file) {
        CloudinaryUploadResponse response = cloudinaryService.updateImage(publicId, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an image scoped for Admins.
     * DELETE /api/admin/images/delete/{publicId}
     */
    @DeleteMapping("/api/admin/images/delete/{*publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdminImage(@PathVariable("publicId") String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.noContent().build();
    }
}
