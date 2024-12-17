package com.web_project.zayavki.models.modeltDTO;

import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.RoleEnum;
import com.web_project.zayavki.models.UserModel;

import java.util.Set;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String numberPhone;
    private Set<RoleEnum> roles;
    private boolean isActive;


    public UserDTO(){}

    public UserDTO(UserModel userModel) {
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.password = userModel.getPassword();
        this.firstName = userModel.getFirstName();
        this.secondName = userModel.getSecondName();
        this.patronymic = userModel.getPatronymic();
        this.numberPhone = userModel.getNumberPhone();
        this.roles = userModel.getRoles();
        this.isActive = userModel.isActive();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEnum> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }
}

