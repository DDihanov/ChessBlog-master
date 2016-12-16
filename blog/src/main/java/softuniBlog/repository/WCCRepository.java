package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.WCCGame;


public interface WCCRepository extends JpaRepository<WCCGame, Integer> {
}
