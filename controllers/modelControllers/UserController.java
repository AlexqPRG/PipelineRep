package com.web_project.zayavki.controllers.modelControllers;

import com.web_project.zayavki.models.RoleEnum;
import com.web_project.zayavki.models.UserModel;
import com.web_project.zayavki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users")
    public String userView(Model model){
        model.addAttribute("userRole", getRole());
        model.addAttribute("user_list", userRepository.findAll());
        return "modelPages/userPage";
    }

    @GetMapping("/users/{id}")
    public String detailView(@PathVariable UUID id, Model model){
        model.addAttribute("userRole", getRole());
        UserModel user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя не существует: " + id));
        model.addAttribute("user_object", user);
        return "modelPages/info";
    }

    @GetMapping("/users/{id}/update")
    public String updateView(@PathVariable UUID id, Model model){
        model.addAttribute("userRole", getRole());
        UserModel user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя не существует: " + id));
        model.addAttribute("user_object", user);
        model.addAttribute("roles", RoleEnum.values());
        return "modelPages/update";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable UUID id,
                             @RequestParam String username,
                             @RequestParam(name = "roles[]", required = false) String[] roles){
        UserModel user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователя не существует: " + id));
        user.setUsername(username);
        user.getRoles().clear();
        if(roles != null){
            for (String role : roles){
                user.getRoles().add(RoleEnum.valueOf(role));
            }
        }
        userRepository.save(user);
        return "redirect:/admin/users/" + id;
    }

    private String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = "";

        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем роли пользователя
            userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }

        return userRole;
    }
}
