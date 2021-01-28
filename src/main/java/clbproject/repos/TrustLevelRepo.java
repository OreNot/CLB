package clbproject.repos;

import clbproject.domain.TrustLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrustLevelRepo extends JpaRepository<TrustLevel, Integer> {

    List<TrustLevel> findAll();
    Optional<TrustLevel> findById(Integer id);
}
