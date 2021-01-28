package clbproject.repos;

import clbproject.domain.ARM;
import clbproject.domain.ARMSystemName;
import clbproject.domain.SystemName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ARMSystemNameRepo extends JpaRepository<ARMSystemName, Integer> {

    List<ARMSystemName> findAll();
    List<ARMSystemName> findByArm(ARM arm);
    List<ARMSystemName> findBySystemName(SystemName systemName);
    List<ARMSystemName> findByArmNot(ARM arm);
    ARMSystemName findByArmAndSystemName(ARM arm, SystemName systemName);
}
