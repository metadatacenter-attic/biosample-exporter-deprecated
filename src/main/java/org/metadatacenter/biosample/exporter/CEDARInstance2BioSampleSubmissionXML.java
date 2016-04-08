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
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

public class CEDARInstance2BioSampleSubmissionXML
{
  public static void main(String[] argc) throws IOException, JAXBException, DatatypeConfigurationException
  {
    ObjectMapper mapper = new ObjectMapper();

    File submissionInstanceJSONFile = new File(CEDARInstance2BioSampleSubmissionXML.class.getClassLoader()
      .getResource("./json/NCBIBioSampleSubmissionInstance1.json").getFile());

    NCBIBioSampleSubmissionTemplate bioSampleSubmissionInstance = mapper
      .readValue(submissionInstanceJSONFile, NCBIBioSampleSubmissionTemplate.class);

    NCBIBioSampleSubmission submission = extractSubmissionFromInstance(bioSampleSubmissionInstance);

    generateNCBIBioSampleSubmissionXML(submission);
  }

  private static NCBIBioSampleSubmission extractSubmissionFromInstance(
    NCBIBioSampleSubmissionTemplate bioSampleSubmissionInstance)
  {
    NCBIBioSampleSubmission submission = new NCBIBioSampleSubmission();

    NCBISubmissionDescription ncbiSubmissionDescription = bioSampleSubmissionInstance.getNCBISubmissionDescription();

    submission.setComment(ncbiSubmissionDescription.getComment().getValue());
    submission.setReleaseDate(ncbiSubmissionDescription.getReleaseDate().getValue());

    submission.setOrganizationRole("XXX role");
    submission.setOrganizationType("XXX type");

    NCBIOrganization ncbiOrganization = ncbiSubmissionDescription.getNCBIOrganization();
    submission.setOrganizationName(ncbiOrganization.getInstitutionName().getValue());

    NCBIContact ncbiContact = ncbiOrganization.getNCBIContact();
    submission.setOrganizationContactEmailAddress(ncbiContact.getEmail().getValue());

    NCBIName ncbiName = ncbiContact.getNCBIName();
    submission.setOrganizationContactFirstName(ncbiName.getFirstName().getValue());
    submission.setOrganizationContactMiddleInitial(ncbiName.getMiddleInitial().getValue());
    submission.setOrganizationContactLastName(ncbiName.getLastName().getValue());

    BioSample bioSample = bioSampleSubmissionInstance.getBioSample();
    submission.setBioSampleBioProjectPrimaryID(bioSample.getBioProjectID().getValue());
    submission.setBioSamplePackageID(bioSample.getPackage().getValue());

    BioSampleSampleID bioSampleSampleID = bioSample.getBioSampleSampleID();
    submission.setBioSampleDescriptorExternalLinkLabel(bioSampleSampleID.getLabel().getValue());
    System.out.println("Display: " + bioSampleSampleID.getDisplay().getValue());
    // TODO

    NCBISPUID ncbiSPUID = bioSampleSampleID.getNCBISPUID();
    submission.setBioSampleSampleIDSPUIDSubmitterIdentifier(ncbiSPUID.getSubmitterID().getValue());
    System.out.println("Namespace: " + ncbiSPUID.getNamespace().getValue());
    // TODO
    System.out.println("Value: " + ncbiSPUID.getValue().getValue());

    BioSampleDescriptor bioSampleDescriptor = bioSample.getBioSampleDescriptor();
    submission.setBioSampleDescriptorTitle(bioSampleDescriptor.getTitle().getValue());
    submission.setBioSampleDescriptorDescription(bioSampleDescriptor.getDescription().getValue());
    submission.setBioSampleDescriptorExternalLinkURL(bioSampleDescriptor.getExternalLink().getValue());

    NCBIOrganism ncbiOrganism = bioSample.getNCBIOrganism();
    submission.setBioSampleOrganismName(ncbiOrganism.getOrganismName().getValue());
    System.out.println("Label: " + ncbiOrganism.getLabel().getValue());
    System.out.println("Strain: " + ncbiOrganism.getStrain().getValue());
    System.out.println("Isolate Name: " + ncbiOrganism.getIsolateName().getValue());
    System.out.println("Breed: " + ncbiOrganism.getBreed().getValue());
    System.out.println("Cultivar: " + ncbiOrganism.getCultivar().getValue());

    BioSamplePathogenCl10Attributes bioSamplePathogenCl10Attributes = bioSample.getBioSamplePathogenCl10Attributes();
    submission.addAttribute("Strain", bioSamplePathogenCl10Attributes.getStrain().getValue());
    submission.addAttribute("Collection Date", bioSamplePathogenCl10Attributes.getCollectionDate().getValue());
    submission.addAttribute("Collected By", bioSamplePathogenCl10Attributes.getCollectedBy().getValue());
    submission.addAttribute("GEO Location Name", bioSamplePathogenCl10Attributes.getGEOLocationName().getValue());
    submission.addAttribute("Isolation Source", bioSamplePathogenCl10Attributes.getIsolationSource().getValue());
    submission.addAttribute("Latitude/longitude", bioSamplePathogenCl10Attributes.getLatitudeLongitude().getValue());
    submission.addAttribute("Host", bioSamplePathogenCl10Attributes.getHost().getValue());
    submission.addAttribute("Host Disease", bioSamplePathogenCl10Attributes.getHostDisease().getValue());

    List<BioSampleAttribute> bioSampleAttributes = bioSample.getBioSampleAttribute();
    for (BioSampleAttribute bioSampleAttribute : bioSampleAttributes) {
      submission.addAttribute(bioSampleAttribute.getAttributeName().getValue(),
        bioSampleAttribute.getAttributeValue().getValue());
    }
    return submission;
  }

