package com.Comp_Emp_Manage.controller;

import com.Comp_Emp_Manage.entity.Employee;
import com.Comp_Emp_Manage.entity.Attendance;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import com.Comp_Emp_Manage.repository.AttendanceRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class WebReportController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    @Data
    @Builder
    public static class EmployeeReportDto {
        private Long id;
        private String name;
        private String department;
        private String designation;
        private long presentCount;
        private long absentCount;
        private double totalHours;
        private String formattedTotalHours;
        private double averageHours;
        private String formattedAverageHours;
        private double attendancePercentage;
        private List<Attendance> attendanceDetails;
    }

    private List<EmployeeReportDto> generateReportData(LocalDate start, LocalDate end, String dept) {
        List<Employee> employees = employeeRepository.findAll();
        if (dept != null && !dept.trim().isEmpty() && !"ALL".equalsIgnoreCase(dept)) {
            employees = employees.stream()
                    .filter(e -> dept.equalsIgnoreCase(e.getDepartment()))
                    .collect(Collectors.toList());
        }

        List<EmployeeReportDto> report = new ArrayList<>();
        for (Employee emp : employees) {
            List<Attendance> attendanceRecords = attendanceRepository.findByEmployeeId(emp.getId()).stream()
                    .filter(a -> !a.getDate().isBefore(start) && !a.getDate().isAfter(end))
                    .collect(Collectors.toList());

            long present = attendanceRecords.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                    .count();

            long absent = attendanceRecords.stream()
                    .filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus()))
                    .count();

            double totalHours = attendanceRecords.stream()
                    .mapToDouble(a -> a.getWorkingHours() != null ? a.getWorkingHours() : 0.0)
                    .sum();

            double avgHours = present > 0 ? (totalHours / present) : 0.0;
            double attendancePct = (present + absent) > 0 ? ((double) present * 100.0 / (present + absent)) : 0.0;

            long hours = (long) totalHours;
            long minutes = Math.round((totalHours - hours) * 60);
            String formattedTotal = String.format("%dh %dm", hours, minutes);

            long avgH = (long) avgHours;
            long avgM = Math.round((avgHours - avgH) * 60);
            String formattedAvg = String.format("%dh %dm", avgH, avgM);

            report.add(EmployeeReportDto.builder()
                    .id(emp.getId())
                    .name(emp.getFirstName() + " " + emp.getLastName())
                    .department(emp.getDepartment() != null ? emp.getDepartment() : "N/A")
                    .designation(emp.getDesignation() != null ? emp.getDesignation() : "N/A")
                    .presentCount(present)
                    .absentCount(absent)
                    .totalHours(totalHours)
                    .formattedTotalHours(formattedTotal)
                    .averageHours(avgHours)
                    .formattedAverageHours(formattedAvg)
                    .attendancePercentage(attendancePct)
                    .attendanceDetails(attendanceRecords)
                    .build());
        }
        return report;
    }

    @GetMapping
    public String viewReports(
            Authentication authentication,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "department", required = false) String department,
            Model model) {

        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            return "redirect:/dashboard";
        }

        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1); // Start of current month
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (department == null) {
            department = "ALL";
        }

        List<EmployeeReportDto> reportData = generateReportData(startDate, endDate, department);
        
        // Get unique departments for filter dropdown
        List<String> departments = employeeRepository.findAll().stream()
                .map(Employee::getDepartment)
                .filter(d -> d != null && !d.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("reportData", reportData);
        model.addAttribute("departments", departments);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedDept", department);
        model.addAttribute("isAdminOrManager", isAdminOrManager);

        return "reports";
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(
            Authentication authentication,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "department", required = false) String department,
            HttpServletResponse response) throws IOException {

        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (department == null) {
            department = "ALL";
        }

        List<EmployeeReportDto> reportData = generateReportData(startDate, endDate, department);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Work_Report_" + startDate + "_to_" + endDate + ".pdf\"");

        try {
            com.lowagie.text.Document document = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4.rotate());
            com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("NexusHR Enterprise - Employee Work Report", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);

            com.lowagie.text.Font subtitleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 11);
            com.lowagie.text.Paragraph subtitle = new com.lowagie.text.Paragraph(
                    "Period: " + startDate + " to " + endDate + "   |   Department: " + department, subtitleFont);
            subtitle.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new com.lowagie.text.Paragraph("\n"));

            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.5f, 2f, 2f, 1.2f, 1.2f, 1.8f, 1.8f, 1.5f});

            // Set Headers
            String[] headers = {"Employee Name", "Department", "Designation", "Present", "Absent", "Total Hours", "Avg Hours/Day", "Attendance %"};
            for (String header : headers) {
                com.lowagie.text.pdf.PdfPCell headerCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(header, com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 10)));
                headerCell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                headerCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (EmployeeReportDto dto : reportData) {
                table.addCell(dto.getName());
                table.addCell(dto.getDepartment());
                table.addCell(dto.getDesignation());
                
                com.lowagie.text.pdf.PdfPCell c1 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(String.valueOf(dto.getPresentCount())));
                c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(c1);

                com.lowagie.text.pdf.PdfPCell c2 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(String.valueOf(dto.getAbsentCount())));
                c2.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(c2);

                com.lowagie.text.pdf.PdfPCell c3 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(dto.getFormattedTotalHours()));
                c3.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(c3);

                com.lowagie.text.pdf.PdfPCell c4 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(dto.getFormattedAverageHours()));
                c4.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(c4);

                com.lowagie.text.pdf.PdfPCell c5 = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(String.format("%.1f%%", dto.getAttendancePercentage())));
                c5.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                table.addCell(c5);
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            log.error("Error generating report PDF", e);
        }
    }

    @GetMapping("/export/csv")
    public void exportToCsv(
            Authentication authentication,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "department", required = false) String department,
            HttpServletResponse response) throws IOException {

        boolean isAdminOrManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));

        if (!isAdminOrManager) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (department == null) {
            department = "ALL";
        }

        List<EmployeeReportDto> reportData = generateReportData(startDate, endDate, department);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"Work_Report_" + startDate + "_to_" + endDate + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Employee Name,Department,Designation,Present Days,Absent Days,Total Hours Worked,Avg Hours Per Day,Attendance %");

        for (EmployeeReportDto dto : reportData) {
            writer.println(String.format("\"%s\",\"%s\",\"%s\",%d,%d,\"%s\",\"%s\",%.1f%%",
                    dto.getName().replace("\"", "\"\""),
                    dto.getDepartment().replace("\"", "\"\""),
                    dto.getDesignation().replace("\"", "\"\""),
                    dto.getPresentCount(),
                    dto.getAbsentCount(),
                    dto.getFormattedTotalHours(),
                    dto.getFormattedAverageHours(),
                    dto.getAttendancePercentage()
            ));
        }
        writer.flush();
    }
}
