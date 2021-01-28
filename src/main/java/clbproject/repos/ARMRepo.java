package clbproject.repos;

import clbproject.domain.ARM;

import clbproject.domain.Antivirus;
import clbproject.domain.Organization;
import clbproject.domain.PakUC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ARMRepo extends JpaRepository<ARM, Integer> {

    List<ARM> findAll();
    List<ARM> findByAntivirus(Antivirus antivirus);
    List<ARM> findByPakUC(PakUC pakUC);
    List<ARM> findByOrganization(Organization organization);
    ARM findByArmName(String armName);
    ARM findByArmNameAndOrganization(String armName, Organization organization);
    Optional<ARM> findById(Integer id);
}
