package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SZI {

    public SZI() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "szi_seq_gen")
    @SequenceGenerator(name = "szi_seq_gen", sequenceName = "szi_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String sziName;


    @NotNull
    private String KS2;


    public String getKS2() {
        return KS2;
    }

    public void setKS2(String KS2) {
        this.KS2 = KS2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSziName() {
        return sziName;
    }

    public void setSziName(String sziName) {
        this.sziName = sziName;
    }


}
