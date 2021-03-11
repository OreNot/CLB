package clbproject.controller;

import clbproject.domain.*;
import clbproject.domain.Document;
import clbproject.domain.System;
import clbproject.repos.*;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.print.Doc;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

@Controller
//@PreAuthorize("hasAuthority('USER')")
public class OperatorController {

    @Value("${download.path}")
    private String downloadloadPath;

    @Value("${logfile}")
    private String logPath;

    @Value("${manlogfile}")
    private String manLogPath;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    @Value("${smtpserver}")
    private String smtpserver;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    OrganizationRepo organizationRepo;

    @Autowired
    OrganizationTypeRepo organizationTypeRepo;

    @Autowired
    ContactRepo contactRepo;

    @Autowired
    DocumentRepo documentRepo;

    @Autowired
    StatusRepo statusRepo;

    @Autowired
    DocumentTypeRepo documentTypeRepo;

    @Autowired
    SKZIRepo skziRepo;

    @Autowired
    SystemRepo systemRepo;

    @Autowired
    SystemTypeRepo systemTypeRepo;

    @Autowired
    SystemStatusRepo systemStatusRepo;

    @Autowired
    AffilationRepo affilationRepo;

    @Autowired
    TrustLevelRepo trustLevelRepo;

    @Autowired
    EsTypeRepo esTypeRepo;

    @Autowired
    AntivirusRepo antivirusRepo;

    @Autowired
    SZIRepo sziRepo;

    @Autowired
    ARMRepo armRepo;

    @Autowired
    PakUCRepo pakUCRepo;

    @Autowired
    KeyCarriersRepo keyCarriersRepo;

    @Autowired
    KeyCarriersTypeRepo keyCarriersTypeRepo;

    @Autowired
    SystemNameRepo systemNameRepo;

    @Autowired
    ARMSZIRepo armsziRepo;

    @Autowired
    ARMSKZIRepo armskziRepo;

    @Autowired
    ARMKcRepo armKcRepo;

    @Autowired
    ARMSystemNameRepo armSystemNameRepo;

    @Autowired
    ARMTypeRepo armTypeRepo;


    private static String CREDS_FILE_NAME = "C:\\Creds\\creds.txt";

   // private static String REPORT_TEMPLATE_FILE = "resources\\docs\\report_template.docx";
    private static String REPORT_TEMPLATE_FILE = "C:\\reportTemplates\\report_template.docx";
    private static String REPORTS_CATALOG_NAME = "C:\\reports\\";


    private static final Logger log = Logger.getLogger(OperatorController.class);

    static String username = "";
    static String password = "";

    static String[] creds;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
    SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    SimpleDateFormat reportFileNameDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS");
    SimpleDateFormat docsFileNameDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS");
    SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    String phonePattern = "^(\\((\\+55)*\\d{2}\\)\\d{2}-\\d{3}-\\d{3})$";
    String emailPattern = "[a-zA-Z]{1}[a-zA-Z\\d\\u002E\\u005F]+@([a-zA-Z]+\\u002E){1,2}((net)|(com)|(org)|(ru))";


