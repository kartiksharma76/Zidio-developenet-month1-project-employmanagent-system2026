package com.Comp_Emp_Manage.employee_service.Repository;

import com.Comp_Emp_Manage.employee_service.Entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByEmployeeId(Long employeeId);
}
