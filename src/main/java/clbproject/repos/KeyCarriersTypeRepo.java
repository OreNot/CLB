package clbproject.repos;


import clbproject.domain.KeyCarriersType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeyCarriersTypeRepo extends JpaRepository<KeyCarriersType, Integer> {
    List<KeyCarriersType> findAll();
    KeyCarriersType findByName(String name);
    Optional<KeyCarriersType> findById(Integer id);
}
