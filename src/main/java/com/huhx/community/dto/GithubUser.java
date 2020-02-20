package com.huhx.community.dto;

import org.springframework.stereotype.Component;

@Component
public class GithubUser {
    private String name;
    private Long ID;
    private String dio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getDio() {
        return dio;
    }

    public void setDio(String dio) {
        this.dio = dio;
    }
}
