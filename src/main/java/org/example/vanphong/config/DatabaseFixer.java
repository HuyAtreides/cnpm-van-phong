package org.example.vanphong.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Applying database fix for Review table...");
            // Attempt to modify the column to be AUTO_INCREMENT
            // Note: This relies on the column being named 'review_id' as per the Entity definition
            jdbcTemplate.execute("ALTER TABLE review MODIFY COLUMN review_id INT AUTO_INCREMENT");
            System.out.println("Database fix applied successfully.");
        } catch (Exception e) {
            System.out.println("Review fix skipped: " + e.getMessage());
        }
        
        try {
            System.out.println("Applying database fix for Voucher table...");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
            jdbcTemplate.execute("ALTER TABLE voucher MODIFY COLUMN voucher_id INT AUTO_INCREMENT");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
            System.out.println("Voucher fix applied successfully.");
        } catch (Exception e) {
             System.out.println("Voucher fix skipped/failed: " + e.getMessage());
             // Ensure FK checks are re-enabled even if fail
             try { jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1"); } catch (Exception ex) {}
        }
    }
}
