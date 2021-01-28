package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PakUC {

    public PakUC() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uc_seq_gen")
    @SequenceGenerator(name = "uc_seq_gen", sequenceName = "uc_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String pakUCName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPakUCName() {
        return pakUCName;
    }

    public void setPakUCName(String pakUCName) {
        this.pakUCName = pakUCName;
    }
}
