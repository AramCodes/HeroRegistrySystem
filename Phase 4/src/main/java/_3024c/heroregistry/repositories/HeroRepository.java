package _3024c.heroregistry.repositories;

import _3024c.heroregistry.models.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 /**
 *   this is the hero repository gaining all attributes through inheritance from the JPA Repository
 *   which allows safer mutation and query transactions against the repo
 **/
@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    /**
     * This query return the average rating of all heroes currently loaded in the Db
     * @return the average of all heroes in the DB
     */
    @Query("select avg(h.rating) from Hero h")
    Double findAverageRating();
}
