package clbproject.repos;

import clbproject.domain.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffilationRepo extends JpaRepository<Affiliation, Integer> {

    List<Affiliation> findAll();
    Optional<Affiliation> findById(Integer id);
}
