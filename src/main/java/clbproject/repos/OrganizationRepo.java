package clbproject.repos;

import clbproject.domain.Organization;
import clbproject.domain.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepo extends JpaRepository<Organization, Integer> {

    List<Organization> findAll();
    List<Organization> findByInn(String inn);
    Optional<Organization> findById(Integer id);
    Organization findByInnAndOrganizationType(String inn, OrganizationType organizationType);
    Organization findByNameAndInnAndOrganizationType(String name, String inn, OrganizationType organizationType);
    Organization findByInnAndGidAndOrganizationType(String inn, String gid, OrganizationType organizationType);
    List<Organization> findByOrganizationType(OrganizationType organizationType);

    Organization findByNameAndOrganizationType(String orgName, OrganizationType organizationType);

}
