package clbproject.repos;

import clbproject.domain.ARM;
import clbproject.domain.ARMSKZI;
import clbproject.domain.SKZI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ARMSKZIRepo extends JpaRepository<ARMSKZI, Integer> {

    List<ARMSKZI> findAll();
    List<ARMSKZI> findByArm(ARM arm);
    List<ARMSKZI> findBySkzi(SKZI skzi);
}
