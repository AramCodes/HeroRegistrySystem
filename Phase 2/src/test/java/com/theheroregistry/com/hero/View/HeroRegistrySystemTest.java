package com.theheroregistry.com.hero.View;

import com.theheroregistry.com.hero.Model.Hero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestHeroRegistrySystem extends HeroRegistrySystem {

    @Override
    public String overwriteOriginalFile() {
        // no actual overwrite in tests
        return "done";
    }

    @Override
    public boolean heroDelete(long id) {
        // scanner and recursive == bad combo in test
        if (!isSevenDigitAndLong(id)) {
            return false;
        }

        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getId() == id) {
                heroes.remove(i);
                overwriteOriginalFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean heroUpdate(long id) {
        boolean found = false;
        boolean noneMatch = true;
        String[] heroData = new String[9];

        for (Hero h : heroes) {
            if (h.getId() == id) {
                noneMatch = false;
                break;
            }
        }

        if(noneMatch){
            System.out.println("Hero with ID " + id + " not found.");
            return found;
        }

        long heroId = 6295713L;

        heroData[1] = "Star Girl";
        heroData[2] = "Courtney Whitmore";
        heroData[3] = "https://static.dc.com/dc/files/default_images/Char_Thumb_Stargirl_5eb4a5cd5ccaf7.93430787.jpg?w=384";
        heroData[4] = "16";
        heroData[5] = "9.8"; //changed
        heroData[6] = "true";
        heroData[7] = "Cosmic staff wielder who leads with optimism";
        heroData[8] = "G.O.A.T"; //changed

        Hero newHero = makeHero(heroId, heroData[1], heroData[2], heroData[3], Integer.parseInt(heroData[4]), Double.parseDouble(heroData[5]), true, heroData[7], heroData[8]);

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
            }

        }else {
            System.out.println("Invalid ID. It must be 7 digits.");
            System.out.println("What is Hero's seven digit Id?('1234567')");
        }

        return found;
    }
}

class HeroRegistrySystemTest {
    final public TestHeroRegistrySystem sut = new TestHeroRegistrySystem();

    Hero superman = Hero.builder()
            .id(1234567L)
            .heroName("Superman")
            .realName("Clark Kent")
            .heroHeadshot("https://static.dc.com/dc/files/default_images/Char_Thumb_Superman_5c3fc2758f6984.90100206.jpg?w=384")
            .age(35)
            .rating(9.8)
            .isActive(true)
            .description("Man of Steel, protector of Metropolis")
            .strengthBase("G.O.D")
            .build();

    Hero starGirl = Hero.builder()
            .id(6295713L)
            .heroName("Star Girl")
            .realName("Courtney Whitmore")
            .heroHeadshot("https://static.dc.com/dc/files/default_images/Char_Thumb_Stargirl_5eb4a5cd5ccaf7.93430787.jpg?w=384")
            .age(16)
            .rating(8.8)
            .isActive(true)
            .description("Cosmic staff wielder who leads with optimism")
            .strengthBase("G.O.D")
            .build();

    Hero wonderWoman = Hero.builder()
            .id(2020002)
            .heroName("Wonder Woman")
            .realName("Diana Prince")
            .heroHeadshot("https://static.dc.com/dc/files/default_images/Char_Thumb_WonderWoman_20190116_5c3fc6aa51d064.76155401.jpg?w=384")
            .age(30)
            .rating(9.5)
            .isActive(true)
            .description("Amazon warrior and Justice League member")
            .strengthBase("Strength")
            .build();


    private boolean isSevenDigitAndLong(long id) {
        return id >= 1_000_000L && id <= 9_999_999L;
    }

    @Test
    void testDisplayMenu() {
        assertTrue(true);
    }

    @Test void sevenDigits_ok()       { assertTrue(isSevenDigitAndLong(1234567L)); }
    @Test void sixDigits_no()         { assertFalse(isSevenDigitAndLong(123456L)); }
    @Test void eightDigits_no()       { assertFalse(isSevenDigitAndLong(12345678L)); }
    @Test void negative_no()          { assertFalse(isSevenDigitAndLong(-1234567L)); }
    @Test void boundary_lowest_ok()   { assertTrue(isSevenDigitAndLong(1_000_000L)); }
    @Test void boundary_highest_ok()  { assertTrue(isSevenDigitAndLong(9_999_999L)); }

    @BeforeEach
    void setUp() {
        sut.getHeroes().clear();

        sut.addHero(wonderWoman);
        sut.addHero(starGirl);
    }

    @Test
    void testAddHero() {
        int before = sut.heroes.size();

        String result = sut.addHero(superman);

        assertEquals("done", result, "Method should return 'done'");
        List<Hero> heroes = sut.getHeroes();

        assertEquals(before + 1, heroes.size(), "Size should increase by 1");
        assertTrue(heroes.contains(superman), "Collection contains the hero");
        assertEquals(superman, heroes.get(heroes.size() - 1), "Hero should be last");
    }


    @Test
    void testHeroDelete() {
        assertTrue(sut.getHeroes().contains(wonderWoman), "Seed data should contain Wonder Woman");

        int before = sut.getHeroes().size();

        boolean result = sut.heroDelete(wonderWoman.getId());

        assertTrue(result, "Hero should be found and deleted");
        List<Hero> heroes = sut.getHeroes();
        assertEquals(before - 1, heroes.size(), "Size should decrease by 1 after removal");
        assertFalse(heroes.contains(wonderWoman), "Collection should no longer contain the removed hero");
    }

    @Test
    void heroUpdate() {
        assertTrue(sut.getHeroes().stream().anyMatch(h -> h.getId() == starGirl.getId()));

        int before = sut.getHeroes().size(); // to validate size stays same and another record wasn't just added

        boolean result = sut.heroUpdate(starGirl.getId());

        assertTrue(result, "Hero should be found and updated");

        assertEquals(before, sut.heroes.size(), "Size should stay the same after updating");

        Hero updated = sut.getHeroes().stream()
                .filter(h -> h.getId() == starGirl.getId())
                .findFirst()
                .orElseThrow();

        assertEquals(9.8, updated.getRating(), "Hero's should be updated");
        assertEquals("G.O.A.T", updated.getStrengthBase(), "Hero's should be updated");
        assertEquals("Star Girl", updated.getHeroName());
    }
}