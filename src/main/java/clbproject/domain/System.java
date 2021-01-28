package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class System {

    public System() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sys_seq_gen")
    @SequenceGenerator(name = "sys_seq_gen", sequenceName = "sys_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String keyExpirationMonths;

    @NotNull
    private String cryptoType;

    @NotNull
    private boolean bankSoftware;


    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "system_type_id")
    private SystemType systemType;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "system_name_id")
    private SystemName systemName;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "es_type_id")
    private EsType esType;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id")
    private Organization bank;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public boolean isBankSoftware() {
        return bankSoftware;
    }

    public void setBankSoftware(boolean bankSoftware) {
        this.bankSoftware = bankSoftware;
    }

    public String getKeyExpirationMonths() {
        return keyExpirationMonths;
    }

    public void setKeyExpirationMonths(String keyExpirationMonths) {
        this.keyExpirationMonths = keyExpirationMonths;
    }

    public String getCryptoType() {
        return cryptoType;
    }

    public void setCryptoType(String cryptoType) {
        this.cryptoType = cryptoType;
    }

    public SystemName getSystemName() {
        return systemName;
    }

    public void setSystemName(SystemName systemName) {
        this.systemName = systemName;
    }

    public Organization getBank() {
        return bank;
    }

    public void setBank(Organization bank) {
        this.bank = bank;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(SystemType systemType) {
        this.systemType = systemType;
    }

    public EsType getEsType() {
        return esType;
    }

    public EsType getestype() {
        return esType;
    }

    public void setEsType(EsType esType) {
        this.esType = esType;
    }
}
