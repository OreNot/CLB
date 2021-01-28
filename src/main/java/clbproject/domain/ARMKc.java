package clbproject.domain;

import javax.persistence.*;

@Entity
public class ARMKc {

    public ARMKc() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "armkc_seq_gen")
    @SequenceGenerator(name = "armkc_seq_gen", sequenceName = "armkc_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_id")
    private ARM arm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "kc_id")
    private KeyCarriers keyCarriers;


    public ARM getArm() {
        return arm;
    }

    public void setArm(ARM arm) {
        this.arm = arm;
    }

    public KeyCarriers getKeyCarriers() {
        return keyCarriers;
    }

    public void setKeyCarriers(KeyCarriers keyCarriers) {
        this.keyCarriers = keyCarriers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
