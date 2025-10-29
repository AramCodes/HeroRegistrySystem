package _3024c.heroregistry.views;

import _3024c.heroregistry.models.Hero;
import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HeroRegistrySystem {
    private final Scanner scanner = new Scanner(System.in);

    @Getter
    static final List<Hero> heroes = new ArrayList<>();
    private long curId; //main commission until single view
    private String command = "";

    /*
        indexOfHeroById
        this method simply verifies that the id exist in memory and returns its index in the array or -1
        the parameter is a long called id
        the return type is in, will return index if found -1 if not
    */
    public int indexOfHeroById(long id) {
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getId() == id) return i;
        }
        return -1;
    }


    /*
        heroUpdate
        this method searches Heroes for matching id  and updates it if found then uses fileWrite to reflect
        change in text file.
        there is a parameter of id this method
        the return type is boolean
    */

//    public boolean heroUpdate(long id) {
//        boolean found = false;
//
//        String[] heroData = enterDataToUpdateHero();
//
//        long heroId = Long.parseLong(heroData[0]);
//        String heroName = heroData[1];
//        String realName = heroData[2];
//        String heroHeadshot = heroData[3];
//        int age = Integer.parseInt(heroData[4]);
//        double rating = Double.parseDouble(heroData[5]);
//        boolean isActive = Boolean.parseBoolean(heroData[6]);
//        String description = heroData[7];
//        String strengthBase = heroData[8];
//
//        Hero newHero = makeHero(heroId, heroName, realName, heroHeadshot, age, rating, isActive, description, strengthBase);
//
//        if (isSevenDigitAndLong(id)) {
//            for (int i = 0; i < heroes.size(); i++) {
//                if (heroes.get(i).getId() == id) {
//                    heroes.set(i, newHero);
//                    found = true;
//                    break;
//                }
//            }
//
//            if (found) {
//                //overwriteOriginalFile();
//                System.out.println(id + " was successfully updated in the Hero Registry System.");
//                System.out.println("The Hero List is now: ");
//               // displayAllHeroes();
//            } else {
//                System.out.println("Hero with ID " + id + " not found.");
//            }
//
//        }else {
//            System.out.println("Invalid ID. It must be 7 digits.");
//            System.out.println("What is Hero's seven digit Id?('1234567')");
//           curId = scanner.nextInt();
//            heroUpdate(curId);
//        }
//
//        return found;
//    }

}

