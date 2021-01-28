package clbproject.domain;

import javax.persistence.*;

@Entity
public class ARMSKZI {

    public ARMSKZI() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "armskzi_seq_gen")
    @SequenceGenerator(name = "armskzi_seq_gen", sequenceName = "armskzi_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_id")
    private ARM arm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "skzi_id")
    private SKZI skzi;

    public ARM getArm() {
        return arm;
    }

    public void setArm(ARM arm) {
        this.arm = arm;
    }

    public SKZI getSkzi() {
        return skzi;
    }

    public void setSkzi(SKZI skzi) {
        this.skzi = skzi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
