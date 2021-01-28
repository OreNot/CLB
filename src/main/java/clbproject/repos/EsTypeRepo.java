package clbproject.repos;

import clbproject.domain.DocumentType;
import clbproject.domain.EsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EsTypeRepo extends JpaRepository<EsType, Integer> {

    List<EsType> findAll();
    EsType findByName(String name);
}
