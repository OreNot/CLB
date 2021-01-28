package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ARM {

    public ARM() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arm_seq_gen")
    @SequenceGenerator(name = "arm_seq_gen", sequenceName = "arm_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String armName;

    private String armNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_type_id")
    private ARMType armType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "av_id")
    private Antivirus antivirus;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "uc_id")
    private PakUC pakUC;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;



    public String getArmNumber() {
        return armNumber;
    }

    public void setArmNumber(String armNumber) {
        this.armNumber = armNumber;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArmName() {
        return armName;
    }

    public void setArmName(String armName) {
        this.armName = armName;
    }

    public ARMType getArmType() {
        return armType;
    }

    public void setArmType(ARMType armType) {
        this.armType = armType;
    }

    public Antivirus getAntivirus() {
        return antivirus;
    }

    public void setAntivirus(Antivirus antivirus) {
        this.antivirus = antivirus;
    }

    public PakUC getPakUC() {
        return pakUC;
    }

    public void setPakUC(PakUC pakUC) {
        this.pakUC = pakUC;
    }
}
