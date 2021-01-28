package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ARMType {

    public ARMType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "armtype_seq_gen")
    @SequenceGenerator(name = "armtype_seq_gen", sequenceName = "armtype_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
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
