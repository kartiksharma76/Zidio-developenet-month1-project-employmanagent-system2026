package com.Comp_Emp_Manage.controller;
import com.Comp_Emp_Manage.entity.Employee;

import com.Comp_Emp_Manage.service.PayrollService;
import com.Comp_Emp_Manage.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.Comp_Emp_Manage.entity.Payroll;

@Controller
@RequestMapping("/payroll")
@RequiredArgsConstructor
@Slf4j
public class WebPayrollController {

    private final PayrollService payrollService;
    private final EmployeeRepository employeeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public String viewPayroll(Model model) {
        model.addAttribute("payrolls", payrollService.getAllPayrolls());
        model.addAttribute("employees", employeeRepository.findAll());
        return "payroll";
    }

    @PostMapping("/generate")
    public String generatePayroll(@RequestParam Long employeeId, 
                                  @RequestParam(defaultValue = "0.0") Double allowances,
                                  RedirectAttributes redirectAttributes) {
        try {
            payrollService.generatePayroll(employeeId, allowances);
            
            // Send Real-Time Notification
            messagingTemplate.convertAndSend("/topic/notifications", 
                "New payslip generated for Employee ID: " + employeeId);
                
            redirectAttributes.addFlashAttribute("success", "Payroll generated successfully!");
        } catch (Exception e) {
            log.error("Error generating payroll for employee {}: {}", employeeId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error generating payroll: " + e.getMessage());
        }
        return "redirect:/payroll";
    }

    @PostMapping("/pay/{id}")
    public String markAsPaid(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            payrollService.markAsPaid(id);
            redirectAttributes.addFlashAttribute("success", "Payroll marked as PAID!");
        } catch (Exception e) {
            log.error("Error marking payroll {} as paid: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error updating payroll: " + e.getMessage());
        }
        return "redirect:/payroll";
    }

    @GetMapping("/download/{id}")
    public void downloadPayslipPdf(@PathVariable Long id, jakarta.servlet.http.HttpServletResponse response) {
        try {
            Payroll payroll = payrollService.getAllPayrolls().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Payroll not found"));

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Payslip_" + payroll.getEmployee().getFirstName() + "_" + payroll.getId() + ".pdf\"");

            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();
            
            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18);
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("NexusHR Enterprise - Payslip", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new com.lowagie.text.Paragraph("\n"));
            document.add(new com.lowagie.text.Paragraph("Employee Name: " + payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName()));
            document.add(new com.lowagie.text.Paragraph("Department: " + payroll.getEmployee().getDepartment()));
            document.add(new com.lowagie.text.Paragraph("Payment Date: " + payroll.getPaymentDate()));
            document.add(new com.lowagie.text.Paragraph("\n"));
            
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
            table.setWidthPercentage(100);
            
            table.addCell("Earnings");
            table.addCell("Amount ($)");
            
            table.addCell("Basic Salary");
            table.addCell(String.valueOf(payroll.getBasicSalary()));
            
            table.addCell("Allowances");
            table.addCell(String.valueOf(payroll.getAllowances()));
            
            table.addCell("Deductions (PF/Tax)");
            table.addCell(String.valueOf(payroll.getDeductions() + payroll.getTax()));
            
            com.lowagie.text.pdf.PdfPCell netPayCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase("Net Salary"));
            netPayCell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            table.addCell(netPayCell);
            
            com.lowagie.text.pdf.PdfPCell netPayValCell = new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Phrase(String.valueOf(payroll.getNetSalary())));
            netPayValCell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            table.addCell(netPayValCell);
            
            document.add(table);
            document.close();
            
        } catch (Exception e) {
            log.error("Error generating PDF for payroll {}: {}", id, e.getMessage(), e);
        }
    }
}
