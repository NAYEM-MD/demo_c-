package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class SchemaLoader {

    public static void main(String[] args) {
        System.out.println("Initializing Database Schema...");
        String schemaPath = "docs/schema.sql";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(schemaPath))) {

            System.out.println("Connected to: " + conn.getMetaData().getURL());

            StringBuilder sql = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null) {
                // Trim line
                String trimmedLine = line.trim();
                
                // Skip comments and empty lines
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                    continue;
                }

                // Append line to buffer (preserve newlines for readability/formatting)
                sql.append(line).append("\n");

                // Check for execution triggers
                boolean execute = false;
                
                // Trigger 1: Line is exactly "/" (PL/SQL block terminator)
                if (trimmedLine.equals("/")) {
                    // Remove the slash from the SQL to be executed
                    sql.setLength(sql.length() - line.length() - 1); // Remove last line + newline
                    execute = true;
                }
                // Trigger 2: Line ends with ";" AND we are not in a BEGIN block
                else if (trimmedLine.endsWith(";") && !sql.toString().toUpperCase().trim().startsWith("BEGIN")) {
                    // Remove the semicolon for JDBC execution
                    sql.setLength(sql.length() - 1); // Remove trailing newline (added above)
                    // We need to remove the semicolon from the buffer.
                    // The buffer currently has "stmt;\n". 
                    // We want "stmt".
                    String currentSql = sql.toString().trim();
                    if (currentSql.endsWith(";")) {
                        sql = new StringBuilder(currentSql.substring(0, currentSql.length() - 1));
                    }
                    execute = true;
                }

                if (execute) {
                    String query = sql.toString().trim();
                    if (!query.isEmpty()) {
                        System.out.println("Executing SQL...");
                        try {
                            stmt.execute(query);
                            System.out.println("✅ Success");
                        } catch (SQLException ex) {
                            System.err.println("❌ Error: " + ex.getMessage());
                            // Don't stop on drop errors (which we handle via PL/SQL anyway, but just in case)
                            // For CREATE statements, we might want to stop or log.
                        }
                    }
                    sql.setLength(0); // Clear buffer
                }
            }
            
            System.out.println("Schema initialization complete.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to setup database.");
        }
    }
}
