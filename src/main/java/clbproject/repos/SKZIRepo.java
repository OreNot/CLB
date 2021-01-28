package clbproject.repos;

import clbproject.domain.Organization;
import clbproject.domain.SKZI;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SKZIRepo extends JpaRepository<SKZI, Integer> {

    List<SKZI> findAll();
    Optional<SKZI> findById(Integer id);
    SKZI findByNameAndVersionAndAndRealizationVariant(String name, String version, String realizationVariant);
    void deleteById(Integer id);

}
