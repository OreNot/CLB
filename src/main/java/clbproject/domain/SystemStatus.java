package clbproject.domain;

import javax.persistence.*;

@Entity
public class SystemStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systemstatus_seq_gen")
    @SequenceGenerator(name = "systemstatus_seq_gen", sequenceName = "systemstatus_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String name;

    public SystemStatus() {
    }

    public SystemStatus(String name) {
        this.name = name;
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