    @GetMapping("/deleteelement")
    public String deleteelement(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String returnid,
            @RequestParam(required = false, defaultValue = "0") String returntype,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        switch (type)
        {
            case "organization" :
            {
               Organization organization = organizationRepo.findById(Integer.parseInt(id)).get();


               for (Document document : documentRepo.findByOrganization(organization))
               {
                   documentRepo.delete(document);
                   log.info("Документ " + document.getDocumentName() + " №  " + document.getDocumentNumber() + " от " + document.getdocumentdate() + " удалён;");
               }



               for (ARM arm : armRepo.findByOrganization(organization))
               {
                   for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm))
                   {
                       armSystemNameRepo.delete(armSystemName);
                   }
                   armRepo.delete(arm);
                   log.info("АРМ " + arm.getArmName() + " удалён;");
               }

               for (Contact contact : contactRepo.findByOrganization(organization))
               {
                   contactRepo.delete(contact);
               }
               organizationRepo.delete(organization);
                log.info("Организация " + organization.getName() + " удалёна;");

                switch (returntype) {

                    case "organizations" : {
                        List<Organization> organizations = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(2).get());
                        model.put("organizations", organizations);
                        return "showorganizations";
                    }
                    case "banks" :
                    {
                        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
                        model.put("banks", banks);
                        return "showbanks";
                    }
                }
            }

            case "antivirus" :
            {
                Antivirus antivirus = antivirusRepo.findById(Integer.parseInt(id)).get();

                model.put("id", returnid);

                switch (returntype)
                {
                    case "antiviruses" :
                    {
                        for(Document delDoc : documentRepo.findByAntivirus(antivirus))
                        {
                            documentRepo.deleteById(delDoc.getId());
                        }

                        for (ARM arm : armRepo.findByAntivirus(antivirus))
                        {
                            arm.setAntivirus(null);
                        }


                        antivirusRepo.delete(antivirus);
                        log.info("Антивирус " + antivirus.getAvName() + " удалён;");

                        List<Antivirus> antiviruses = antivirusRepo.findAll();


                        model.put("antiviruses", antiviruses);
                        return "showantiviruses";
                    }

                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();

                        arm.setAntivirus(null);


                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        if (arm.getAntivirus() != null) {
                            antivirusList.add(arm.getAntivirus());
                        }
                        List<PakUC> pakUCList = new ArrayList<>();
                        if (arm.getPakUC() != null) {
                            pakUCList.add(arm.getPakUC());
                        }

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }

            }
            case "szi" :
            {
                SZI szi = sziRepo.findById(Integer.parseInt(id)).get();



                model.put("id", returnid);

                switch (returntype)
                {
                    case "szis" :
                    {

                        for (Document document : documentRepo.findBySzi(szi))
                        {
                            documentRepo.delete(document);
                            log.info("Документ " + document.getDocumentName() + " №  " + document.getDocumentNumber() + " от " + document.getdocumentdate() + " удалён;");
                        }
                        List<ARMSzi> armSziList = armsziRepo.findBySzi(szi);
                        for (ARMSzi armSzi : armSziList)
                        {
                            armsziRepo.delete(armSzi);

                        }
                        sziRepo.delete(szi);
                        log.info("СЗИ " + szi.getSziName() + " удалено;");

                        List<SZI> szis = sziRepo.findAll();


                        model.put("szis", szis);
                        return "showszis";
                    }
                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();
                        List<ARMSzi> armSziList = armsziRepo.findByArm(arm);
                        for (ARMSzi armSzi : armSziList)
                        {
                            armsziRepo.delete(armSzi);
                        }

                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        antivirusList.add(arm.getAntivirus());

                        List<PakUC> pakUCList = new ArrayList<>();
                        pakUCList.add(arm.getPakUC());

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }
            }

            case "pakuc" :
            {
                PakUC pakUC = pakUCRepo.findById(Integer.parseInt(id)).get();

                model.put("id", returnid);

                switch (returntype)
                {
                    case "pakucs" :
                    {
                        for(Document delDoc : documentRepo.findByPakUC(pakUC))
                        {
                            documentRepo.deleteById(delDoc.getId());
                        }
                        pakUCRepo.delete(pakUC);
                        log.info("ПАК УЦ " + pakUC.getPakUCName() + " удалён;");

                        List<PakUC> pakUCList = pakUCRepo.findAll();

                        model.put("pakUCList", pakUCList);

                        return "showpakcs";
                    }

                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();
                        arm.setPakUC(null);

                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        if (arm.getAntivirus() != null) {
                            antivirusList.add(arm.getAntivirus());
                        }
                        List<PakUC> pakUCList = new ArrayList<>();
                        if (arm.getPakUC() != null) {
                            pakUCList.add(arm.getPakUC());
                        }

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }
            }
            case "keycarriers" :
            {
                KeyCarriers kc = keyCarriersRepo.findById(Integer.parseInt(id)).get();



                model.put("id", returnid);
                switch (returntype) {

                    case "keycarrierses": {

                        for (Document document : documentRepo.findByKeyCarriers(kc))
                        {
                            documentRepo.delete(document);
                        }
                        List<ARMKc> armKcList = armKcRepo.findByKeyCarriers(kc);
                        for (ARMKc armKc : armKcList)
                        {
                            armKcRepo.delete(armKc);
                        }

                        keyCarriersRepo.delete(kc);
                        log.info("Ключевой носитель " + kc.getKeyCarriersName() + " удалён;");

                        List<KeyCarriers> keyCarriersList = keyCarriersRepo.findAll();


                        model.put("keyCarriersList", keyCarriersList);
                       return "showkcs";
                    }

                    case "arm": {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();
                        List<ARMKc> armKcList = armKcRepo.findByArm(arm);
                        for (ARMKc armKc : armKcList)
                        {
                            armKcRepo.delete(armKc);
                        }

                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        antivirusList.add(arm.getAntivirus());

                        List<PakUC> pakUCList = new ArrayList<>();
                        pakUCList.add(arm.getPakUC());

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }
            }
            case "system" : {
                System system = systemRepo.findById(Integer.parseInt(id)).get();


                switch (returntype) {
                    case "systems": {
                        for (Document delDoc : documentRepo.findBySystem(system)) {
                            documentRepo.deleteById(delDoc.getId());
                        }
                        systemRepo.deleteById(Integer.parseInt(id));
                        log.info("Система " + systemRepo.findById(Integer.parseInt(id)).get().getSystemName() + " удалена;");

                        List<System> systems = systemRepo.findAll();
                        model.put("systems", systems);

                        return "showsystems";
                    }
                }
            }
            case "skzi" :
            {
                SKZI skzi = skziRepo.findById(Integer.parseInt(id)).get();


                switch (returntype) {
                    case "skzis": {
                        for(Document delDoc : documentRepo.findBySkzi(skzi))
                        {
                            documentRepo.deleteById(delDoc.getId());
                            log.info("Документ " + documentRepo.findById(delDoc.getId()).get().getDocumentName() + " № " + documentRepo.findById(delDoc.getId()).get().getDocumentNumber() + " от " + documentRepo.findById(delDoc.getId()).get().getDocumentDate() +" удалён;");
                        }
                        List<ARMSKZI> armSkzis = armskziRepo.findBySkzi(skzi);
                        for (ARMSKZI armskzi : armSkzis)
                        {
                            armskziRepo.delete(armskzi);
                        }
                        skziRepo.deleteById(Integer.parseInt(id));
                        log.info("СКЗИ " + skziRepo.findById(Integer.parseInt(id)) +" удалено;");


                        List<SKZI> skzis = skziRepo.findAll();
                        model.put("skzis", skzis);

                        return "showskzis";
                    }

                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();
                        List<ARMSKZI> armskziList = armskziRepo.findByArm(arm);
                        for (ARMSKZI armskzi : armskziList)
                        {
                            armskziRepo.delete(armskzi);
                        }

                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        antivirusList.add(arm.getAntivirus());

                        List<PakUC> pakUCList = new ArrayList<>();
                        pakUCList.add(arm.getPakUC());

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }

            }

            case "arm" :
            {
                ARM arm = armRepo.findById(Integer.parseInt(id)).get();
                for (ARMSzi armSzi : armsziRepo.findByArm(arm))
                {
                    armsziRepo.delete(armSzi);
                }
                for (ARMSKZI armskzi : armskziRepo.findByArm(arm))
                {
                    armskziRepo.delete(armskzi);
                }
                for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm))
                {
                    armSystemNameRepo.delete(armSystemName);
                }
                for (ARMKc armKc : armKcRepo.findByArm(arm))
                {
                    armKcRepo.delete(armKc);
                }
                for(Document delDoc : documentRepo.findByArm(arm))
                {
                    documentRepo.deleteById(delDoc.getId());
                }


                armRepo.delete(arm);
                log.info("АРМ " + arm.getArmName() +" удалён;");


                model.put("id", returnid);

                switch (returntype)
                {
                    case "organization" :
                    {
                        Organization organization = organizationRepo.findById(Integer.parseInt(returnid)).get();

                        List<Contact> contacts = contactRepo.findByOrganization(organization);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(organization);



                        model.put("contacts", contacts);
                        model.put("documents", documents);

                        model.put("armList", armList);

                        model.put("organization", organization);
                        return "showoneorganization";
                    }

                    case "bank" :
                    {
                        Organization organization = organizationRepo.findById(Integer.parseInt(returnid)).get();

                        List<Contact> contacts = contactRepo.findByOrganization(organization);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(organization);


                        model.put("contacts", contacts);
                        model.put("documents", documents);
                        model.put("armList", armList);
                        model.put("bank", organization);
                        return "showonebank";
                    }
                }

            }

            case "contact" :
            {
                contactRepo.deleteById(Integer.parseInt(id));
                log.info("Контактное лицо " + contactRepo.findById(Integer.parseInt(id)) +" удалено;");

                switch (returntype)
                {

                    case "organization" :
                    {
                        Organization organization = organizationRepo.findById(Integer.parseInt(returnid)).get();
                        List<Contact> contacts = contactRepo.findByOrganization(organization);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(organization);

                        model.put("contacts", contacts);
                        model.put("documents", documents);

                        model.put("armList", armList);

                        model.put("organization", organization);
                        return "showoneorganization";

                    }

                    case "bank" :
                    {
                        Organization organization = organizationRepo.findById(Integer.parseInt(returnid)).get();
                        List<Contact> contacts = contactRepo.findByOrganization(organization);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(organization);

                        model.put("contacts", contacts);
                        model.put("documents", documents);
                        model.put("armList", armList);
                        model.put("bank", organization);
                        return "showonebank";

                    }
                }
            }
            case "document" :
            {
                documentRepo.deleteById(Integer.parseInt(id));

                switch (returntype)
                {
                    case "systemnames" :
                    {
                        List<SystemName> systemNames = systemNameRepo.findAll();
                        List<Document> documents = documentRepo.findBySystemName(systemNames.get(0));



                        model.put("selectedsystemname", returnid.equals("0") ? systemNames.get(0).getName() : returnid);


                        model.put("systemnames", systemNames);
                        model.put("documents", documents);

                        return "defaultdocsforsystems";
                    }
                    case "system" :
                    {
                        System system = systemRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findBySystemAndDocumentTypeNot(system, documentTypeRepo.findById(45).get());

                        List<ARM> bankArms = new ArrayList<>();
                        List<ARM> organizationArms = new ArrayList<>();
                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findBySystemName(system.getSystemName());
                        List<ARM> allArmsBySystemName = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            allArmsBySystemName.add(armSystemName.getArm());
                        }

                        for (ARM arm : allArmsBySystemName)
                        {
                            if (arm.getOrganization() == system.getBank())
                            {
                                bankArms.add(arm);
                            }
                            if (arm.getOrganization() == system.getOrganization())
                            {
                                organizationArms.add(arm);
                            }
                        }
                        model.put("id", returnid);
                        model.put("system", system);
                        model.put("bankarms", bankArms);
                        model.put("organizationarms", organizationArms);
                        model.put("documents", documents);

                        return "showonesystem";
                    }
                    case "antivirus" :
                    {
                        Antivirus antivirus = antivirusRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findByAntivirusAndDocumentTypeNot(antivirus, documentTypeRepo.findById(45).get());
                        model.put("id", returnid);
                        model.put("antivirus", antivirus);
                        model.put("documents", documents);

                        return "showoneantivirus";
                    }

                    case "organization" :
                    {
                        Organization organization = organizationRepo.findById(Integer.parseInt(returnid)).get();
                        List<Contact> contacts = contactRepo.findByOrganization(organization);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(organization);


                        model.put("contacts", contacts);
                        model.put("documents", documents);
                        model.put("armList", armList);
                        model.put("id", returnid);
                        model.put("organization", organization);
                        return "showoneorganization";
                    }

                    case "bank" :
                    {
                        Organization bank = organizationRepo.findById(Integer.parseInt(returnid)).get();
                        List<Contact> contacts = contactRepo.findByOrganization(bank);
                        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(bank, documentTypeRepo.findById(45).get());
                        List<ARM> armList = armRepo.findByOrganization(bank);

                        model.put("contacts", contacts);
                        model.put("documents", documents);
                        model.put("armList", armList);
                        model.put("id", returnid);
                        model.put("bank", bank);
                        return "showonebank";
                    }

                    case "szi" :
                    {
                        SZI szi = sziRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findBySziAndDocumentTypeNot(szi, documentTypeRepo.findById(45).get());
                        model.put("id", returnid);
                        model.put("szi", szi);
                        model.put("documents", documents);

                        return "showoneszi";
                    }
                    case "pakuc" :
                    {
                        PakUC pakuc = pakUCRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findByPakUCAndDocumentTypeNot(pakuc, documentTypeRepo.findById(45).get());
                        model.put("id", returnid);
                        model.put("pakuc", pakuc);
                        model.put("documents", documents);

                        return "showonepakuc";
                    }
                    case "keycarriers" :
                    {
                        KeyCarriers keycarriers = keyCarriersRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findByKeyCarriersAndDocumentTypeNot(keycarriers, documentTypeRepo.findById(45).get());
                        model.put("id", returnid);
                        model.put("keycarriers", keycarriers);
                        model.put("documents", documents);

                        return "showonekeycarriers";
                    }

                    case "skzi" :
                    {
                        SKZI skzi = skziRepo.findById(Integer.parseInt(returnid)).get();
                        List<Document> documents = documentRepo.findBySkziAndDocumentTypeNot(skzi, documentTypeRepo.findById(45).get());
                        model.put("id", id);
                        model.put("skzi", skzi);
                        model.put("documents", documents);

                        return "showoneskzi";
                    }

                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(returnid)).get();


                        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

                        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
                        List<SZI> szis = new ArrayList<>();

                        for (ARMSzi armSzi : armSzis)
                        {
                            szis.add(armSzi.getSzi());
                        }

                        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
                        List<SKZI> skzis = new ArrayList<>();

                        for (ARMSKZI armSkzi : armSkzis)
                        {
                            skzis.add(armSkzi.getSkzi());
                        }

                        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
                        List<KeyCarriers> kcs = new ArrayList<>();

                        for (ARMKc armKc : armKcs)
                        {
                            kcs.add(armKc.getKeyCarriers());
                        }

                        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
                        List<SystemName> systemNames = new ArrayList<>();

                        for (ARMSystemName armSystemName : armSystemNames)
                        {
                            systemNames.add(armSystemName.getSystemName());
                        }

                        List<Antivirus> antivirusList = new ArrayList<>();
                        antivirusList.add(arm.getAntivirus());

                        List<PakUC> pakUCList = new ArrayList<>();
                        pakUCList.add(arm.getPakUC());

                        model.put("arm", arm);
                        model.put("documents", documents);
                        model.put("szis", szis);
                        model.put("skzis", skzis);
                        model.put("kcs", kcs);
                        model.put("systemnames", systemNames);
                        model.put("antivirusList", antivirusList);
                        model.put("pakUCList", pakUCList);
                        return "showonearm";
                    }
                }

            }


            default:
                return "main";

        }

    }


    @GetMapping("/delsysdocument")
    public String delsysdocument(
            @RequestParam(required = false, defaultValue = "0") String docid,
            @RequestParam(required = false, defaultValue = "0") String sysid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        Document delDocument = documentRepo.findById(Integer.parseInt(docid)).get();
        System system = systemRepo.findById(Integer.parseInt(sysid)).get();

        delDocument.setSystem(null);
        documentRepo.save(delDocument);
        List<ARM> bankArms = new ArrayList<>();
        List<ARM> organizationArms = new ArrayList<>();
        List<ARMSystemName> armSystemNames = armSystemNameRepo.findBySystemName(system.getSystemName());
        List<ARM> allArmsBySystemName = new ArrayList<>();

        for (ARMSystemName armSystemName : armSystemNames)
        {
            allArmsBySystemName.add(armSystemName.getArm());
        }

        for (ARM arm : allArmsBySystemName)
        {
            if (arm.getOrganization() == system.getBank())
            {
                bankArms.add(arm);
            }
            if (arm.getOrganization() == system.getOrganization())
            {
                organizationArms.add(arm);
            }
        }

        List<Document> documents = documentRepo.findBySystem(system);

        model.put("skziid", sysid);
        model.put("system", system);
        model.put("documents", documents);
        model.put("bankarms", bankArms);
        model.put("organizationarms", organizationArms);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "showonesystem";
    }


    @GetMapping("/addarm")
    public String addarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        Organization organization = organizationRepo.findById(Integer.parseInt(id)).get();
        List<Antivirus> antiviruses = antivirusRepo.findAll();
        List<SZI> szis = sziRepo.findAll();
        List<PakUC> pakucs = pakUCRepo.findAll();
        List<SKZI> skzis = skziRepo.findAll();
        List<KeyCarriers> keyCarriers = keyCarriersRepo.findAll();
        List<SystemName> systemNames = systemNameRepo.findAll();


        model.put("organization", organization);
        model.put("antiviruses", antiviruses);
        model.put("keycarriers", keyCarriers);
        model.put("systemnames", systemNames);
        model.put("szis", szis);
        model.put("pakucs", pakucs);
        model.put("skzis", skzis);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addarm";
    }

    @PostMapping("/addarm")
    public String addarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String armname,
            @RequestParam(required = false, defaultValue = "0") String armnumber,
            @RequestParam(required = false, defaultValue = "0") String armtype,
            @RequestParam(required = false, defaultValue = "0") String antivirus,
            @RequestParam(required = false, defaultValue = "0") String szi,
            @RequestParam(required = false, defaultValue = "0") String pakuc,
            @RequestParam(required = false, defaultValue = "0") String skzi,
            @RequestParam(required = false, defaultValue = "0") String kc,
            @RequestParam(required = false, defaultValue = "0") List<String> sysids,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        Organization organization = organizationRepo.findById(Integer.parseInt(id)).get();
        armname = armname.trim();
        armnumber = armnumber.trim();

        if (!armname.equals("0"))
        {
            ARM newArm = new ARM();

            newArm.setArmName(armname);
            if (armtype.equals("1"))
            {

            }
            newArm.setArmNumber(armnumber);
            newArm.setOrganization(organization);
            newArm.setArmType(armTypeRepo.findById(Integer.parseInt(armtype)).get());

            if (!antivirus.equals("0")) {
                newArm.setAntivirus(antivirusRepo.findById(Integer.parseInt(antivirus)).get());
            }
            if (armtype.equals("1") && !pakuc.equals("0"))
            {
                newArm.setPakUC(pakUCRepo.findById(Integer.parseInt(pakuc)).get());
            }

            armRepo.save(newArm);
            log.info("АРМ " + newArm.getArmName() + " создан;");
            log.info("АРМу " + newArm.getArmName() + " добавлен антивирус " + antivirusRepo.findById(Integer.parseInt(antivirus)).get().getAvName() +  ";");
            log.info("АРМу " + newArm.getArmName() + " добавлен ПАК УЦ " + pakUCRepo.findById(Integer.parseInt(pakuc)).get().getPakUCName() +  ";");
            newArm = armRepo.findByArmNameAndOrganization(newArm.getArmName(), organization);

            if (!szi.equals("0")) {
                SZI sziForArm = sziRepo.findById(Integer.parseInt(szi)).get();

                ARMSzi armSzi = new ARMSzi();
                armSzi.setArm(newArm);
                armSzi.setSzi(sziForArm);
                log.info("АРМу " + newArm.getArmName() + " добавлено СЗИ " + sziForArm.getSziName() +  ";");
                armsziRepo.save(armSzi);
            }
            if (!skzi.equals("0")) {
                SKZI skziForArm = skziRepo.findById(Integer.parseInt(skzi)).get();

                ARMSKZI armskzi = new ARMSKZI();
                armskzi.setArm(newArm);
                armskzi.setSkzi(skziForArm);
                log.info("АРМу " + newArm.getArmName() + " добавлено СКЗИ " + skziForArm.getName() +  ";");
                armskziRepo.save(armskzi);
            }
            if (!kc.equals("0")) {
                KeyCarriers kcForArm = keyCarriersRepo.findById(Integer.parseInt(kc)).get();

                ARMKc armKc = new ARMKc();
                armKc.setArm(newArm);
                armKc.setKeyCarriers(kcForArm);
                log.info("АРМу " + newArm.getArmName() + " добавлен ключевой носитель " + kcForArm.getKeyCarriersName() +  ";");
                armKcRepo.save(armKc);
            }

            if (sysids.size() > 0) {
                for (String sysid : sysids) {
                    ARMSystemName armSystemName = new ARMSystemName();
                    armSystemName.setArm(newArm);
                    log.info("АРМу " + newArm.getArmName() + " добавлена система " + armSystemName +  ";");
                    armSystemName.setSystemName(systemNameRepo.findById(Integer.parseInt(sysid)).get());
                    armSystemNameRepo.save(armSystemName);
                }
            }
            armRepo.save(newArm);
            model.put("error", "*АРМ " + armname + " добавлен");

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("armname", armname.equals("0") ? "" : armname);
            model.put("armnumber", armnumber.equals("0") ? "" : armnumber);
        }

        List<Antivirus> antiviruses = antivirusRepo.findAll();
        List<SZI> szis = sziRepo.findAll();
        List<PakUC> pakucs = pakUCRepo.findAll();
        List<SKZI> skzis = skziRepo.findAll();
        List<KeyCarriers> keycarriers = keyCarriersRepo.findAll();
        List<SystemName> systemNames = systemNameRepo.findAll();

        model.put("organization", organization);
        model.put("antiviruses", antiviruses);
        model.put("keycarriers", keycarriers);
        model.put("systemnames", systemNames);
        model.put("szis", szis);
        model.put("pakucs", pakucs);
        model.put("skzis", skzis);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addarm";
    }

    @GetMapping("/addsystemforarm")
    public String addsystemforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        List<ARMSystemName> armSystemNameList = armSystemNameRepo.findByArm(arm);
        List<SystemName> armSystemNames = new ArrayList<>();

        for (ARMSystemName armSystemName : armSystemNameList)
        {
            armSystemNames.add(armSystemName.getSystemName());
        }


        List<SystemName> armNotSystemNames = systemNameRepo.findAll();

        for (SystemName armNotSystemName : armSystemNames)
        {
            armNotSystemNames.remove(armNotSystemName);
        }



        model.put("armsystemnames", armSystemNames);
        model.put("armnotsystemnames", armNotSystemNames);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addsystemforarm";
    }

    @PostMapping("/addsystemforarm")
    public String addsystemforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") List<String> sysids,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        List<ARMSystemName> armSystemNameDelList = armSystemNameRepo.findByArm(arm);
        for (ARMSystemName armSystemName : armSystemNameDelList)
        {
            armSystemNameRepo.delete(armSystemName);
        }

        if (sysids.size() > 0) {
            for (String sysid : sysids) {
                if (armSystemNameRepo.findByArmAndSystemName(arm, systemNameRepo.findById(Integer.parseInt(sysid)).get()) == null)
                {

                    ARMSystemName armSystemName = new ARMSystemName();
                armSystemName.setArm(arm);
                armSystemName.setSystemName(systemNameRepo.findById(Integer.parseInt(sysid)).get());
                armSystemNameRepo.save(armSystemName);
            }
            }
        }
        List<ARMSystemName> armSystemNameList = armSystemNameRepo.findByArm(arm);
        List<SystemName> armSystemNames = new ArrayList<>();

        for (ARMSystemName armSystemName : armSystemNameList)
        {
            armSystemNames.add(armSystemName.getSystemName());
        }


        List<SystemName> armNotSystemNames = systemNameRepo.findAll();

        for (SystemName armNotSystemName : armSystemNames)
        {
            armNotSystemNames.remove(armNotSystemName);
        }


        model.put("armsystemnames", armSystemNames);
        model.put("armnotsystemnames", armNotSystemNames);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addsystemforarm";
    }

    @GetMapping("/addskziforarm")
    public String addskziforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<SKZI> skzis = skziRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();



        model.put("skzis", skzis);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addskziforarm";
    }

    @PostMapping("/addskziforarm")
    public String addskziforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String skziid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<SKZI> skzis = skziRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        if (!skziid.equals("0")) {

            SKZI skzi = skziRepo.findById(Integer.parseInt(skziid)).get();

            ARMSKZI armskzi = new ARMSKZI();
            armskzi.setArm(arm);
            log.info("АРМу " + arm.getArmName() + " добавлено СКЗИ " + skzi.getName() +  ";");
            armskzi.setSkzi(skzi);
            armskziRepo.save(armskzi);
            model.put("error", "СЗИ  " + skzi.getName() + " для " + arm.getArmName() + " добавлено");
        }
        else
        {
            model.put("error", "Необходимо указать СКЗИ");
        }


        model.put("skzis", skzis);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addskziforarm";
    }

    @GetMapping("/addpakucforarm")
    public String addpakucforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<PakUC> pakUCList = pakUCRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();



        model.put("pakucs", pakUCList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addpakucforarm";
    }

    @PostMapping("/addpakucforarm")
    public String addpakucforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String pakucid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<PakUC> pakUCList = pakUCRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        if (!pakucid.equals("0")) {

            PakUC pakUC = pakUCRepo.findById(Integer.parseInt(pakucid)).get();

            arm.setPakUC(pakUC);
            log.info("АРМу " + arm.getArmName() + " добавлен ПАК УЦ " + pakUC.getPakUCName() +  ";");
            armRepo.save(arm);

            model.put("error", "ПАК УЦ  " + pakUC.getPakUCName() + " для " + arm.getArmName() + " добавлен");
        }
        else
        {
            model.put("error", "Необходимо указать ПАК УЦ");
        }


        model.put("pakucs", pakUCList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addpakucforarm";
    }

    @GetMapping("/addavforarm")
    public String addavforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<Antivirus> antivirusList = antivirusRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();



        model.put("antiviruses", antivirusList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addavforarm";
    }

    @PostMapping("/addavforarm")
    public String addavforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String avid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<Antivirus> antivirusList = antivirusRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        if (!avid.equals("0")) {

            Antivirus antivirus = antivirusRepo.findById(Integer.parseInt(avid)).get();

           arm.setAntivirus(antivirus);
           log.info("АРМу " + arm.getArmName() + " добавлен антивирус " + antivirus.getAvName() +  ";");
           armRepo.save(arm);

            model.put("error", "Антивирус  " + antivirus.getAvName() + " для " + arm.getArmName() + " добавлен");
        }
        else
        {
            model.put("error", "Необходимо указать Антивирус");
        }


        model.put("antiviruses", antivirusList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addavforarm";
    }

    @GetMapping("/addsziforarm")
    public String addsziforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String sourcetype,
            @RequestParam(required = false, defaultValue = "0") String sourceid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<SZI> szis;
        switch (sourcetype)
        {
            default: {
                szis = sziRepo.findAll();
                break;
            }
        }

        ARM arm = armRepo.findById(Integer.parseInt(id)).get();



        model.put("szis", szis);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addsziforarm";
    }

    @PostMapping("/addsziforarm")
    public String addsziforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String sziid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        List<SZI> szis = sziRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();

        if (!sziid.equals("0")) {

            SZI szi = sziRepo.findById(Integer.parseInt(sziid)).get();

            ARMSzi armSzi = new ARMSzi();
            armSzi.setArm(arm);
            armSzi.setSzi(szi);
            log.info("АРМу " + arm.getArmName() + " добавлено СЗИ " + szi.getSziName() +  ";");
            armsziRepo.save(armSzi);
            model.put("error", "СЗИ  " + szi.getSziName() + " для " + arm.getArmName() + " добавлено");
        }
        else
        {
            model.put("error", "Необходимо указать СЗИ");
        }


        model.put("szis", szis);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addsziforarm";
    }

    @GetMapping("/addkcforarm")
    public String addkcforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

            List<KeyCarriers> keyCarriersList = keyCarriersRepo.findAll();
            ARM arm = armRepo.findById(Integer.parseInt(id)).get();



        model.put("kcs", keyCarriersList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addkcforarm";
    }

    @PostMapping("/addkcforarm")
    public String addkcforarm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String kcid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {


        List<KeyCarriers> keyCarriersList = keyCarriersRepo.findAll();
        ARM arm = armRepo.findById(Integer.parseInt(id)).get();
        if (!kcid.equals("0")) {

            KeyCarriers keyCarriers = keyCarriersRepo.findById(Integer.parseInt(kcid)).get();

            ARMKc armKc = new ARMKc();
            armKc.setArm(arm);
            armKc.setKeyCarriers(keyCarriers);
            log.info("АРМу " + arm.getArmName() + " добавлен ключевой носитель " + keyCarriers.getKeyCarriersName() +  ";");
            armKcRepo.save(armKc);
            model.put("error", "Ключевые носители " + keyCarriers.getKeyCarriersName() + " для " + arm.getArmName() + " добавлены");
        }
        else
        {
            model.put("error", "Необходимо указать Ключевые носители");
        }



        model.put("kcs", keyCarriersList);
        model.put("arm", arm);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addkcforarm";
    }

    @GetMapping("/addkc")
    public String addkc(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        switch (type) {

            case "arm" :
            {
                ARM arm = armRepo.findById(Integer.parseInt(id)).get();
                model.put("arm", arm);
                break;
            }
        }
        List<KeyCarriersType> keyCarriersTypeList = keyCarriersTypeRepo.findAll();



        model.put("kctypes", keyCarriersTypeList);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addkc";
    }



    @PostMapping("/addkc")
    public String addkc(

            @RequestParam(required = false, defaultValue = "0") String kcname,
            @RequestParam(required = false, defaultValue = "0") String kctype,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        kcname = kcname.trim();

        List<KeyCarriersType> keyCarriersTypeList = keyCarriersTypeRepo.findAll();

        if (!kcname.equals("0"))
        {
            if (keyCarriersRepo.findByKeyCarriersName(kcname) != null) {
                model.put("error", "Такие Ключевые носители уже существуют");
            }
            else
            {
                KeyCarriers newKeyCarriers = new KeyCarriers();
                newKeyCarriers.setKeyCarriersName(kcname);
                newKeyCarriers.setKeyCarriersType(keyCarriersTypeRepo.findById(Integer.parseInt(kctype)).get());
                switch (type) {

                    case "arm" :
                    {
                        ARM arm = armRepo.findById(Integer.parseInt(id)).get();
                        Organization organization = arm.getOrganization();

                        keyCarriersRepo.save(newKeyCarriers);
                        newKeyCarriers = keyCarriersRepo.findByKeyCarriersName(kcname);
                        ARMKc armKc = new ARMKc();
                        armKc.setKeyCarriers(newKeyCarriers);
                        armKc.setArm(arm);
                        armKcRepo.save(armKc);

                        model.put("arm", arm);
                        break;
                    }
                }
                keyCarriersRepo.save(newKeyCarriers);
                log.info("Создан ключевой носитель " + newKeyCarriers.getKeyCarriersName() +  ";");


                model.put("error", "Ключевые носители " + kcname + " добавлены");
            }

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("kcname", kcname.equals("0") ? "" : kcname);

        }



        model.put("kctypes", keyCarriersTypeList);
        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addkc";
    }

    @GetMapping("/addpakuc")
    public String addpakuc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {


        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addpakuc";
    }

    @PostMapping("/addpakuc")
    public String addpakuc(

            @RequestParam(required = false, defaultValue = "0") String pakucname,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        pakucname = pakucname.trim();


        if (!pakucname.equals("0"))
        {
            if (pakUCRepo.findByPakUCName(pakucname) != null) {
                model.put("error", "Такой ПАК УЦ уже существует");
            }
            else
            {
                PakUC newPakUc = new PakUC();
                newPakUc.setPakUCName(pakucname);
                pakUCRepo.save(newPakUc);
                log.info("Создан ПАК УЦ " + newPakUc.getPakUCName() +  ";");
                model.put("error", "ПАК УЦ " + pakucname + " добавлен");
            }

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("pakucame", pakucname.equals("0") ? "" : pakucname);

        }

        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addpakuc";
    }

    @GetMapping("/addszi")
    public String addszi(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {



        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addszi";
    }

    @PostMapping("/addszi")
    public String addszi(
            @RequestParam(required = false, defaultValue = "0") String sziname,
            @RequestParam(required = false, defaultValue = "0") String kslevel,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        sziname = sziname.trim();

        if (!sziname.equals("0"))
        {
            if (sziRepo.findBySziName(sziname) != null) {
                model.put("error", "Такое СЗИ уже существует");
            }
            else
            {
                SZI newSZI = new SZI();
                newSZI.setSziName(sziname);
                newSZI.setKS2(kslevel);
                sziRepo.save(newSZI);
                log.info("Создано СЗИ " + newSZI.getSziName() +  ";");
                model.put("error", "СЗИ " + sziname + " добавлено");
            }

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("sziame", sziname.equals("0") ? "" : sziname);

        }


        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addszi";
    }



    @GetMapping("/addav")
    public String addav(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {



        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addav";
    }

    @PostMapping("/addav")
    public String addav(
            @RequestParam(required = false, defaultValue = "0") String avname,
            @RequestParam(required = false, defaultValue = "0") String avversion,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        avname = avname.trim();
        avversion = avversion.trim();



        if (!avname.equals("0") && !avversion.equals("0"))
        {
            if (antivirusRepo.findByAvNameAndAvVersion(avname, avversion) != null) {
                model.put("error", "Такой антивирус уже существует");
            }
            else
            {
                Antivirus newAntivirus = new Antivirus();
                newAntivirus.setAvName(avname);
                newAntivirus.setAvVersion(avversion);
                antivirusRepo.save(newAntivirus);
                log.info("Создан антивирус " + newAntivirus.getAvName() + " версия " + newAntivirus.getAvVersion() +  ";");
                model.put("error", "Антивирус " + avname + " добавлен");
            }

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("avname", avname.equals("0") ? "" : avname);
            model.put("avversion", avversion.equals("0") ? "" : avversion);
        }


        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addav";
    }

    @GetMapping("/addskzi")
    public String addskzi(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {



        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addskzi";
    }

    @PostMapping("/addskzi")
    public String addskzipost(
            @RequestParam(required = false, defaultValue = "0") String skziname,
            @RequestParam(required = false, defaultValue = "0") String skziversion,
            @RequestParam(required = false, defaultValue = "0") String skzirealizationvariant,
            @RequestParam(required = false, defaultValue = "0") String kslevel,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

       if (!skziname.equals("0") || !skziversion.equals("0") || !skzirealizationvariant.equals("0"))
       {
           SKZI newSkzi;


           if (skziRepo.findByNameAndVersionAndAndRealizationVariant(skziname, skziversion, skzirealizationvariant) != null) {
               newSkzi = skziRepo.findByNameAndVersionAndAndRealizationVariant(skziname, skziversion, skzirealizationvariant);
               model.put("error", "СКЗИ уже существует");
           }
           else {

               newSkzi = new SKZI();
               newSkzi.setName(skziname.trim());
               newSkzi.setVersion(skziversion.trim());
               newSkzi.setRealizationVariant(skzirealizationvariant.trim());
               newSkzi.setKS(kslevel);


               skziRepo.save(newSkzi);
               log.info("Создано СКЗИ " + newSkzi.getName() + " версия " + newSkzi.getVersion() +  ";");
               model.put("error", "СКЗИ добавлен");
           }


       }
       else
       {
           model.put("error", "*Необходимо заполнить все поля");
           model.put("skziname", skziname.equals("0") ? "" : skziname);
           model.put("skziversion", skziversion.equals("0") ? "" : skziversion);
           model.put("skzirealizationvariant", skzirealizationvariant.equals("0") ? "" : skzirealizationvariant);

       }

        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addskzi";
    }

    @GetMapping("/generatepaper")
    public String generatepaper(
            @RequestParam(required = false, defaultValue = "0") String reportid,
            @RequestParam(required = false, defaultValue = "0") String skzisource,
            @RequestParam(required = false, defaultValue = "0") String banknotgetinformation,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        Document report = documentRepo.findById(Integer.parseInt(reportid)).get();


        try {

            if (!Files.exists(Paths.get(REPORTS_CATALOG_NAME)))
            {
                Files.createDirectory(Paths.get(REPORTS_CATALOG_NAME));
            }

            XWPFDocument doc = new XWPFDocument(new FileInputStream(REPORT_TEMPLATE_FILE));

            replaseText(doc, "SYSTEM_NAME", report.getSystem().getName());
            replaseTableText(doc, "SYSTEM_NAME", report.getSystem().getName());

            replaseText(doc, "BANK_NAME", report.getBank().getName());
            replaseTableText(doc, "BANK_NAME", report.getBank().getName());

            replaseText(doc, "YEAR", yearDateFormat.format(new Date()));
            replaseTableText(doc, "YEAR", yearDateFormat.format(new Date()));

            replaseText(doc, "CLIENT_NAME", report.getOrganization().getName());
            replaseTableText(doc, "CLIENT_NAME", report.getOrganization().getName());

            if(report.getOrganization().getName().toUpperCase().contains("Атомэнергопром".toUpperCase())
                    || report.getOrganization().getName().toUpperCase().contains("Росэнергоатом".toUpperCase())
                    || report.getOrganization().getName().toUpperCase().contains("АЭПК".toUpperCase())
                    || report.getOrganization().getName().toUpperCase().contains("ГК".toUpperCase())
            )
            {
                replaseText(doc, "OSNOVANIE",
                        "заявление " + report.getOrganization().getName() + " о присоединении от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate() +
                                " (50,2) № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber() +
                                " (50,2) к договору присоединения от 06.07.2012 №22/2143-Д на оказание услуг, составляющих лицензируемую деятельность, в отношении шифровальных (криптографических) средств и заявление от " +
                                documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentDate() + " № " +
                                documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentNumber() + " на подключение услуги по осуществление контроля (оценки) уровня доверия и контроля приведения в соответствие требованиям Госкорпорации «Росатом» защищенных с использованием шифровальных (криптографических) средств информационных и телекоммуникационных систем."
                        );
                replaseTableText(doc, "OSNOVANIE", "заявление " + report.getOrganization().getName() + " о присоединении от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate() +
                        " (50,2) № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber() +
                        " (50,2) к договору присоединения от 06.07.2012 №22/2143-Д на оказание услуг, составляющих лицензируемую деятельность, в отношении шифровальных (криптографических) средств и заявление от " +
                        documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentDate() + " № " +
                        documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentNumber() + " на подключение услуги по осуществление контроля (оценки) уровня доверия и контроля приведения в соответствие требованиям Госкорпорации «Росатом» защищенных с использованием шифровальных (криптографических) средств информационных и телекоммуникационных систем."
                );
            }
            else
            {
                replaseText(doc, "OSNOVANIE", "договор от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate() +
                        " (50,2) № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber() + " (50,2");
                replaseTableText(doc, "OSNOVANIE", "договор от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate() +
                        " (50,2) № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber() + " (50,2");
            }

            if (report.getSystem().getSystemType().getId() == 1)
            {
                replaseText(doc, "SYSTEM_TYPE_VAR", "программного обеспечения, установленного на АРМ Клиента.");
                replaseTableText(doc, "SYSTEM_TYPE_VAR", "программного обеспечения, установленного на АРМ Клиента.");
            }
            else
            {
                replaseText(doc, "SYSTEM_TYPE_VAR", "Браузера.");
                replaseTableText(doc, "SYSTEM_TYPE_VAR", "Браузера.");
            }

            if (report.getSystem().isBankSoftware())
            {
                replaseText(doc, "BANK_SOFTWARE", "Подтверждение права Банка на использование Системы не требуется, так как Система является внутренней разработкой Банка.");
                replaseTableText(doc, "BANK_SOFTWARE", "Подтверждение права Банка на использование Системы не требуется, так как Система является внутренней разработкой Банка.");
            }
            else
            {
                try {
                    replaseText(doc, "BANK_SOFTWARE", "Право на использование Системы предоставляется в соответствии с документом «" + documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(43).get()).get(0).getDocumentName() + "» .");
                    replaseTableText(doc, "BANK_SOFTWARE", "Право на использование Системы предоставляется в соответствии с документом «" + documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(43).get()).get(0).getDocumentName() + "» .");
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_SOFTWARE", "Информация о праве на использование Системы не предоставлена.");
                    replaseTableText(doc, "BANK_SOFTWARE", "Информация о праве на использование Системы не предоставлена.");
                }
            }

            try {
                Document acreditationDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0);
                replaseText(doc, "ACREDITATION", "УЦ имеет свидетельство об аккредитации в Министерстве цифрового развития, связи и массовых коммуникаций Российской Федерации.");
                replaseTableText(doc, "ACREDITATION", "УЦ имеет свидетельство об аккредитации в Министерстве цифрового развития, связи и массовых коммуникаций Российской Федерации.");
            }
            catch (Exception e)
            {
                replaseText(doc, "ACREDITATION", "УЦ не имеет свидетельства об аккредитации в Министерстве цифрового развития, связи и массовых коммуникаций Российской Федерации.");
                replaseTableText(doc, "ACREDITATION", "УЦ не имеет свидетельства об аккредитации в Министерстве цифрового развития, связи и массовых коммуникаций Российской Федерации.");
            }




            replaseText(doc, "JOINING_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate());
            replaseTableText(doc, "JOINING_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentDate());

            replaseText(doc, "JOINING_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "JOINING_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(64).get()).get(0).getDocumentNumber());

            replaseText(doc, "SERVICE_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentDate());
            replaseTableText(doc, "SERVICE_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentDate());

            replaseText(doc, "SERVICE_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "SERVICE_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(61).get()).get(0).getDocumentNumber());

            replaseText(doc, "CLIENT_BANK_ORDER_NAME", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentName());
            replaseTableText(doc, "CLIENT_BANK_ORDER_NAME", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentName());

            replaseText(doc, "CLIENT_BANK_ORDER_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_BANK_ORDER_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_BANK_ORDER_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_BANK_ORDER_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(51).get()).get(0).getDocumentNumber());


            replaseText(doc, "BANK_FSB_LICENSE_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(46).get()).get(0).getDocumentDate());
            //replaseTableText(doc, "BANK_FSB_LICENSE_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(46).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_FSB_LICENSE_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(46).get()).get(0).getDocumentNumber());
            //replaseTableText(doc, "BANK_FSB_LICENSE_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(46).get()).get(0).getDocumentNumber());

            replaseText(doc, "SYSTEM_TYPE", report.getSystem().getSystemType().getName());
            replaseTableText(doc, "SYSTEM_TYPE", report.getSystem().getSystemType().getName());

            replaseText(doc, "BANK_ACCREDITATION_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getDocumentDate());
            replaseTableText(doc, "BANK_ACCREDITATION_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_ACCREDITATION_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getValidUntilDate().equals("01.01.3020") ? "Бессрочно" : documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_ACCREDITATION_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getValidUntilDate().equals("01.01.3020") ? "Бессрочно" : documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_ACCREDITATION_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "BANK_ACCREDITATION_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0).getDocumentNumber());

            replaseText(doc, "BANK_REGLAMENT_UC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(8).get()).get(0).getDocumentName());
            replaseTableText(doc, "BANK_REGLAMENT_UC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(8).get()).get(0).getDocumentName());

            List<ARM> bankARMs = armRepo.findByOrganization(report.getBank());
            List<ARM> bankClientARMs = new ArrayList<>();
            List<ARMSystemName> systemARMs = armSystemNameRepo.findBySystemName(report.getSystem().getSystemName());
            List<ARM> bankSystemARMs = new ArrayList<>();
            PakUC bankPacUC = null;
            ARM bankPacUcARM = null;

            for (ARMSystemName armSystemName : systemARMs) {
                if (bankARMs.contains(armSystemName.getArm()))
                {
                    bankSystemARMs.add(armSystemName.getArm());
                }
            }

            for (ARM arm : bankSystemARMs)
            {
                if (arm.getArmType().getId() == 2) {
                    bankClientARMs.add(arm);
                }
            }

            for (ARM arm : bankSystemARMs)
            {
                if (arm.getPakUC() != null)
                {
                    bankPacUC = arm.getPakUC();
                    bankPacUcARM = arm;
                    break;
                }

            }

            if (bankClientARMs.size() == 0)
            {
                replaseText(doc, "BANK_KEY_STORE", "Хранение ключевых документов на стороне Банка не осуществляется по причине отсутствия Пользователей ключевых документов на стороне Банка.");
                replaseTableText(doc, "BANK_KEY_STORE", "Хранение ключевых документов на стороне Банка не осуществляется по причине отсутствия Пользователей ключевых документов на стороне Банка.");

                replaseText(doc, "BANK_KK_REMOVE", "Уничтожение ключевых документов Пользователей Банка не осуществляется, т.к. Пользователи на стороне Банка отсутствуют.");
                replaseTableText(doc, "BANK_KK_REMOVE", "Уничтожение ключевых документов Пользователей Банка не осуществляется, т.к. Пользователи на стороне Банка отсутствуют.");

            }
            else
            {
                try {
                    Document armKKDocument = documentRepo.findByArmAndDocumentType(bankClientARMs.get(0), documentTypeRepo.findById(68).get()).get(0);
                    replaseText(doc, "BANK_KEY_STORE", "Хранение ключевых документов Пользователей Банка осуществляется в соответствии с инструкцией " +
                            "об организации и обеспечении безопасности хранения, обработки и передачи по каналам связи с использованием средств криптографической защиты информации " +
                            "с ограниченным доступом, не содержащей сведений, составляющих государственную тайну, утвержденной приказом ФАПСИ от 13 июня 2001 года №152, " +
                            "согласно «" + armKKDocument.getDocumentName() + " № " + armKKDocument.getDocumentNumber() + " от " + armKKDocument.getDocumentDate() + "».");
                    replaseTableText(doc, "BANK_KEY_STORE", "Хранение ключевых документов Пользователей Банка осуществляется в соответствии с инструкцией " +
                            "об организации и обеспечении безопасности хранения, обработки и передачи по каналам связи с использованием средств криптографической защиты информации " +
                            "с ограниченным доступом, не содержащей сведений, составляющих государственную тайну, утвержденной приказом ФАПСИ от 13 июня 2001 года №152, " +
                            "согласно «" + armKKDocument.getDocumentName() + " № " + armKKDocument.getDocumentNumber() + " от " + armKKDocument.getDocumentDate() + "».");
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_KEY_STORE", "Информация о хранении ключевых документов Пользователей Банка не предоставлена.");
                    replaseTableText(doc, "BANK_KEY_STORE", "Информация о хранении ключевых документов Пользователей Банка не предоставлена.");
                }

                try {
                    Document szpuDocument = documentRepo.findByArmAndDocumentType(bankClientARMs.get(0), documentTypeRepo.findById(68).get()).get(0);
                    replaseText(doc, "BANK_KK_REMOVE", "Уничтожение ключевых документов Пользователей Банка осуществляется в соответствии с инструкцией " +
                            "об организации и обеспечении безопасности хранения, обработки и передачи по каналам связи с использованием средств криптографической защиты информации " +
                            "с ограниченным доступом, не содержащей сведений, составляющих государственную тайну, утвержденной приказом ФАПСИ от 13 июня 2001 года №152, " +
                            "согласно " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentName() +
                            " № " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentNumber() +
                             " от " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentDate() + ".");
                    replaseTableText(doc, "BANK_KK_REMOVE", "Уничтожение ключевых документов Пользователей Банка осуществляется в соответствии с инструкцией " +
                            "об организации и обеспечении безопасности хранения, обработки и передачи по каналам связи с использованием средств криптографической защиты информации " +
                            "с ограниченным доступом, не содержащей сведений, составляющих государственную тайну, утвержденной приказом ФАПСИ от 13 июня 2001 года №152," +
                            "согласно " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentName() +
                             " № " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentNumber() +
                             " от " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(70).get()).get(0).getDocumentDate() + ".");
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_KK_REMOVE", "Информация об уничтожении ключевых документов Пользователей Банка не предоставлена.");
                    replaseTableText(doc, "BANK_KK_REMOVE", "Информация об уничтожении ключевых документов Пользователей Банка не предоставлена.");
                }
            }




            replaseText(doc, "BANK_PAC_UC_NAME", bankPacUC.getPakUCName());
            replaseTableText(doc, "BANK_PAC_UC_NAME", bankPacUC.getPakUCName());

            replaseText(doc, "CERT_TYPE_NAME", report.getSystem().getEsType().getName());
            replaseTableText(doc, "CERT_TYPE_NAME", report.getSystem().getEsType().getName());

            replaseText(doc, "BANK_PAC_UC_CERT_DATE", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getDocumentDate());
            replaseTableText(doc, "BANK_PAC_UC_CERT_DATE", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_PAC_UC_CERT_BEFORE", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_PAC_UC_CERT_BEFORE", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_PAC_UC_CERT_NUM", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "BANK_PAC_UC_CERT_NUM", documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0).getDocumentNumber());

            List<ARMSKZI> bankArmSKZIList = armskziRepo.findByArm(bankPacUcARM);

            SKZI bankPacUCSkzi = bankArmSKZIList.get(0).getSkzi();

            try {
                replaseText(doc, "PRAVO_BANKA", "Подтверждением права Банка на использование средств УЦ является «" + bankPacUCSkzi.getName() + "».");
                replaseTableText(doc, "PRAVO_BANKA", "Подтверждением права Банка на использование средств УЦ является «" + bankPacUCSkzi.getName() + "».");
            }
            catch (Exception e)
            {
                replaseText(doc, "PRAVO_BANKA", "Подтверждение наличия права Банка на использование средств УЦ не предоставлено.");
                replaseTableText(doc, "PRAVO_BANKA", "Подтверждение наличия права Банка на использование средств УЦ не предоставлено.");
            }

            replaseText(doc, "BANK_PAC_UC_SKZI_NAME", bankPacUCSkzi.getName());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_NAME", bankPacUCSkzi.getName());

            replaseText(doc, "BANK_PAC_UC_SKZI_VERSION", bankPacUCSkzi.getVersion());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_VERSION", bankPacUCSkzi.getVersion());

            replaseText(doc, "BANK_PAC_UC_SKZI_VARIANT", bankPacUCSkzi.getRealizationVariant());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_VARIANT", bankPacUCSkzi.getRealizationVariant());

            replaseText(doc, "BANK_PAC_UC_SKZI_CERT_DATE", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_CERT_DATE", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_PAC_UC_SKZI_CERT_BEFORE", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_CERT_BEFORE", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_PAC_UC_SKZI_CERT_NUM", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_CERT_NUM", documentRepo.findBySkziAndDocumentType(bankPacUCSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());

            replaseText(doc, "BANK_PAC_UC_SKZI_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(4).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(4).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_PAC_UC_SKZI_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(4).get()).get(0).getDocumentName());
            replaseTableText(doc, "BANK_PAC_UC_SKZI_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(4).get()).get(0).getDocumentName());

            replaseText(doc, "BANK_PAC_UC_AV_NAME", bankPacUcARM.getAntivirus().getAvName());
            replaseTableText(doc, "BANK_PAC_UC_AV_NAME", bankPacUcARM.getAntivirus().getAvName());

            replaseText(doc, "BANK_PAC_UC_AV_FSTEC_DATE", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());
            replaseTableText(doc, "BANK_PAC_UC_AV_FSTEC_DATE", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());

            replaseText(doc, "BANK_PAC_UC_AV_FSTEC_BEFORE", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());
            replaseTableText(doc, "BANK_PAC_UC_AV_FSTEC_BEFORE", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());

            replaseText(doc, "BANK_PAC_UC_AV_FSTEC_NUM", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());
            replaseTableText(doc, "BANK_PAC_UC_AV_FSTEC_NUM", documentRepo.findByAntivirusAndDocumentType(bankPacUcARM.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());

            List<ARMSzi> bankPacUcARMSzi = armsziRepo.findByArm(bankPacUcARM);
            SZI bankPacUcSzi = bankPacUcARMSzi.get(0).getSzi();

            replaseText(doc, "BANK_PAC_UC_SZI_NAME", bankPacUcSzi.getSziName());
            replaseTableText(doc, "BANK_PAC_UC_SZI_NAME", bankPacUcSzi.getSziName());

            replaseText(doc, "BANK_PAC_UC_SZI_FSTEC_DATE", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getDocumentDate());
            replaseTableText(doc, "BANK_PAC_UC_SZI_FSTEC_DATE", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getDocumentDate());

            replaseText(doc, "BANK_PAC_UC_SZI_FSTEC_BEFORE", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());
            replaseTableText(doc, "BANK_PAC_UC_SZI_FSTEC_BEFORE", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());

            replaseText(doc, "BANK_PAC_UC_SZI_FSTEC_NUM", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());
            replaseTableText(doc, "BANK_PAC_UC_SZI_FSTEC_NUM", documentRepo.findBySziAndDocumentType(bankPacUcSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());


            List<ARM> clientARMs = armRepo.findByOrganization(report.getOrganization());
            List<ARM> clientSystemARMs = new ArrayList<>();


            for (ARMSystemName armSystemName : systemARMs) {
                if (clientARMs.contains(armSystemName.getArm()))
                {
                    clientSystemARMs.add(armSystemName.getArm());
                }
            }


            List <Document> clientSkziActList = new ArrayList<>();
            for (ARM clientArm : clientSystemARMs)
            {
               List<Document> clientArmSkziActList = documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(68).get());

                    for (Document clientArmSkziAct : clientArmSkziActList)
                    {
                        if (!clientSkziActList.contains(clientArmSkziAct))
                        {
                            clientSkziActList.add(clientArmSkziAct);
                        }
                    }

            }
            if (clientSkziActList.size() > 1)
            {
                StringBuilder clientSkziActsSb = new StringBuilder();
                clientSkziActsSb.append("что подтверждается актами готовности СКЗИ к эксплуатации ");
                for (Document document : clientSkziActList)
                {
                    clientSkziActsSb.append( " от " + document.getDocumentDate() + " № " + document.getDocumentNumber() + " ");
                }
                replaseText(doc, "CLIENT_SKZI_ACTS", clientSkziActsSb.toString());
                replaseTableText(doc, "CLIENT_SKZI_ACTS", clientSkziActsSb.toString());
            }
            else if (clientSkziActList.size() == 1)
            {
                replaseText(doc, "CLIENT_SKZI_ACTS", " от " + clientSkziActList.get(0).getDocumentDate()  + " № " + clientSkziActList.get(0).getDocumentNumber());
                replaseTableText(doc, "CLIENT_SKZI_ACTS", " от " + clientSkziActList.get(0).getDocumentDate()  + " № " + clientSkziActList.get(0).getDocumentNumber());
            }
            else
            {
                replaseText(doc, "CLIENT_SKZI_ACTS", "акт готовности СКЗИ к эксплуатации не предоставлен");
                replaseTableText(doc, "CLIENT_SKZI_ACTS", "акт готовности СКЗИ к эксплуатации не предоставлен");
            }

            ARM clientArm = clientSystemARMs.get(0);
            List<ARMKc> clientARMKeyCarriersList = armKcRepo.findByArm(clientArm);

            KeyCarriers clientKc = clientARMKeyCarriersList.get(0).getKeyCarriers();

            replaseText(doc, "CLIENT_KC_NAME", clientKc.getKeyCarriersName());
            replaseTableText(doc, "CLIENT_KC_NAME", clientKc.getKeyCarriersName());

            replaseText(doc, "CLIENT_KC_DOC_NAME", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentName());
            replaseTableText(doc, "CLIENT_KC_DOC_NAME", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentName());

            replaseText(doc, "CLIENT_KC_DOC_DATE", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_KC_DOC_DATE", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_KC_DOC_BEFORE", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "CLIENT_KC_DOC_BEFORE", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getValidUntilDate());

            replaseText(doc, "CLIENT_KC_DOC_NUM", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_KC_DOC_NUM", documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0).getDocumentNumber());

            List<ARMKc> bankARMKeyCarriersList = armKcRepo.findByArm(bankPacUcARM);

            KeyCarriers bankKc = bankARMKeyCarriersList.get(0).getKeyCarriers();

            replaseText(doc, "BANK_KC_NAME", bankKc.getKeyCarriersName());
            replaseTableText(doc, "BANK_KC_NAME", bankKc.getKeyCarriersName());

            if (clientSystemARMs.size() == 0)
            {
                replaseText(doc, "KK_BANK", "Получение, создание и замена ключевых документов на стороне Банка не осуществляется по причине отсутствия Пользователей ключевых документов на стороне Банка.");
                replaseTableText(doc, "KK_BANK", "Получение, создание и замена ключевых документов на стороне Банка не осуществляется по причине отсутствия Пользователей ключевых документов на стороне Банка.");
            }
            else
            {
                try {
                    replaseText(doc, "KK_BANK", "На стороне Банка в качестве ключевых носителей ключей электронной подписи используются " + bankKc.getKeyCarriersName() + ".");
                    replaseTableText(doc, "KK_BANK", "На стороне Банка в качестве ключевых носителей ключей электронной подписи используются " + bankKc.getKeyCarriersName() + ".");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            replaseText(doc, "BANK_KC_DOC_NAME", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentName());
            replaseTableText(doc, "BANK_KC_DOC_NAME", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentName());

            replaseText(doc, "BANK_KC_DOC_DATE", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentDate());
            replaseTableText(doc, "BANK_KC_DOC_DATE", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_KC_DOC_BEFORE", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_KC_DOC_BEFORE", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_KC_DOC_NUM", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "BANK_KC_DOC_NUM", documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0).getDocumentNumber());

            boolean bankClientsArms = false;
            for (ARM bankSystemArm : bankSystemARMs)
            {
                if (bankSystemArm.getArmType().getId() == 2)
                {
                    bankClientsArms = true;
                    break;
                }

            }
            if (bankClientsArms == true)
            {
                replaseText(doc, "BANK_CLIENTS_ARMS", "на стороне Банка пользователей СКЗИ есть");
                replaseTableText(doc, "BANK_CLIENTS_ARMS", "на стороне Банка пользователей СКЗИ есть");
            }
            else
            {
                replaseText(doc, "BANK_CLIENTS_ARMS", "на стороне Банка пользователей СКЗИ нет");
                replaseTableText(doc, "BANK_CLIENTS_ARMS", "на стороне Банка пользователей СКЗИ нет");
            }


            replaseText(doc, "CLIENT_ADMIN_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentName());
            replaseTableText(doc, "CLIENT_ADMIN_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentName());

            replaseText(doc, "CLIENT_ADMIN_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_ADMIN_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_ADMIN_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ADMIN_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(65).get()).get(0).getDocumentNumber());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_DATE", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_DATE", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_NUM", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_NUM", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentNumber());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_NAME", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentName());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_NAME", documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get()).get(0).getDocumentName());


            List<ARMSKZI> clientArmSKZIList = armskziRepo.findByArm(clientArm);

            SKZI clientSkzi = clientArmSKZIList.get(0).getSkzi();

            if (skzisource.equals("0"))
            {
                replaseText(doc, "WHO_GETS_SKZI", "АО \"Гринатом\"");
                replaseTableText(doc, "WHO_GETS_SKZI", "АО \"Гринатом\"");

                replaseText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());
                replaseTableText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());


                try {
                    replaseText(doc, "WHO_GET_SKZI", "Подтверждением права передачи АО «Гринатом» Клиенту СКЗИ и эксплуатационной и технической документации к ним, " +
                            "использующихся в работе Системы является заявление " + report.getOrganization().getName() + " о присоединении от " +
                            documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(50).get()).get(0).getDocumentDate() + " № " +
                            documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(50).get()).get(0).getDocumentNumber() +
                            " к договору присоединения от 06.07.2012 №22/2143-Д на оказание услуг, составляющих лицензируемую деятельность, в отношении шифровальных (криптографических) средств.");
                    replaseTableText(doc, "WHO_GET_SKZI", "Подтверждением права передачи АО «Гринатом» Клиенту СКЗИ и эксплуатационной и технической документации к ним, "  +
                            "использующихся в работе Системы является заявление " + report.getOrganization().getName() + " о присоединении от " +
                            documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(50).get()).get(0).getDocumentDate() + " № "  +
                            documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(50).get()).get(0).getDocumentNumber() +
                            " к договору присоединения от 06.07.2012 №22/2143-Д на оказание услуг, составляющих лицензируемую деятельность, в отношении шифровальных (криптографических) средств.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (!skzisource.equals("0") && !banknotgetinformation.equals("0"))
            {
                try {
                    replaseText(doc, "WHO_GETS_SKZI", report.getBank().getName());
                    replaseTableText(doc, "WHO_GETS_SKZI", report.getBank().getName());

                    replaseText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());
                    replaseTableText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());

                    replaseText(doc, "WHO_GET_SKZI", "Информация о праве передачи Банком Клиенту СКЗИ и эксплуатационной и технической документации к ним, использующихся в работе Системы не предоставлена.");
                    replaseTableText(doc, "WHO_GET_SKZI", "Информация о праве передачи Банком Клиенту СКЗИ и эксплуатационной и технической документации к ним, использующихся в работе Системы не предоставлена.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (!skzisource.equals("0") && banknotgetinformation.equals("0"))
            {
                try {

                    replaseText(doc, "WHO_GETS_SKZI", report.getBank().getName());
                    replaseTableText(doc, "WHO_GETS_SKZI", report.getBank().getName());

                    replaseText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());
                    replaseTableText(doc, "CLIENT_SKZI_NAME", clientSkzi.getName());

                    replaseText(doc, "WHO_GET_SKZI", "Подтверждением права передачи Банком Клиенту СКЗИ и эксплуатационной и технической документации к ним, " +
                            "использующихся в работе Системы является " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(52).get()).get(0).getDocumentName() + ".");
                    replaseTableText(doc, "WHO_GET_SKZI", "Подтверждением права передачи Банком Клиенту СКЗИ и эксплуатационной и технической документации к ним, " +
                            "использующихся в работе Системы является " + documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(52).get()).get(0).getDocumentName() + ".");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                /*
                replaseText(doc, "ZHPU_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).getDocumentDate());
                replaseTableText(doc, "ZHPU_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).getDocumentDate());

                replaseText(doc, "ZHPU_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).getDocumentNumber());
                replaseTableText(doc, "ZHPU_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).getDocumentNumber());
                 */
                List<Document> zhpuClientDocs = documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get());

                if (zhpuClientDocs.size() > 1) {

                    StringBuilder zhpuClientSb = new StringBuilder("Журналах поэкземплярного учета СКЗИ ");
                    for (Document zhpuDoc : zhpuClientDocs)
                    {
                        zhpuClientSb.append("от " + zhpuDoc.getDocumentDate() + " № " + zhpuDoc + " ");
                    }

                    replaseText(doc, "ZHPU_INFO", zhpuClientSb.toString());
                    replaseTableText(doc, "ZHPU_INFO", zhpuClientSb.toString());
                }
                else if (zhpuClientDocs.size() == 1) {
                    replaseText(doc, "ZHPU_INFO", "Согласно схемы организации криптографической защиты информации ОКЗ АО «Гринатом» полученное СКЗИ и эксплуатационная и техническая документация к нему учтены в Журнале поэкземплярного учета СКЗИ от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).get(0).getDocumentNumber() + ".");
                    replaseTableText(doc, "ZHPU_INFO", "Согласно схемы организации криптографической защиты информации ОКЗ АО «Гринатом» полученное СКЗИ и эксплуатационная и техническая документация к нему учтены в Журнале поэкземплярного учета СКЗИ от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get()).get(0).getDocumentNumber() + ".");
                }
                else
                {
                    replaseText(doc, "ZHPU_INFO", "");
                    replaseTableText(doc, "ZHPU_INFO", "");
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();


            }

            try {

                List<Document> zhpuClientsDocs = documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(67).get());

                if (zhpuClientsDocs.size() > 1)
                {
                    StringBuilder zhpuDocsSb = new StringBuilder();
                    zhpuDocsSb.append("в Журналах поэкземплярного учета СКЗИ (п.3.6.6 Регламента ОКЗ, Журналы поэкземплярного учета СКЗИ ");

                    for (Document zhpuDoc : zhpuClientsDocs)
                    {
                        zhpuDocsSb.append("от "+ zhpuDoc.getDocumentDate() + " № " + zhpuDoc.getDocumentNumber());
                    }

                    replaseText(doc, "ZHPU_ONE_INFO", zhpuDocsSb.toString());
                    replaseTableText(doc, "ZHPU_ONE_INFO", zhpuDocsSb.toString());
                }
                else if (zhpuClientsDocs.size() == 1)
                {
                    replaseText(doc, "ZHPU_ONE_INFO", "в Журнале поэкземплярного учета СКЗИ (п.3.6.6 Регламента ОКЗ, Журнал поэкземплярного учета СКЗИ от " + zhpuClientsDocs.get(0).getDocumentDate() + " № " + zhpuClientsDocs.get(0).getDocumentNumber());
                    replaseTableText(doc, "ZHPU_ONE_INFO", "в Журнале поэкземплярного учета СКЗИ (п.3.6.6 Регламента ОКЗ, Журнал поэкземплярного учета СКЗИ от " + zhpuClientsDocs.get(0).getDocumentDate() + " № " + zhpuClientsDocs.get(0).getDocumentNumber());
                }
                else
                {
                    replaseText(doc, "ZHPU_ONE_INFO", ", Журнал поэкземплярного учета СКЗИ не предоставлен");
                    replaseTableText(doc, "ZHPU_ONE_INFO", ", Журнал поэкземплярного учета СКЗИ не предоставлен");
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
               // replaseText(doc, "CLIENT_PREPARE_ACT",  documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(68).get()).getDocumentName());
                replaseText(doc, "CLIENT_PREPARE_ACT",  "Хранение ключевых документов Пользователей Клиента осуществляется в личных опечатываемых личными печатями пеналах, хранящихся в запираемых ящиках столов или в личных металлических сейфах, закрывающихся на механический замок и опечатываемых личными печатями, или в металлических сейфах, имеющих кодовый замок, что " + documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(68).get()).get(0).getDocumentName() + ".");
                replaseTableText(doc, "CLIENT_PREPARE_ACT",  "Хранение ключевых документов Пользователей Клиента осуществляется в личных опечатываемых личными печатями пеналах, хранящихся в запираемых ящиках столов или в личных металлических сейфах, закрывающихся на механический замок и опечатываемых личными печатями, или в металлических сейфах, имеющих кодовый замок, что " + documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(68).get()).get(0).getDocumentName() + ".");
                //replaseTableText(doc, "CLIENT_PREPARE_ACT",  documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(68).get()).getDocumentName());

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            try
            {
                replaseText(doc, "KEY_EX_MONTHS", report.getSystem().getKeyExpirationMonths());
                replaseTableText(doc, "KEY_EX_MONTHS", report.getSystem().getKeyExpirationMonths());

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            replaseText(doc, "CLIENT_ARM_SKZI_NAME", clientSkzi.getName());
            replaseTableText(doc, "CLIENT_ARM_SKZI_NAME", clientSkzi.getName());

            replaseText(doc, "CLIENT_ARM_SKZI_VERSION", clientSkzi.getVersion());
            replaseTableText(doc, "CLIENT_ARM_SKZI_VERSION", clientSkzi.getVersion());

            replaseText(doc, "CLIENT_ARM_SKZI_VARIANT", clientSkzi.getRealizationVariant());
            replaseTableText(doc, "CLIENT_ARM_SKZI_VARIANT", clientSkzi.getRealizationVariant());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_DATE", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_DATE", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_BEFORE", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_BEFORE", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());

            replaseText(doc, "CLIENT_ARM_SKZI_DOC_NUM", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ARM_SKZI_DOC_NUM", documentRepo.findBySkziAndDocumentType(clientSkzi, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());

            replaseText(doc, "CLIENT_ZHPU_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(15).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_ZHPU_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(15).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_ZHPU_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(15).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ZHPU_SKZI_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(15).get()).get(0).getDocumentNumber());

            replaseText(doc, "CLIENT_ARM_NUM", clientArm.getArmNumber());
            replaseTableText(doc, "CLIENT_ARM_NUM", clientArm.getArmNumber());

            replaseText(doc, "CLIENT_ARM_AV_NAME_AND_VERSION", clientArm.getAntivirus().getAvName() + ", версия " + clientArm.getAntivirus().getAvVersion());
            replaseTableText(doc, "CLIENT_ARM_AV_NAME_AND_VERSION", clientArm.getAntivirus().getAvName() + ", версия " + clientArm.getAntivirus().getAvVersion());

            replaseText(doc, "CLIENT_ARM_AV_DOC_DATE", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());
            replaseTableText(doc, "CLIENT_ARM_AV_DOC_DATE", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());

            replaseText(doc, "CLIENT_ARM_AV_DOC_BEFORE", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());
            replaseTableText(doc, "CLIENT_ARM_AV_DOC_BEFORE", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());

            replaseText(doc, "CLIENT_ARM_AV_DOC_NUM", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ARM_AV_DOC_NUM", documentRepo.findByAntivirusAndDocumentType(clientArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());

            List<ARMSzi> clientArmSZIList = armsziRepo.findByArm(clientArm);

            SZI clientArmSzi = clientArmSZIList.get(0).getSzi();

            replaseText(doc, "CLIENT_ARM_SZI_NAME_AND_VERSION", clientArmSzi.getSziName());
            replaseTableText(doc, "CLIENT_ARM_SZI_NAME_AND_VERSION", clientArmSzi.getSziName());

            replaseText(doc, "CLIENT_ARM_SZI_DOC_DATE", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getDocumentDate());
            replaseTableText(doc, "CLIENT_ARM_SZI_DOC_DATE", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getDocumentDate());

            replaseText(doc, "CLIENT_ARM_SZI_DOC_BEFORE", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());
            replaseTableText(doc, "CLIENT_ARM_SZI_DOC_BEFORE", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());

            replaseText(doc, "CLIENT_ARM_SZI_DOC_NUM", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());
            replaseTableText(doc, "CLIENT_ARM_SZI_DOC_NUM", documentRepo.findBySziAndDocumentType(clientArmSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());

            try{
                List<Document> clientEducationDocs = documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get());

                if (clientEducationDocs.size() > 1)
                {
                    StringBuilder clientEducationSb = new StringBuilder("что подтверждается заключениями о сдачи зачетов ");

                    for (Document clientEducationDoc : clientEducationDocs)
                    {
                        clientEducationSb.append("от " + clientEducationDoc.getDocumentDate() + " № " + clientEducationDoc.getDocumentNumber());
                    }

                    replaseText(doc, "CLIENT_EDUCATION_INFO", clientEducationSb.toString());
                    replaseTableText(doc, "CLIENT_EDUCATION_INFO", clientEducationSb.toString());

                }
                else if (clientEducationDocs.size() == 1)
                {
                    replaseText(doc, "CLIENT_EDUCATION_INFO", "что подтверждается заключением о сдаче зачетов от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentNumber());
                    replaseTableText(doc, "CLIENT_EDUCATION_INFO", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentNumber());

                }
                else
                {
                    replaseText(doc, "CLIENT_EDUCATION_INFO", "");
                    replaseTableText(doc, "CLIENT_EDUCATION_INFO", "");

                }

           }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            replaseText(doc, "CLIENT_EDUCATION_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_EDUCATION_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(37).get()).get(0).getDocumentNumber());

            try
            {
                List<Document> clientAccessDocs = documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get());

                if(clientAccessDocs.size() > 1)
                {
                    StringBuilder clientAccessSb = new StringBuilder("что подтверждается приказами о допуски пользователей к самостоятельной работе с СКЗИ ");
                    for (Document clientAccessDoc : clientAccessDocs)
                    {
                        clientAccessSb.append(" от " + clientAccessDoc.getDocumentDate() + " № " + clientAccessDoc.getDocumentNumber());
                    }
                    replaseText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", clientAccessSb.toString());
                    replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", clientAccessSb.toString());
                }
                else if (clientAccessDocs.size() == 1)
                {
                    replaseText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", "что подтверждается приказом о допуске пользователей к самостоятельной работе с СКЗИ от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentNumber());
                    replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", "что подтверждается приказом о допуске пользователей к самостоятельной работе с СКЗИ от " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentDate() + " № " + documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentNumber());

                }
                else
                {
                    replaseText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", "");
                    replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_INFO", "");

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            replaseText(doc, "CLIENT_USERS_ACCESS_SKZI_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentDate());
            replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentDate());

            replaseText(doc, "CLIENT_USERS_ACCESS_SKZI_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "CLIENT_USERS_ACCESS_SKZI_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(36).get()).get(0).getDocumentNumber());

            List<Document> clientRolesDocuments = documentRepo.findByOrganizationAndDocumentType(report.getOrganization(), documentTypeRepo.findById(38).get());

            if (clientRolesDocuments.size() > 1)
            {
                StringBuilder sb = new StringBuilder("Права подписей Пользователям Клиента предоставлены согласно документа ");
                for (Document document : clientRolesDocuments)
                {
                    sb.append("от " + document.getDocumentDate() + " № " + document.getDocumentNumber());
                }
                replaseText(doc, "CLIENT_CLIENT_ROLES", sb.toString());
                replaseTableText(doc, "CLIENT_CLIENT_ROLES", sb.toString());
            }
            else if (clientRolesDocuments.size() == 1)
            {
                replaseText(doc, "CLIENT_CLIENT_ROLES", "Права подписей Пользователям Клиента предоставлены согласно документа от " + clientRolesDocuments.get(0).getDocumentDate() + " № " + clientRolesDocuments.get(0).getDocumentNumber());
                replaseTableText(doc, "CLIENT_CLIENT_ROLES", "Права подписей Пользователям Клиента предоставлены согласно документа от " + clientRolesDocuments.get(0).getDocumentDate() + " № " + clientRolesDocuments.get(0).getDocumentNumber());
            }
            else
            {
                replaseText(doc, "CLIENT_CLIENT_ROLES", "Информация о правах подписей Пользователей Клиента не предоставлена");
                replaseTableText(doc, "CLIENT_CLIENT_ROLES", "Информация о правах подписей Пользователей Клиента не предоставлена");
            }

            try
            {
            SKZI bankClientsSKZI = null;
            if (bankClientsArms) {
                ARM etaloneBankSystemArm = null;

                for (ARM bankSystemArm : bankSystemARMs) {
                    if (bankSystemArm.getArmType().getId() == 2) {
                        etaloneBankSystemArm = bankSystemArm;
                        bankClientsSKZI = armskziRepo.findByArm(bankSystemArm).get(0).getSkzi();
                        break;
                    }

                }

                if (bankClientsSKZI == null)
                {
                    replaseText(doc, "BANK_USERS_SKZI_NAME", "Информация об используемых СКЗИ на стороне Банка не предоставлена");
                    replaseTableText(doc, "BANK_USERS_SKZI_NAME", "Информация об используемых СКЗИ на стороне Банка не предоставлена");
                }
                else
                {
                    replaseText(doc, "BANK_USERS_SKZI_NAME", "На стороне Банка используются " + bankClientsSKZI.getName());
                    replaseTableText(doc, "BANK_USERS_SKZI_NAME", "На стороне Банка используются " + bankClientsSKZI.getName());
                }

                List<Document> bankSkziPrepare = documentRepo.findByArmAndDocumentType(etaloneBankSystemArm, documentTypeRepo.findById(68).get());

                if (bankSkziPrepare.size() == 0)
                {
                    replaseText(doc, "BANK_SKZI_PREPARE", "Информация о готовности " + bankClientsSKZI.getName() + " к эксплуатации на АРМ Пользователей Банка не предоставлена");
                    replaseTableText(doc, "BANK_SKZI_PREPARE", "Информация о готовности " + bankClientsSKZI.getName() + " к эксплуатации на АРМ Пользователей Банка не предоставлена");
                }
                else if(bankSkziPrepare.size() == 1)
                {
                    replaseText(doc, "BANK_SKZI_PREPARE", "Банк подтвердил готовность СКЗИ " + bankClientsSKZI.getName() + " к эксплуатации на АРМ Пользователей Банка документом " + bankSkziPrepare.get(0).getDocumentName() + " от " + bankSkziPrepare.get(0).getDocumentDate() + " № " + bankSkziPrepare.get(0).getDocumentNumber());
                    replaseTableText(doc, "BANK_SKZI_PREPARE", "Банк подтвердил готовность СКЗИ " + bankClientsSKZI.getName() + " к эксплуатации на АРМ Пользователей Банка документом " + bankSkziPrepare.get(0).getDocumentName() + " от " + bankSkziPrepare.get(0).getDocumentDate() + " № " + bankSkziPrepare.get(0).getDocumentNumber());
                }
                else
                {
                    StringBuilder sb = new StringBuilder("Несколько");
                        /*
                    for (Document document : bankSkziPrepare)
                    {

                    }
                    */
                    replaseText(doc, "BANK_SKZI_PREPARE", "Несколько");
                    replaseTableText(doc, "BANK_SKZI_PREPARE", "Несколько");


                }
                try
                {
                    replaseText(doc, "BANK_USERS_AV_ZACL", "На АРМ Пользователей Банка в качестве антивирусного средства используется " + etaloneBankSystemArm.getAntivirus().getAvName() + ", версия " + etaloneBankSystemArm.getAntivirus().getAvVersion());
                    replaseTableText(doc, "BANK_USERS_AV_ZACL", "На АРМ Пользователей Банка в качестве антивирусного средства используется " + etaloneBankSystemArm.getAntivirus().getAvName() + ", версия " + etaloneBankSystemArm.getAntivirus().getAvVersion());
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_USERS_AV_ZACL", "Информация о наличии антивирусного средства на АРМ Пользователей Банка не предоставлена");
                    replaseTableText(doc, "BANK_USERS_AV_ZACL", "Информация о наличии антивирусного средства на АРМ Пользователей Банка не предоставлена");

                }





                replaseText(doc, "BANK_USERS_SKZI_NAME", bankClientsSKZI.getName());
                replaseTableText(doc, "BANK_USERS_SKZI_NAME", bankClientsSKZI.getName());

                replaseText(doc, "BANK_USERS_SKZI_DOC_DATE", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());
                replaseTableText(doc, "BANK_USERS_SKZI_DOC_DATE", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getDocumentDate());

                replaseText(doc, "BANK_USERS_SKZI_DOC_BEFORE", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());
                replaseTableText(doc, "BANK_USERS_SKZI_DOC_BEFORE", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getValidUntilDate());

                replaseText(doc, "BANK_USERS_SKZI_DOC_NUM", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());
                replaseTableText(doc, "BANK_USERS_SKZI_DOC_NUM", documentRepo.findBySkziAndDocumentType(bankClientsSKZI, documentTypeRepo.findById(28).get()).get(0).getDocumentNumber());

                replaseText(doc, "BANK_USERS_AV_NAME_AND_VERSION", etaloneBankSystemArm.getAntivirus().getAvName() + ", версия " + etaloneBankSystemArm.getAntivirus().getAvVersion());
                replaseTableText(doc, "BANK_USERS_AV_NAME_AND_VERSION", etaloneBankSystemArm.getAntivirus().getAvName() + ", версия " + etaloneBankSystemArm.getAntivirus().getAvVersion());

                replaseText(doc, "BANK_USERS_AV_DOC_DATE", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());
                replaseTableText(doc, "BANK_USERS_AV_DOC_DATE", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentDate());

                replaseText(doc, "BANK_USERS_AV_DOC_BEFORE", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());
                replaseTableText(doc, "BANK_USERS_AV_DOC_BEFORE", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getValidUntilDate());

                replaseText(doc, "BANK_USERS_AV_DOC_NUM", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());
                replaseTableText(doc, "BANK_USERS_AV_DOC_NUM", documentRepo.findByAntivirusAndDocumentType(etaloneBankSystemArm.getAntivirus(), documentTypeRepo.findById(34).get()).getDocumentNumber());

                SZI bankClientsArmSzi = armsziRepo.findByArm(etaloneBankSystemArm).get(0).getSzi();

                try {
                    replaseText(doc, "BANK_USERS_SZI_INFO", "На АРМ Пользователей Банка установлено СЗИ от НСД " + bankClientsArmSzi.getSziName());
                    replaseTableText(doc, "BANK_USERS_SZI_INFO", "На АРМ Пользователей Банка установлено СЗИ от НСД " + bankClientsArmSzi.getSziName());
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_USERS_SZI_INFO", "Информация о наличии СЗИ от НСД на АРМ Пользователей Банка не предоставлена");
                    replaseTableText(doc, "BANK_USERS_SZI_INFO", "Информация о наличии СЗИ от НСД на АРМ Пользователей Банка не предоставлена");

                }

                try {
                    List<Document> bankUsersSkziEducDocs = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get());
                    replaseText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Банк подтвердил прохождение Пользователями Банка обучения правилам работы с СКЗИ, сдачу зачетов по программе обучения документом " + bankUsersSkziEducDocs.get(0).getDocumentName() + " от " + bankUsersSkziEducDocs.get(0).getDocumentDate() + " № " + bankUsersSkziEducDocs.get(0).getDocumentNumber());
                    replaseTableText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Банк подтвердил прохождение Пользователями Банка обучения правилам работы с СКЗИ, сдачу зачетов по программе обучения документом " + bankUsersSkziEducDocs.get(0).getDocumentName() + " от " + bankUsersSkziEducDocs.get(0).getDocumentDate() + " № " + bankUsersSkziEducDocs.get(0).getDocumentNumber());
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Информация об обучении Пользователей Банка не предоставлена");
                    replaseTableText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Информация об обучении Пользователей Банка не предоставлена");

                }

                try
                {
                    List<Document> bankUsersAccessDocs = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(23).get());
                    if(bankUsersAccessDocs.size() != 0)
                    {
                        StringBuilder sb = new StringBuilder("Банк подтвердил допуск пользователей Банка к работе с СКЗИ документами : ");

                        for (Document accessDoc : bankUsersAccessDocs) {
                            sb.append(accessDoc.getDocumentName() + " от " + accessDoc.getDocumentDate() + " № " + accessDoc.getDocumentNumber());
                        }

                        replaseText(doc, "BANK_USERS_ACCESS", sb.toString());
                        replaseTableText(doc, "BANK_USERS_ACCESS", sb.toString());
                    }
                    else
                    {
                        replaseText(doc, "BANK_USERS_ACCESS", "Информация о допуске пользователей Банка к работе с СКЗИ не предоставлена");
                        replaseTableText(doc, "BANK_USERS_ACCESS", "Информация о допуске пользователей Банка к работе с СКЗИ не предоставлена");
                    }


                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_USERS_ACCESS", "Информация о допуске пользователей Банка к работе с СКЗИ не предоставлена");
                    replaseTableText(doc, "BANK_USERS_ACCESS", "Информация о допуске пользователей Банка к работе с СКЗИ не предоставлена");
                }

                try {
                    List<Document> bankRolesDocs = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(25).get());
                    if (bankRolesDocs.size() == 0)
                    {
                        replaseText(doc, "BANK_USERS_ROLES", "Информация о правах и ролях работников Банка не предоставлена");
                        replaseTableText(doc, "BANK_USERS_ROLES", "Информация о правах и ролях работников Банка не предоставлена");

                    }
                else
                    {
                        StringBuilder sb = new StringBuilder("Банк подтвердил наличие утвержденных прав и ролей работников Банка документом ");
;
                        for (Document document : bankRolesDocs)
                        {
                            sb.append(document.getDocumentName() + " от " + document.getDocumentDate() + " № " + document.getDocumentNumber());
                        }
                        replaseText(doc, "BANK_USERS_ROLES", sb.toString());
                        replaseTableText(doc, "BANK_USERS_ROLES", sb.toString());

                    }
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_USERS_ROLES", "Наличие информации о правах и ролях работников Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                    replaseTableText(doc, "BANK_USERS_ROLES", "Наличие информации о правах и ролях работников Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                }



                try
                {
                    List<Document> bankPeriodicControlDocs = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(26).get());

                    if (bankPeriodicControlDocs.size() == 0)
                    {
                        replaseText(doc, "BANK_PERIODIC_CONTROL", "Информация о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не предоставлена");
                        replaseTableText(doc, "BANK_PERIODIC_CONTROL", "Информация о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не предоставлена");

                    }
                    else
                    {
                        StringBuilder sb = new StringBuilder("Банк подтвердил наличие периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка документом ");

                        for (Document document : bankPeriodicControlDocs)
                        {
                            sb.append(document.getDocumentName() + " от " + document.getDocumentDate() + " № " + document.getDocumentNumber());
                        }
                        replaseText(doc, "BANK_PERIODIC_CONTROL", sb.toString());
                        replaseTableText(doc, "BANK_PERIODIC_CONTROL", sb.toString());

                    }
                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_PERIODIC_CONTROL", "Информация о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не предоставлена");
                    replaseTableText(doc, "BANK_PERIODIC_CONTROL", "Информация о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не предоставлена");

                }

                try
                {
                    List<Document> bankSkziRemoveDocs = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(71).get());
                    replaseText(doc, "BANK_SKZI_REMOVE", bankSkziRemoveDocs.get(0).getDocumentName());
                    replaseTableText(doc, "BANK_SKZI_REMOVE", bankSkziRemoveDocs.get(0).getDocumentName());

                }
                catch (Exception e)
                {
                    replaseText(doc, "BANK_SKZI_REMOVE", "Информация об уничтожении СКЗИ Пользователей Банка не предоставлена");
                    replaseTableText(doc, "BANK_SKZI_REMOVE", "Информация об уничтожении СКЗИ Пользователей Банка не предоставлена");
                }

                if (report.getSystem().getCryptoType().contains("ГОСТ"))
                {
                    replaseText(doc, "SYSTEM_CRYPTO_TYPE", "протокол TLS с применением алгоритма ГОСТ с использованием " + bankClientsSKZI.getName());
                    replaseTableText(doc, "SYSTEM_CRYPTO_TYPE", "протокол TLS с применением алгоритма ГОСТ с использованием " + bankClientsSKZI.getName());
                }
                else if (report.getSystem().getCryptoType().contains("RSA"))
                {
                    replaseText(doc, "SYSTEM_CRYPTO_TYPE", "протокол TLS с применением алгоритма RSA");
                    replaseTableText(doc, "SYSTEM_CRYPTO_TYPE", "протокол TLS с применением алгоритма RSA");
                }

                try
                {
                    Document epTypeDocs = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(40).get()).get(0);
                    if(epTypeDocs == null)
                    {
                        throw new NullPointerException();
                    }
                    replaseText(doc, "SYSTEM_EP_TYPE", "усиленная квалифицированная электронная подпись");
                    //replaseTableText(doc, "SYSTEM_EP_TYPE", "усиленная квалифицированная электронная подпись");
                }
                catch (NullPointerException e)
                {
                    try {
                        Document epTypeDocs = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(41).get()).get(0);
                        if(epTypeDocs == null)
                        {
                            throw new NullPointerException();
                        }
                        replaseText(doc, "SYSTEM_EP_TYPE", "усиленная неквалифицированная электронная подпись");
                        //replaseTableText(doc, "SYSTEM_EP_TYPE", "усиленная неквалифицированная электронная подпись");
                    }
                    catch (NullPointerException ex)
                    {
                        e.printStackTrace();
                    }
                }

                try
                {
                    Document strongNequalEpDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(42).get()).get(0);
                    if (strongNequalEpDoc == null)
                    {
                        replaseText(doc, "STRONG_NEQUAL_EP_ORDER", "не используются");
                        replaseTableText(doc, "STRONG_NEQUAL_EP_ORDER", "не используются");

                    }
                    else
                    {
                        replaseText(doc, "STRONG_NEQUAL_EP_ORDER", "Используются");
                        replaseTableText(doc, "STRONG_NEQUAL_EP_ORDER", "Используются");

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    Document clientSystemActDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(33).get()).get(0);
                    if (clientSystemActDoc == null)
                    {
                        replaseText(doc, "CLIENT_SYSTEM_ACT_DOC","Информация о выполнении требований по безопасности информации на стороне Клиента не предоставлена");
                        replaseTableText(doc, "CLIENT_SYSTEM_ACT_DOC", "Информация о выполнении требований по безопасности информации на стороне Клиента не предоставлена");
                    }
                    else
                    {
                        replaseText(doc, "CLIENT_SYSTEM_ACT_DOC","Выполнение требований по безопасности информации на стороне Клиента подтверждается документом " + clientSystemActDoc.getDocumentName());
                        replaseTableText(doc, "CLIENT_SYSTEM_ACT_DOC", "Выполнение требований по безопасности информации на стороне Клиента подтверждается документом " + clientSystemActDoc.getDocumentName());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    List<Document> clientOKZReportDoc = documentRepo.findByArmAndDocumentType(clientArm, documentTypeRepo.findById(30).get());
                    if (clientOKZReportDoc.size() == 0)
                    {
                        replaseText(doc, "CLIENT_OKZ_REPORT","Информация о выполнении требований ФСБ России к условиям эксплуатации СКЗИ на стороне Клиента не представлена");
                        replaseTableText(doc, "CLIENT_OKZ_REPORT", "Информация о выполнении требований ФСБ России к условиям эксплуатации СКЗИ на стороне Клиента не представлена");
                    }
                    else
                    {
                        replaseText(doc, "CLIENT_OKZ_REPORT","Выполнение требований ФСБ России к условиям эксплуатации СКЗИ на стороне Клиента подтверждается заключением о возможности эксплуатации СКЗИ");
                        replaseTableText(doc, "CLIENT_OKZ_REPORT", "Выполнение требований ФСБ России к условиям эксплуатации СКЗИ на стороне Клиента подтверждается заключением о возможности эксплуатации СКЗИ");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    List<Document> bankStandartDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(11).get());

                    if (bankStandartDoc.size() == 0)
                    {
                        replaseText(doc, "BANK_STANDART", "Информация о выполнении требований Банка России не предоставлена");
                        replaseTableText(doc, "BANK_STANDART", "Информация о выполнении требований Банка России не предоставлена");
                    }
                    else
                    {
                        replaseText(doc, "BANK_STANDART", "Выполнение требований Банка России подтверждается документом " + bankStandartDoc.get(0).getDocumentName());
                        replaseTableText(doc, "BANK_STANDART", "Выполнение требований Банка России подтверждается документом " + bankStandartDoc.get(0).getDocumentName());
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    List<Document> bankSystemAtestatDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(18).get());

                    if (bankSystemAtestatDoc.size() != 0)
                    {
                        replaseText(doc, "BANK_SYSTEM_ATESTAT", "Выполнение требований по безопасности информации на стороне Банка подтверждается документом " + bankSystemAtestatDoc.get(0).getDocumentName());
                        replaseTableText(doc, "BANK_SYSTEM_ATESTAT", "Выполнение требований по безопасности информации на стороне Банка подтверждается документом " + bankSystemAtestatDoc.get(0).getDocumentName());
                    }
                    else
                    {
                        List<Document> bankStandartDocList = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(11).get());
                        if (bankStandartDocList.size() != 0 && bankStandartDocList.get(0).getDocumentName().contains("0403202"))
                        {
                            replaseText(doc, "BANK_SYSTEM_ATESTAT", "Выполнение требований по безопасности информации на стороне Банка подтверждается документом «Отчет о результатах проведения оценки выполнения требований к обеспечению защиты информации при осуществлении переводов денежных средств, установленных Положением Банка России от 09 июня 2012г. №382-П»");
                            replaseTableText(doc, "BANK_SYSTEM_ATESTAT", "Выполнение требований по безопасности информации на стороне Банка подтверждается документом «Отчет о результатах проведения оценки выполнения требований к обеспечению защиты информации при осуществлении переводов денежных средств, установленных Положением Банка России от 09 июня 2012г. №382-П»");

                        }
                        else
                            {
                                replaseText(doc, "BANK_SYSTEM_ATESTAT", "Информация о выполнении требований по безопасности информации на стороне Банка не предоставлена");
                                replaseTableText(doc, "BANK_SYSTEM_ATESTAT", "Информация о выполнении требований по безопасности информации на стороне Банка не предоставлена");

                            }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    List<Document> bankOKZOrderDoc = documentRepo.findByArmAndDocumentType(bankClientARMs.get(0), documentTypeRepo.findById(30).get());

                    if (bankOKZOrderDoc.size() == 0)
                    {
                        replaseText(doc, "BANK_OKZ_ORDER_SKZI", "Информация о выполнении требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка не представлена");
                        replaseTableText(doc, "BANK_OKZ_ORDER_SKZI", "Информация о выполнении требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка не представлена");
                    }
                    else
                    {
                        replaseText(doc, "BANK_OKZ_ORDER_SKZI", "Выполнение требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка подтверждается «" + bankOKZOrderDoc.get(0).getDocumentName() + "»");
                        replaseTableText(doc, "BANK_OKZ_ORDER_SKZI", "Выполнение требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка подтверждается «" + bankOKZOrderDoc.get(0).getDocumentName() + "»");

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                try
                {
                    Document skziKorVstraiDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(17).get()).get(0);

                    if (skziKorVstraiDoc == null)
                    {
                        replaseText(doc, "SYSTEM_KOR_VSTR", "Информация о выполнении требований по корректности встраивания СКЗИ в Систему не предоставлена");
                        replaseTableText(doc, "SYSTEM_KOR_VSTR", "Информация о выполнении требований по корректности встраивания СКЗИ в Систему не предоставлена");
                    }
                    else
                    {
                        replaseText(doc, "SYSTEM_KOR_VSTR", "Реализованные вызовы функций СКЗИ соответствуют «Перечню вызовов, использование которых при разработке прикладного ПО с применением " + clientSkzi.getName() + " возможно без проведения дополнительных тематических исследований»");
                        replaseTableText(doc, "SYSTEM_KOR_VSTR", "Реализованные вызовы функций СКЗИ соответствуют «Перечню вызовов, использование которых при разработке прикладного ПО с применением " + clientSkzi.getName() + " возможно без проведения дополнительных тематических исследований»");
                    }
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseText(doc, "SYSTEM_KOR_VSTR", "Реализованные вызовы функций СКЗИ соответствуют «Перечню вызовов, использование которых при разработке прикладного ПО с применением " + clientSkzi.getName() + " возможно без проведения дополнительных тематических исследований»");
                    replaseTableText(doc, "SYSTEM_KOR_VSTR", "Реализованные вызовы функций СКЗИ соответствуют «Перечню вызовов, использование которых при разработке прикладного ПО с применением " + clientSkzi.getName() + " возможно без проведения дополнительных тематических исследований»");

                    e.printStackTrace();
                }

                try
                {
                    Document systemDocLinkDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(44).get()).get(0);

                    if (systemDocLinkDoc != null)
                    {
                        replaseText(doc, "SYSTEM_LINK", systemDocLinkDoc.getLink());
                        replaseTableText(doc, "SYSTEM_LINK", systemDocLinkDoc.getLink());
                    }
                    else
                    {
                        throw new NullPointerException();
                    }

                }
                catch (IndexOutOfBoundsException e)
                {
                  e.printStackTrace();
                }

                Set<String> trustLevelSet = new HashSet<>();

                try
                {
                    Document bankLicDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(46).get()).get(0);

                    if (bankLicDoc != null)
                    {
                        replaseTableText(doc, "BANK_FSB_LICENSE_TABLE", "ДА");
                        replaseTableText(doc, "BANK_FSB_LICENSE_DATE_TABLE", bankLicDoc.getDocumentDate());
                        replaseTableText(doc, "BANK_FSB_LICENSE_UNTIL_DATE_TABLE", bankLicDoc.getValidUntilDate());
                        replaseTableText(doc, "BANK_FSB_LICENSE_NUM_TABLE",  bankLicDoc.getDocumentNumber());
                        replaseTableText(doc, "BANK_FSB_LICENSE_TRUST_LEVEL_TABLE",  "В");
                        trustLevelSet.add("В");

                    }
                    else
                    {
                        replaseTableText(doc, "BANK_FSB_LICENSE_TABLE", "НЕТ");
                        replaseTableText(doc, "BANK_FSB_LICENSE_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_FSB_LICENSE_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_FSB_LICENSE_NUM_TABLE",  "-");
                        replaseTableText(doc, "BANK_FSB_LICENSE_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "BANK_FSB_LICENSE_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_FSB_LICENSE_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "BANK_FSB_LICENSE_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_FSB_LICENSE_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_FSB_LICENSE_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document clientRoghtToPakDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(1).get()).get(0);

                    if (clientRoghtToPakDoc != null)
                    {
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_TABLE", "ДА");
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_DATE_TABLE", clientRoghtToPakDoc.getDocumentDate());
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_UNTIL_DATE_TABLE", clientRoghtToPakDoc.getValidUntilDate());
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_NUM_TABLE",  clientRoghtToPakDoc.getDocumentNumber());
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_TRUST_LEVEL_TABLE",  "В");
                        trustLevelSet.add("В");

                    }
                    else
                    {
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_TABLE", "НЕТ");
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_DATE_TABLE", "-");
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_NUM_TABLE",  "-");
                        replaseTableText(doc, "CLIENT_RIGHT_PAK_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_TABLE", "НЕТ");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_NUM_TABLE",  "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_TABLE", "НЕТ");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_NUM_TABLE",  "-");
                    replaseTableText(doc, "CLIENT_RIGHT_PAK_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document pakUCCertDoc = documentRepo.findByPakUCAndDocumentType(bankPacUC, documentTypeRepo.findById(5).get()).get(0);

                    if (pakUCCertDoc != null)
                    {
                        replaseTableText(doc, "BANK_PAK_CERT_TABLE", "ДА");
                        replaseTableText(doc, "BANK_PAK_CERT_DATE_TABLE", pakUCCertDoc.getDocumentDate());
                        replaseTableText(doc, "BANK_PAK_CERT_UNTIL_DATE_TABLE", pakUCCertDoc.getValidUntilDate());
                        replaseTableText(doc, "BANK_PAK_CERT_NUM_TABLE",  pakUCCertDoc.getDocumentNumber());
                        replaseTableText(doc, "BANK_PAK_CERT_TRUST_LEVEL_TABLE",  "В");
                        trustLevelSet.add("В");

                    }
                    else
                    {
                        replaseTableText(doc, "BANK_PAK_CERT_TABLE", "НЕТ");
                        replaseTableText(doc, "BANK_PAK_CERT_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_PAK_CERT_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_PAK_CERT_NUM_TABLE",  "-");
                        replaseTableText(doc, "BANK_PAK_CERT_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "BANK_PAK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_PAK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_PAK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_PAK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_PAK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "BANK_PAK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_PAK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_PAK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_PAK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_PAK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document clientKeyKarriersDoc = documentRepo.findByKeyCarriersAndDocumentType(clientKc, documentTypeRepo.findById(7).get()).get(0);

                    if (clientKeyKarriersDoc != null)
                    {
                        replaseTableText(doc, "CLIENT_KK_CERT_TABLE", "ДА");
                        replaseTableText(doc, "CLIENT_KK_CERT_DATE_TABLE", clientKeyKarriersDoc.getDocumentDate());
                        replaseTableText(doc, "CLIENT_KK_CERT_UNTIL_DATE_TABLE", clientKeyKarriersDoc.getValidUntilDate());
                        replaseTableText(doc, "CLIENT_KK_CERT_NUM_TABLE",  clientKeyKarriersDoc.getDocumentNumber());
                        replaseTableText(doc, "CLIENT_KK_CERT_TRUST_LEVEL_TABLE",  "С");
                        trustLevelSet.add("С");

                    }
                    else
                    {
                        replaseTableText(doc, "CLIENT_KK_CERT_TABLE", "НЕТ");
                        replaseTableText(doc, "CLIENT_KK_CERT_DATE_TABLE", "-");
                        replaseTableText(doc, "CLIENT_KK_CERT_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "CLIENT_KK_CERT_NUM_TABLE",  "-");
                        replaseTableText(doc, "CLIENT_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "CLIENT_KK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "CLIENT_KK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "CLIENT_KK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "CLIENT_KK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "CLIENT_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document bankKeyKarriersDoc = documentRepo.findByKeyCarriersAndDocumentType(bankKc, documentTypeRepo.findById(7).get()).get(0);

                    if (bankKeyKarriersDoc != null)
                    {
                        replaseTableText(doc, "BANK_KK_CERT_TABLE", "ДА");
                        replaseTableText(doc, "BANK_KK_CERT_DATE_TABLE", bankKeyKarriersDoc.getDocumentDate());
                        replaseTableText(doc, "BANK_KK_CERT_UNTIL_DATE_TABLE", bankKeyKarriersDoc.getValidUntilDate());
                        replaseTableText(doc, "BANK_KK_CERT_NUM_TABLE",  bankKeyKarriersDoc.getDocumentNumber());
                        replaseTableText(doc, "BANK_KK_CERT_TRUST_LEVEL_TABLE",  "С");
                        trustLevelSet.add("С");

                    }
                    else
                    {
                        replaseTableText(doc, "BANK_KK_CERT_TABLE", "НЕТ");
                        replaseTableText(doc, "BANK_KK_CERT_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_KK_CERT_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_KK_CERT_NUM_TABLE",  "-");
                        replaseTableText(doc, "BANK_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "BANK_KK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_KK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_KK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_KK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "BANK_KK_CERT_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_KK_CERT_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_KK_CERT_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_KK_CERT_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_KK_CERT_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document bankReglUCDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(8).get()).get(0);

                    if (bankReglUCDoc != null)
                    {
                        replaseTableText(doc, "BANK_REG_UC_TABLE", "ДА");
                        replaseTableText(doc, "BANK_REG_UC_DATE_TABLE", bankReglUCDoc.getDocumentDate());
                        replaseTableText(doc, "BANK_REG_UC_UNTIL_DATE_TABLE", bankReglUCDoc.getValidUntilDate());
                        replaseTableText(doc, "BANK_REG_UC_NUM_TABLE",  bankReglUCDoc.getDocumentNumber());
                        replaseTableText(doc, "BANK_REG_UC_TRUST_LEVEL_TABLE",  "В");
                        trustLevelSet.add("В");

                    }
                    else
                    {
                        replaseTableText(doc, "BANK_REG_UC_TABLE", "НЕТ");
                        replaseTableText(doc, "BANK_REG_UC_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_REG_UC_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_REG_UC_NUM_TABLE",  "-");
                        replaseTableText(doc, "BANK_REG_UC_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "BANK_REG_UC_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_REG_UC_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_REG_UC_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_REG_UC_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_REG_UC_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "BANK_REG_UC_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_REG_UC_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_REG_UC_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_REG_UC_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_REG_UC_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document bankAccreditationDoc = documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(9).get()).get(0);

                    if (bankAccreditationDoc != null)
                    {
                        replaseTableText(doc, "BANK_ACC_TABLE", "ДА");
                        replaseTableText(doc, "BANK_ACC_DATE_TABLE", bankAccreditationDoc.getDocumentDate());
                        replaseTableText(doc, "BANK_ACC_UNTIL_DATE_TABLE", bankAccreditationDoc.getValidUntilDate());
                        replaseTableText(doc, "BANK_ACC_NUM_TABLE",  bankAccreditationDoc.getDocumentNumber());
                        replaseTableText(doc, "BANK_ACC_TRUST_LEVEL_TABLE",  "С");
                        trustLevelSet.add("С");

                    }
                    else
                    {
                        replaseTableText(doc, "BANK_ACC_TABLE", "НЕТ");
                        replaseTableText(doc, "BANK_ACC_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_ACC_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "BANK_ACC_NUM_TABLE",  "-");
                        replaseTableText(doc, "BANK_ACC_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }
                catch (NullPointerException e)
                {
                    replaseTableText(doc, "BANK_ACC_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_ACC_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_ACC_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_ACC_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_ACC_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }
                catch (IndexOutOfBoundsException e)
                {
                    replaseTableText(doc, "BANK_ACC_TABLE", "НЕТ");
                    replaseTableText(doc, "BANK_ACC_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_ACC_UNTIL_DATE_TABLE", "-");
                    replaseTableText(doc, "BANK_ACC_NUM_TABLE",  "-");
                    replaseTableText(doc, "BANK_ACC_TRUST_LEVEL_TABLE",  "Н");
                    trustLevelSet.add("Н");
                    e.printStackTrace();
                }

                try
                {
                    Document systemEPTypeUKEPDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(40).get()).get(0);

                    if (systemEPTypeUKEPDoc != null)
                    {
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TABLE", "ДА");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_DATE_TABLE", systemEPTypeUKEPDoc.getDocumentDate());
                        replaseTableText(doc, "SYSTEM_EP_TYPE_UNTIL_DATE_TABLE", systemEPTypeUKEPDoc.getValidUntilDate());
                        replaseTableText(doc, "SYSTEM_EP_TYPE_NUM_TABLE",  systemEPTypeUKEPDoc.getDocumentNumber());
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TRUST_LEVEL_TABLE",  "С");
                        trustLevelSet.add("С");

                    }
                    else
                    {
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TABLE", "НЕТ");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_DATE_TABLE", "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_NUM_TABLE",  "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                    }


                }

                catch (IndexOutOfBoundsException e)
                {
                    try
                    {
                        Document systemEPTypeUNEPDoc = documentRepo.findBySystemAndDocumentType(report.getSystem(), documentTypeRepo.findById(41).get()).get(0);

                        if (systemEPTypeUNEPDoc != null)
                        {
                            replaseTableText(doc, "SYSTEM_EP_TYPE_TABLE", "ДА");
                            replaseTableText(doc, "SYSTEM_EP_TYPE_DATE_TABLE", systemEPTypeUNEPDoc.getDocumentDate());
                            replaseTableText(doc, "SYSTEM_EP_TYPE_UNTIL_DATE_TABLE", systemEPTypeUNEPDoc.getValidUntilDate());
                            replaseTableText(doc, "SYSTEM_EP_TYPE_NUM_TABLE",  systemEPTypeUNEPDoc.getDocumentNumber());
                            replaseTableText(doc, "SYSTEM_EP_TYPE_TRUST_LEVEL_TABLE",  "С");
                            trustLevelSet.add("С");

                        }
                        else
                        {
                            replaseTableText(doc, "SYSTEM_EP_TYPE_TABLE", "НЕТ");
                            replaseTableText(doc, "SYSTEM_EP_TYPE_DATE_TABLE", "-");
                            replaseTableText(doc, "SYSTEM_EP_TYPE_UNTIL_DATE_TABLE", "-");
                            replaseTableText(doc, "SYSTEM_EP_TYPE_NUM_TABLE",  "-");
                            replaseTableText(doc, "SYSTEM_EP_TYPE_TRUST_LEVEL_TABLE",  "Н");
                            trustLevelSet.add("Н");
                        }


                    }

                    catch (IndexOutOfBoundsException ex)
                    {
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TABLE", "НЕТ");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_DATE_TABLE", "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_UNTIL_DATE_TABLE", "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_NUM_TABLE",  "-");
                        replaseTableText(doc, "SYSTEM_EP_TYPE_TRUST_LEVEL_TABLE",  "Н");
                        trustLevelSet.add("Н");
                        ex.printStackTrace();
                    }
                }

                replaseText(doc, "BANK_USERS_SZI_NAME", bankClientsArmSzi.getSziName());
                replaseTableText(doc, "BANK_USERS_SZI_NAME", bankClientsArmSzi.getSziName());

                replaseText(doc, "BANK_USERS_SZI_DOC_DATE", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getDocumentDate());
                replaseTableText(doc, "BANK_USERS_SZI_DOC_DATE", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getDocumentDate());

                replaseText(doc, "BANK_USERS_SZI_DOC_BEFORE", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());
                replaseTableText(doc, "BANK_USERS_SZI_DOC_BEFORE", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getValidUntilDate());

                replaseText(doc, "BANK_USERS_SZI_DOC_NUM", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());
                replaseTableText(doc, "BANK_USERS_SZI_DOC_NUM", documentRepo.findBySziAndDocumentType(bankClientsArmSzi, documentTypeRepo.findById(22).get()).getDocumentNumber());

                replaseText(doc, "BANK_EDUCATION_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentNumber());
                replaseTableText(doc, "BANK_EDUCATION_DOC_NUM", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentNumber());

                replaseText(doc, "BANK_EDUCATION_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentName());
                replaseTableText(doc, "BANK_EDUCATION_DOC_NAME", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentName());

                replaseText(doc, "BANK_EDUCATION_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentDate());
                replaseTableText(doc, "BANK_EDUCATION_DOC_DATE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getDocumentDate());

                replaseText(doc, "BANK_EDUCATION_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getValidUntilDate());
                replaseTableText(doc, "BANK_EDUCATION_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getValidUntilDate());


            }
            else
            {
                replaseText(doc, "BANK_USERS_SKZI_NAME", "На стороне Банка пользователи СКЗИ отсутствуют");
                replaseTableText(doc, "BANK_USERS_SKZI_NAME", "На стороне Банка пользователи СКЗИ отсутствуют");

                replaseText(doc, "BANK_SKZI_PREPARE", "Подтверждение готовности СКЗИ к эксплуатации на АРМ Пользователей Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_SKZI_PREPARE", "Подтверждение готовности СКЗИ к эксплуатации на АРМ Пользователей Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_USERS_AV_ZACL", "Наличие антивирусного средства не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_USERS_AV_ZACL", "Наличие антивирусного средства не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_USERS_SZI_INFO", "Наличие СЗИ от НСД не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_USERS_SZI_INFO", "Наличие СЗИ от НСД не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Наличие информации об обучении Пользователей Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_USERS_SZKI_EDUC_INFO", "Наличие информации об обучении Пользователей Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_USERS_ACCESS", "Наличие информации о допуске пользователей Банка к работе с СКЗИ не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_USERS_ACCESS", "Наличие информации о допуске пользователей Банка к работе с СКЗИ не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_USERS_ROLES", "Наличие информации о правах и ролях работников Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_USERS_ROLES", "Наличие информации о правах и ролях работников Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_PERIODIC_CONTROL", "Наличие информации о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");
                replaseTableText(doc, "BANK_PERIODIC_CONTROL", "Наличие информации о проведении периодического контроля администраторами безопасности условий использования СКЗИ на стороне Банка не влияет на уровень доверия по причине отсутствия Пользователей Банка");

                replaseText(doc, "BANK_SKZI_REMOVE", "Уничтожение СКЗИ Пользователей Банка не осуществляется, т.к. Пользователи на стороне Банка отсутствуют");
                replaseTableText(doc, "BANK_SKZI_REMOVE", "Уничтожение СКЗИ Пользователей Банка не осуществляется, т.к. Пользователи на стороне Банка отсутствуют");

                replaseText(doc, "BANK_OKZ_ORDER_SKZI", "Оценка выполнения требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка не проводилась, т.к. пользователи СКЗИ отсутствуют");
                replaseTableText(doc, "BANK_OKZ_ORDER_SKZI", "Оценка выполнения требований ФСБ России к условиям эксплуатации СКЗИ на стороне Банка не проводилась, т.к. пользователи СКЗИ отсутствуют");




            }
            }
            catch(Exception e)
            {

            }

            replaseText(doc, "BANK_SKZI_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getValidUntilDate());
            replaseTableText(doc, "BANK_EDUCATION_DOC_BEFORE", documentRepo.findByOrganizationAndDocumentType(report.getBank(), documentTypeRepo.findById(24).get()).get(0).getValidUntilDate());

            replaseText(doc, "BANK_PAKUC_SKZI_DOC_DATE", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentDate());
            replaseTableText(doc, "BANK_PAKUC_SKZI_DOC_DATE", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentDate());

            replaseText(doc, "BANK_PAKUC_SKZI_DOC_NUM", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentNumber());
            replaseTableText(doc, "BANK_PAKUC_SKZI_DOC_NUM", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentNumber());

            replaseText(doc, "BANK_PAKUC_SKZI_DOC_NAME", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentName());
            replaseTableText(doc, "BANK_PAKUC_SKZI_DOC_NAME", documentRepo.findByArmAndDocumentType(bankPacUcARM, documentTypeRepo.findById(30).get()).get(0).getDocumentName());





            File reportFile = new File(REPORTS_CATALOG_NAME + report.getId() + "_" + reportFileNameDateFormat.format(new Date()) + ".docx");

            if (reportFile.exists()) {
                reportFile.delete();
            }

            OutputStream out = new FileOutputStream(reportFile);
            doc.write(out);

            out.flush();
            out.close();

            report.setDocumentFileName(reportFile.getName());
            log.info("Сформировано заключение на материальном носителе " + report.getDocumentName() + " № " + report.getDocumentNumber() + " от " + report.getDocumentDate() + ". имя файла - " + reportFile.getName() + ";");

        } catch (Exception e) {

            model.put("paperreporterror", "1");
            e.printStackTrace();
        }

        String dbvaliduntildate = null;
        try {
            dbvaliduntildate = dbDateFormat.format(dayDateFormat.parse(report.getValidUntilDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        List<SystemStatus> systemStatuses = systemStatusRepo.findAll();
        List<Status> statuses = statusRepo.findAll();

        model.put("reportid", reportid);
        model.put("report", report);
        model.put("dbvaliduntildate", dbvaliduntildate);
        model.put("systemstatuses", systemStatuses);
        model.put("statuses", statuses);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "showonereport";
    }

    @GetMapping("/addreportskzi")
    public String addreportskzi(
            @RequestParam(required = false, defaultValue = "0") String reportnumber,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        Document newReport = documentRepo.findByDocumentNumberAndDocumentType(reportnumber, documentTypeRepo.findById(45).get());

        Organization bank = organizationRepo.findById(newReport.getBank().getId()).get();
        Organization organization = organizationRepo.findById(newReport.getOrganization().getId()).get();
        System system = systemRepo.findById(newReport.getSystem().getId()).get();

        //List<SKZI> bankSKZIs = skziRepo.findByOrganization(bank);
        //List<SKZI> organizationSKZIs = skziRepo.findByOrganization(organization);

        List<Document> bankDocuments = documentRepo.findByOrganization(bank);
        List<Document> organizationDocuments = documentRepo.findByOrganization(organization);
        List<Document> systemDocuments = documentRepo.findBySystem(system);

       // List<Document> bankSkziDocuments

                /*
        newReport.setContactLog(generateContactLogString("Заключение №" + documentnumber + " от " + documentdate + " создано"));
*/

       // model.put("bankskzis", bankSKZIs);
       // model.put("organizationskzis", organizationSKZIs);
        model.put("reportnumber", reportnumber);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addreportskzi";
    }

    @GetMapping("/showonereport")
    public String showonereport(
            @RequestParam(required = false, defaultValue = "0") String reportid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        Document report = documentRepo.findById(Integer.parseInt(reportid)).get();
        List<SystemStatus> systemStatuses = systemStatusRepo.findAll();
        List<Status> statuses = statusRepo.findAll();



        try {
            if (getDateDiff(new Date(), dayDateFormat.parse(report.getValidUntilDate()), TimeUnit.DAYS) <= 3)
            {
                model.put("danger", "danger");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
/* Определение уровня доверия


        List<DocumentType> etalonMiddleBankDocumentsList = new ArrayList<>();
        List<DocumentType> etalonHighBankDocumentsList = new ArrayList<>();

        List<DocumentType> etalonMiddleClientDocumentsList = new ArrayList<>();
        List<DocumentType> etalonHighClientDocumentsList = new ArrayList<>();
        List<DocumentType> etalonMiddleSystemDocumentsList = new ArrayList<>();
        List<DocumentType> etalonHighSystemDocumentsList = new ArrayList<>();
        List<DocumentType> etalonMiddleSkziDocumentsList = new ArrayList<>();
        List<DocumentType> etalonHighSkziDocumentsList = new ArrayList<>();

        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(46).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(1).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(2).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(3).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(4).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(5).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(6).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(8).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(10).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(11).get());
      //  etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(12).get());
        // etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(13).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(14).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(15).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(19).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(20).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(21).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(22).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(23).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(24).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(25).get());
        etalonMiddleBankDocumentsList.add(documentTypeRepo.findById(26).get());

        etalonHighBankDocumentsList.add(documentTypeRepo.findById(7).get());
        etalonHighBankDocumentsList.add(documentTypeRepo.findById(9).get());
        // etalonHighBankDocumentsList.add(documentTypeRepo.findById(16).get());
        etalonHighBankDocumentsList.add(documentTypeRepo.findById(17).get());
        etalonHighBankDocumentsList.add(documentTypeRepo.findById(18).get());

        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(34).get());
        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(35).get());
        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(36).get());
        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(37).get());
        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(38).get());
        etalonMiddleClientDocumentsList.add(documentTypeRepo.findById(39).get());

        etalonHighClientDocumentsList.add(documentTypeRepo.findById(27).get());
        etalonHighClientDocumentsList.add(documentTypeRepo.findById(33).get());

        etalonMiddleSystemDocumentsList.add(documentTypeRepo.findById(40).get());
        etalonMiddleSystemDocumentsList.add(documentTypeRepo.findById(43).get());
        etalonMiddleSystemDocumentsList.add(documentTypeRepo.findById(44).get());

        etalonHighSystemDocumentsList.add(documentTypeRepo.findById(41).get());
        etalonHighSystemDocumentsList.add(documentTypeRepo.findById(42).get());

        etalonMiddleSkziDocumentsList.add(documentTypeRepo.findById(28).get());
        etalonMiddleSkziDocumentsList.add(documentTypeRepo.findById(29).get());
        etalonMiddleSkziDocumentsList.add(documentTypeRepo.findById(31).get());
       // etalonMiddleSkziDocumentsList.add(documentTypeRepo.findById(32).get());

        etalonHighSkziDocumentsList.add(documentTypeRepo.findById(30).get());




        List<DocumentType> notBankSkziDocsForMiddleTruslLevel = new ArrayList<>();
        notBankSkziDocsForMiddleTruslLevel.addAll(etalonMiddleSkziDocumentsList);

        List<DocumentType> notClientSkziDocsForMiddleTruslLevel = new ArrayList<>();
        notClientSkziDocsForMiddleTruslLevel.addAll(etalonMiddleSkziDocumentsList);

        List<DocumentType> notClientDocsForMiddleTruslLevel = new ArrayList<>();
        notClientDocsForMiddleTruslLevel.addAll(etalonMiddleClientDocumentsList);

        List<DocumentType> notBankDocsForMiddleTruslLevel = new ArrayList<>();
        notBankDocsForMiddleTruslLevel.addAll(etalonMiddleBankDocumentsList);

        List<DocumentType> notSystemDocsForMiddleTruslLevel = new ArrayList<>();
        notSystemDocsForMiddleTruslLevel.addAll(etalonMiddleSystemDocumentsList);

        List<DocumentType> notBankSkziDocsForHighTruslLevel = new ArrayList<>();
        notBankSkziDocsForHighTruslLevel.addAll(etalonHighSkziDocumentsList);

        List<DocumentType> notClientSkziDocsForHighTruslLevel = new ArrayList<>();
        notClientSkziDocsForHighTruslLevel.addAll(etalonHighSkziDocumentsList);

        List<DocumentType> notClientDocsForHighTruslLevel = new ArrayList<>();
        notClientDocsForHighTruslLevel.addAll(etalonHighClientDocumentsList);

        List<DocumentType> notBankDocsForHighTruslLevel = new ArrayList<>();
        notBankDocsForHighTruslLevel.addAll(etalonHighBankDocumentsList);

        List<DocumentType> notSystemDocsForHighTruslLevel = new ArrayList<>();
        notSystemDocsForHighTruslLevel.addAll(etalonHighSystemDocumentsList);

        if (report.gettrustlevel().getId() == 3)
        {

            StringBuilder forMiddleSb = new StringBuilder("<p><h3 class=\"display-5\">Для достижения <strong>среднего</strong> уровня доверия необходимо наличие актуальных документов следующих типов:\n</h3></p>");

            for (Document bankSkziDocument : documentRepo.findBySkzi(report.getReportBankSkzi()))
            {
                if (etalonMiddleSkziDocumentsList.contains(bankSkziDocument.getDocumentType()))
                {
                    notBankSkziDocsForMiddleTruslLevel.remove(bankSkziDocument.getDocumentType());
                }
            }

            for (Document clientSkziDocument : documentRepo.findBySkzi(report.getReportBankSkzi()))
            {
                if (etalonMiddleSkziDocumentsList.contains(clientSkziDocument.getDocumentType()))
                {
                    notClientSkziDocsForMiddleTruslLevel.remove(clientSkziDocument.getDocumentType());
                }
            }

            for (Document clientDocument : documentRepo.findByOrganization(report.getOrganization()))
            {
                if (etalonMiddleClientDocumentsList.contains(clientDocument.getDocumentType()))
                {
                    notClientDocsForMiddleTruslLevel.remove(clientDocument.getDocumentType());
                }
            }

            for (Document bankDocument : documentRepo.findByOrganization(report.getBank()))
            {
                if (etalonMiddleBankDocumentsList.contains(bankDocument.getDocumentType()))
                {
                    notBankDocsForMiddleTruslLevel.remove(bankDocument.getDocumentType());
                }
            }

            for (Document systemDocument : documentRepo.findBySystem(report.getSystem()))
            {
                if (etalonMiddleSystemDocumentsList.contains(systemDocument.getDocumentType()))
                {
                    notSystemDocsForMiddleTruslLevel.remove(systemDocument.getDocumentType());
                }
            }

            if (!notBankDocsForMiddleTruslLevel.isEmpty()){
                forMiddleSb.append("<p><h4 class=\"display-6\">Для банка " + report.getBank().getName() + " :\n</h4></p>");
                for (DocumentType docType : notBankDocsForMiddleTruslLevel)
                forMiddleSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notBankSkziDocsForMiddleTruslLevel.isEmpty()){
                forMiddleSb.append("<br>");
                forMiddleSb.append("<p><h4 class=\"display-6\">Для СКЗИ банка " + report.getBank().getName() + " :\n</h4></p>");
                for (DocumentType docType : notBankSkziDocsForMiddleTruslLevel)
                    forMiddleSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notClientDocsForMiddleTruslLevel.isEmpty()){
                forMiddleSb.append("<br>");
                forMiddleSb.append("<p><h4 class=\"display-6\">Для Клиента " + report.getOrganization().getName() + " :\n</h4></p>");
                for (DocumentType docType : notClientDocsForMiddleTruslLevel)
                    forMiddleSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notClientSkziDocsForMiddleTruslLevel.isEmpty()){
                forMiddleSb.append("<br>");
                forMiddleSb.append("<p><h4 class=\"display-6\">Для СКЗИ Клиента " + report.getOrganization().getName() + " :\n</h4></p>");
                for (DocumentType docType : notClientSkziDocsForMiddleTruslLevel)
                    forMiddleSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notSystemDocsForMiddleTruslLevel.isEmpty()){
                forMiddleSb.append("<br>");
                forMiddleSb.append("<p><h4 class=\"display-6\">Для Системы " + report.getSystem().getName() + " :\n</h4></p>");
                for (DocumentType docType : notSystemDocsForMiddleTruslLevel)
                    forMiddleSb.append(" - " + docType.getName() + ";\n<br>");
            }

            model.put("recommendations", forMiddleSb.toString());

        }
        else if (report.gettrustlevel().getId() == 2)
        {
            StringBuilder forHighSb = new StringBuilder("<p><h3 class=\"display-5\">Для достижения <strong>высокого</strong> уровня доверия необходимо наличие актуальных документов следующих типов:\n</h3></p>");

            for (Document bankSkziDocument : documentRepo.findBySkzi(report.getReportBankSkzi()))
            {
                if (etalonHighSkziDocumentsList.contains(bankSkziDocument.getDocumentType()))
                {
                    notBankSkziDocsForHighTruslLevel.remove(bankSkziDocument.getDocumentType());
                }
            }

            for (Document clientSkziDocument : documentRepo.findBySkzi(report.getReportBankSkzi()))
            {
                if (etalonHighSkziDocumentsList.contains(clientSkziDocument.getDocumentType()))
                {
                    notClientSkziDocsForHighTruslLevel.remove(clientSkziDocument.getDocumentType());
                }
            }

            for (Document clientDocument : documentRepo.findByOrganization(report.getOrganization()))
            {
                if (etalonHighClientDocumentsList.contains(clientDocument.getDocumentType()))
                {
                    notClientDocsForHighTruslLevel.remove(clientDocument.getDocumentType());
                }
            }

            for (Document bankDocument : documentRepo.findByOrganization(report.getBank()))
            {
                if (etalonHighBankDocumentsList.contains(bankDocument.getDocumentType()))
                {
                    notBankDocsForHighTruslLevel.remove(bankDocument.getDocumentType());
                }
            }

            for (Document systemDocument : documentRepo.findBySystem(report.getSystem()))
            {
                if (etalonHighSystemDocumentsList.contains(systemDocument.getDocumentType()))
                {
                    notSystemDocsForHighTruslLevel.remove(systemDocument.getDocumentType());
                }
            }

            if (!notBankDocsForHighTruslLevel.isEmpty()){
                forHighSb.append("<br>");
                forHighSb.append("<p><h4 class=\"display-6\">Для банка " + report.getBank().getName() + " :\n</h4></p>");
                for (DocumentType docType : notBankDocsForHighTruslLevel)
                    forHighSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notBankSkziDocsForHighTruslLevel.isEmpty()){
                forHighSb.append("<br>");
                forHighSb.append("<p><h4 class=\"display-6\">Для СКЗИ банка " + report.getBank().getName() + " :\n</h4></p>");
                for (DocumentType docType : notBankSkziDocsForHighTruslLevel)
                    forHighSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notClientDocsForHighTruslLevel.isEmpty()){
                forHighSb.append("<br>");
                forHighSb.append("<p><h4 class=\"display-6\">Для Клиента " + report.getOrganization().getName() + " :\n</h4></p>");
                for (DocumentType docType : notClientDocsForHighTruslLevel)
                    forHighSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notClientSkziDocsForHighTruslLevel.isEmpty()){
                forHighSb.append("<br>");
                forHighSb.append("<p><h4 class=\"display-6\">Для СКЗИ Клиента " + report.getOrganization().getName() + " :\n</h4></p>");
                for (DocumentType docType : notClientSkziDocsForHighTruslLevel)
                    forHighSb.append(" - " + docType.getName() + ";\n<br>");
            }

            if (!notSystemDocsForHighTruslLevel.isEmpty()){
                forHighSb.append("<br>");
                forHighSb.append("<p><h4 class=\"display-6\">Для Системы " + report.getSystem().getName() + " :\n</h4</p>");
                for (DocumentType docType : notSystemDocsForHighTruslLevel)
                    forHighSb.append(" - " + docType.getName() + ";\n<br>");
            }
            model.put("recommendations", forHighSb.toString());
        }
*/
        String dbvaliduntildate = null;
        try {
            dbvaliduntildate = dbDateFormat.format(dayDateFormat.parse(report.getValidUntilDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        model.put("reportid", reportid);
        model.put("report", report);
        model.put("systemstatuses", systemStatuses);
        model.put("dbvaliduntildate", dbvaliduntildate);
        model.put("statuses", statuses);

        model.put("urlprefixPath", urlprefixPath);
        model.put("hostname", hostname);

        return "showonereport";
    }


    @PostMapping("/showonereport")
    public String showonereport(
            @RequestParam(required = false, defaultValue = "0") String reportid,
            @RequestParam(required = false, defaultValue = "0") String addtochronos,
            @RequestParam(required = false, defaultValue = "0") String reportstatusname,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String reportsystemstatusname,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        Document report = documentRepo.findById(Integer.parseInt(reportid)).get();
        List<SystemStatus> systemStatuses = systemStatusRepo.findAll();
        List<Status> statuses = statusRepo.findAll();

        try {
            validuntildate = dayDateFormat.format(dbDateFormat.parse(validuntildate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!addtochronos.equals("0"))
        {
            try {
                StringBuilder chronosSb = new StringBuilder(report.getContactLog());
                chronosSb.append(dateFormat.format(new Date()) + addtochronos.trim()  + ";\n");
                report.setContactLog(chronosSb.toString());
                report.setLastDocumentStatusUpdateDate(dayDateFormat.format(new Date()));
            }
            catch (NullPointerException e)
            {
                report.setContactLog(dateFormat.format(new Date()) + addtochronos.trim()  + ";\n");
            }

        }

        if (!reportstatusname.equals("0") && !reportstatusname.equals(report.getStatus().getName()))
        {
            report.setStatus(statusRepo.findByName(reportstatusname));
            report.setLastDocumentStatusUpdateDate(dayDateFormat.format(new Date()));
        }

        if (!validuntildate.equals("0") && !validuntildate.equals(report.getValidUntilDate()))
        {
            report.setValidUntilDate(validuntildate);
        }

        if (!reportsystemstatusname.equals("0") && !reportsystemstatusname.equals(report.getsystemstatus().getName()))
        {
            report.setSystemStatus(systemStatusRepo.findByName(reportsystemstatusname));
        }

        documentRepo.save(report);


        try {
            if (getDateDiff(new Date(), dayDateFormat.parse(report.getValidUntilDate()), TimeUnit.DAYS) <= 3)
            {
                model.put("danger", "danger");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dbvaliduntildate = null;
        try {
            dbvaliduntildate = dbDateFormat.format(dayDateFormat.parse(report.getValidUntilDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        model.put("error", "Документ обновлен");
        model.put("report", report);
        model.put("dbvaliduntildate", dbvaliduntildate);
        model.put("systemstatuses", systemStatuses);
        model.put("statuses", statuses);
        model.put("hostname", hostname);

        model.put("urlprefixPath", urlprefixPath);

        return "showonereport";
    }

    @GetMapping("/addreport")
    public String adddreport(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Organization> organizations = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(2).get());
        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
        List<TrustLevel> trustLevels = trustLevelRepo.findAll();


        model.put("trustlevels", trustLevels);
        model.put("organizations", organizations);
        model.put("banks", banks);


        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addreport";
    }

    @PostMapping("/addreport")
    public String adddreport(
            @RequestParam(required = false, defaultValue = "0") String documentnumber,
            @RequestParam(required = false, defaultValue = "0") String documentdate,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String parameters,
            @RequestParam(required = false, defaultValue = "0") String organization,
            @RequestParam(required = false, defaultValue = "0") String bank,
            @RequestParam(required = false, defaultValue = "0") String system,
            @RequestParam(required = false, defaultValue = "0") String trustLevel,
            @RequestParam(required = false, defaultValue = "0") String selectedtrustLevel,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Organization> organizations = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(2).get());
        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
        List<System> systems = null;
        List<TrustLevel> trustLevels = trustLevelRepo.findAll();

        if (!documentnumber.equals("0")
                && !documentdate.equals("0")
                && !validuntildate.equals("0")
                && !parameters.equals("0")
                && !organization.equals("0")
                && !bank.equals("0")
                && !system.equals("0")
                )
        {
            if (documentRepo.findByDocumentNumberAndDocumentType(documentnumber, documentTypeRepo.findById(45).get()) == null) {
                Document newReport = new Document();
                newReport.setDocumentType(documentTypeRepo.findById(45).get());
                newReport.setStatus(statusRepo.findById(1).get());
                newReport.setSystemStatus(systemStatusRepo.findById(1).get());
                newReport.setDocumentNumber(documentnumber);
                try {
                    newReport.setDocumentDate(dayDateFormat.format(dbDateFormat.parse(documentdate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    newReport.setValidUntilDate(dayDateFormat.format(dbDateFormat.parse(validuntildate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newReport.setParameters(parameters);

                newReport.setSystem(systemRepo.findById(Integer.parseInt(system)).get());
                newReport.setOrganization(organizationRepo.findById(Integer.parseInt(organization)).get());
                newReport.setBank(organizationRepo.findById(Integer.parseInt(bank)).get());

                //setTrustLevel(newReport);
                newReport.setTrustLevel(trustLevelRepo.findById(Integer.parseInt(trustLevel)).get());

                newReport.setLastDocumentStatusUpdateDate(dayDateFormat.format(new Date()));


                documentRepo.save(newReport);
                log.info("Создано заключение " + newReport.getDocumentName() + " № " + newReport.getDocumentNumber() + " от " + newReport.getDocumentDate() + ";");



                List<Document> documents = documentRepo.findByDocumentType(documentTypeRepo.findById(45).get());

                model.put("trustlevels", trustLevels);
                model.put("banks", banks);
                model.put("organizations", organizations);
                model.put("systems", systems);
                model.put("error", "Заключение " + newReport.getDocumentNumber() + " добавлено");
                model.put("reportnumber", newReport.getDocumentNumber());
                model.put("hostname", hostname);
                model.put("urlprefixPath", urlprefixPath);

                return "addreport";
            }
            else
            {
                systems = systemRepo.findByBankAndOrganization(organizationRepo.findById(Integer.parseInt(bank)).get(), organizationRepo.findById(Integer.parseInt(organization)).get());
                model.put("error", "*Заключение с таким номером уже существует");
                model.put("organizations", organizations);
                model.put("selectedorganization", organization);
                model.put("systems", systems);
                model.put("selectedsystem", system);
                model.put("banks", banks);
                model.put("selectedbank", bank);
                model.put("documentnumber",  documentnumber);
                model.put("documentdate",  documentdate);
                model.put("validuntildate",  validuntildate);
                model.put("parameters",  parameters);
                model.put("hostname", hostname);
                model.put("urlprefixPath", urlprefixPath);

                return "addreport";
            }
        }
        else if(bank.equals("0") || organization.equals("0"))
        {



            model.put("trustlevels", trustLevels);
            model.put("selectedtrustLevel", selectedtrustLevel.equals("0") ? "Уровень доверия" : selectedtrustLevel);
            model.put("organizations", organizations);
            model.put("selectedorganization", organization.equals("0") ? "Клиент" : organizationRepo.findById(Integer.parseInt(organization)).get().getName());
            model.put("banks", banks);
            model.put("selectedbank", bank.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bank)).get().getName());
            model.put("selectedsystem", system.equals("0") ? "Система" : systemRepo.findById(Integer.parseInt(system)).get().getName());
            model.put("systems", systems);
            model.put("documentnumber",  documentnumber.equals("0") ? "" : documentnumber);
            model.put("documentdate",  documentdate.equals("0") ? "" : documentdate);
            model.put("validuntildate",  validuntildate.equals("0") ? "" : validuntildate);
            model.put("parameters",  parameters.equals("0") ? "" : parameters);
            model.put("hostname", hostname);
            model.put("urlprefixPath", urlprefixPath);

            return "addreport";
        }

        else if(!bank.equals("0") && !organization.equals("0"))
        {

            systems = systemRepo.findByBankAndOrganization(organizationRepo.findById(Integer.parseInt(bank)).get(), organizationRepo.findById(Integer.parseInt(organization)).get());


            model.put("trustlevels", trustLevels);
            model.put("selectedtrustLevel", selectedtrustLevel.equals("0") ? "Уровень доверия" : selectedtrustLevel);
            model.put("organizations", organizations);
            model.put("selectedorganization", organization.equals("0") ? "Клиент" : organizationRepo.findById(Integer.parseInt(organization)).get().getName());
            model.put("banks", banks);
            model.put("selectedbank", bank.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bank)).get().getName());
            model.put("selectedsystem", system.equals("0") ? "Система" : systemRepo.findById(Integer.parseInt(system)).get().getName());
            model.put("systems", systems);
            model.put("documentnumber",  documentnumber.equals("0") ? "" : documentnumber);
            model.put("documentdate",  documentdate.equals("0") ? "" : documentdate);
            model.put("validuntildate",  validuntildate.equals("0") ? "" : validuntildate);
            model.put("parameters",  parameters.equals("0") ? "" : parameters);
            model.put("hostname", hostname);
            model.put("urlprefixPath", urlprefixPath);
            return "addreport";

        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
            model.put("organizations", organizations);
            model.put("selectedorganization", organization);
            model.put("selectedsystem", system);
            model.put("banks", banks);
            model.put("selectedbank", bank);
            model.put("systems", systems);
            model.put("documentnumber",  documentnumber);
            model.put("documentdate",  documentdate);
            model.put("validuntildate",  validuntildate);
            model.put("parameters",  parameters);
            model.put("hostname", hostname);
            model.put("urlprefixPath", urlprefixPath);

            return "addreport";
        }






        //return "addreport";
    }
/*
    @GetMapping("/adddocumentforsystem")
    public String adddocumentforsystem(
            @RequestParam(required = false, defaultValue = "0") String sysid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Status> statuses = statusRepo.findAll();
        List<DocumentType> documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
        System system = systemRepo.findById(Integer.parseInt(sysid)).get();

        model.put("skziid", sysid);
        model.put("system", system);
        model.put("statuses", statuses);
        model.put("documenttypes", documenttypes);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocumentforsystem";
    }

    @PostMapping("/adddocumentforsystem")
    public String adddocumentforsystem(
            @RequestParam(required = false, defaultValue = "0") String sysid,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String documentnumber,
            @RequestParam(required = false, defaultValue = "0") String documentdate,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String parameters,
            @RequestParam(required = false, defaultValue = "0") String documenttype,
            @RequestParam(required = false, defaultValue = "0") String unlimiteddate,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Status> statuses = statusRepo.findAll();
        List<DocumentType> documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
        System system = systemRepo.findById(Integer.parseInt(sysid)).get();

        if (!documentnumber.equals("0")
                && !documentdate.equals("0")
                && ((!validuntildate.equals("0") && unlimiteddate.equals("0")) || (validuntildate.equals("0") && unlimiteddate.equals("on")))
                && !documenttype.equals("0")
                && !documenttype.equals("Тип Документа")
                && documentRepo.findByDocumentNumberAndDocumentDate(documentnumber, documentdate) == null
                )
        {

            Document newDocument = new Document();
            newDocument.setDocumentNumber(documentnumber);
            try {
                newDocument.setDocumentDate(dayDateFormat.format(dbDateFormat.parse(documentdate)));
            } catch (ParseException e) {


            }


            if (!validuntildate.equals("0") && unlimiteddate.equals("0"))
            {
                try {
                    newDocument.setValidUntilDate(dayDateFormat.format(dbDateFormat.parse(validuntildate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (validuntildate.equals("0") && unlimiteddate.equals("on"))
            {
                newDocument.setValidUntilDate("01.01.3020");
            }
            newDocument.setParameters(parameters);
            DocumentType documentType = null;

            if (type.equals("0"))
            {
                documentType =  documentTypeRepo.findById(45).get();
            }
            else {
                switch (type) {
                    case "bank":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(1).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(1).get());
                        break;

                    case "organization":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(2).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(2).get());
                        break;

                    case "system":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        break;

                    case "skzi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(4).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
                        break;

                    default:
                        break;
                }
            }

            File destFile = null;
            if(file != null && !file.getOriginalFilename().equals(""))
            {
                if (!Files.exists(Paths.get(manUpPath))) {
                    try {
                        Files.createDirectory(Paths.get(manUpPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String dateForFileName = fileNameDateFormat.format(new Date());
                File uploadDir = new File(manUpPath + "\\" + dateForFileName);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String newFileName =  docsFileNameDateFormat.format(new Date()) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                destFile = new File(uploadDir + "\\" + newFileName);
                String fileNameForBase = dateForFileName + "/" + newFileName;

                try {
                    file.transferTo(destFile);

                        newDocument.setDocumentFileName(fileNameForBase);

                } catch (IOException e) {
                    e.printStackTrace();

                }

            }

            newDocument.setDocumentType(documentType);
            newDocument.setSystem(system);
            log.info("Пользователем " + user.getUsername() + " системе " + system.getSystemName() + " добавлен документ " + newDocument.getDocumentName() + " № " + newDocument.getDocumentNumber() + " от " + newDocument.getDocumentDate() + ";");

            documentRepo.save(newDocument);

            model.put("error", "Документ добавлен");
        }
        else
        {
            model.put("error", "*Необходимо добавить все поля");
            model.put("documentnumber", documentnumber);
            model.put("documentdate",  documentdate);
            model.put("documenttype",  documenttype);
            model.put("validuntildate",  validuntildate);
            model.put("parameters",  parameters);

        }
        model.put("statuses", statuses);
        model.put("documenttypes", documenttypes);
        model.put("sysid", sysid);
        model.put("system", system);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocumentforsystem";
    }

    @GetMapping("/adddocumentforskzi")
    public String adddocumentforskzi(
            @RequestParam(required = false, defaultValue = "0") String skziid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Status> statuses = statusRepo.findAll();
        List<DocumentType> documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
        SKZI skzi = skziRepo.findById(Integer.parseInt(skziid)).get();

        model.put("skziid", skziid);
        model.put("skzi", skzi);
        model.put("statuses", statuses);
        model.put("documenttypes", documenttypes);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocumentforskzi";
    }

    @PostMapping("/adddocumentforskzi")
    public String adddocumentforskzi(
            @RequestParam(required = false, defaultValue = "0") String skziid,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String documentnumber,
            @RequestParam(required = false, defaultValue = "0") String documentdate,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String parameters,
            @RequestParam(required = false, defaultValue = "0") String documenttype,
            @RequestParam(required = false, defaultValue = "0") String unlimiteddate,
            @RequestParam(required = false, defaultValue = "0") String osname,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Status> statuses = statusRepo.findAll();
        List<DocumentType> documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
        SKZI skzi = skziRepo.findById(Integer.parseInt(skziid)).get();

        if (!documentnumber.equals("0")
                && !documentdate.equals("0")
                && ((!validuntildate.equals("0") && unlimiteddate.equals("0")) || (validuntildate.equals("0") && unlimiteddate.equals("on")))
                && !documenttype.equals("0")
                && !documenttype.equals("Тип Документа")
                //&& documentRepo.findByDocumentNumberAndDocumentDate(documentnumber, documentdate) == null
                )
        {

            DocumentType documentType = null;

            if (type.equals("0"))
            {
                documentType =  documentTypeRepo.findById(45).get();
            }
            else {


                switch (type) {
                    case "bank":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(1).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(1).get());
                        break;

                    case "organization":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(2).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(2).get());
                        break;

                    case "system":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        break;

                    case "skzi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(4).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
                        break;

                    default:
                        break;
                }
            }

            if (documentType.getId() == 30 && osname.equals("0"))
            {
                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("documenttype", documenttype);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
                model.put("dopparams", documentType.getId().toString());
                model.put("osname", osname.equals("0") ? "" : osname);


            }


            Document newDocument = new Document();
            newDocument.setDocumentNumber(documentnumber);
            try {
                newDocument.setDocumentDate(dayDateFormat.format(dbDateFormat.parse(documentdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!validuntildate.equals("0") && unlimiteddate.equals("0"))
            {
                try {
                    newDocument.setValidUntilDate(dayDateFormat.format(dbDateFormat.parse(validuntildate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (validuntildate.equals("0") && unlimiteddate.equals("on"))
            {
                newDocument.setValidUntilDate("01.01.3020");
            }
            newDocument.setParameters(parameters);


            newDocument.setDocumentType(documentType);

            if (documentType.getId() == 30)
            {
                newDocument.setOsname(osname.trim());
            }


            File destFile = null;
            if(file != null && !file.getOriginalFilename().equals(""))
            {
                if (!Files.exists(Paths.get(manUpPath))) {
                    try {
                        Files.createDirectory(Paths.get(manUpPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String dateForFileName = fileNameDateFormat.format(new Date());
                File uploadDir = new File(manUpPath + "\\" + dateForFileName);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String newFileName =  docsFileNameDateFormat.format(new Date()) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                destFile = new File(uploadDir + "\\" + newFileName);
                String fileNameForBase = dateForFileName + "/" + newFileName;

                try {
                    file.transferTo(destFile);

                    newDocument.setDocumentFileName(fileNameForBase);

                } catch (IOException e) {
                    e.printStackTrace();

                }

            }

            newDocument.setSkzi(skzi);
            log.info("СКЗИ " + skzi.getName() + " добавлен документ " + newDocument.getDocumentName() + " № " + newDocument.getDocumentNumber() + " от " + newDocument.getDocumentDate() + ";");


            documentRepo.save(newDocument);

            model.put("error", "Документ добавлен");
        }
        else
        {
            if (!documenttype.equals("0") || !documenttype.equals("Тип документа"))
            {

                DocumentType documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(4).get());
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());




                model.put("dopparams", documentType.getId().toString());
                //model.put("documentnumber", documentnumber);
                //model.put("documentdate", documentdate);
                //model.put("validuntildate", validuntildate);
                //model.put("parameters", parameters);
                model.put("selecteddocumenttype", documentType.getName());

            }
            else {
                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("selecteddocumenttype", documenttype);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
            }

        }
        model.put("statuses", statuses);
        model.put("documenttypes", documenttypes);
        model.put("skziid", skziid);
        model.put("skzi", skzi);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocumentforskzi";
    }
*/
    @GetMapping("/adddocument")
    public String adddocument(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        String objectName = "";
        List<DocumentType> documenttypes = null;
        switch (type)
        {
            case "bank" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(1).get());
                objectName = organizationRepo.findById(Integer.parseInt(id)).get().getName();

            break;

            case "organization" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(2).get());
                objectName = organizationRepo.findById(Integer.parseInt(id)).get().getName();
                break;

            case "systemname" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                objectName = systemNameRepo.findById(Integer.parseInt(id)).get().getName();
                break;

            case "system" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                objectName = systemRepo.findById(Integer.parseInt(id)).get().getName();
                break;

            case "skzi" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
                objectName = skziRepo.findById(Integer.parseInt(id)).get().getName();
                break;

            case "antivirus" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(6).get());
                objectName = antivirusRepo.findById(Integer.parseInt(id)).get().getAvName();
                break;

            case "szi" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(8).get());
                objectName = sziRepo.findById(Integer.parseInt(id)).get().getSziName();
                break;

            case "pakuc" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(7).get());
                objectName = pakUCRepo.findById(Integer.parseInt(id)).get().getPakUCName();
                break;

            case "keycarriers" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(9).get());
                objectName = keyCarriersRepo.findById(Integer.parseInt(id)).get().getKeyCarriersName();
                break;

            case "arm" :
                documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(10).get());
                objectName = armRepo.findById(Integer.parseInt(id)).get().getArmName();
                break;

                default:
                    break;
        }

        model.put("id", id);
        model.put("objectName", objectName);
        model.put("type", type);
        model.put("documenttypes", documenttypes);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocument";
    }

    @PostMapping("/adddocument")
    public String adddocument(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String documentnumber,
            @RequestParam(required = false, defaultValue = "0") String documentname,
            @RequestParam(required = false, defaultValue = "0") String documentdate,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String parameters,
            @RequestParam(required = false, defaultValue = "0") String link,
            @RequestParam(required = false, defaultValue = "0") String documenttype,
            @RequestParam(required = false, defaultValue = "0") String unlimiteddate,
            @RequestParam(required = false, defaultValue = "0") String keyinftool,
            @RequestParam(required = false, defaultValue = "0") String sziname,
            @RequestParam(required = false, defaultValue = "0") String sziversion,
            @RequestParam(required = false, defaultValue = "0") String bankid,
            @RequestParam(required = false, defaultValue = "0") String tokentypename,
            @RequestParam(required = false, defaultValue = "0") String antivirusid,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        String objectName = "";
        List<DocumentType> documenttypes = null;

        if (!documentnumber.equals("0")
                && !documentdate.equals("0")
                && ((!validuntildate.equals("0") && unlimiteddate.equals("0")) || (validuntildate.equals("0") && unlimiteddate.equals("on")))
                && !documenttype.equals("0")
                && !documenttype.equals("Тип Документа")
                //&& documentRepo.findByDocumentNumberAndDocumentDate(documentnumber, documentdate) == null
                )
        {

            DocumentType documentType = null;
            Document newDocument = new Document();

            if (type.equals("0"))
            {
                documentType =  documentTypeRepo.findById(45).get();
            }
            else {


                switch (type) {
                    case "bank":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(1).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(1).get());
                        objectName = "Банк " + organizationRepo.findById(Integer.parseInt(id)).get().getName();
                        newDocument.setOrganization(organizationRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "organization":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(2).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(2).get());
                        objectName = "Организация " + organizationRepo.findById(Integer.parseInt(id)).get().getName();
                        newDocument.setOrganization(organizationRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "systemname":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        objectName = "Система " + systemNameRepo.findById(Integer.parseInt(id)).get().getName();
                        newDocument.setSystemName(systemNameRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "system":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        objectName = "Система " + systemRepo.findById(Integer.parseInt(id)).get().getName();
                        newDocument.setSystem(systemRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "skzi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(4).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
                        objectName = "СКЗИ " + skziRepo.findById(Integer.parseInt(id)).get().getName();
                        newDocument.setSkzi(skziRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "antivirus":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(6).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(6).get());
                        objectName = "Антивирус " + antivirusRepo.findById(Integer.parseInt(id)).get().getAvName();
                        newDocument.setAntivirus(antivirusRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "szi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(8).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(8).get());
                        objectName = "СЗИ " + sziRepo.findById(Integer.parseInt(id)).get().getSziName();
                        newDocument.setSzi(sziRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "pakuc":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(7).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(7).get());
                        objectName = "ПАК УЦ " + pakUCRepo.findById(Integer.parseInt(id)).get().getPakUCName();
                        newDocument.setPakUC(pakUCRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "keycarriers":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(9).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(9).get());
                        objectName = "Ключевой носитель " + keyCarriersRepo.findById(Integer.parseInt(id)).get().getKeyCarriersName();
                        newDocument.setKeyCarriers(keyCarriersRepo.findById(Integer.parseInt(id)).get());
                        break;

                    case "arm":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(10).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(10).get());
                        objectName = "АРМ " + armRepo.findById(Integer.parseInt(id)).get().getArmName();
                        newDocument.setArm(armRepo.findById(Integer.parseInt(id)).get());
                        break;

                    default:
                        break;
                }
            }


            if (documentType.getId() == 35 && (sziname.equals("0") || sziversion.equals("0")))
            {
                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
                model.put("selecteddocumenttype", documenttype);
                model.put("dopparams", documentType.getId().toString());
            }

            if (documentType.getId() == 51 && bankid.equals("0"))
            {
                List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());

                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
                model.put("selecteddocumenttype", documenttype);
                model.put("dopparams", documentType.getId().toString());
                model.put("banks", banks);

            }


            if ((documentType.getId() == 1 || documentType.getId() == 2) && keyinftool.equals("0"))
            {
                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
                model.put("selecteddocumenttype", documenttype);
                model.put("dopparams", documentType.getId().toString());
                model.put("keyinftool", keyinftool.equals("0") ? "" : keyinftool);
            }


           newDocument.setDocumentNumber(documentnumber.trim());
           if (!documentname.equals("0")) {
               newDocument.setDocumentName(documentname.trim());
           }
            try {
                newDocument.setDocumentDate(dayDateFormat.format(dbDateFormat.parse(documentdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!validuntildate.equals("0") && unlimiteddate.equals("0"))
            {
                try {
                    newDocument.setValidUntilDate(dayDateFormat.format(dbDateFormat.parse(validuntildate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (validuntildate.equals("0") && unlimiteddate.equals("on"))
           {
               newDocument.setValidUntilDate("01.01.3020");
           }
           newDocument.setParameters(parameters);

           if (!link.equals("0"))
            {
                newDocument.setParameters(link);
            }


           newDocument.setDocumentType(documentType);

           if (documentType.getId() == 20 || documentType.getId() == 34)
           {
               /*
               newDocument.setAvname(avname.trim());
               newDocument.setAvversion(avversion.trim());

                */
           }

            if (documentType.getId() == 1 || documentType.getId() == 2)
            {
                /*
                newDocument.setKeyinftool(keyinftool);
                */

            }

            if (documentType.getId() == 7 || documentType.getId() == 27)
            {
                /*
                newDocument.setTokentypename(tokentypename);

                 */
            }

            if (documentType.getId() == 35)
            {
                /*
                newDocument.setSziname(sziname);
                newDocument.setSziversion(sziversion);

                 */
            }

            if (documentType.getId() == 51)
            {
                newDocument.setBank(organizationRepo.findById(Integer.parseInt(bankid)).get());
            }


            File destFile = null;
            if(file != null && !file.getOriginalFilename().equals(""))
            {
                if (!Files.exists(Paths.get(manUpPath))) {
                    try {
                        Files.createDirectory(Paths.get(manUpPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String dateForFileName = fileNameDateFormat.format(new Date());
                File uploadDir = new File(manUpPath + "\\" + dateForFileName);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String newFileName =  docsFileNameDateFormat.format(new Date()) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                destFile = new File(uploadDir + "\\" + newFileName);
                String fileNameForBase = dateForFileName + "/" + newFileName;

                try {
                    file.transferTo(destFile);

                    newDocument.setDocumentFileName(fileNameForBase);

                } catch (IOException e) {
                    e.printStackTrace();

                }

            }



           documentRepo.save(newDocument);
           log.info("Пользователем " + user.getUsername() + " для  " + objectName + " добавлен документ " + newDocument.getDocumentName() + " № " + newDocument.getDocumentNumber() + " от " + newDocument.getDocumentDate() + ";");


            model.put("error", "Документ для " + objectName + " добавлен");
        }
        else
        {
            if (!documenttype.equals("0") || !documenttype.equals("Тип документа"))
            {

                DocumentType documentType = null;

                switch (type) {
                    case "bank":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(1).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(1).get());
                        objectName = organizationRepo.findById(Integer.parseInt(id)).get().getName();
                        break;

                    case "organization":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(2).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(2).get());
                        objectName = organizationRepo.findById(Integer.parseInt(id)).get().getName();
                        break;

                    case "systemname":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        objectName = systemNameRepo.findById(Integer.parseInt(id)).get().getName();
                        break;

                    case "system":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(3).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(3).get());
                        objectName = systemRepo.findById(Integer.parseInt(id)).get().getName();
                        break;

                    case "skzi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(4).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(4).get());
                        objectName = skziRepo.findById(Integer.parseInt(id)).get().getName();
                        break;

                    case "antivirus":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(6).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(6).get());
                        objectName = antivirusRepo.findById(Integer.parseInt(id)).get().getAvName();
                        model.put("antivirusid", id);
                        break;

                    case "szi":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(8).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(8).get());
                        objectName = sziRepo.findById(Integer.parseInt(id)).get().getSziName();
                        model.put("sziid", id);
                        break;

                    case "pakuc":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(7).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(7).get());
                        objectName = pakUCRepo.findById(Integer.parseInt(id)).get().getPakUCName();
                        break;

                    case "keycarriers":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(9).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(9).get());
                        objectName = keyCarriersRepo.findById(Integer.parseInt(id)).get().getKeyCarriersName();
                        break;

                    case "arm":
                        documentType = documentTypeRepo.findByIdAndAffiliation(Integer.parseInt(documenttype), affilationRepo.findById(10).get());
                        documenttypes = documentTypeRepo.findByAffiliation(affilationRepo.findById(10).get());
                        objectName = armRepo.findById(Integer.parseInt(id)).get().getArmName();
                        break;

                    default:
                        break;
                }

                if (documentType.getId() == 20 ||
                    documentType.getId() == 51 ||
                    documentType.getId() == 35 ||
                    documentType.getId() == 1 ||
                    documentType.getId() == 2)
                {
                    model.put("dopparams", documentType.getId().toString());
                }
                if (documentType.getId() == 51) {
                    List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
                    model.put("banks", banks);
                }


                //model.put("documentnumber", documentnumber);
                //model.put("documentdate", documentdate);
                //model.put("validuntildate", validuntildate);
                //model.put("parameters", parameters);
                model.put("selecteddocumenttype", documentType.getName());

            }
            else
                {
                model.put("error", "*Необходимо добавить все поля");
                model.put("documentnumber", documentnumber);
                model.put("documentdate", documentdate);
                model.put("validuntildate", validuntildate);
                model.put("parameters", parameters);
                model.put("selecteddocumenttype", documenttype);
            }
        }


        List<Status> statuses = statusRepo.findAll();

        model.put("id", id);
        model.put("objectName", objectName);
        model.put("type", type);
        model.put("statuses", statuses);
        model.put("documenttypes", documenttypes);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "adddocument";
    }

    @GetMapping("/changebank")
    public String changebank(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Organization bank = organizationRepo.findById(Integer.parseInt(id)).get();
        model.put("id", id);
        model.put("bank", bank);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "changebank";
    }

    @PostMapping ("/changebank")
    public String changebank(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String bankname,
            @RequestParam(required = false, defaultValue = "0") String inn,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Organization bank = organizationRepo.findById(Integer.parseInt(id)).get();

        if ((!bankname.equals("0") && !bankname.equals(bank.getName())) || (!inn.equals("0") && !inn.equals(bank.getInn())))
        {
            bank.setName(bankname);
            organizationRepo.save(bank);
            model.put("error", "Свойства банка изменены");
            log.info("Данные банка  " + bank + " изменены;");

        }
        else
        {
            model.put("error", "Параметр пуст, либо соответствует предыдущему значению");
        }

        model.put("id", id);
        model.put("bank", bank);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "changebank";
    }

    @PostMapping ("/showonedocument")
    public String showonedocument(
            @RequestParam(required = false, defaultValue = "0") String docid,
            @RequestParam(required = false, defaultValue = "0") String documentnumber,
            @RequestParam(required = false, defaultValue = "0") String documentname,
            @RequestParam(required = false, defaultValue = "0") String documentdate,
            @RequestParam(required = false, defaultValue = "0") String validuntildate,
            @RequestParam(required = false, defaultValue = "0") String unlimiteddate,
            @RequestParam(required = false, defaultValue = "0") String parameters,
            @RequestParam(required = false, defaultValue = "0") String link,
            @RequestParam(required = false, defaultValue = "0") String deldoc,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Document document = documentRepo.findById(Integer.parseInt(docid)).get();

        try {
        document.setDocumentName(document.getDocumentName().equals(documentname.trim()) ? document.getDocumentName() : documentname.trim());
          }catch (NullPointerException e)
           {

          }
           try {
        document.setDocumentNumber(document.getDocumentNumber().equals(documentnumber.trim()) ? document.getDocumentNumber() : documentnumber.trim());
        }catch (NullPointerException e)
        {

        }
        try {
            document.setDocumentDate(dbDateFormat.format(dayDateFormat.parse(document.getdocumentdate())).equals(documentdate) ? document.getdocumentdate() : dayDateFormat.format(dbDateFormat.parse(documentdate)));
            if (!unlimiteddate.equals("on")) {
                document.setValidUntilDate(dbDateFormat.format(dayDateFormat.parse(document.getValidUntilDate())).equals(validuntildate) ? document.getValidUntilDate() : dayDateFormat.format(dbDateFormat.parse(validuntildate)));
            }
            else
            {
                document.setValidUntilDate("01.01.3020");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        document.setParameters(document.getParameters().equals(parameters.trim()) ? document.getParameters() : parameters.trim());
        try {
            document.setLink(document.getLink().equals(link.trim()) ? document.getLink() : link.trim());
        }catch (NullPointerException e)
        {

        }
/////
        if(deldoc.equals("0")) {
            File destFile = null;
            if (file != null && !file.getOriginalFilename().equals("")) {
                if (!Files.exists(Paths.get(manUpPath))) {
                    try {
                        Files.createDirectory(Paths.get(manUpPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String dateForFileName = fileNameDateFormat.format(new Date());
                File uploadDir = new File(manUpPath + "\\" + dateForFileName);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String newFileName = docsFileNameDateFormat.format(new Date()) + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
                destFile = new File(uploadDir + "\\" + newFileName);
                String fileNameForBase = dateForFileName + "/" + newFileName;

                try {
                    file.transferTo(destFile);

                    document.setDocumentFileName(fileNameForBase);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        else
        {
            document.setDocumentFileName(null);
        }

        documentRepo.save(document);

        String dbvaliduntildate = null;
        String dbdocumentdate = null;
        try {
            dbvaliduntildate = dbDateFormat.format(dayDateFormat.parse(document.getValidUntilDate()));
            dbdocumentdate = dbDateFormat.format(dayDateFormat.parse(document.getdocumentdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //model.put("urgency", urgency);

        model.put("document", document);
        model.put("dbvaliduntildate", dbvaliduntildate);
        model.put("dbdocumentdate", dbdocumentdate);

        model.put("docid", docid);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "showonedocument";
    }

    @GetMapping("/addcontact")
    public String addcontact(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        model.put("id", id);
        model.put("type", type);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "addcontact";
    }
    @PostMapping("/addcontact")
    public String addcontact(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String type,
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam(required = false, defaultValue = "0") String position,
            @RequestParam(required = false, defaultValue = "0") String phonenumber,
            @RequestParam(required = false, defaultValue = "0") String email,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        if (//contactRepo.findByFioAndPositionAndPhoneNumberAndEmail(fio, position, phonenumber, email) == null &&
                !fio.equals("0"))
        {
            Contact newContact = new Contact();
            newContact.setFio(fio.trim());
            newContact.setPosition(position.equals("0") ? "" : position.trim());
            newContact.setPhoneNumber(phonenumber.equals("0") ? "" : phonenumber.trim());
            newContact.setEmail(email.equals("0") ? "" : email.trim());
           // switch (type)
           // {
           //     case "bank" :
                    newContact.setOrganization(organizationRepo.findById(Integer.parseInt(id)).get());
          //          break;
           // }

            contactRepo.save(newContact);
            log.info("Создано контактное лицо " + newContact.getFio() + ";");
            model.put("error", "Контакт добавлен");
        }
        else
        {
            model.put("error", "*Необходимо заполнить все поля");
        }

        //model.put("urgency", urgency);


        model.put("id", id);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "addcontact";
    }


    @GetMapping("/showonesystem")
    public String showonesystem(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        System system = systemRepo.findById(Integer.parseInt(id)).get();
        List<ARM> bankArms = new ArrayList<>();
        List<ARM> organizationArms = new ArrayList<>();
        List<ARMSystemName> armSystemNames = armSystemNameRepo.findBySystemName(system.getSystemName());
        List<ARM> allArmsBySystemName = new ArrayList<>();

        for (ARMSystemName armSystemName : armSystemNames)
        {
            allArmsBySystemName.add(armSystemName.getArm());
        }

        for (ARM arm : allArmsBySystemName)
        {
            if (arm.getOrganization() == system.getBank())
            {
                bankArms.add(arm);
            }
            if (arm.getOrganization() == system.getOrganization())
            {
                organizationArms.add(arm);
            }
        }

        List<Document> documents = documentRepo.findBySystemAndDocumentTypeNot(system, documentTypeRepo.findById(45).get());





        //model.put("urgency", urgency);

        model.put("id", id);
        model.put("system", system);
        model.put("documents", documents);
        model.put("bankarms", bankArms);
        model.put("organizationarms", organizationArms);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonesystem";
    }

    @GetMapping("/showonearm")
    public String showonearm(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String sourcetype,
            @RequestParam(required = false, defaultValue = "0") String sourceid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        ARM arm = armRepo.findById(Integer.parseInt(id)).get();
        List<Document> documents = documentRepo.findByArmAndDocumentTypeNot(arm, documentTypeRepo.findById(45).get());

        List<ARMSzi> armSzis = armsziRepo.findByArm(arm);
        List<SZI> szis = new ArrayList<>();

        for (ARMSzi armSzi : armSzis)
        {
            szis.add(armSzi.getSzi());
        }

        List<ARMSKZI> armSkzis = armskziRepo.findByArm(arm);
        List<SKZI> skzis = new ArrayList<>();

        for (ARMSKZI armSkzi : armSkzis)
        {
            skzis.add(armSkzi.getSkzi());
        }

        List<ARMKc> armKcs = armKcRepo.findByArm(arm);
        List<KeyCarriers> kcs = new ArrayList<>();

        for (ARMKc armKc : armKcs)
        {
            kcs.add(armKc.getKeyCarriers());
        }

        List<ARMSystemName> armSystemNames = armSystemNameRepo.findByArm(arm);
        List<SystemName> systemNames = new ArrayList<>();

        for (ARMSystemName armSystemName : armSystemNames)
        {
            systemNames.add(armSystemName.getSystemName());
        }


        List<Antivirus> antivirusList = new ArrayList<>();
        if (arm.getAntivirus() != null) {
            antivirusList.add(arm.getAntivirus());
        }

        List<PakUC> pakUCList = new ArrayList<>();
        if (arm.getPakUC() != null) {
            pakUCList.add(arm.getPakUC());
        }


        model.put("id", id);
        model.put("arm", arm);
        model.put("sourcetype", sourcetype);
        model.put("sourceid", sourceid);
        model.put("documents", documents);
        model.put("szis", szis);
        model.put("skzis", skzis);
        model.put("kcs", kcs);
        model.put("systemnames", systemNames);
        model.put("antivirusList", antivirusList);
        model.put("pakUCList", pakUCList);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonearm";
    }

    @GetMapping("/showonekeycarriers")
    public String showonekeycarriers(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        KeyCarriers keyCarriers = keyCarriersRepo.findById(Integer.parseInt(id)).get();
        List<Document> documents = documentRepo.findByKeyCarriersAndDocumentTypeNot(keyCarriers, documentTypeRepo.findById(45).get());


        model.put("id", id);
        model.put("keycarriers", keyCarriers);
        model.put("documents", documents);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonekeycarriers";
    }

    @GetMapping("/showonepakuc")
    public String showonepakuc(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        PakUC pakUC = pakUCRepo.findById(Integer.parseInt(id)).get();
        List<Document> documents = documentRepo.findByPakUCAndDocumentTypeNot(pakUC, documentTypeRepo.findById(45).get());


        model.put("id", id);
        model.put("pakuc", pakUC);
        model.put("documents", documents);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonepakuc";
    }

    @GetMapping("/showoneszi")
    public String showoneszi(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        SZI szi = sziRepo.findById(Integer.parseInt(id)).get();
        List<Document> documents = documentRepo.findBySziAndDocumentTypeNot(szi, documentTypeRepo.findById(45).get());


        model.put("id", id);
        model.put("szi", szi);
        model.put("documents", documents);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showoneszi";
    }

    @GetMapping("/showoneantivirus")
    public String showoneantivirus(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Antivirus antivirus = antivirusRepo.findById(Integer.parseInt(id)).get();
        List<Document> documents = documentRepo.findByAntivirusAndDocumentTypeNot(antivirus, documentTypeRepo.findById(45).get());


        model.put("id", id);
        model.put("antivirus", antivirus);
        model.put("documents", documents);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showoneantivirus";
    }

    @GetMapping("/showoneskzi")
    public String showoneskzi(
            @RequestParam(required = false, defaultValue = "0") String skziid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {


        SKZI skzi = skziRepo.findById(Integer.parseInt(skziid)).get();
        List<Document> documents = documentRepo.findBySkziAndDocumentTypeNot(skzi, documentTypeRepo.findById(45).get());





        //model.put("urgency", urgency);

        model.put("skziid", skziid);
        model.put("skzi", skzi);
        model.put("documents", documents);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showoneskzi";
    }

    @GetMapping("/showoneorganization")
    public String showoneorganization(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Optional<Organization> organizationOptional = organizationRepo.findById(Integer.parseInt(id));
        Organization organization =organizationOptional.get();

        List<Contact> contacts = contactRepo.findByOrganization(organization);
        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(organization, documentTypeRepo.findById(45).get());
        List<ARM> armList = armRepo.findByOrganization(organization);



        //model.put("urgency", urgency);

        model.put("organization", organization);
        model.put("contacts", contacts);
        //model.put("skzis", skzis);
        model.put("documents", documents);
        model.put("armList", armList);

        //model.put("urgency", urgency);

        model.put("organization", organization);
        model.put("contacts", contacts);
       // model.put("skzis", skzis);
        model.put("documents", documents);
        model.put("id", id);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showoneorganization";
    }

    @GetMapping("/showonedocument")
    public String showonedocument(
            @RequestParam(required = false, defaultValue = "0") String docid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

       Document document = documentRepo.findById(Integer.parseInt(docid)).get();


        String dbvaliduntildate = null;
        String dbdocumentdate = null;
        try {
            dbvaliduntildate = dbDateFormat.format(dayDateFormat.parse(document.getValidUntilDate()));
            dbdocumentdate = dbDateFormat.format(dayDateFormat.parse(document.getdocumentdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //model.put("urgency", urgency);

        model.put("document", document);
        model.put("dbvaliduntildate", dbvaliduntildate);
        model.put("dbdocumentdate", dbdocumentdate);
        model.put("hostname", hostname);
        model.put("docid", docid);
        model.put("urlprefixPath", urlprefixPath);


        return "showonedocument";
    }

    @GetMapping("/showonecontact")
    public String showonecontact(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String sourcetype,
            @RequestParam(required = false, defaultValue = "0") String sourceid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Contact contact = contactRepo.findById(Integer.parseInt(id)).get();



        //model.put("urgency", urgency);

        model.put("contact", contact);
        model.put("id", id);
        model.put("sourceid", sourceid);
        model.put("sourcetype", sourcetype);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonecontact";
    }

    @PostMapping("/showonecontact")
    public String showonecontact(
            @RequestParam(required = false, defaultValue = "0") String id,
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam(required = false, defaultValue = "0") String position,
            @RequestParam(required = false, defaultValue = "0") String phoneNumber,
            @RequestParam(required = false, defaultValue = "0") String email,
            @RequestParam(required = false, defaultValue = "0") String sourcetype,
            @RequestParam(required = false, defaultValue = "0") String sourceid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {



        Contact contact = contactRepo.findById(Integer.parseInt(id)).get();

        if (fio.equals("0"))
        {
            model.put("contact", contact);
            model.put("id", id);
            model.put("hostname", hostname);
            model.put("sourcetype", sourcetype);
            model.put("sourceid", sourceid);
            model.put("error", "Ф.И.О. не должно быть пустым!");
            model.put("urlprefixPath", urlprefixPath);


            return "showonecontact";
        }

        try {
            contact.setFio(contact.getFio().equals(fio.trim()) ? contact.getFio() : fio.trim());
        }catch (NullPointerException e)
        {

        }
        try {
            contact.setPosition(contact.getPosition().equals(position.trim()) ? contact.getPosition() : position.trim());
        }catch (NullPointerException e)
        {

        }
        try {
            contact.setPhoneNumber(contact.getPhoneNumber().equals(phoneNumber.trim()) ? contact.getPhoneNumber() : phoneNumber.trim());
        }catch (NullPointerException e)
        {

        }
        try {
            contact.setEmail(contact.getEmail().equals(email.trim()) ? contact.getEmail() : email.trim());
        }catch (NullPointerException e)
        {

        }

        contactRepo.save(contact);

        //model.put("urgency", urgency);

        model.put("contact", contact);
        model.put("id", id);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonecontact";
    }


    @GetMapping("/showpakcs")
    public String showpakcs(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<PakUC> pakUCList = pakUCRepo.findAll();


        model.put("pakUCList", pakUCList);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showpakcs";
    }

    @GetMapping("/showkcs")
    public String showkcs(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<KeyCarriers> keyCarriersList = keyCarriersRepo.findAll();


        model.put("keyCarriersList", keyCarriersList);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showkcs";
    }

    @GetMapping("/showszis")
    public String showszis(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<SZI> szis = sziRepo.findAll();


        model.put("szis", szis);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showszis";
    }

    @GetMapping("/showantiviruses")
    public String showantiviruses(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        List<Antivirus> antiviruses = antivirusRepo.findAll();


        model.put("antiviruses", antiviruses);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showantiviruses";
    }

    @GetMapping("/showskzis")
    public String showskzis(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

       List<SKZI> skzis = skziRepo.findAll();


        model.put("skzis", skzis);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showskzis";
    }


    @GetMapping("/showonebank")
    public String showonebank(
            @RequestParam(required = false, defaultValue = "0") String id,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        Optional<Organization> organizationOptional = organizationRepo.findById(Integer.parseInt(id));
        Organization bank = organizationOptional.get();

        List<Contact> contacts = contactRepo.findByOrganization(bank);
        List<Document> documents = documentRepo.findByOrganizationAndDocumentTypeNot(bank, documentTypeRepo.findById(45).get());
        List<ARM> armList = armRepo.findByOrganization(bank);


        //model.put("urgency", urgency);

        model.put("bank", bank);
        model.put("contacts", contacts);
        //model.put("skzis", skzis);
        model.put("documents", documents);
        model.put("armList", armList);
        model.put("id", id);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);


        return "showonebank";
    }

    @GetMapping("/showorganizations")
    public String showorganizations(
            Map<String, Object> model)
    {
        List<Organization> organizations = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(2).get());


        model.put("organizations", organizations);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "showorganizations";
    }


    @GetMapping("/defaultdocsforsystems")
    public String defaultdocsforsystems(
            Map<String, Object> model)
    {
        List<SystemName> systemNames = systemNameRepo.findAll();
        List<Document> documents = documentRepo.findBySystemName(systemNames.get(0));

        model.put("selectedsystemname", systemNames.get(0).getName());

        model.put("systemnames", systemNames);
        model.put("documents", documents);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "defaultdocsforsystems";
    }

    @PostMapping("/defaultdocsforsystems")
    public String defaultdocsforsystems(
            @RequestParam(required = false, defaultValue = "0") String systemid,
            Map<String, Object> model)
    {
        List<SystemName> systemNames = systemNameRepo.findAll();
        List<Document> documents = documentRepo.findBySystemName(systemNameRepo.findById(Integer.parseInt(systemid)).get());



        model.put("selectedsystemname", systemid.equals("0") ? systemNames.get(0).getName() : systemNameRepo.findById(Integer.parseInt(systemid)).get().getName());


        model.put("systemnames", systemNames);
        model.put("documents", documents);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "defaultdocsforsystems";
    }

    @GetMapping("/showsystems")
    public String showsystems(
            Map<String, Object> model)
    {
        List<System> systems = systemRepo.findAll();


        model.put("systems", systems);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "showsystems";
    }

    @GetMapping("/showreports")
    public String showreports(
            Map<String, Object> model) {

        List<Document> documents = documentRepo.findByDocumentType(documentTypeRepo.findById(45).get());

        for (Document report : documents)
        {

            try {
                if (getDateDiff(new Date(), dayDateFormat.parse(report.getLastDocumentStatusUpdateDate()), TimeUnit.DAYS) < -3)
                {
                    report.setLastDocumentStatusUpdateStatus("3");
                }
                else if (getDateDiff(new Date(), dayDateFormat.parse(report.getLastDocumentStatusUpdateDate()), TimeUnit.DAYS) >= -3 && getDateDiff(new Date(), dayDateFormat.parse(report.getLastDocumentStatusUpdateDate()), TimeUnit.DAYS) < 0)
                {
                    report.setLastDocumentStatusUpdateStatus("2");
                }
                else
                {
                    report.setLastDocumentStatusUpdateStatus("1");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //setTrustLevel(report);


        }

        model.put("hostname", hostname);
        model.put("documents", documents);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);

        return "showreports";
    }



    @GetMapping("/showbanks")
    public String showbanks(
            Map<String, Object> model)
    {
        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());


        model.put("banks", banks);
        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "showbanks";
    }

    @GetMapping("/addorganization")
    public String addorganization(
            Map<String, Object> model) {


        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addorganization";
    }

    @PostMapping("/addorganization")
    public String addorganization(
            @RequestParam(required = false, defaultValue = "0") String organizationname,
            @RequestParam(required = false, defaultValue = "0") String inn,
            @RequestParam(required = false, defaultValue = "0") String gid,
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam(required = false, defaultValue = "0") String position,
            @RequestParam(required = false, defaultValue = "0") String phonenumber,
            @RequestParam(required = false, defaultValue = "0") String email,
            Map<String, Object> model) {

        organizationname = organizationname.trim();

        if (!organizationname.equals("0"))
        {
            Organization org = organizationRepo.findByNameAndOrganizationType(organizationname, organizationTypeRepo.findById(2).get());
            boolean tr = org != null;

            if (organizationRepo.findByNameAndOrganizationType(organizationname, organizationTypeRepo.findById(2).get()) == null) {
                Organization newOrganization = new Organization();

                    newOrganization = new Organization();

                    newOrganization.setOrgType(organizationTypeRepo.findById(2).get());

                    newOrganization.setName(organizationname);
                    newOrganization.setInn(inn.equals("0") ? null : inn.trim().replaceAll("[^0-9]", ""));
                    newOrganization.setGid(gid.equals("0") ? null : gid.trim().replaceAll("[^0-9]", ""));
                    organizationRepo.save(newOrganization);

                if ( !fio.equals("0")) {

                    fio = fio.trim().replaceAll("[^А-Яа-я ]", "");

                        Contact newContact = new Contact();
                        newContact.setFio(fio);
                        newContact.setPosition(position.equals("0") ? null : position.trim());
                        newContact.setPhoneNumber(phonenumber.equals("0") ? null : phonenumber.trim());
                        newContact.setEmail(email.equals("0") ? null : email.trim());




                    newContact.setOrganization(newOrganization);
                    contactRepo.save(newContact);
                }

                log.info("Создана организация " + newOrganization.getName() + ";");
                model.put("error", "Организация добавлена");
            }
            else
                {
                    model.put("organizationname", organizationname.equals("0") ? "" : organizationname);
                    model.put("fio", fio.equals("0") ? "" : fio);
                    model.put("position", position.equals("0") ? "" : position);
                    model.put("inn", inn.equals("0") ? "" : inn);
                    model.put("gid", gid.equals("0") ? "" : gid);
                    model.put("organization", phonenumber.equals("0") ? "" : phonenumber);
                    model.put("email", email.equals("0") ? "" : email);
                    model.put("error", "*Организация с таким именем уже существует");
                }

        } else {

            model.put("organizationname", organizationname.equals("0") ? "" : organizationname);
            model.put("fio", fio.equals("0") ? "" : fio);
            model.put("position", position.equals("0") ? "" : position);
            model.put("inn", inn.equals("0") ? "" : inn);
            model.put("gid", gid.equals("0") ? "" : gid);
            model.put("organization", phonenumber.equals("0") ? "" : phonenumber);
            model.put("email", email.equals("0") ? "" : email);
            model.put("error", "*Необходимо заполнить все поля");

        }
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addorganization";
    }

    @GetMapping("/addsystem")
    public String addsystem(
            Map<String, Object> model) {

        List<SystemType> systemtypes = systemTypeRepo.findAll();
        List<SystemName> systemNames = systemNameRepo.findAll();
        List<Organization> organizations = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(2).get());
        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
        List<EsType> esTypes = esTypeRepo.findAll();

        model.put("error", "");
        model.put("banks", banks);
        model.put("organizations", organizations);
        model.put("systemnames", systemNames);
        model.put("systemtypes", systemtypes);
        model.put("estypes", esTypes);
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addsystem";
    }

    @PostMapping("/addsystem")
    public String addsystem(
            @RequestParam(required = false, defaultValue = "0") String systemname,
            @RequestParam(required = false, defaultValue = "0") String systemnameid,
            @RequestParam(required = false, defaultValue = "0") String systemtype,
            @RequestParam(required = false, defaultValue = "0") String estype,
            @RequestParam(required = false, defaultValue = "0") String organizationid,
            @RequestParam(required = false, defaultValue = "0") String bankid,
            @RequestParam(required = false, defaultValue = "0") String keyExpirationMonths,
            @RequestParam(required = false, defaultValue = "0") String cryptoType,
            @RequestParam(required = false, defaultValue = "0") String banksoftware,
            Map<String, Object> model) {


        List<SystemType> systemtypes = systemTypeRepo.findAll();

        List<Organization> banks = organizationRepo.findByOrganizationType(organizationTypeRepo.findById(1).get());
        List<EsType> esTypes = esTypeRepo.findAll();

        model.put("error", "");
        model.put("banks", banks);


        model.put("systemtypes", systemtypes);
        model.put("estypes", esTypes);

        if (!systemname.equals("0")
                && !systemtype.equals("0")
                && !systemtype.equals("0")
                && !estype.equals("0")
                && !systemnameid.equals("0")
                && !organizationid.equals("0")
                && !bankid.equals("0")
            )
        {
            systemname = systemname.trim();
            systemtype = systemtype.trim();
            estype = estype.trim();

          if (systemRepo.findByNameAndAndSystemType(systemname, systemTypeRepo.findById(Integer.parseInt(systemtype)).get()) == null)
          {
              System newSystem = new System();
              newSystem.setName(systemname);
              newSystem.setSystemType(systemTypeRepo.findById(Integer.parseInt(systemtype)).get());
              newSystem.setEsType(esTypeRepo.findById(Integer.parseInt(estype)).get());
              newSystem.setBank(organizationRepo.findById(Integer.parseInt(bankid)).get());
              newSystem.setOrganization(organizationRepo.findById(Integer.parseInt(organizationid)).get());
              newSystem.setSystemName(systemNameRepo.findById(Integer.parseInt(systemnameid)).get());
              newSystem.setCryptoType(cryptoType);
              newSystem.setKeyExpirationMonths(keyExpirationMonths);
              if(!banksoftware.equals("0")) {
                  newSystem.setBankSoftware(true);
              }
              else {
                  newSystem.setBankSoftware(false);
              }
              systemRepo.save(newSystem);

              for (Document document : documentRepo.findBySystemName(systemNameRepo.findById(Integer.parseInt(systemnameid)).get()))
              {
                  Document newDocument = new Document();
                  newDocument.setSystem(newSystem);
                  newDocument.setDocumentNumber(document.getDocumentNumber());
                  newDocument.setDocumentName(document.getDocumentName());
                  newDocument.setDocumentDate(document.getDocumentDate());
                  newDocument.setParameters(document.getParameters());
                  newDocument.setLink(document.getLink());
                  newDocument.setDocumentType(document.getDocumentType());
                  newDocument.setValidUntilDate(document.getValidUntilDate());
                  newDocument.setDocumentFileName(document.getDocumentFileName());
                  documentRepo.save(newDocument);
              }
              log.info("Создана система " + newSystem.getSystemName() + ";");
              model.put("error", "Система добавлена");
          }
          else
          {
              model.put("error", "*система с таким система уже существует");
              model.put("selectedsystemname", systemnameid.equals("0") ? "Система" : systemNameRepo.findById(Integer.parseInt(systemnameid)).get().getName());
              model.put("selectedsystemtype", systemtype.equals("0") ? "Тип системы" : systemTypeRepo.findById(Integer.parseInt(systemtype)).get().getName());
              model.put("selectedorganization", organizationid.equals("0") ? "Организация" : organizationRepo.findById(Integer.parseInt(organizationid)).get().getName());
              model.put("selectedbank", bankid.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bankid)).get().getName());
              model.put("systemname", systemname.equals("0") ? "" : systemname);
              model.put("selectedestype", estype.equals("0") ? "Вид электронной подписи" : esTypeRepo.findById(Integer.parseInt(estype)).get().getName());
          }
        }
        else
        {
            if (systemnameid.equals("0") && !bankid.equals("0") && organizationid.equals("0"))
            {
                List<ARM> allOrgArms = armRepo.findByOrganization(organizationRepo.findById(Integer.parseInt(bankid)).get());
                List<SystemName> systemNameList = new ArrayList<>();

                if (allOrgArms.size() > 0) {
                    //bankarms.addAll(allOrgArms);

                for (ARM arm : allOrgArms)
                {
                    for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm))
                    {
                        if (!systemNameList.contains(armSystemName.getSystemName())) {
                            systemNameList.add(armSystemName.getSystemName());
                        }
                    }
                }

                    if (systemNameList.size() > 0) {
                        model.put("systemnames", systemNameList);
                    }
                }

               // model.put("selectedsystemname", systemnameid.equals("0") ? "Система" : systemNameRepo.findById(Integer.parseInt(systemnameid)).get().getName());
               // model.put("selectedsystemtype", systemtype.equals("0") ? "Тип системы" : systemTypeRepo.findById(Integer.parseInt(systemtype)).get().getName());
               // model.put("selectedorganization", organizationid.equals("0") ? "Организация" : organizationRepo.findById(Integer.parseInt(organizationid)).get().getName());
               // model.put("selectedorganizationarm", organizationarmid.equals("0") ? "АРМ Организации" : armRepo.findById(Integer.parseInt(organizationarmid)).get().getArmName());
                model.put("selectedbank", bankid.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bankid)).get().getName());
               // model.put("selectedbankarm", bankarmid.equals("0") ? "АРМ Банка" : armRepo.findById(Integer.parseInt(bankarmid)).get().getArmName());
               // model.put("systemname", systemname.equals("0") ? "" : systemname);
               // model.put("selectedestype", estype.equals("0") ? "Вид электронной подписи" : esTypeRepo.findById(Integer.parseInt(estype)).get().getName());



                model.put("systemtypes", systemtypes);
                model.put("estypes", esTypes);
                model.put("hostname", hostname);
                model.put("urlprefixPath", urlprefixPath);
                return "addsystem";
            }
            else if (!systemnameid.equals("0") && !bankid.equals("0") && organizationid.equals("0"))
            {
                List<Organization> organizations = new ArrayList<>();


                List<ARMSystemName> armSystemNames = armSystemNameRepo.findBySystemName(systemNameRepo.findById(Integer.parseInt(systemnameid)).get());

                if (armSystemNames.size() > 0) {

                   for (ARMSystemName armSystemName : armSystemNames)
                   {
                       if (armSystemName.getArm().getOrganization().getOrgType() == organizationTypeRepo.findById(2).get())
                       {
                           if (!organizations.contains(armSystemName.getArm().getOrganization())) {
                               organizations.add(armSystemName.getArm().getOrganization());
                           }
                       }
                   }

                    if (organizations.size() > 0) {
                        model.put("organizations", organizations);
                    }
                }

                List<ARM> allOrgArms = armRepo.findByOrganization(organizationRepo.findById(Integer.parseInt(bankid)).get());
                List<SystemName> systemNameList = new ArrayList<>();

                if (allOrgArms.size() > 0) {
                    //bankarms.addAll(allOrgArms);

                    for (ARM arm : allOrgArms)
                    {
                        for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm))
                        {
                            if (!systemNameList.contains(armSystemName.getSystemName())) {
                                systemNameList.add(armSystemName.getSystemName());
                            }
                        }
                    }

                    if (systemNameList.size() > 0) {
                        model.put("systemnames", systemNameList);
                    }
                }


                model.put("selectedsystemname", systemnameid.equals("0") ? "Система" : systemNameRepo.findById(Integer.parseInt(systemnameid)).get().getName());
                //model.put("selectedsystemtype", systemtype.equals("0") ? "Тип системы" : systemTypeRepo.findById(Integer.parseInt(systemtype)).get().getName());
               //model.put("selectedorganization", organizationid.equals("0") ? "Организация" : organizationRepo.findById(Integer.parseInt(organizationid)).get().getName());
               // model.put("selectedorganizationarm", organizationarmid.equals("0") ? "АРМ Организации" : armRepo.findById(Integer.parseInt(organizationarmid)).get().getArmName());
                model.put("selectedbank", bankid.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bankid)).get().getName());
                //model.put("selectedbankarm", bankarmid.equals("0") ? "АРМ Банка" : armRepo.findById(Integer.parseInt(bankarmid)).get().getArmName());
                //model.put("systemname", systemname.equals("0") ? "" : systemname);
               // model.put("selectedestype", estype.equals("0") ? "Вид электронной подписи" : esTypeRepo.findById(Integer.parseInt(estype)).get().getName());



                model.put("systemtypes", systemtypes);
                model.put("estypes", esTypes);
                model.put("hostname", hostname);
                model.put("urlprefixPath", urlprefixPath);
                return "addsystem";
            }
            else if (!systemnameid.equals("0") && !bankid.equals("0") && !organizationid.equals("0"))
            {
                List<Organization> organizations = new ArrayList<>();


                List<ARMSystemName> armSystemNames = armSystemNameRepo.findBySystemName(systemNameRepo.findById(Integer.parseInt(systemnameid)).get());

                if (armSystemNames.size() > 0) {

                    for (ARMSystemName armSystemName : armSystemNames)
                    {
                        if (armSystemName.getArm().getOrganization().getOrgType() == organizationTypeRepo.findById(2).get())
                        {
                            if (!organizations.contains(armSystemName.getArm().getOrganization())) {
                                organizations.add(armSystemName.getArm().getOrganization());
                            }
                        }
                    }

                    if (organizations.size() > 0) {
                        model.put("organizations", organizations);
                    }
                }

                List<ARM> allOrgArms = armRepo.findByOrganization(organizationRepo.findById(Integer.parseInt(bankid)).get());
                List<SystemName> systemNameList = new ArrayList<>();


                if (allOrgArms.size() > 0) {
                    //bankarms.addAll(allOrgArms);

                    for (ARM arm : allOrgArms) {
                        for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm)) {
                            if (!systemNameList.contains(armSystemName.getSystemName())) {
                                systemNameList.add(armSystemName.getSystemName());

                            }
                        }
                    }
                }
                    List<ARM> allClientArms = armRepo.findByOrganization(organizationRepo.findById(Integer.parseInt(organizationid)).get());
                    String clientArmSKZI = null;

                    if (allClientArms.size() > 0) {
                        //bankarms.addAll(allOrgArms);

                        for (ARM arm : allClientArms)
                        {
                            for (ARMSystemName armSystemName : armSystemNameRepo.findByArm(arm))
                            {
                                if (systemNameList.contains(armSystemName.getSystemName())) {

                                    try {

                                        clientArmSKZI = armskziRepo.findByArm(arm).get(0).getSkzi().getName() + " версия " + armskziRepo.findByArm(armSystemName.getArm()).get(0).getSkzi().getVersion() +
                                                " (исполнение " + armskziRepo.findByArm(armSystemName.getArm()).get(0).getSkzi().getRealizationVariant() + ")";
                                    }
                                    catch (Exception e)
                                    {
                                        clientArmSKZI = "У Арма клиента нет СКЗИ";
                                    }
                                }
                            }
                        }


                    model.put("clientArmSKZI", clientArmSKZI);
                    if (systemNameList.size() > 0) {
                        model.put("systemnames", systemNameList);
                    }
                }


                model.put("selectedsystemname", systemnameid.equals("0") ? "Система" : systemNameRepo.findById(Integer.parseInt(systemnameid)).get().getName());
                model.put("selectedsystemtype", systemtype.equals("0") ? "Тип системы" : systemTypeRepo.findById(Integer.parseInt(systemtype)).get().getName());
                model.put("selectedorganization", organizationid.equals("0") ? "Организация" : organizationRepo.findById(Integer.parseInt(organizationid)).get().getName());
                model.put("selectedbank", bankid.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bankid)).get().getName());
                model.put("systemname", systemname.equals("0") ? "" : systemname);
                model.put("selectedestype", estype.equals("0") ? "Вид электронной подписи" : esTypeRepo.findById(Integer.parseInt(estype)).get().getName());
            }
            else {
                model.put("error", "*Необходимо заполнить все поля");
                model.put("selectedsystemname", systemnameid.equals("0") ? "Система" : systemNameRepo.findById(Integer.parseInt(systemnameid)).get().getName());
                model.put("selectedsystemtype", systemtype.equals("0") ? "Тип системы" : systemTypeRepo.findById(Integer.parseInt(systemtype)).get().getName());
                model.put("selectedorganization", organizationid.equals("0") ? "Организация" : organizationRepo.findById(Integer.parseInt(organizationid)).get().getName());
                model.put("selectedbank", bankid.equals("0") ? "Банк" : organizationRepo.findById(Integer.parseInt(bankid)).get().getName());
                model.put("systemname", systemname.equals("0") ? "" : systemname);
                model.put("selectedestype", estype.equals("0") ? "Вид электронной подписи" : esTypeRepo.findById(Integer.parseInt(estype)).get().getName());
            }
        }



        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addsystem";
    }

    @GetMapping("/addbank")
    public String addbank(
            Map<String, Object> model) {


        model.put("error", "");
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addbank";
    }

    @PostMapping("/addbank")
    public String addtoarchive(
            @RequestParam(required = false, defaultValue = "0") String bankname,
            @RequestParam(required = false, defaultValue = "0") String inn,
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam(required = false, defaultValue = "0") String position,
            @RequestParam(required = false, defaultValue = "0") String phonenumber,
            @RequestParam(required = false, defaultValue = "0") String email,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        bankname = bankname.trim();

        inn = inn.trim().replaceAll("[^0-9]", "");

        if (!bankname.equals("0")) {

            if (organizationRepo.findByNameAndOrganizationType(bankname, organizationTypeRepo.findById(1).get()) == null) {

                Organization newBank = new Organization();

                newBank.setOrgType(organizationTypeRepo.findById(1).get());

                newBank.setName(bankname);
                if (!inn.equals("0")) {
                    newBank.setInn(inn);
                }

                organizationRepo.save(newBank);
                log.info("Создан банк " + newBank.getName() + ";");

                if (!fio.equals("0"))
                {
                    fio = fio.trim().replaceAll("[^А-Яа-я ]", "");
                    Contact newContact = new Contact();

                        newContact = new Contact();
                        newContact.setFio(fio);
                        newContact.setPosition(position.equals("0") ? null : position.trim());
                        newContact.setPhoneNumber(phonenumber.equals("0") ? null : phonenumber.trim());
                        newContact.setEmail(email.equals("0") ? null : email.trim());

                        newContact.setOrganization(newBank);
                       contactRepo.save(newContact);
                }
                model.put("error", "Банк добавлен");
            }
            else
            {
                model.put("bankname", bankname.equals("0") ? "" : bankname);
                model.put("fio", fio.equals("0") ? "" : fio);
                model.put("position", position.equals("0") ? "" : position);
                model.put("organization", phonenumber.equals("0") ? "" : phonenumber);
                model.put("email", email.equals("0") ? "" : email);
                model.put("error", "*Банк с таким наименованием уже существует");
            }




        } else {

            model.put("bankname", bankname.equals("0") ? "" : bankname);
            model.put("fio", fio.equals("0") ? "" : fio);
            model.put("position", position.equals("0") ? "" : position);
            model.put("organization", phonenumber.equals("0") ? "" : phonenumber);
            model.put("email", email.equals("0") ? "" : email);
            model.put("error", "*Необходимо заполнить все поля");

        }
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);
        return "addbank";
    }

    @GetMapping("/")
    public String showmain(Map<String, Object> model) {
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);



        return "/main";
    }

    @PostMapping("/")
    public String showmainpost(Map<String, Object> model) {
        model.put("hostname", hostname);
        model.put("urlprefixPath", urlprefixPath);



        return "/main";
    }

    String generateContactLogString(String logString)
    {
        return dateFormat.format(new Date()) + logString  + ";\n";
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {


        long diffInMillies = date2.getTime() - date1.getTime();


        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    private void setTrustLevel(Document report) {

        Map<Integer, Document> allReportDocuments = new HashMap<>();

        for (Document bankDocument : documentRepo.findByOrganizationAndDocumentTypeNot(organizationRepo.findById(report.getBank().getId()).get(), documentTypeRepo.findById(45).get())) {
            allReportDocuments.put(bankDocument.getDocumentType().getId(), bankDocument);
        }
/*
        for (Document bankSkziDocument : documentRepo.findBySkziAndDocumentTypeNot(skziRepo.findById(report.getReportBankSkzi().getId()).get(), documentTypeRepo.findById(45).get())) {
            allReportDocuments.put(bankSkziDocument.getDocumentType().getId(), bankSkziDocument);
        }
*/
        for (Document organizationDocument : documentRepo.findByOrganizationAndDocumentTypeNot(organizationRepo.findById(report.getOrganization().getId()).get(), documentTypeRepo.findById(45).get())) {
            allReportDocuments.put(organizationDocument.getDocumentType().getId(), organizationDocument);
        }
/*
        for (Document organizationSkziDocument : documentRepo.findBySkziAndDocumentTypeNot(skziRepo.findById(report.getReportOrganizationSkzi().getId()).get(), documentTypeRepo.findById(45).get())) {
            allReportDocuments.put(organizationSkziDocument.getDocumentType().getId(), organizationSkziDocument);
        }
*/
        for (Document systemDocument : documentRepo.findBySystemAndDocumentTypeNot(systemRepo.findById(report.getSystem().getId()).get(), documentTypeRepo.findById(45).get())) {
            allReportDocuments.put(systemDocument.getDocumentType().getId(), systemDocument);
        }


        try {

            if (    ((allReportDocuments.containsKey(1) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(1).getValidUntilDate()), TimeUnit.DAYS) >= 0) || allReportDocuments.containsKey(2) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(2).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && ((allReportDocuments.containsKey(3) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(3).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(4) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(4).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && (allReportDocuments.containsKey(5) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(5).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(6) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(6).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(8) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(8).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(46) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(46).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && ((allReportDocuments.containsKey(10) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(10).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(11) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(11).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && ((allReportDocuments.containsKey(12) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(12).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(13) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(13).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && ((allReportDocuments.containsKey(14) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(14).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(15) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(15).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && (allReportDocuments.containsKey(19) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(19).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(20) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(20).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(21) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(21).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(22) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(22).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(23) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(23).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(24) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(24).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(25) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(25).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(26) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(26).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && ((allReportDocuments.containsKey(28) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(28).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(29) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(29).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && ((allReportDocuments.containsKey(31) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(31).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(32) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(32).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                    && (allReportDocuments.containsKey(34) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(34).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(35) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(35).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(36) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(36).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(37) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(37).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(38) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(38).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(39) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(39).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(40) && getDateDiff(new Date(),dayDateFormat.parse(allReportDocuments.get(40).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(43) && getDateDiff(new Date(),dayDateFormat.parse(allReportDocuments.get(43).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    && (allReportDocuments.containsKey(44) && getDateDiff(new Date(),dayDateFormat.parse(allReportDocuments.get(44).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                    )
            {

                report.setTrustLevel(trustLevelRepo.findById(2).get());

                if ((allReportDocuments.containsKey(7) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(7).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(9) && getDateDiff(new Date(), dayDateFormat .parse(allReportDocuments.get(9).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(16) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(16).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(17) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(17).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(18) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(18).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(27) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(27).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(30) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(30).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(33) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(33).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(41) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(41).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(42) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(42).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && ((allReportDocuments.containsKey(1) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(1).getValidUntilDate()), TimeUnit.DAYS) >= 0) || allReportDocuments.containsKey(2) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(2).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && ((allReportDocuments.containsKey(3) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(3).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(4) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(4).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && (allReportDocuments.containsKey(5) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(5).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(6) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(6).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(8) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(8).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(46) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(46).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && ((allReportDocuments.containsKey(10) && getDateDiff(new Date(), dayDateFormat .parse(allReportDocuments.get(10).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(11) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(11).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && ((allReportDocuments.containsKey(12) && getDateDiff(new Date(), dayDateFormat .parse(allReportDocuments.get(12).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(13) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(13).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && ((allReportDocuments.containsKey(14) && getDateDiff(new Date(), dayDateFormat .parse(allReportDocuments.get(14).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(15) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(15).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && (allReportDocuments.containsKey(19) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(19).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(20) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(20).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(21) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(21).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(22) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(22).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(23) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(23).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(24) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(24).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(25) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(25).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(26) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(26).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && ((allReportDocuments.containsKey(28) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(28).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(29) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(29).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && ((allReportDocuments.containsKey(31) && getDateDiff(new Date(), dayDateFormat .parse(allReportDocuments.get(31).getValidUntilDate()), TimeUnit.DAYS) >= 0) || (allReportDocuments.containsKey(32) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(32).getValidUntilDate()), TimeUnit.DAYS) >= 0))
                        && (allReportDocuments.containsKey(34) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(34).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(35) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(35).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(36) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(36).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(37) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(37).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(38) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(38).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(39) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(39).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(40) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(40).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(43) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(43).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        && (allReportDocuments.containsKey(44) && getDateDiff(new Date(), dayDateFormat.parse(allReportDocuments.get(44).getValidUntilDate()), TimeUnit.DAYS) >= 0)
                        )
                {
                    report.setTrustLevel(trustLevelRepo.findById(1).get());
                }
            } else {
                report.setTrustLevel(trustLevelRepo.findById(3).get());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    private void mailSending(Mail mail) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat dateFormatBody = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat subFormatBody = new SimpleDateFormat("ddMMyyyy");

        //String dateBody = dateFormatBody.format(new Date());
        String dateBody = dateFormatBody.format(calendar.getTime());
        //String dateSub = subFormatBody.format(new Date());
        String dateSub = subFormatBody.format(calendar.getTime());


        creds = credRead();
        username = creds[0].replaceAll("\n", "").replaceAll("\r", "");
        password = creds[1].replaceAll("\n", "").replaceAll("\r", "");


        try {

            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", smtpserver);
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);

            Transport transport = mailSession.getTransport();

            transport.connect();
            MimeMessage message = null;
            message = new MimeMessage(mailSession);

            message.setSubject(mail.getTheme());

            message.setFrom(new InternetAddress("sep@rosatom.ru"));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("aamartynyuk@greenatom.ru"));

            for (String recepient : mail.getRecepientList())
            {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(recepient));
            }

            MimeBodyPart textBodyPart = new MimeBodyPart();


            Multipart multipart = new MimeMultipart();

            textBodyPart.setText(mail.getText());

            multipart.addBodyPart(textBodyPart);
            message.setContent(multipart);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();

            //JOptionPane.showMessageDialog(null, "Файлы отправлены адресатам!");

        } catch (MessagingException e1) {
            //JOptionPane.showMessageDialog(null, "Ошибка отправки почты (возможно истек пароль к УЗ): " + e1.getMessage());
            e1.printStackTrace();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Ошибка отправки почты (возможно истек пароль к УЗ): " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static String[] credRead()
    {
        String[]creds=new String[0];

        try(FileReader reader=new FileReader(CREDS_FILE_NAME))
        {
            char[]buf=new char[256];
            int c;
            while((c=reader.read(buf))>0){

                if(c < 256){
                    buf= Arrays.copyOf(buf,c);
                }
                creds=String.valueOf(buf).trim().replaceAll(" ","").split("\n");
            }
        }
        catch(IOException ex){

            //logging();
        }


        return creds;
    }

    private static void replaseText(XWPFDocument doc, String sourceText, String destText)
    {
        for (XWPFParagraph p : doc.getParagraphs()) {

            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(sourceText)) {
                        text = text.replace(sourceText, destText);
                        r.setText(text, 0);
                    }
                }
            }
        }
    }

    private static void replaseTableText(XWPFDocument doc, String sourceText, String destText)
    {
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains(sourceText)) {
                                text = text.replace(sourceText, destText);
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}


