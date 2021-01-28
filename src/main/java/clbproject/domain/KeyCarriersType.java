package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class KeyCarriersType {
    public KeyCarriersType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kctype_seq_gen")
    @SequenceGenerator(name = "kctype_seq_gen", sequenceName = "kctype_sequence", initialValue = 1, allocationSize = 1)
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
