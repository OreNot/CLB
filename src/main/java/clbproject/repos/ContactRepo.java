package clbproject.repos;


import clbproject.domain.Contact;
import clbproject.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    List<Contact> findAll();
    Contact findByFioAndPositionAndPhoneNumberAndEmail(String fio, String position, String phoneNumber, String email);
    Optional<Contact> findById(Integer id);
    List<Contact> findByOrganization(Organization organization);

}
