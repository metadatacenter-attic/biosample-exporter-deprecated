package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.sp.TypeBlock;
import common.sp.TypeDescriptor;
import common.sp.TypeExternalLink;
import common.sp.TypeOrganism;
import common.sp.TypePrimaryId;
import common.sp.TypeRefId;
import generated.TypeAttribute;
import generated.TypeBioSample;
import generated.TypeBioSampleIdentifier;
import generated.TypeContactInfo;
import generated.TypeName;
import generated.TypeOrganization;
import generated.TypeSubmission;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.util.GregorianCalendar;

public class CEDAR2BioSample
{
  public static void main(String[] argc) throws IOException, JAXBException, DatatypeConfigurationException
  {
    generated.ObjectFactory objectFactory = new generated.ObjectFactory();
    common.sp.ObjectFactory spCommonObjectFactory = new common.sp.ObjectFactory();

    ObjectMapper mapper = new ObjectMapper();
//    File bioSampleJSONFile = new File(
//      CEDAR2BioSample.class.getClassLoader().getResource("./json/BioSample1.json").getFile());
//    File ncbiSubmissionDescriptionJSONFile = new File(
//      CEDAR2BioSample.class.getClassLoader().getResource("./json/NCBISubmissionDescription1.json").getFile());
//
//    BioSample bioSample = mapper.readValue(bioSampleJSONFile, BioSample.class);
//    NCBISubmissionDescription ncbiSubmissionDescription = mapper
//      .readValue(bioSampleJSONFile, NCBISubmissionDescription.class);
//
//    System.out.println("Comment: " + ncbiSubmissionDescription.getComment().getValue());
//    System.out.println("Release Date: " + ncbiSubmissionDescription.getReleaseDate().getValue());
//
//    NCBIOrganization ncbiOrganization = ncbiSubmissionDescription.getNCBIOrganization();
//    System.out.println("Institution Name: " + ncbiOrganization.getInstitutionName().getValue());
//
//    NCBIContact ncbiContact = ncbiOrganization.getNCBIContact();
//    System.out.println("Email: " + ncbiContact.getEmail().getValue());
//
//    NCBIName ncbiName = ncbiContact.getNCBIName();
//    System.out.println("First Name: " + ncbiName.getFirstName().getValue());
//    System.out.println("Middle Initial: " + ncbiName.getMiddleInitial().getValue());
//    System.out.println("Last Name: " + ncbiName.getLastName().getValue());
//
//    System.out.println("BioProjectID: " + bioSample.getBioProjectID().getValue());
//    System.out.println("Package: " + bioSample.getPackage().getValue());
//
//    BioSampleSampleID bioSampleSampleID = bioSample.getBioSampleSampleID();
//    System.out.println("Label: " + bioSampleSampleID.getLabel().getValue());
//    System.out.println("Display: " + bioSampleSampleID.getDisplay().getValue());
//
//    NCBISPUID ncbiSPUID = bioSampleSampleID.getNCBISPUID();
//    System.out.println("SubmitterID: " + ncbiSPUID.getSubmitterID().getValue());
//    System.out.println("Namespace: " + ncbiSPUID.getNamespace().getValue());
//    System.out.println("Value: " + ncbiSPUID.getValue().getValue());
//
//    BioSampleDescriptor bioSampleDescriptor = bioSample.getBioSampleDescriptor();
//    System.out.println("Title: " + bioSampleDescriptor.getTitle().getValue());
//    System.out.println("Description: " + bioSampleDescriptor.getDescription().getValue());
//    System.out.println("External Link: " + bioSampleDescriptor.getExternalLink().getValue());
//
//    NCBIOrganism ncbiOrganism = bioSample.getNCBIOrganism();
//    System.out.println("Organism Name: " + ncbiOrganism.getOrganismName().getValue());
//    System.out.println("Label: " + ncbiOrganism.getLabel().getValue());
//    System.out.println("Strain: " + ncbiOrganism.getStrain().getValue());
//    System.out.println("Isolate Name: " + ncbiOrganism.getIsolateName().getValue());
//    System.out.println("Breed: " + ncbiOrganism.getBreed().getValue());
//    System.out.println("Cultivar: " + ncbiOrganism.getCultivar().getValue());
//
//    BioSamplePathogenCl10Attributes bioSamplePathogenCl10Attributes = bioSample.getBioSamplePathogenCl10Attributes();
//    System.out.println("Strain: " + bioSamplePathogenCl10Attributes.getStrain().getValue());
//    System.out.println("Collection Date: " + bioSamplePathogenCl10Attributes.getCollectionDate().getValue());
//    System.out.println("Collected By: " + bioSamplePathogenCl10Attributes.getCollectedBy().getValue());
//    System.out.println("GEO Location Name: " + bioSamplePathogenCl10Attributes.getGEOLocationName().getValue());
//    System.out.println("Isolation Source: " + bioSamplePathogenCl10Attributes.getIsolationSource().getValue());
//    System.out.println("Latitude/longitude: " + bioSamplePathogenCl10Attributes.getLatitudeLongitude().getValue());
//    System.out.println("Host: " + bioSamplePathogenCl10Attributes.getHost().getValue());
//    System.out.println("Host Disease: " + bioSamplePathogenCl10Attributes.getHostDisease().getValue());
//
//    List<BioSampleAttribute> bioSampleAttributes = bioSample.getBioSampleAttribute();
//    for (BioSampleAttribute bioSampleAttribute : bioSampleAttributes) {
//      System.out.println("Attribute Name: " + bioSampleAttribute.getAttributeName().getValue());
//      System.out.println("Attribute Value: " + bioSampleAttribute.getAttributeValue().getValue());
//    }

    // XML

    TypeSubmission submission = objectFactory.createTypeSubmission();
    // schema_version="2.0"
    // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    // xsi:noNamespaceSchemaLocation="http://www.ncbi.nlm.nih.gov/viewvc/v1/trunk/submit/public-docs/common/submission.xsd?view=co"

    // Submission/Description
    TypeSubmission.Description description = objectFactory.createTypeSubmissionDescription();
    submission.setDescription(description);
    description.setComment("a comment");

    // Submission/Description/Hold
    TypeSubmission.Description.Hold hold = objectFactory.createTypeSubmissionDescriptionHold();
    description.setHold(hold);
    hold.setReleaseDate(createXMLGregorianCalendar("1999-01-01"));

    // Submission/Description/Organization
    TypeOrganization organization = objectFactory.createTypeOrganization();
    description.getOrganization().add(organization);
    organization.setRole("a role");
    organization.setType("a type");

    // Submission/Description/Organization/Name
    TypeOrganization.Name organizationName = objectFactory.createTypeOrganizationName();
    organization.setName(organizationName);
    organizationName.setValue("org name");

    // Submission/Description/Organization/ContactInfo
    TypeContactInfo contactInfo = objectFactory.createTypeContactInfo();
    organization.getContact().add(contactInfo);
    contactInfo.setEmail("email");

    // Submission/Description/Organization/ContactInfo/Name
    TypeName name = objectFactory.createTypeName();
    contactInfo.setName(name);
    name.setFirst("First");
    name.setMiddle("Middle");
    name.setLast("Last");

    // Submission/Action
    TypeSubmission.Action action = objectFactory.createTypeSubmissionAction();
    submission.getAction().add(action);

    // Submission/Action/AddData
    TypeSubmission.Action.AddData addData = objectFactory.createTypeSubmissionActionAddData();
    action.setAddData(addData);
    addData.setTargetDb("BioSample");

    // Submission/Action/AddData/Identifier?
    //    <Identifier><SPUID spuid_namespace="Institute Name">Unique submitter identifier</SPUID></Identifier>

    // Submission/Action/AddData/Data
    TypeSubmission.Action.AddData.Data data = objectFactory.createTypeSubmissionActionAddDataData();
    addData.getData().add(data);
    data.setContentType("XML");

    // Submission/Action/AddData/Data/XMLContent
    TypeSubmission.Action.AddData.Data.XmlContent xmlContent = objectFactory.createTypeInlineDataXmlContent();
    data.setXmlContent(xmlContent);

    // Submission/Action/AddData/Data/XMLContent/BioSample
    TypeBioSample bioSample = objectFactory.createTypeBioSample();
    xmlContent.setBioSample(bioSample);
    bioSample.setSchemaVersion("2.0");
    // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    // xsi:noNamespaceSchemaLocation="http://www.ncbi.nlm.nih.gov/viewvc/v1/trunk/submit/public-docs/biosample/biosample.xsd?view=co"

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID
    TypeBioSampleIdentifier sampleID = objectFactory.createTypeBioSampleIdentifier();
    bioSample.setSampleId(sampleID);

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID/SPUID
    TypeBioSampleIdentifier.SPUID spuid = objectFactory.createTypeBioSampleIdentifierSPUID();
    sampleID.getSPUID().add(spuid);
    spuid.setSpuidNamespace("Institute Name");
    spuid.setValue("Unique submitter identifier");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor
    TypeDescriptor descriptor = spCommonObjectFactory.createTypeDescriptor();
    bioSample.setDescriptor(descriptor);
    descriptor.setTitle("a title");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor/Description
    TypeBlock descriptorDescription = spCommonObjectFactory.createTypeBlock();
     descriptor.setDescription(descriptorDescription);
    //descriptorDescription.getPOrUlOrOl().add(<some content>)

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor/ExternalLink
    TypeExternalLink externalLink = spCommonObjectFactory.createTypeExternalLink();
    descriptor.getExternalLink().add(externalLink);
    externalLink.setLabel("Displayed text for URL");
    externalLink.setURL("http://www.thisistheurl.org");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Organism
    TypeOrganism organism = spCommonObjectFactory.createTypeOrganism();
    bioSample.setOrganism(organism);
    organism.setOrganismName("Escherichia coli HVH 138 (4-6066704)");

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject
    TypeRefId bioProject = spCommonObjectFactory.createTypeRefId();
    bioSample.getBioProject().add(bioProject);

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject/PrimaryID
    TypePrimaryId bioProjectPrimaryID = spCommonObjectFactory.createTypePrimaryId();
    bioProject.setPrimaryId(bioProjectPrimaryID);
    bioProjectPrimaryID.setDb("BioProject");
    bioProjectPrimaryID.setValue("PRJNA186165");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Package
    bioSample.setPackage("Pathogen.cl.1.0");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes
    TypeBioSample.Attributes attributes = objectFactory.createTypeBioSampleAttributes();
    bioSample.setAttributes(attributes);

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes/Attribute
    TypeAttribute attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("att name");
    attribute.setValue("att value");

    JAXBElement<TypeSubmission> submissionRoot = objectFactory.createSubmission(submission);

    JAXBContext ctx = JAXBContext.newInstance(TypeSubmission.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.marshal(submissionRoot, System.out);
  }

  private static XMLGregorianCalendar createXMLGregorianCalendar(String date) throws DatatypeConfigurationException
  {
    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
    GregorianCalendar gc = new GregorianCalendar();

    return datatypeFactory.newXMLGregorianCalendar(gc);
  }
}
