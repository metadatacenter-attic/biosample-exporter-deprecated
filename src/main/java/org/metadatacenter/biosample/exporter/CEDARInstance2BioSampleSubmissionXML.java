package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.sp.TypeDescriptor;
import common.sp.TypeOrganism;
import common.sp.TypePrimaryId;
import common.sp.TypeRefId;
import generated.BioSampleValidate;
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

public class CEDARInstance2BioSampleSubmissionXML
{
  public static void main(String[] argc) throws IOException, JAXBException, DatatypeConfigurationException
  {
    ObjectMapper mapper = new ObjectMapper();
    File submissionInstanceJSONFile = new File(CEDARInstance2BioSampleSubmissionXML.class.getClassLoader()
      .getResource("./json/AMIA2016DemoBioSampleInstance1.json").getFile());

    AMIA2016DemoBioSampleTemplate amiaBioSampleSubmission = mapper
      .readValue(submissionInstanceJSONFile, AMIA2016DemoBioSampleTemplate.class);

    generateNCBIBioSampleSubmissionXML(amiaBioSampleSubmission);

    BioSampleValidate bioSampleValidate = new BioSampleValidate();
  }

  private static void generateNCBIBioSampleSubmissionXML(AMIA2016DemoBioSampleTemplate amiaBioSampleSubmission)
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
    sampleID.getSPUID().add(spuid);
    spuid.setSpuidNamespace("CEDAR");
    spuid.setValue(amiaBioSampleSubmission.getSampleName().getValue());

    // Submission/Action/AddData/Data/XMLContent/BioSample/Descriptor
    TypeDescriptor descriptor = spCommonObjectFactory.createTypeDescriptor();
    bioSample.setDescriptor(descriptor);
    descriptor.setTitle("Example CEDAR-generated BioSample submission using the Human.1.0 package");

    // Submission/Action/AddData/Data/XMLContent/BioSample/Organism
    TypeOrganism organism = spCommonObjectFactory.createTypeOrganism();
    bioSample.setOrganism(organism);
    organism.setOrganismName(amiaBioSampleSubmission.getOrganism().getValueLabel());

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

    // Required attributes
    TypeAttribute attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("isolate");
    attribute.setValue(amiaBioSampleSubmission.getIsolate().getValue());

    attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("age");
    attribute.setValue(amiaBioSampleSubmission.getAge().getValue());

    attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("sex");
    attribute.setValue(amiaBioSampleSubmission.getSex().getValueLabel());

    attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("biomaterial provider");
    attribute.setValue(amiaBioSampleSubmission.getBiomaterialProvider().getValue());

    attribute = objectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("tissue");
    attribute.setValue(amiaBioSampleSubmission.getTissue().getValueLabel());

    for (OptionalAttribute optionalAttribute : amiaBioSampleSubmission.getOptionalAttribute()) {
      attribute = objectFactory.createTypeAttribute();
      attributes.getAttribute().add(attribute);
      attribute.setAttributeName(optionalAttribute.getName().getValue());
      attribute.setValue(optionalAttribute.getValue().getValue());
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
