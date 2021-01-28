package clbproject.repos;

import clbproject.domain.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationTypeRepo  extends JpaRepository<OrganizationType, Integer> {

    List<OrganizationType> findAll();
    Optional<OrganizationType> findById(Integer id);
}
