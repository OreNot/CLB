package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class KeyCarriers {

    public KeyCarriers() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kc_seq_gen")
    @SequenceGenerator(name = "kc_seq_gen", sequenceName = "kc_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String keyCarriersName;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "kctype_id")
    private KeyCarriersType keyCarriersType;


    public KeyCarriersType getKeyCarriersType() {
        return keyCarriersType;
    }

    public void setKeyCarriersType(KeyCarriersType keyCarriersType) {
        this.keyCarriersType = keyCarriersType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyCarriersName() {
        return keyCarriersName;
    }

    public void setKeyCarriersName(String keyCarriersName) {
        this.keyCarriersName = keyCarriersName;
    }

}
