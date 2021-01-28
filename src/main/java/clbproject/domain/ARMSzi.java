package clbproject.domain;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ARMSzi {

    public ARMSzi() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "armszi_seq_gen")
    @SequenceGenerator(name = "armszi_seq_gen", sequenceName = "armszi_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_id")
    private ARM arm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "szi_id")
    private SZI szi;


    public ARM getArm() {
        return arm;
    }

    public void setArm(ARM arm) {
        this.arm = arm;
    }

    public SZI getSzi() {
        return szi;
    }

    public void setSzi(SZI szi) {
        this.szi = szi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
