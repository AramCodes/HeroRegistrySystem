package _3024c.heroregistry.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/*
This is the main data the application will be centered around

 */
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
