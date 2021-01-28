package clbproject.repos;


import clbproject.domain.KeyCarriers;
import clbproject.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeyCarriersRepo extends JpaRepository<KeyCarriers, Integer> {

    List<KeyCarriers> findAll();
    Optional<KeyCarriers> findById(Integer id);
    KeyCarriers findByKeyCarriersName(String kcName);
}
