package clbproject.domain;


import javax.persistence.*;

@Entity
public class DocumentType {

    public DocumentType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctype_seq_gen")
    @SequenceGenerator(name = "doctype_seq_gen", sequenceName = "doctype_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "affiliation_id")
    private Affiliation affiliation;

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

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }
}
