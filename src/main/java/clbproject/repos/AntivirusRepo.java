package clbproject.repos;


import clbproject.domain.Antivirus;
import clbproject.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AntivirusRepo extends JpaRepository<Antivirus, Integer> {

    List<Antivirus> findAll();
    Antivirus findByAvNameAndAvVersion(String avName, String avVersion);
    void deleteById(Integer id);
}
