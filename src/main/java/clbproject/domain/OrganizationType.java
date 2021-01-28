package clbproject.domain;

import javax.persistence.*;

@Entity
public class OrganizationType {

    public OrganizationType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orgtype_seq_gen")
    @SequenceGenerator(name = "orgtype_seq_gen", sequenceName = "orgtype_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
