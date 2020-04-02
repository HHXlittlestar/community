package com.huhx.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String criteria;
    private List<String> content;
}
