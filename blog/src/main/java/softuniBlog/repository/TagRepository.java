package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Tag;

/**
 * Created by Austin on 06/12/2016.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);
}
