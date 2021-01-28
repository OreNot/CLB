package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Antivirus {

    public Antivirus() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "av_seq_gen")
    @SequenceGenerator(name = "av_seq_gen", sequenceName = "av_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String avName;

    @NotNull
    private String avVersion;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvName() {
        return avName;
    }

    public void setAvName(String avName) {
        this.avName = avName;
    }

    public String getAvVersion() {
        return avVersion;
    }

    public void setAvVersion(String avVersion) {
        this.avVersion = avVersion;
    }
}
