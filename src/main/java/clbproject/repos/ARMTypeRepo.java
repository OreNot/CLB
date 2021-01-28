package clbproject.repos;

import clbproject.domain.ARMType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ARMTypeRepo extends JpaRepository<ARMType, Integer> {

    List<ARMType> findAll();
    Optional<ARMType> findById(Integer id);

}
