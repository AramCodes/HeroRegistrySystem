package _3024c.heroregistry.models;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "heroName")
    private String heroName;

    @Column(name = "realName")
    private String realName;

    @Column(name = "heroHeadshot")
    private String heroHeadshot;

    private int age;

    private double rating;

    @Column(name = "isActive")
    private boolean isActive;

    private String description;

    @Column(name = "strengthBase")
    private String strengthBase;

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

}
