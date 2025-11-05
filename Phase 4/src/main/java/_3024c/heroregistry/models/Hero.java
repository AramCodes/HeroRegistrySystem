package _3024c.heroregistry.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
This is the main data the application will be centered around

 */
@Entity
@Table(name = "heroes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hero {

    @Id
    @GeneratedValue
    private Long id;

    private String heroName;

    private String realName;

    private String heroHeadshot;

    private int age;

    private double rating;

    private boolean isActive;

    private String description;

    private String strengthBase;

    public boolean getIsActive(){
        return isActive;
    }

}
