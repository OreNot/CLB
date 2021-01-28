package clbproject.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Document {

    public Document() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq_gen")
    @SequenceGenerator(name = "document_seq_gen", sequenceName = "document_sequence", initialValue = 1, allocationSize = 1)
    private Integer id;

    @NotNull
    String documentNumber;

    @NotNull
    String documentDate;

    @NotNull
    String validUntilDate;

    String lastDocumentStatusUpdateDate;

    String lastDocumentStatusUpdateStatus;

    String documentName;

    String parameters;

    String link;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "systemname_id")
    private SystemName systemName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id")
    private Organization bank;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "system_id")
    private System system;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "arm_id")
    private ARM arm;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "av_id")
    private Antivirus antivirus;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "szi_id")
    private SZI szi;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "uc_id")
    private PakUC pakUC;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "kc_id")
    private KeyCarriers keyCarriers;
/*
    @Null
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "check_id")
    private Report report;
*/

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "skzi_id")
    private SKZI skzi;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "system_status_id")
    private SystemStatus systemStatus;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "trust_level_id")
    private TrustLevel trustLevel;

    @Size(min = 0, max = 1000)
    private String contactLog;

    private String documentFileName;

    private String osname;


    public SystemName getSystemName() {
        return systemName;
    }

    public void setSystemName(SystemName systemName) {
        this.systemName = systemName;
    }

    public ARM getArm() {
        return arm;
    }

    public void setArm(ARM arm) {
        this.arm = arm;
    }

    public Antivirus getAntivirus() {
        return antivirus;
    }

    public void setAntivirus(Antivirus antivirus) {
        this.antivirus = antivirus;
    }

    public SZI getSzi() {
        return szi;
    }

    public void setSzi(SZI szi) {
        this.szi = szi;
    }

    public PakUC getPakUC() {
        return pakUC;
    }

    public void setPakUC(PakUC pakUC) {
        this.pakUC = pakUC;
    }

    public KeyCarriers getKeyCarriers() {
        return keyCarriers;
    }

    public void setKeyCarriers(KeyCarriers keyCarriers) {
        this.keyCarriers = keyCarriers;
    }

    public String getOsname() {
        return osname;
    }

    public void setOsname(String osname) {
        this.osname = osname;
    }

    public String getDocumentFileName() {
        return documentFileName;
    }

    public String getdocumentfilename() {
        return documentFileName;
    }

    public void setDocumentFileName(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public String getLastDocumentStatusUpdateStatus() {
        return lastDocumentStatusUpdateStatus;
    }

    public String getlastdocumentstatusupdatestatus() {
        return lastDocumentStatusUpdateStatus;
    }

    public void setLastDocumentStatusUpdateStatus(String lastDocumentStatusUpdateStatus) {
        this.lastDocumentStatusUpdateStatus = lastDocumentStatusUpdateStatus;
    }

    public String getLastDocumentStatusUpdateDate() {
        return lastDocumentStatusUpdateDate;
    }

    public void setLastDocumentStatusUpdateDate(String lastDocumentStatusUpdateDate) {
        this.lastDocumentStatusUpdateDate = lastDocumentStatusUpdateDate;
    }

    public Organization getBank() {
        return bank;
    }

    public void setBank(Organization bank) {
        this.bank = bank;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SystemStatus getSystemStatus() {
        return systemStatus;
    }
    public SystemStatus getsystemstatus() {
        return systemStatus;
    }

    public void setSystemStatus(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    public TrustLevel getTrustLevel() {
        return trustLevel;
    }

    public TrustLevel gettrustlevel() {
        return trustLevel;
    }

    public void setTrustLevel(TrustLevel trustLevel) {
        this.trustLevel = trustLevel;
    }

    public String getContactLog() {
        return contactLog;
    }

    public String getcontactlog() {
        return contactLog;
    }

    public void setContactLog(String contactLog) {
        this.contactLog = contactLog;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getdocumentnumber() {
        return documentNumber;
    }

    public String getdocumentdate() {
        return documentDate;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentDate() {
        return documentDate;
    }


    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getValidUntilDate() {
        return validUntilDate;
    }

    public String getvaliduntildate() {
        return validUntilDate;
    }

    public void setValidUntilDate(String validUntilDate) {
        this.validUntilDate = validUntilDate;
    }

    public String getParameters() {
        return parameters;
    }

    public String getparameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }


    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public DocumentType getdocumenttype() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public SKZI getSkzi() {
        return skzi;
    }

    public void setSkzi(SKZI skzi) {
        this.skzi = skzi;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
