package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class EsType {

    public EsType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_seq_gen")
    @SequenceGenerator(name = "sys_seq_gen", sequenceName = "sys_sequence", initialValue = 1, allocationSize = 1)
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
