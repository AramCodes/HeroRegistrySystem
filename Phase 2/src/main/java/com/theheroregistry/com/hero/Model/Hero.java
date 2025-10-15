package com.theheroregistry.com.hero.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Hero {
    private long id;
    private String heroName;
    private String realName;
    private String heroHeadshot;
    private int age;
    private double rating;
    private boolean isActive;
    private String description;
    private String strengthBase;
}
