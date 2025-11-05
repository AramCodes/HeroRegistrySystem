package _3024c.heroregistry.controllers;

import org.hibernate.ObjectNotFoundException;
import org.owasp.encoder.Encode;
import _3024c.heroregistry.models.Hero;
import _3024c.heroregistry.repositories.HeroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("api/v1/heroes")
public class HeroController {

//    helpers
    private void sanitizeHero(Hero hero) {
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

    private final HeroRepository repository;
    public HeroController(HeroRepository repository) {
        this.repository = repository;
    }

    // Read
    @GetMapping("/all")
    public List<Hero> showHeroes(){

        return repository.findAll();
    }


    //  Create
    @PostMapping("/save")
    public ResponseEntity saveHero(@RequestBody Hero hero){
        sanitizeHero(hero);
        Hero saved = repository.save(hero);
        sanitizeHero(saved); // ensure returned object is safe
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }


    //Update
    //PutMapping("/save")
    @PutMapping("/update")
    public ResponseEntity updateHero(Hero oldHero){
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
        existingDocument.setActive(oldHero.getIsActive());
        if (oldHero.getDescription() != null) existingDocument.setDescription(oldHero.getDescription());
        if (oldHero.getStrengthBase() != null) existingDocument.setStrengthBase(oldHero.getStrengthBase());

        Hero saved = repository.save(existingDocument);
        sanitizeHero(saved); // ensure returned object is safe
        return new ResponseEntity<>(saved, HttpStatus.OK);

    }




    //Delete
    @DeleteMapping("/id/{heroId}")
    public ResponseEntity<Void> deleteHero(@PathVariable Long heroId) {
        if (!repository.existsById(heroId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        repository.deleteById(heroId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
