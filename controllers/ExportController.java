package com.web_project.zayavki.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ExportController {

    @Autowired
    private DataController dataSerivce; // Сервис, для получения данных из базы

    @GetMapping("/exportcsv/{tableName}")
    public ResponseEntity<Void> exportToCSV(@PathVariable String tableName, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tableName + ".csv\"");

        OutputStream outputStream = response.getOutputStream();
        dataSerivce.writeDataToCSV(tableName, outputStream);
        outputStream.flush();
        outputStream.close();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/exportsql/{tableName}")
    public ResponseEntity<Void> exportToSQL(@PathVariable String tableName, HttpServletResponse response) throws IOException {
        response.setContentType("application/sql");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tableName + ".sql\"");

        OutputStream outputStream = response.getOutputStream();
        dataSerivce.writeDataToSQL(tableName, outputStream);
        outputStream.flush();
        outputStream.close();
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/exportPage")
    public String exportPage(Model model){
        model.addAttribute("userRole", getRole());
        return "modelPages/export";
    }

    private String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = "";

        if (authentication != null && authentication.isAuthenticated()) {
            userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }

        return userRole;
    }

}
