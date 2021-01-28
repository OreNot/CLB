package clbproject.repos;


import clbproject.domain.ARM;
import clbproject.domain.ARMKc;
import clbproject.domain.KeyCarriers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ARMKcRepo extends JpaRepository<ARMKc, Integer> {

    List<ARMKc> findAll();
    List<ARMKc> findByArm(ARM arm);
    List<ARMKc> findByKeyCarriers(KeyCarriers keyCarriers);
}
