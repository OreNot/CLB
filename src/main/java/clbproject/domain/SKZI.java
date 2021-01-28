package clbproject.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
public class SKZI {

    public SKZI() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skzi_seq_gen")
    @SequenceGenerator(name = "skzi_seq_gen", sequenceName = "skzi_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String version;

    @NotNull
    private String realizationVariant;

    @NotNull
    private String KS;


    public String getKS() {
        return KS;
    }

    public void setKS(String KS) {
        this.KS = KS;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRealizationVariant() {
        return realizationVariant;
    }

    public void setRealizationVariant(String realixationVariant) {
        this.realizationVariant = realixationVariant;
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


