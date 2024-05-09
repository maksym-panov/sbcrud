package com.maksympanov.hneu.mjt.sbcrud.model;

public enum UserRole {
    USER,
    VENDOR,
    ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }

}
