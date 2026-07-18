package com.Comp_Emp_Manage.scheduler;

import com.Comp_Emp_Manage.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceScheduler {

    private final AttendanceService attendanceService;

    // Runs every hour at the top of the hour
    @Scheduled(cron = "0 0 * * * *")
    public void autoPunchOut() {
        log.info("Running automatic punch out scheduler...");
        try {
            attendanceService.autoPunchOutPreviousDays();
            log.info("Automatic punch out scheduler completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during automatic punch out scheduler", e);
        }
    }
}
