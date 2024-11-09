package org.example.trackit.services.impl;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
@Service
@Slf4j
public class BackupService {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${backupPath}")
    private String backupPath;

    @Scheduled(cron = "0 0 2 * * ?")
    @PostConstruct
    public void backupDatabase() {
        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
        String path = backupPath + "db_backup_" + LocalDate.now() + ".backup";

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "pg_dump", "-U", dbUsername, "-h", "localhost", "-d", dbName, "-F", "c", "-f", path);
            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", dbPassword);

            Process process = pb.start();
            process.waitFor();
            log.info("Database backup completed successfully");
        } catch (Exception e) {
            log.error("Unsuccessful database backup: {}", e.getMessage());
        }
    }
}
