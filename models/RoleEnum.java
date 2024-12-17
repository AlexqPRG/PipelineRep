package com.web_project.zayavki.models;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    USER, ADMIN, STAFF, MANAGER;

    @Override
    public String getAuthority(){
        return name();
    }
}