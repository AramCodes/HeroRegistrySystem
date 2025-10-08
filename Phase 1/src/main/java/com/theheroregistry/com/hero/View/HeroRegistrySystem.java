package com.theheroregistry.com.hero.View;

import com.theheroregistry.com.hero.Model.Hero;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class HeroRegistrySystem {
    private final Scanner scanner = new Scanner(System.in);

    private final List<Hero> heroes = new ArrayList<>();
    private long curId; //main commission until single view
    private String command = "";

    /*
        displayMenu
        this method simply prints menu on screen
        there are no parameters needed for this method
        the return type is boolean, will return true if it reaches after print
    */
    public boolean displayMenu(){
        System.out.println("What would you like to do today?(read, display, create, remove, update, custom, or exit)");
        System.out.print(">>> ");
        return true;
    }

    /*
        isSevenDigitAndLong
        this method simply verifies that the number in it is a seven digit long(id format)
        the parameter is a long called id
        the return type is boolean, will return true if it is valid false if not
    */

    public boolean isSevenDigitAndLong(long id) {
        return id >= 1_000_000L && id <= 9_999_999L;
    }

    /*
        formatHeroRecord
        this method simply converts the Hero Objects into the string format used in files.
        the parameter is an object containing a heroes data
        the return type is a String, will return converted data
    */

    public String formatHeroRecord(Hero hero) {
        if (hero == null) {
            return "";
        }

        return String.format(
                "%d-%s-%s-%s-%d-%.1f-%b-%s-%s",
                hero.getId(),
                hero.getHeroName(),
                hero.getRealName(),
                hero.getHeroHeadshot(),
                hero.getAge(),
                hero.getRating(),
                hero.isActive(),
                hero.getDescription(),
                hero.getStrengthBase()
        );
    }

    /*
        formatHeroObject
        this method simply converts the Hero data into the object format used in Db and heroes' list.
        the parameters are all off the hero's attribute, during flat file execution the id will be allowed manually
        but during phase of Db integration DBMS will handle auto generation of Ids.
        the return type is a Object, will return converted data
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
        parseHeroFromLine
        this method simply reads Hero data  and converts it into an hero object
        the parameter is a single line

        the return type is an Optional<Hero>, will return converted data
    */

    public Optional<Hero> parseHeroFromLine(String line){
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Line cannot be null or empty");
        }

        String[] parts = line.split("-", 9);

        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid hero data format: " + line);
        }

        try {
            long id = Long.parseLong(parts[0]);
            String heroName = parts[1];
            String realName = parts[2];
            String heroHeadshot = parts[3];
            int age = Integer.parseInt(parts[4]);
            double rating = Double.parseDouble(parts[5]);
            boolean isActive = Boolean.parseBoolean(parts[6]);
            String description = parts[7];
            String strengthBase = parts[8];

            Hero ParsedHero = Hero.builder()
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

            return Optional.of(ParsedHero);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing fields in hero data: " + line, e);
        }
    }

    /*
        enterData
        this method simply takes in Hero data  and converts it into an object (to be used in both create and update)
        the parameter is all the hero data attributes
        the return type is a String Array
    */

    public String[] enterData(){
        String[] heroData = new String[9];
        System.out.println("Let's add a new Hero! Or Edit an old one...");
        scanner.nextLine();//consumes new line leftover

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
            if (input.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
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
        overwriteOriginalFile()
        this method writes and saves the items from heroes list into data file.
        there are no parameters needed for this method
        the return type is String
    */
    public String overwriteOriginalFile() {
        String filePath = "data.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Hero hero : heroes) {
                String heroLine = formatHeroRecord(hero);

                writer.write(heroLine);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "done";
    }

    /*
        batchUpload()
        this method read and writes new records from input file to original db.
        there are no parameters needed for this method
        the return type is int, the amount of items added
    */

    public int batchUpload(){


        while (true) {
            System.out.println("Path to the text file to import:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Please enter a file path (e.g., import.txt or C:\\Users\\You\\import.txt).");
                continue;
            }

            if ((input.startsWith("\"") && input.endsWith("\"")) ||
                    (input.startsWith("'") && input.endsWith("'"))) {
                input = input.substring(1, input.length() - 1);
            }

            Path importPath = Paths.get(input);
            if (!importPath.isAbsolute()) {
                importPath = Paths.get("").toAbsolutePath().resolve(importPath);
            }

            if (!Files.isRegularFile(importPath) || !Files.isReadable(importPath)) {
                System.err.println("Cannot read: " + importPath);
                System.err.println("Tip: current working directory is: " + Paths.get("").toAbsolutePath());
                System.err.println("Try again.\n");
                continue;
            }

            Set<Hero> merged = new LinkedHashSet<>(heroes);

            int before = merged.size();
            int importedCount = 0;
            int skippedMalformed = 0;

            try (Stream<String> lines = Files.lines(importPath, StandardCharsets.UTF_8)) {
                for (String line : (Iterable<String>) lines::iterator) {
                    String trimmed = line == null ? "" : line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue;
                    }

                    Optional<Hero> parsed = parseHeroFromLine(trimmed);

                    if (parsed.isPresent()) {
                        if (merged.add(parsed.get())) {
                            importedCount++;
                        }
                    } else {
                        skippedMalformed++;
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading import file: " + e.getMessage());
                return 1;
            }

            heroes.clear();
            heroes.addAll(merged);
            overwriteOriginalFile();
            int after = merged.size();

            System.out.println("Imported " + (after - before) + " total records into the Hero Registry System" );
            break;
        }

        return 0;
    }

    /*
        displayAllHeroes
        (in the heroes list)
        this method prints all heroes to the screen.
        there are no parameters needed for this method
        the return type is int, a 1 if it is successful
    */

    public int displayAllHeroes(){
        copyHeroesFromOriginal();

        if (!heroes.isEmpty()){

            for (Hero hero : heroes) {
                System.out.println(hero);
            }
            System.out.println();
        } else {
            System.out.println("There are no heroes available.");
        }


        return 1;
    }

    public String addHero(Hero hero) {
        heroes.add(hero);
        overwriteOriginalFile();

        return "done";
    }

    /*
       copyHeroesFromOriginal
       (file to list)
       this method makes sure that the data in the original file is inside heroes List. Used before mutations to maintain the order of data
       and update chosen source of truth(temporary heroes' list until Db).
       there are no parameters needed for this method
       the return type is int, will return int of total records
   */

    public int copyHeroesFromOriginal() {
        if (!this.heroes.isEmpty()) {
            heroes.clear();
        }


        String filePath = "data.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");

                if (parts.length != 9) { //pseudo-defense to only accept line with 9 parts for the 9 attributes of a hero
                    System.err.println("Invalid line (skipped): " + line);
                }

                try {
                    Hero hero = Hero.builder()
                            .id(Long.parseLong(parts[0].trim()))
                            .heroName(parts[1].trim())
                            .realName(parts[2].trim())
                            .heroHeadshot(parts[3].trim())
                            .age(Integer.parseInt(parts[4].trim()))
                            .rating(Double.parseDouble(parts[5].trim()))
                            .isActive(Boolean.parseBoolean(parts[6].trim()))
                            .description(parts[7].trim())
                            .strengthBase(parts[8].trim())
                            .build();

                    heroes.add(hero);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse number in line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        System.out.println();
        System.out.println("List Loaded!!!");
        System.out.println();

        if (!this.heroes.isEmpty()){
            return heroes.size();
        }

        return 0;
    }


    /*
        heroDelete
        this method searches Heroes for matching id  and removes if found then uses fileWrite to reflect
        deletion in text file.
        there is a parameter of id this method
        the return type is void
    */

    public boolean heroDelete(long id) {
        boolean found = false;

        if (isSevenDigitAndLong(id)) {
            for (int i = 0; i < heroes.size(); i++) {
                if (heroes.get(i).getId() == id) {
                    heroes.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                overwriteOriginalFile();
                System.out.println(id + " was successfully deleted from the Hero Registry System.");
                System.out.println("The Hero List is now: ");
                displayAllHeroes();
            } else {
                System.out.println("Hero with ID " + id + " not found.");
            }
            scanner.nextLine();
        }else {
            System.out.println("Invalid ID. It must be 7 digits.");
            System.out.println("What is Hero's seven digit Id?('1234567')");
            curId = scanner.nextInt();
            heroDelete(curId);
        }


        return found;
    }

        /*
        heroUpdate
        this method searches Heroes for matching id  and updates it if found then uses fileWrite to reflect
        change in text file.
        there is a parameter of id this method
        the return type is boolean
    */

    public boolean heroUpdate(long id) {
        boolean found = false;

        String[] heroData = enterData();

        long heroId = Long.parseLong(heroData[0]);
        String heroName = heroData[1];
        String realName = heroData[2];
        String heroHeadshot = heroData[3];
        int age = Integer.parseInt(heroData[4]);
        double rating = Double.parseDouble(heroData[5]);
        boolean isActive = Boolean.parseBoolean(heroData[6]);
        String description = heroData[7];
        String strengthBase = heroData[8];

        Hero newHero = makeHero(heroId, heroName, realName, heroHeadshot, age, rating, isActive, description, strengthBase);

        if (isSevenDigitAndLong(id)) {
            for (int i = 0; i < heroes.size(); i++) {
                if (heroes.get(i).getId() == id) {
                    heroes.set(i, newHero);
                    found = true;
                    break;
                }
            }

            if (found) {
                overwriteOriginalFile();
                System.out.println(id + " was successfully updated in the Hero Registry System.");
                System.out.println("The Hero List is now: ");
                displayAllHeroes();
            } else {
                System.out.println("Hero with ID " + id + " not found.");
            }
            scanner.nextLine();
        }else {
            System.out.println("Invalid ID. It must be 7 digits.");
            System.out.println("What is Hero's seven digit Id?('1234567')");
            curId = scanner.nextInt();
            heroUpdate(curId);
        }


        return found;
    }

    /*
        subMenu
        this method pops up a sub menu inside the custom option and uses another scanner to prevent collision and stacking
        there is not a parameter on this method
        the return type is boolean
    */

    public boolean subMenu(){
        Scanner subScanner = new Scanner(System.in);
        int selection = 0;

        while (selection != 4) {
            System.out.println("which function would you like to use, pick a number below");

            System.out.println("--> 1. Send SOS");
            System.out.println("--> 2. Toggle Retirement");
            System.out.println("--> 3. Average Rating");
            System.out.println("--> 4. Back to main menu");
            System.out.println(">>>");

            try {
                selection = subScanner.nextInt();

                switch (selection) {
                    case 1 -> {
                        System.out.println("What is the Hero's seven digit Id?('1234567')");
                        curId = scanner.nextInt();
                        System.out.println(sendingSOS(curId));
                        System.out.println();
                    }
                    case 2 -> {
                        System.out.println("What is the Hero's seven digit Id?('1234567')");
                        curId = scanner.nextInt();
                        System.out.println(heroRetirementToggle(curId));
                        System.out.println();
                    }
                    case 3 -> {
                        System.out.println("Average Rating for all heroes is " + getAverageRating());
                        System.out.println();
                    }
                    case 4 -> {
                        System.out.println("Exiting back to main menu...");
                        System.out.println();
                    }
                    default -> System.out.println("Unknown command. Try: 1, 2, 3, or 4");
                }
            } catch (InputMismatchException e) {
                System.out.println();
                System.out.println("⚠ Invalid input! Please enter a number between 1 and 4.");
                System.out.println();
                subScanner.nextLine(); // Clears the invalid input
            }
        }

        return true;
    }

//    Custom functions

    /*
        getAverageRating()
        this method sums all the rating data and divides it by the total number of heroes
        there is not a parameter on this method
        the return type is double which returns is the average waiting
    */
    public double getAverageRating() {
        double total = 00.00;
        for (Hero hero : heroes) {
            total += hero.getRating();
        }

        return total / heroes.size();
    }

    /*
        heroRetirementToggle()
        this method switches the boolean attribute on a hero object to the opposite
        there is a long id called id
        the return type is String it returns the current status after boolean change
    */
    public String heroRetirementToggle(long id) {
        String retirementStatus = "";

        if (isSevenDigitAndLong(id)){
            for (int i = 0; i < heroes.size(); i++) {
                if (heroes.get(i).getId() == id) {
                    heroes.get(i).setActive(!heroes.get(i).isActive());
                }

                if (heroes.get(i).isActive() == true ) {
                    retirementStatus = heroes.get(i).getHeroName() + " is active and ready for action.";
                } else {
                    retirementStatus = heroes.get(i).getHeroName() + " is not active, go and bother someone else.";
                }
            }
        } else {
            System.out.println("Invalid ID. It must be 7 digits.");
        }
        return retirementStatus;
    }

    /*
        sendingSOS()
        this method sends SOS to hero base on hero id if hero is not inactive
        there is a long id called id
        the return type is String it returns the status/result of the SOS
    */
    public String sendingSOS(long id) {
        String message = "";

        if (isSevenDigitAndLong(id)) {
            for (Hero hero : heroes) {
                if (hero.getId() == id && hero.isActive()) {
                    message = hero.getHeroName() + " received your SOS, will be there soon";
                } else if (hero.getId() == id && !hero.isActive()) {
                    message = hero.getHeroName() + " received your SOS, and close the chat! that hero is retired";
                }
            }
        } else {
            message = "Invalid ID. It must be 7 digits and a number";
        }

        return message;
    }

    public void run() {

        copyHeroesFromOriginal(); //original population of list

        while (!command.equalsIgnoreCase("exit")) {
            displayMenu();
            command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "read" -> {
                    batchUpload();
                }
                case "display" -> {
                    displayAllHeroes();
                }
                case "create" -> {
                    String[] parts = enterData();

                    long id = Long.parseLong(parts[0]);
                    String heroName = parts[1];
                    String realName = parts[2];
                    String heroHeadshot = parts[3];
                    int age = Integer.parseInt(parts[4]);
                    double rating = Double.parseDouble(parts[5]);
                    boolean isActive = Boolean.parseBoolean(parts[6]);
                    String description = parts[7];
                    String strengthBase = parts[8];

                    Hero newHero = makeHero(id, heroName, realName, heroHeadshot, age, rating, isActive, description, strengthBase);
                    addHero(newHero);
                    System.out.println("Hero named " + newHero.getHeroName() + " with Id " + newHero.getId() + " has been created.");
                }
                case "remove" -> {
                    System.out.println("What is Hero's seven digit Id?('1234567')");
                    curId = scanner.nextInt();
                    heroDelete(curId);
                }
                case "update" -> {
                    System.out.println("What is Hero's seven digit Id?('1234567')");
                    curId = scanner.nextInt();
                    heroUpdate(curId);
                }
                case "custom" -> {
                    subMenu();
                }
                case "exit" -> System.out.println("Exiting the program now...");
                default -> System.out.println("Unknown command. Try: read, display, create, remove, update, custom, or exit");
            }
        }
    }
}

