package _3024c.heroregistry.controllers;

import _3024c.heroregistry.models.Hero;
import _3024c.heroregistry.repositories.HeroRepository;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Controller
@RequestMapping("api/v1/heroes")

/**
 * This class contains the controller for the hero objects.
 */
public class HeroController {

     //    helpers
    private static void sanitizeHero(Hero hero) {
        if (hero == null) return;

        if (hero.getHeroName() != null) {
            hero.setHeroName(Encode.forHtml(hero.getHeroName()));
        }

        if (hero.getRealName()!= null) {
            hero.setRealName(Encode.forHtml(hero.getRealName()));
        }

        if (hero.getHeroHeadshot() != null) {
            hero.setHeroHeadshot(Encode.forHtml(hero.getHeroHeadshot()));
        }

        if (hero.getDescription() != null) {
            hero.setDescription(Encode.forHtml(hero.getDescription()));
        }

        if (hero.getStrengthBase() != null) {
            hero.setStrengthBase(Encode.forHtml(hero.getStrengthBase()));
        }

    }

    private static HeroRepository repository = null;

    /**
     * constructor to inject dependency of repository to avoid deprecated autowired annotation
     * @param repository
     */
    public HeroController(HeroRepository repository) {
        this.repository = repository;
    }

    // Read

    /**
     * This method represents the main read functionality of the app
     * @return a list containing all hero objects in the Db
     */
    @GetMapping("/all")
    public static List<Hero> getHeroes(){

        return repository.findAll();
    }


    //  Create

    /**
     * This method represents the main create functionality of the app
     * @param hero is a hero Object that will be created
     * @return the return is a combination of the object sent and the staus code received through the process
     */
    @PostMapping("/save")
    public static ResponseEntity saveHero(@RequestBody Hero hero){
        sanitizeHero(hero);
        Hero saved = repository.save(hero);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    //Update

    /**
     * This method represents the main update functionality of the app
     * @param oldHero is an copy of the original document
     * @return is a combination of the object sent and the staus code received through the process
     */
    @PutMapping("/update")
    public static ResponseEntity updateHero(Hero oldHero){
        if (oldHero == null || oldHero.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hero and ID must be provided.");
        }

        // sanitize incoming data to prevent XSS
        sanitizeHero(oldHero);

        Hero existingDocument = repository.findById(oldHero.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Hero with ID " + oldHero.getId() + " not found."));

        // copy/updatable fields only
        if (oldHero.getHeroName() != null) existingDocument.setHeroName(oldHero.getHeroName());
        if (oldHero.getRealName() != null) existingDocument.setRealName(oldHero.getRealName());
        if (oldHero.getHeroHeadshot() != null) existingDocument.setHeroHeadshot(oldHero.getHeroHeadshot());
        existingDocument.setAge(oldHero.getAge());
        existingDocument.setRating(oldHero.getRating());
        existingDocument.setIsActive(oldHero.getIsActive());
        if (oldHero.getDescription() != null) existingDocument.setDescription(oldHero.getDescription());
        if (oldHero.getStrengthBase() != null) existingDocument.setStrengthBase(oldHero.getStrengthBase());

        Hero saved = repository.save(existingDocument);
        sanitizeHero(saved);
        return new ResponseEntity<>(saved, HttpStatus.OK);

    }

    //Delete
    /**
     * This method represents the main delete functionality of the app
     * @param heroId this id belongs to the hero that needs to be deleted.
     * @return
     */
    @DeleteMapping("/id/{heroId}")
    public static ResponseEntity<Void> deleteHero(@PathVariable Long heroId) {
        if (!repository.existsById(heroId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(heroId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
