package clbproject.domain;

import javax.persistence.*;

@Entity
public class ARMSystemName {

    public ARMSystemName() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "armsysname_seq_gen")
    @SequenceGenerator(name = "armsysname_seq_gen", sequenceName = "armsysname_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_id")
    private ARM arm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "systemname_id")
    private SystemName systemName;

    public ARM getArm() {
        return arm;
    }

    public void setArm(ARM arm) {
        this.arm = arm;
    }

    public SystemName getSystemName() {
        return systemName;
    }

    public void setSystemName(SystemName systemName) {
        this.systemName = systemName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
