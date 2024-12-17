package com.web_project.zayavki.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void writeDataToCSV(String tableName, OutputStream outputStream) {
        String sql = "SELECT * FROM " + tableName;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            if (!rows.isEmpty()) {
                String headers = String.join(",", rows.get(0).keySet());
                writer.println(headers);
            }
            for (Map<String, Object> row : rows) {
                String line = row.values().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                writer.println(line);
            }
        }
    }

    public void writeDataToSQL(String tableName, OutputStream outputStream) {
        String sql = "SELECT * FROM " + tableName;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            for (Map<String, Object> row : rows) {
                StringBuilder sqlInsert = new StringBuilder("INSERT INTO " + tableName + " (");
                String columns = String.join(", ", row.keySet());
                sqlInsert.append(columns).append(") VALUES (");

                String values = row.values().stream()
                        .map(value -> "'" + value.toString().replace("'", "''") + "'") // Экранирование одинарных кавычек
                        .collect(Collectors.joining(", "));
                sqlInsert.append(values).append(");");

                writer.println(sqlInsert.toString());
            }
        }
    }


}

