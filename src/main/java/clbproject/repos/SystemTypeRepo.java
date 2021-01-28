package clbproject.repos;

import clbproject.domain.System;
import clbproject.domain.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemTypeRepo extends JpaRepository<SystemType, Integer> {

    List<SystemType> findAll();
    SystemType findByName(String name);
}
