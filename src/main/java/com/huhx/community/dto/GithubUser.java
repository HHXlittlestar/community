package com.huhx.community.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class GithubUser {
    private String name;
    private Long ID;
    private String bio;
    private String avatarUrl;
}