  private static void generateNCBIBioSampleSubmissionXML(NCBIBioSampleSubmission submission)
    throws DatatypeConfigurationException, JAXBException
  {
    generated.ObjectFactory objectFactory = new generated.ObjectFactory();
    common.sp.ObjectFactory spCommonObjectFactory = new common.sp.ObjectFactory();

    TypeSubmission xmlSubmission = objectFactory.createTypeSubmission();
    // schema_version="2.0"
    // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    // xsi:noNamespaceSchemaLocation="http://www.ncbi.nlm.nih.gov/viewvc/v1/trunk/submit/public-docs/common/submission.xsd?view=co"

    // Submission/Description
    TypeSubmission.Description description = objectFactory.createTypeSubmissionDescription();
    xmlSubmission.setDescription(description);
    description.setComment(submission.getComment());

    // Submission/Description/Hold
    TypeSubmission.Description.Hold hold = objectFactory.createTypeSubmissionDescriptionHold();
    description.setHold(hold);
    hold.setReleaseDate(createXMLGregorianCalendar(submission.getReleaseDate()));

    // Submission/Description/Organization
    TypeOrganization organization = objectFactory.createTypeOrganization();
    description.getOrganization().add(organization);
    organization.setRole(submission.getOrganizationRole());
    organization.setType(submission.getOrganizationType());

    // Submission/Description/Organization/Name
    TypeOrganization.Name organizationName = objectFactory.createTypeOrganizationName();
    organization.setName(organizationName);
    organizationName.setValue(submission.getOrganizationName());

    // Submission/Description/Organization/ContactInfo
    TypeContactInfo contactInfo = objectFactory.createTypeContactInfo();
    organization.getContact().add(contactInfo);
    contactInfo.setEmail(submission.getOrganizationContactEmailAddress());

    // Submission/Description/Organization/ContactInfo/Name
    TypeName name = objectFactory.createTypeName();
    contactInfo.setName(name);
    name.setFirst(submission.getOrganizationContactFirstName());
    name.setMiddle(submission.getOrganizationContactMiddleInitial());
    name.setLast(submission.getOrganizationContactLastName());

    // Submission/Action
    TypeSubmission.Action action = objectFactory.createTypeSubmissionAction();
    xmlSubmission.getAction().add(action);

    // Submission/Action/AddData
    TypeSubmission.Action.AddData addData = objectFactory.createTypeSubmissionActionAddData();
    action.setAddData(addData);
    addData.setTargetDb(submission.getTargetDatabase());

    // Submission/Action/AddData/Identifier?
    //    <Identifier><SPUID spuid_namespace="Institute Name">Unique submitter identifier</SPUID></Identifier>

    // Submission/Action/AddData/Data
    TypeSubmission.Action.AddData.Data data = objectFactory.createTypeSubmissionActionAddDataData();
    addData.getData().add(data);
    data.setContentType(submission.getBioSampleSubmissionContentType());

    // Submission/Action/AddData/Data/XMLContent
    TypeSubmission.Action.AddData.Data.XmlContent xmlContent = objectFactory.createTypeInlineDataXmlContent();
    data.setXmlContent(xmlContent);

    // Submission/Action/AddData/Data/XMLContent/BioSample
    TypeBioSample bioSample = objectFactory.createTypeBioSample();
    xmlContent.setBioSample(bioSample);
    bioSample.setSchemaVersion(submission.getBioSampleSchemaVersion());
    // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    // xsi:noNamespaceSchemaLocation=submission.getBioSampleSchemaLocation();

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID
    TypeBioSampleIdentifier sampleID = objectFactory.createTypeBioSampleIdentifier();
    bioSample.setSampleId(sampleID);

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID/SPUID
    TypeBioSampleIdentifier.SPUID spuid = objectFactory.createTypeBioSampleIdentifierSPUID();
    sampleID.getSPUID().add(spuid);
    spuid.setSpuidNamespace(submission.getBioSampleSampleIDSPUIDNamespace());
    spuid.setValue(submission.getBioSampleSampleIDSPUIDSubmitterIdentifier());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor
    TypeDescriptor descriptor = spCommonObjectFactory.createTypeDescriptor();
    bioSample.setDescriptor(descriptor);
    descriptor.setTitle(submission.getBioSampleDescriptorTitle());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor/Description
    TypeBlock descriptorDescription = spCommonObjectFactory.createTypeBlock();
    descriptor.setDescription(descriptorDescription);
    //descriptorDescription.getPOrUlOrOl().add(submission.getBioSampleDescriptorDescription())

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor/ExternalLink
    TypeExternalLink externalLink = spCommonObjectFactory.createTypeExternalLink();
    descriptor.getExternalLink().add(externalLink);
    externalLink.setLabel(submission.getBioSampleDescriptorExternalLinkLabel());
    externalLink.setURL(submission.getBioSampleDescriptorExternalLinkURL());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Organism
    TypeOrganism organism = spCommonObjectFactory.createTypeOrganism();
    bioSample.setOrganism(organism);
    organism.setOrganismName(submission.getBioSampleOrganismName());

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject
    TypeRefId bioProject = spCommonObjectFactory.createTypeRefId();
    bioSample.getBioProject().add(bioProject);

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject/PrimaryID
    TypePrimaryId bioProjectPrimaryID = spCommonObjectFactory.createTypePrimaryId();
    bioProject.setPrimaryId(bioProjectPrimaryID);
    bioProjectPrimaryID.setDb(submission.getBioSampleBioProjectPrimaryIDDatabase());
    bioProjectPrimaryID.setValue(submission.getBioSampleBioProjectPrimaryID());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Package
    bioSample.setPackage(submission.getBioSamplePackageID());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes
    TypeBioSample.Attributes attributes = objectFactory.createTypeBioSampleAttributes();
    bioSample.setAttributes(attributes);

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes/Attribute
    for (NCBIBioSampleSubmission.Attribute bioSampleAttribute : submission.getAttributes()) {
      TypeAttribute attribute = objectFactory.createTypeAttribute();
      attributes.getAttribute().add(attribute);
      attribute.setAttributeName(bioSampleAttribute.getAttributeName());
      attribute.setValue(bioSampleAttribute.getAttributeValue());
    }

    JAXBElement<TypeSubmission> submissionRoot = objectFactory.createSubmission(xmlSubmission);

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
