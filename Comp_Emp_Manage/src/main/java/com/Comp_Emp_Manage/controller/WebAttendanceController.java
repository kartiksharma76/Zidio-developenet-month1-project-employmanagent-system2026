package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.UserAuth;
import com.Comp_Emp_Manage.entity.Attendance;
import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class WebAttendanceController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private double[] parseVector(String vectorStr) {
        if (vectorStr == null || vectorStr.trim().isEmpty()) {
            return new double[0];
        }
        try {
            return objectMapper.readValue(vectorStr, double[].class);
        } catch (Exception e) {
            return new double[0];
        }
    }

    private double calculateEuclideanDistance(double[] v1, double[] v2) {
        if (v1.length != v2.length || v1.length == 0) {
            return Double.MAX_VALUE;
        }
        double sum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            double diff = v1[i] - v2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;

    @org.springframework.web.bind.annotation.GetMapping
    public String viewAttendance(Authentication authentication, org.springframework.ui.Model model) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);
        
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
                
        if (isAdminOrManager) {
            model.addAttribute("attendanceRecords", attendanceRepository.findAll());
        } else if (optEmployee.isPresent()) {
            model.addAttribute("attendanceRecords", attendanceRepository.findByEmployeeId(optEmployee.get().getId()));
        }
        
        if (optEmployee.isPresent()) {
            model.addAttribute("employee", optEmployee.get());
        }
        
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "attendance";
    }

    @PostMapping("/punch-in")
    public String punchIn(Authentication authentication, 
                          @RequestParam(value = "faceVector", required = false) String faceVector, 
                          RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            
            // If faceVector is provided, perform strict biometric verification using Euclidean Distance
            if (faceVector != null && !faceVector.trim().isEmpty()) {
                if (employee.getFaceVector() == null || !"APPROVED".equalsIgnoreCase(employee.getFaceStatus())) {
                    redirectAttributes.addFlashAttribute("error", "Biometric face profile not registered or approved.");
                    return "redirect:/attendance";
                }
                
                double[] newVector = parseVector(faceVector);
                double[] approvedVector = parseVector(employee.getFaceVector());
                
                if (newVector.length == 0 || approvedVector.length == 0) {
                    redirectAttributes.addFlashAttribute("error", "Invalid face data.");
                    return "redirect:/attendance";
                }
                
                double distance = calculateEuclideanDistance(newVector, approvedVector);
                if (distance > 0.50) {
                    redirectAttributes.addFlashAttribute("error", "Face verification confidence is below the threshold.");
                    return "redirect:/attendance";
                }
            }
            
            try {
                Attendance attendance = attendanceService.checkIn(employee.getId());
                redirectAttributes.addFlashAttribute("success", "Punched in successfully at " + attendance.getPunchInTimeFormatted());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee profile not found.");
        }
        return "redirect:/attendance";
    }

    @PostMapping("/punch-out")
    public String punchOut(Authentication authentication, 
                           @RequestParam(value = "faceVector", required = false) String faceVector, 
                           RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            
            // If faceVector is provided, perform strict biometric verification using Euclidean Distance
            if (faceVector != null && !faceVector.trim().isEmpty()) {
                if (employee.getFaceVector() == null || !"APPROVED".equalsIgnoreCase(employee.getFaceStatus())) {
                    redirectAttributes.addFlashAttribute("error", "Biometric face profile not registered or approved.");
                    return "redirect:/attendance";
                }
                
                double[] newVector = parseVector(faceVector);
                double[] approvedVector = parseVector(employee.getFaceVector());
                
                if (newVector.length == 0 || approvedVector.length == 0) {
                    redirectAttributes.addFlashAttribute("error", "Invalid face data.");
                    return "redirect:/attendance";
                }
                
                double distance = calculateEuclideanDistance(newVector, approvedVector);
                if (distance > 0.50) {
                    redirectAttributes.addFlashAttribute("error", "Face verification confidence is below the threshold.");
                    return "redirect:/attendance";
                }
            }
            
            try {
                Attendance attendance = attendanceService.checkOut(employee.getId());
                redirectAttributes.addFlashAttribute("success", "Punched out successfully at " + attendance.getPunchOutTimeFormatted());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee profile not found.");
        }
        return "redirect:/attendance";
    }

    @PostMapping("/register-face")
    public String registerFace(Authentication authentication, 
                               @RequestParam("facePhoto") String facePhoto, 
                               @RequestParam("faceVector") String faceVector, 
                               RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Employee> optEmployee = employeeRepository.findByEmail(email);

        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();

            // Check for duplicate face registration across other profiles using Euclidean Distance
            try {
                double[] newVector = parseVector(faceVector);
                if (newVector.length > 0) {
                    List<Employee> allEmployees = employeeRepository.findAll();
                    for (Employee other : allEmployees) {
                        if (other.getId().equals(employee.getId())) continue;
                        if (other.getFaceVector() != null && 
                            ("APPROVED".equalsIgnoreCase(other.getFaceStatus()) || "PENDING".equalsIgnoreCase(other.getFaceStatus()))) {
                            double[] otherVector = parseVector(other.getFaceVector());
                            if (otherVector.length == newVector.length) {
                                double distance = calculateEuclideanDistance(newVector, otherVector);
                                if (distance < 0.50) {
                                    redirectAttributes.addFlashAttribute("error", "This face is already registered with another account.");
                                    return "redirect:/attendance";
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // Fail-safe: log exception and proceed
            }

            employee.setFacePhoto(facePhoto);
            employee.setFaceVector(faceVector);
            employee.setFaceStatus("PENDING");
            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", "Face registered successfully! Pending admin approval.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found.");
        }
        return "redirect:/attendance";
    }

    @org.springframework.web.bind.annotation.GetMapping("/face-approvals")
    public String viewFaceApprovals(Authentication authentication, org.springframework.ui.Model model) {
        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            return "redirect:/dashboard";
        }

        List<Employee> pendingEmployees = employeeRepository.findAll().stream()
                .filter(e -> "PENDING".equalsIgnoreCase(e.getFaceStatus()))
                .collect(Collectors.toList());

        model.addAttribute("pendingEmployees", pendingEmployees);
        model.addAttribute("isAdminOrManager", isAdminOrManager);
        return "face_approvals";
    }

    @PostMapping("/face-approve")
    public String approveFace(@RequestParam("employeeId") Long employeeId, RedirectAttributes redirectAttributes) {
        Optional<Employee> optEmployee = employeeRepository.findById(employeeId);
        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();

            // Validate that the face vector is unique before approving using Euclidean Distance
            if (employee.getFaceVector() != null) {
                try {
                    double[] newVector = parseVector(employee.getFaceVector());
                    if (newVector.length > 0) {
                        List<Employee> allEmployees = employeeRepository.findAll();
                        for (Employee other : allEmployees) {
                            if (other.getId().equals(employee.getId())) continue;
                            if ("APPROVED".equalsIgnoreCase(other.getFaceStatus()) && other.getFaceVector() != null) {
                                double[] otherVector = parseVector(other.getFaceVector());
                                if (otherVector.length == newVector.length) {
                                    double distance = calculateEuclideanDistance(newVector, otherVector);
                                    if (distance < 0.50) {
                                        redirectAttributes.addFlashAttribute("error", "Cannot approve! This face is already registered with another account.");
                                        return "redirect:/attendance/face-approvals";
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignore and proceed
                }
            }

            employee.setFaceStatus("APPROVED");
            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", "Face registration approved for " + employee.getFirstName() + " " + employee.getLastName());
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found.");
        }
        return "redirect:/attendance/face-approvals";
    }

    @PostMapping("/face-reject")
    public String rejectFace(@RequestParam("employeeId") Long employeeId, RedirectAttributes redirectAttributes) {
        Optional<Employee> optEmployee = employeeRepository.findById(employeeId);
        if (optEmployee.isPresent()) {
            Employee employee = optEmployee.get();
            employee.setFaceStatus("REJECTED");
            employee.setFacePhoto(null);
            employee.setFaceVector(null);
            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("success", "Face registration rejected and cleared.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found.");
        }
        return "redirect:/attendance/face-approvals";
    }
}
