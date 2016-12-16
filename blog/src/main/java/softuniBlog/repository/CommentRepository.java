package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Comment;

/**
 * Created by Austin on 16/12/2016.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
