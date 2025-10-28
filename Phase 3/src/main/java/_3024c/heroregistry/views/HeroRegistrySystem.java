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
        formatHeroObject
        this method simply converts the Hero data into the object format used in Db and heroes' list.
        the parameters are all off the hero's attribute, during flat file execution the id will be allowed manually
        but during phase of Db integration DBMS will handle auto generation of Ids.
        the return type is an Object, will return converted data
    */

    public Hero makeHero(long id, String heroName, String realName, String heroHeadshot, int age, double rating, boolean isActive, String description, String strengthBase) {

        return Hero.builder()
                .id(id)
                .heroName(heroName)
                .realName(realName)
                .heroHeadshot(heroHeadshot)
                .age(age)
                .rating(rating)
                .isActive(isActive)
                .description(description)
                .strengthBase(strengthBase)
                .build();
    }



      /*
        enterDataToUpdateHero
        this method simply takes in Hero data  and converts it into an object (to be used in both create and update)
        the parameter is all the hero data attributes
        the return type is a String Array
    */

    public String[] enterDataToUpdateHero(){
        String[] heroData = new String[9];
        System.out.println("Let's Edit a hero");

        while (true) {
            System.out.print("1. Enter Hero ID (7-digit number): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{7}")) {
                heroData[0] = input;
                break;
            } else {
                System.out.println("❌ Invalid input. Please enter exactly 7 digits (e.g., 1234567).");
            }
        }

        while (true) {
            System.out.print("2. Enter Hero Name (e.g., Aquaman): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                heroData[1] = input;
                break;
            } else {
                System.out.println("❌ Hero name cannot be empty.");
            }
        }

        while (true) {
            System.out.print("3. Enter Real Name (e.g., Arthur Curry): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                heroData[2] = input;
                break;
            } else {
                System.out.println("❌ Real name cannot be empty.");
            }
        }

        while (true) {
            System.out.print("4. Enter Hero Headshot URL: ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (input.matches("^(https?|ftp)://[^\\s/$.?#].\\S*$")) {
                heroData[3] = input;
                break;
            } else {
                System.out.println("❌ Invalid URL format. Please enter a valid link starting with http:// or https://");
            }
        }

        while (true) {
            System.out.print("5. Enter Age (integer): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            try {
                int age = Integer.parseInt(input);
                if (age > 0 && age < 200) {
                    heroData[4] = String.valueOf(age);
                    break;
                } else {
                    System.out.println("❌ Age must be between 1 and 199.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please enter a whole number.");
            }
        }

        while (true) {
            System.out.print("6. Enter Rating (decimal, e.g., 8.5): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            try {
                double rating = Double.parseDouble(input);
                if (rating >= 0 && rating <= 10) {
                    heroData[5] = String.valueOf(rating);
                    break;
                } else {
                    System.out.println("❌ Rating must be between 0.0 and 10.0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a decimal number (e.g., 7.8).");
            }
        }

        while (true) {
            System.out.print("7. Is Hero Active? (true/false): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                heroData[6] = input;
                break;
            } else {
                System.out.println("❌ Please type 'true' or 'false'.");
            }
        }

        while (true) {
            System.out.print("8. Enter Hero Description: ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                heroData[7] = input;
                break;
            } else {
                System.out.println("❌ Description cannot be empty.");
            }
        }

        while (true) {
            System.out.print("9. Enter Strength Base (e.g., Agility, Intellect, etc.): ");
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                heroData[8] = input;
                break;
            } else {
                System.out.println("❌ Strength base cannot be empty.");
            }
        }

        System.out.println("\n✅ Hero data entered successfully!\n");
        return heroData;
    }



    /*
        promptAndUpdateHeroById()
        this method verifies id is valid and passes it to update method if verified
        there are no parameters needed for this method
        the return type is void
    */
    public void promptAndUpdateHeroById() {
        String input = "";
        while (!input.equalsIgnoreCase("q")) {
            System.out.print("What is Hero's seven digit Id? (e.g., 1234567 or 'q' to quit)\n>>> ");

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting update.");
                return;
            }

            if (!input.matches("^\\d{7}$")) {
                System.out.println("❌ Invalid input. Please enter exactly 7 digits (e.g., 1234567).");
                continue; // ask again
            }

            try {
                curId = Long.parseLong(input);
            } catch (NumberFormatException nfe) {
                System.out.println("❌ Invalid numeric format. Try again.");
                continue;
            }

            int idx = indexOfHeroById(curId);
            if (idx < 0) {
                System.out.println("Hero with ID " + curId + " not found. Try another ID or 'q' to quit.");
                continue;
            }

//            boolean updated = heroUpdate(curId);
//            if (updated) {
//                System.out.println(curId + " was successfully updated in the Hero Registry System.");
//            } else {
//                System.out.println("Update aborted or failed validation.");
//            }
//            return;
        }
    }



    public String addHero(Hero hero) {
        heroes.add(hero);
        //overwriteOriginalFile();

        return hero.getHeroName() + " has been added to the Hero Registry System";
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

