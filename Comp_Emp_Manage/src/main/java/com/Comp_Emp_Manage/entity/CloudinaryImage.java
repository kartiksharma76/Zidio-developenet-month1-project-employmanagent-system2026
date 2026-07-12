package com.Comp_Emp_Manage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity class mapping to the database table for storing Cloudinary upload details.
 * Contains imageUrl, publicId, originalFilename, uploader's username, and role scope.
 */
@Entity
@Table(name = "cloudinary_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudinaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String publicId;

    private String originalFilename;

    private String uploadedByUsername;

    private String roleScope; // e.g., EMPLOYEE, MANAGER, ADMIN, PUBLIC

    private String resourceType; // e.g., image, raw

    private LocalDateTime uploadTime;

    @PrePersist
    protected void onCreate() {
        uploadTime = LocalDateTime.now();
    }
}
