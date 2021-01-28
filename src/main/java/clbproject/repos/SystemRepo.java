package clbproject.repos;

import clbproject.domain.Organization;
import clbproject.domain.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;
import clbproject.domain.System;

import java.util.List;
import java.util.Optional;

public interface SystemRepo extends JpaRepository<System, Integer> {

    List<System> findAll();
    List<System> findByBankAndOrganization(Organization bank, Organization organization);
    Optional<System> findById(Integer id);
    System findByNameAndAndSystemType(String name, SystemType systemType);
}
