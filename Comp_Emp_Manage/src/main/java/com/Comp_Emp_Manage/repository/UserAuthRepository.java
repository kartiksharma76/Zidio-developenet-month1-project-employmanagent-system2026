package com.Comp_Emp_Manage.repository;

import com.Comp_Emp_Manage.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByUserEmail(String userEmail);
    Boolean existsByUserEmail(String userEmail);

}