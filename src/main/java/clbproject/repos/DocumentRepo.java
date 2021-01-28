package clbproject.repos;


import clbproject.domain.*;
import clbproject.domain.System;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepo extends JpaRepository<Document, Integer> {

    List<Document> findAll();
    Optional<Document> findById(Integer id);
    Document findByDocumentNumberAndDocumentDate(String documentNumber, String documentDate);
    List<Document> findByOrganization(Organization organization);
    List<Document> findByOrganizationAndDocumentTypeNot(Organization organization, DocumentType documentType);
    List<Document> findByOrganizationAndDocumentType(Organization organization, DocumentType documentType);
    Document findByOrganizationAndBankAndDocumentType(Organization organization, Organization bank, DocumentType documentType);
    List<Document> findBySkzi(SKZI skzi);
    List<Document> findBySkziAndDocumentTypeNot(SKZI skzi, DocumentType documentType);
    List<Document> findByAntivirus(Antivirus antivirus);
    List<Document> findBySzi(SZI szi);
    List<Document> findByPakUC(PakUC pakUC);
    List<Document> findByKeyCarriers(KeyCarriers keyCarriers);
    List<Document> findByArm(ARM arm);
    List<Document> findBySystemName(SystemName systemName);
    List<Document> findByAntivirusAndDocumentTypeNot(Antivirus antivirus, DocumentType documentType);
    Document findByAntivirusAndDocumentType(Antivirus antivirus, DocumentType documentType);
    List<Document> findBySziAndDocumentTypeNot(SZI szi, DocumentType documentType);
    Document findBySziAndDocumentType(SZI szi, DocumentType documentType);
    List<Document> findByPakUCAndDocumentTypeNot(PakUC pakUC, DocumentType documentType);
    List<Document> findByPakUCAndDocumentType(PakUC pakUC, DocumentType documentType);
    List<Document> findByKeyCarriersAndDocumentTypeNot(KeyCarriers keyCarriers, DocumentType documentType);
    List<Document> findByKeyCarriersAndDocumentType(KeyCarriers keyCarriers, DocumentType documentType);
    List<Document> findByArmAndDocumentTypeNot(ARM arm, DocumentType documentType);
    List<Document> findByArmAndDocumentType(ARM arm, DocumentType documentType);
    List<Document> findBySkziAndDocumentType(SKZI skzi, DocumentType documentType);
    List<Document> findBySystem(System system);
    List<Document> findBySystemAndDocumentTypeNot(System system, DocumentType documentType);
    List<Document> findBySystemAndDocumentType(System system, DocumentType documentType);
    List<Document> findByDocumentType(DocumentType documentType);
    Document findByDocumentNumberAndDocumentType(String documentNumber, DocumentType documentType);
    void deleteById(Integer id);
    void deleteByAntivirus(Antivirus antivirus);


}
