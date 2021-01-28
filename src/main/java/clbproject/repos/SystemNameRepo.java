package clbproject.repos;

import clbproject.domain.SystemName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemNameRepo extends JpaRepository<SystemName, Integer> {

    List<SystemName> findAll();

    Optional<SystemName> findById(Integer id);
}
