package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.sp.TypeDescriptor;
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
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CEDARInstance2BioSampleSubmissionXML
{
  public static void main(String[] argc) throws IOException, JAXBException, DatatypeConfigurationException
  {
    ObjectMapper mapper = new ObjectMapper();

    mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

      File submissionInstanceJSONFile = new File(CEDARInstance2BioSampleSubmissionXML.class.getClassLoader()
      .getResource("./json/NCBIBioSampleSubmissionInstance1.json").getFile());

    NCBIBioSampleSubmissionTemplate bioSampleSubmission = mapper
      .readValue(submissionInstanceJSONFile, NCBIBioSampleSubmissionTemplate.class);

    generateNCBIBioSampleSubmissionXML(bioSampleSubmission);
  }

  private static void generateNCBIBioSampleSubmissionXML(NCBIBioSampleSubmissionTemplate bioSampleSubmission)
    throws DatatypeConfigurationException, JAXBException
  {
    generated.ObjectFactory objectFactory = new generated.ObjectFactory();
    common.sp.ObjectFactory spCommonObjectFactory = new common.sp.ObjectFactory();

    TypeSubmission xmlSubmission = objectFactory.createTypeSubmission();
    // schema_version="2.0"
    // xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    // xsi:noNamespaceSchemaLocation="http://www.ncbi.nlm.nih.gov/viewvc/v1/trunk/submit/public-docs/common/submission.xsd?view=co"

    // Submission/Description/Comment
    TypeSubmission.Description description = objectFactory.createTypeSubmissionDescription();
    xmlSubmission.setDescription(description);
    description.setComment("Example CEDAR-generated BioSample submission using the Human.1.0 package");

    // Submission/Description/Hold/releaseDate
    TypeSubmission.Description.Hold hold = objectFactory.createTypeSubmissionDescriptionHold();
    description.setHold(hold);
    hold.setReleaseDate(createXMLGregorianCalendar("2016-10-10"));

    // Submission/Description/Organization
    TypeOrganization organization = objectFactory.createTypeOrganization();
    description.getOrganization().add(organization);
    organization.setRole("master");
    organization.setType("institute");

    // Submission/Description/Organization/Name
    TypeOrganization.Name organizationName = objectFactory.createTypeOrganizationName();
    organization.setName(organizationName);
    organizationName.setValue("CEDAR");

    // Submission/Description/Organization/ContactInfo/email
    TypeContactInfo contactInfo = objectFactory.createTypeContactInfo();
    organization.getContact().add(contactInfo);
    contactInfo.setEmail("metadatacenter@gmail.com");

    // Submission/Description/Organization/ContactInfo/Name
    TypeName name = objectFactory.createTypeName();
    contactInfo.setName(name);
    name.setFirst("Mr.");
    name.setLast("CEDAR");

    // Submission/Action
    TypeSubmission.Action action = objectFactory.createTypeSubmissionAction();
    xmlSubmission.getAction().add(action);

    // Submission/Action/AddData/target_db
    TypeSubmission.Action.AddData addData = objectFactory.createTypeSubmissionActionAddData();
    action.setAddData(addData);
    addData.setTargetDb("BioSample");

    // Submission/Action/AddData/Data/content_type
    TypeSubmission.Action.AddData.Data data = objectFactory.createTypeSubmissionActionAddDataData();
    addData.getData().add(data);
    data.setContentType("XML");

    // Submission/Action/AddData/Data/XMLContent
    TypeSubmission.Action.AddData.Data.XmlContent xmlContent = objectFactory.createTypeInlineDataXmlContent();
    data.setXmlContent(xmlContent);

    // Submission/Action/AddData/Data/XMLContent/BioSample/schema_version
    TypeBioSample bioSample = objectFactory.createTypeBioSample();
    xmlContent.setBioSample(bioSample);
    bioSample.setSchemaVersion("2.0");

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID
    TypeBioSampleIdentifier sampleID = objectFactory.createTypeBioSampleIdentifier();
    bioSample.setSampleId(sampleID);

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID/SPUID
    TypeBioSampleIdentifier.SPUID spuid = objectFactory.createTypeBioSampleIdentifierSPUID();
    spuid.setSpuidNamespace("CEDAR");
    spuid.setValue("XXX"); // TODO Sample ID

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor
    TypeDescriptor descriptor = spCommonObjectFactory.createTypeDescriptor();
    bioSample.setDescriptor(descriptor);
    descriptor.setTitle("Example CEDAR-generated BioSample submission using the Human.1.0 package");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Organism
    TypeOrganism organism = spCommonObjectFactory.createTypeOrganism();
    bioSample.setOrganism(organism);
    organism.setOrganismName("XXX"); // TODO Organism

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject
    TypeRefId bioProject = spCommonObjectFactory.createTypeRefId();
    bioSample.getBioProject().add(bioProject);

    // Submission/Action/AddData/Data/XMLContent/BioSample/BioProject/PrimaryID
    TypePrimaryId bioProjectPrimaryID = spCommonObjectFactory.createTypePrimaryId();
    bioProject.setPrimaryId(bioProjectPrimaryID);
    bioProjectPrimaryID.setDb("BioProject");
    bioProjectPrimaryID.setValue("PRJNA212117");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Package
    bioSample.setPackage("Human.1.0");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes
    TypeBioSample.Attributes attributes = objectFactory.createTypeBioSampleAttributes();
    bioSample.setAttributes(attributes);

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes/Attribute
    for (NCBIBioSampleSubmission.Attribute bioSampleAttribute : new ArrayList<NCBIBioSampleSubmission.Attribute>()) { // TODO attributes
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

  private static NCBIBioSampleSubmission extractBioSampleSubmissionFromCEDARInstance(
    NCBIBioSampleSubmissionTemplate bioSampleSubmissionInstance)
  {
    NCBIBioSampleSubmission bioSampleSubmission = new NCBIBioSampleSubmission();

    NCBISubmissionDescription ncbiSubmissionDescription = bioSampleSubmissionInstance.getNCBISubmissionDescription();

    bioSampleSubmission.setComment(ncbiSubmissionDescription.getComment().getValue());
    bioSampleSubmission.setReleaseDate(ncbiSubmissionDescription.getReleaseDate().getValue());

    bioSampleSubmission.setOrganizationType("master");
    bioSampleSubmission.setOrganizationRole("institute");

    NCBIOrganization ncbiOrganization = ncbiSubmissionDescription.getNCBIOrganization();
    bioSampleSubmission.setOrganizationName(ncbiOrganization.getInstitutionName().getValue());

    NCBIContact ncbiContact = ncbiOrganization.getNCBIContact();
    bioSampleSubmission.setOrganizationContactEmailAddress(ncbiContact.getEmail().getValue());

    NCBIName ncbiName = ncbiContact.getNCBIName();
    bioSampleSubmission.setOrganizationContactFirstName(ncbiName.getFirstName().getValue());
    bioSampleSubmission.setOrganizationContactMiddleInitial(ncbiName.getMiddleInitial().getValue());
    bioSampleSubmission.setOrganizationContactLastName(ncbiName.getLastName().getValue());

    BioSample bioSample = bioSampleSubmissionInstance.getBioSample();
    bioSampleSubmission.setBioSampleBioProjectPrimaryID(bioSample.getBioProjectID().getValue());
    bioSampleSubmission.setBioSamplePackageID(bioSample.getPackage().getValue());

    BioSampleSampleID bioSampleSampleID = bioSample.getBioSampleSampleID();
    bioSampleSubmission.setBioSampleDescriptorExternalLinkLabel(bioSampleSampleID.getLabel().getValue());
    System.out.println("Display: " + bioSampleSampleID.getDisplay().getValue());
    // TODO

    NCBISPUID ncbiSPUID = bioSampleSampleID.getNCBISPUID();
    bioSampleSubmission.setBioSampleSampleIDSPUIDSubmitterIdentifier(ncbiSPUID.getSubmitterID().getValue());
    System.out.println("Namespace: " + ncbiSPUID.getNamespace().getValue());
    // TODO
    System.out.println("Value: " + ncbiSPUID.getValue().getValue());

    BioSampleDescriptor bioSampleDescriptor = bioSample.getBioSampleDescriptor();
    bioSampleSubmission.setBioSampleDescriptorTitle(bioSampleDescriptor.getTitle().getValue());
    bioSampleSubmission.setBioSampleDescriptorDescription(bioSampleDescriptor.getDescription().getValue());
    bioSampleSubmission.setBioSampleDescriptorExternalLinkURL(bioSampleDescriptor.getExternalLink().getValue());

    NCBIOrganism ncbiOrganism = bioSample.getNCBIOrganism();
    bioSampleSubmission.setBioSampleOrganismName(ncbiOrganism.getOrganismName().getValue());
    System.out.println("Label: " + ncbiOrganism.getLabel().getValue());
    System.out.println("Strain: " + ncbiOrganism.getStrain().getValue());
    System.out.println("Isolate Name: " + ncbiOrganism.getIsolateName().getValue());
    System.out.println("Breed: " + ncbiOrganism.getBreed().getValue());
    System.out.println("Cultivar: " + ncbiOrganism.getCultivar().getValue());

    BioSamplePathogenCl10Attributes bioSamplePathogenCl10Attributes = bioSample.getBioSamplePathogenCl10Attributes();
    bioSampleSubmission.addAttribute("strain", bioSamplePathogenCl10Attributes.getStrain().getValue());
    bioSampleSubmission.addAttribute("collection_date", bioSamplePathogenCl10Attributes.getCollectionDate().getValue());
    bioSampleSubmission.addAttribute("collected_by", bioSamplePathogenCl10Attributes.getCollectedBy().getValue());
    bioSampleSubmission.addAttribute("geo_loc_name", bioSamplePathogenCl10Attributes.getGEOLocationName().getValue());
    bioSampleSubmission.addAttribute("isolation_source", bioSamplePathogenCl10Attributes.getIsolationSource().getValue());
    bioSampleSubmission.addAttribute("lat_lon", bioSamplePathogenCl10Attributes.getLatitudeLongitude().getValue());
    bioSampleSubmission.addAttribute("isolate_name_alias", ncbiOrganism.getIsolateName().getValue());
    bioSampleSubmission.addAttribute("Host", bioSamplePathogenCl10Attributes.getHost().getValue());
    bioSampleSubmission.addAttribute("Host Disease", bioSamplePathogenCl10Attributes.getHostDisease().getValue());

    List<BioSampleAttribute> bioSampleAttributes = bioSample.getBioSampleAttribute();
    for (BioSampleAttribute bioSampleAttribute : bioSampleAttributes) {
      bioSampleSubmission.addAttribute(bioSampleAttribute.getAttributeName().getValue(),
        bioSampleAttribute.getAttributeValue().getValue());
    }
    return bioSampleSubmission;
  }


  private static XMLGregorianCalendar createXMLGregorianCalendar(String date) throws DatatypeConfigurationException
  {
    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
    GregorianCalendar gc = new GregorianCalendar();

    return datatypeFactory.newXMLGregorianCalendar(gc);
  }
}
