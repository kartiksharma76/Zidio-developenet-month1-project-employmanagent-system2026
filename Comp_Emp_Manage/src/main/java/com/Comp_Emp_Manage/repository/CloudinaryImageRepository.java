package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.CloudinaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing CloudinaryImage persistence operations.
 */
@Repository
public interface CloudinaryImageRepository extends JpaRepository<CloudinaryImage, Long> {
    
    /**
     * Find an image record by its unique Cloudinary public ID.
     * @param publicId Cloudinary public ID
     * @return Optional containing the record if found
     */
    Optional<CloudinaryImage> findByPublicId(String publicId);

    /**
     * Retrieve all image records uploaded by a specific user.
     * @param username Email or username of the uploader
     * @return List of image records
     */
    List<CloudinaryImage> findByUploadedByUsername(String username);

    /**
     * Retrieve all image records matching a specific role scope (e.g., PUBLIC).
     * @param roleScope The target role scope
     * @return List of image records
     */
    List<CloudinaryImage> findByRoleScope(String roleScope);

    /**
     * Retrieve all records where role scope starts with a specific prefix.
     * @param prefix Prefix (e.g., "DOCUMENT_")
     * @return List of matched records
     */
    List<CloudinaryImage> findByRoleScopeStartingWith(String prefix);
}
