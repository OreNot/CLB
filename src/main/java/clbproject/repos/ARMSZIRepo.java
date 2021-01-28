package clbproject.repos;

import clbproject.domain.ARM;
import clbproject.domain.ARMSzi;
import clbproject.domain.SZI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ARMSZIRepo extends JpaRepository<ARMSzi, Integer> {

    List<ARMSzi> findAll();
    List<ARMSzi> findByArm(ARM arm);
    List<ARMSzi> findBySzi(SZI szi);
    Optional<ARMSzi> findById(Integer id);
    void deleteAllByArm(ARM arm);
    void deleteByArm(ARM arm);
}
