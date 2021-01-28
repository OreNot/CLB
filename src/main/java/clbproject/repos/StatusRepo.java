package clbproject.repos;

import clbproject.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepo extends JpaRepository<Status, Integer> {

    List<Status> findAll();
    Optional<Status> findById(Integer id);
    Status findByName(String name);

}
