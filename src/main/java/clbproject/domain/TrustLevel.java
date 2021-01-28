package clbproject.domain;

import javax.persistence.*;

@Entity
public class TrustLevel {

    public TrustLevel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trustlevel_seq_gen")
    @SequenceGenerator(name = "trustlevel_seq_gen", sequenceName = "trustlevel_sequence", initialValue = 1, allocationSize = 1)
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

    public String getname() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
