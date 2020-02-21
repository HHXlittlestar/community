package com.huhx.community.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String accountID;
    private String token;
    private Long gmtCreat;
    private Long gmtModify;
    private String avatarUrl;
}
