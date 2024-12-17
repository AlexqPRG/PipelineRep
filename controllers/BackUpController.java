package com.web_project.zayavki.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.Date;

@Controller
public class BackUpController {
    @PostMapping("/backup")
    private String backup() throws IOException, InterruptedException {
        Date date = new Date();
        String[] command = String.format("./Contents/SharedSupport/pg_dump --file=./backups/%s --host=localhost --port=5434 --username=postgres --no-password --verbose --format=d autoservicedb", String.valueOf(date.getTime())).split(" ");

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.directory(new File(System.getProperty("user.dir")));
        Process process = builder.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }

        process.waitFor();
        bufferedReader.close();
        return "redirect:/backups";
    }

    @GetMapping("/backups")
    private String getBackup(Model model){
        model.addAttribute("userRole", getRole());
        File file = new File("./backups");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });
        model.addAttribute("backups", directories);
        return "backups";
    }

    @PostMapping("/restore")
    public String postRestore(@RequestParam("versions") String versions, Model model) throws IOException, InterruptedException {
        String[] command = String.format("./Contents/SharedSupport/pg_restore --host=localhost --port=5434 --username=postgres --no-password --dbname autoservicedb --format=d --clean --verbose ./backups/%s", versions).split(" ");

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.directory(new File(System.getProperty("user.dir")));
        Process process = builder.start();

        process.waitFor();
        return "redirect:/backups";
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
