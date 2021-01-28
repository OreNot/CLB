package clbproject.repos;

import clbproject.domain.Affiliation;
import clbproject.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeRepo extends JpaRepository<DocumentType, Integer> {

    List<DocumentType> findAll();
    List<DocumentType> findByNameNot(String name);
    List<DocumentType> findByAffiliation(Affiliation affiliation);
    List<DocumentType> findByNameNotAndNameNot(String name1, String name2);
    DocumentType findByName(String name);
    DocumentType findByNameAndAffiliation(String name, Affiliation affiliation);
    DocumentType findByIdAndAffiliation(Integer id, Affiliation affiliation);
    Optional<DocumentType> findById(Integer id);
}
