package clbproject.repos;

import clbproject.domain.Organization;
import clbproject.domain.SZI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SZIRepo extends JpaRepository<SZI, Integer> {
    List<SZI> findAll();
    SZI findBySziName(String sziName);
    Optional<SZI> findById(Integer id);
    void deleteById(Integer id);
}
