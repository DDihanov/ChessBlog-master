package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Category;

/**
 * Created by Austin on 06/12/2016.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
