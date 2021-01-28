package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class SystemName {

    public SystemName() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systemname_seq_gen")
    @SequenceGenerator(name = "systemname_seq_gen", sequenceName = "systemname_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String name;

    @Size(min = 0, max = 1000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
