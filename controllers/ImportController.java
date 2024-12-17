package com.web_project.zayavki.controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.web_project.zayavki.models.SpareModel;
import com.web_project.zayavki.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/import")
public class ImportController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("")
    public String getUploadPage(Model model) {
        model.addAttribute("userRole", getRole());
        return "import";
    }

    @PostMapping("/spare/csv")
    public String importSpare(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/spare/all";
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> models = csvReader.readAll();
            for (int i = 0; i < models.size(); i++) {
                if (i == 0) {
                    continue;
                }
                String[] model = models.get(i);
                SpareModel spare = new SpareModel();
                spare.setArticle(model[1]);
                spare.setPrice(Double.parseDouble(model[3]));
                spare.setQuantity(Integer.parseInt(model[4]));
                apiService.setDataToApi("/spare", spare);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return "redirect:/spare/all";
    }

    @PostMapping("/spare/sql")
    public String importSpareFromSql(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/spare/all";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Генерируем новый UUID
                String newId = UUID.randomUUID().toString();

                // Заменяем старый UUID на новый в строке
                String updatedLine = line.replaceFirst("'[0-9a-fA-F-]{36}'", "'" + newId + "'");

                // Выполняем обновленный SQL-запрос
                jdbcTemplate.execute(updatedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return "redirect:/spare/all";
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
