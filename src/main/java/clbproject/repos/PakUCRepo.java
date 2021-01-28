package clbproject.repos;

import clbproject.domain.Organization;
import clbproject.domain.PakUC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PakUCRepo extends JpaRepository<PakUC, Integer> {
    List<PakUC> findAll();
    PakUC findByPakUCName(String pakUcName);
    void deleteById(Integer id);
}
