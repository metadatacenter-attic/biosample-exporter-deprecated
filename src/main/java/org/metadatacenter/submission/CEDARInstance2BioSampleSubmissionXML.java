package org.metadatacenter.submission;

import biosample.TypeAttribute;
import biosample.TypeBioSample;
import biosample.TypeBioSampleIdentifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.sp.*;
import generated.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.metadatacenter.submission.biosample.AMIA2016DemoBioSampleTemplate;
import org.metadatacenter.submission.biosample.CEDARBioSampleValidationResponse;
import org.metadatacenter.submission.biosample.OptionalAttribute;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CEDARInstance2BioSampleSubmissionXML
{
  public static void main(String[] argc) throws IOException, JAXBException, DatatypeConfigurationException
  {
    ObjectMapper mapper = new ObjectMapper();

    JAXBContext jaxbBioSampleValidateContext = JAXBContext.newInstance(BioSampleValidate.class);
    Unmarshaller jaxbBioSampleValidateUnmarshaller = jaxbBioSampleValidateContext.createUnmarshaller();

    File submissionInstanceJSONFile = new File(CEDARInstance2BioSampleSubmissionXML.class.getClassLoader()
      .getResource("./json/AMIA2016DemoBioSampleInstance1.json").getFile());

    AMIA2016DemoBioSampleTemplate amiaBioSampleSubmission = mapper
      .readValue(submissionInstanceJSONFile, AMIA2016DemoBioSampleTemplate.class);

    BioSampleValidate bioSampleValidationResponse = validateAMIABioSampleSubmission(amiaBioSampleSubmission);

    CEDARBioSampleValidationResponse s = bioSampleValidationResponse2CEDARValidationResponse(
      bioSampleValidationResponse);
  }

  //  File bioSampleValidatorResponseXMLFile = new File(
  //    CEDARInstance2BioSampleSubmissionXML.class.getClassLoader().getResource("./xml/BioSampleValidatorResponse1.xml")
  //      .getFile());
  //
  //  BioSampleValidate bioSampleValidationResponse = (BioSampleValidate)jaxbBioSampleValidateUnmarshaller
  //    .unmarshal(bioSampleValidatorResponseXMLFile);

  private static BioSampleValidate validateAMIABioSampleSubmission(
    AMIA2016DemoBioSampleTemplate amiaBioSampleSubmission)
    throws IOException, JAXBException, DatatypeConfigurationException
  {
    String url = "https://www.ncbi.nlm.nih.gov/projects/biosample/validate/";
    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost(url);

    post.setHeader("Accept", "application/xml");
    post.setHeader("Content-type", "application/xml");
    String submissionXML = generateNCBIBioSampleSubmissionXML(amiaBioSampleSubmission);
    StringEntity requestEntity = new StringEntity(submissionXML, ContentType.APPLICATION_XML);
    post.setEntity(requestEntity);

    CloseableHttpResponse response = client.execute(post);
    try {
      if (response.getStatusLine().getStatusCode() == 200) {
        HttpEntity entity = response.getEntity();
        //String xmlResponse = EntityUtils.toString(entity);
        InputStream xmlResponse = entity.getContent();
        return bioSampleXMLResponse2BioSampleValidate(xmlResponse);
      } else
        return null; // TODO
    } finally {
      response.close();
    }
  }

  private static BioSampleValidate bioSampleXMLResponse2BioSampleValidate(InputStream xmlResponse) throws JAXBException
  {
    JAXBContext jaxbBioSampleValidateContext = JAXBContext.newInstance(BioSampleValidate.class);
    Unmarshaller jaxbBioSampleValidateUnmarshaller = jaxbBioSampleValidateContext.createUnmarshaller();
    BioSampleValidate bioSampleValidate = (BioSampleValidate)jaxbBioSampleValidateUnmarshaller.unmarshal(xmlResponse);
    return bioSampleValidate;
  }

  private static CEDARBioSampleValidationResponse bioSampleValidationResponse2CEDARValidationResponse(
    BioSampleValidate bioSampleValidationResponse)
  {
    CEDARBioSampleValidationResponse cedarValidationResponse = new CEDARBioSampleValidationResponse();
    List<String> messages = new ArrayList<>();
    cedarValidationResponse.setMessages(messages);

    List<TypeActionStatus> actionStatuses = bioSampleValidationResponse.getAction();
    if (!actionStatuses.isEmpty()) {
      TypeActionStatus actionStatus = actionStatuses.get(0);
      TypeStatus status = actionStatus.getStatus();
      if (status == TypeStatus.PROCESSED_OK)
        cedarValidationResponse.setIsValid(true);
      else
        cedarValidationResponse.setIsValid(false);

      // Even if validation passes there can be warning messages
      List<TypeActionStatus.Response> statusResponses = actionStatus.getResponse();
      for (TypeActionStatus.Response statusResponse : statusResponses) {
        String message = statusResponse.getMessage().getValue();
        messages.add(message);
      }
    }
    return cedarValidationResponse;
  }

  private static String generateNCBIBioSampleSubmissionXML(AMIA2016DemoBioSampleTemplate amiaBioSampleSubmission)
    throws DatatypeConfigurationException, JAXBException
  {
    generated.ObjectFactory objectFactory = new generated.ObjectFactory();
    biosample.ObjectFactory biosampleObjectFactory = new biosample.ObjectFactory();
    common.sp.ObjectFactory spCommonObjectFactory = new common.sp.ObjectFactory();

    Submission xmlSubmission = objectFactory.createSubmission();

    // Submission/Description/Comment
    Submission.Description description = objectFactory.createSubmissionDescription();
    xmlSubmission.setDescription(description);
    description.setComment("Example CEDAR-generated BioSample submission using the Human.1.0 package");

    // Submission/Description/Hold/releaseDate
    Submission.Description.Hold hold = objectFactory.createSubmissionDescriptionHold();
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
    TypeContactInfo contactInfo = spCommonObjectFactory.createTypeContactInfo();
    organization.getContact().add(contactInfo);
    contactInfo.setEmail("metadatacenter@gmail.com");

    // Submission/Description/Organization/ContactInfo/Name
    TypeName name =  spCommonObjectFactory.createTypeName();
    contactInfo.setName(name);
    name.setFirst("Mr.");
    name.setLast("CEDAR");

    // Submission/Action
    Submission.Action action = objectFactory.createSubmissionAction();
    xmlSubmission.getAction().add(action);

    // Submission/Action/AddData/target_db
    Submission.Action.AddData addData = objectFactory.createSubmissionActionAddData();
    action.setAddData(addData);
    addData.setTargetDb(TypeTargetDb.BIO_SAMPLE);

    // Submission/Action/AddData/Data/content_type
    Submission.Action.AddData.Data data = objectFactory.createSubmissionActionAddDataData();
    addData.setData(data);
    data.setContentType("XML");

    // Submission/Action/AddData/Data/XMLContent
    Submission.Action.AddData.Data.XmlContent xmlContent = objectFactory.createTypeInlineDataXmlContent();
    data.setXmlContent(xmlContent);

    // Submission/Action/AddData/Data/XMLContent/BioSample/schema_version
    TypeBioSample bioSample = biosampleObjectFactory.createTypeBioSample();
    xmlContent.setBioSample(bioSample);
    bioSample.setSchemaVersion("2.0");

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID
    TypeBioSampleIdentifier sampleID = biosampleObjectFactory.createTypeBioSampleIdentifier();
    bioSample.setSampleId(sampleID);

    // Submission/Action/AddData/Data/XMLContent/BioSample/SampleID/SPUID
    TypeBioSampleIdentifier.SPUID spuid = biosampleObjectFactory.createTypeBioSampleIdentifierSPUID();
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
    TypeBioSample.Attributes attributes = biosampleObjectFactory.createTypeBioSampleAttributes();
    bioSample.setAttributes(attributes);

    // Submission/Action/AddData/Data/XMLContent/BioSample/Attributes/Attribute

    // Required attributes
    TypeAttribute attribute = biosampleObjectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("isolate");
    attribute.setValue(amiaBioSampleSubmission.getIsolate().getValue());

    attribute = biosampleObjectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("age");
    attribute.setValue(amiaBioSampleSubmission.getAge().getValue());

    attribute = biosampleObjectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("sex");
    attribute.setValue(amiaBioSampleSubmission.getSex().getValueLabel());

    attribute = biosampleObjectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("biomaterial provider");
    attribute.setValue(amiaBioSampleSubmission.getBiomaterialProvider().getValue());

    attribute = biosampleObjectFactory.createTypeAttribute();
    attributes.getAttribute().add(attribute);
    attribute.setAttributeName("tissue");
    attribute.setValue(amiaBioSampleSubmission.getTissue().getValueLabel());

    for (OptionalAttribute optionalAttribute : amiaBioSampleSubmission.getOptionalAttribute()) {
      attribute = biosampleObjectFactory.createTypeAttribute();
      attributes.getAttribute().add(attribute);
      attribute.setAttributeName(optionalAttribute.getName().getValue());
      attribute.setValue(optionalAttribute.getValue().getValue());
    }
    StringWriter writer = new StringWriter();
    JAXBContext ctx = JAXBContext.newInstance(Submission.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.marshal(xmlSubmission, System.out);
    marshaller.marshal(xmlSubmission, writer);

    return writer.toString();
  }

  private static XMLGregorianCalendar createXMLGregorianCalendar(String date) throws DatatypeConfigurationException
  {
    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
    GregorianCalendar gc = new GregorianCalendar();

    return datatypeFactory.newXMLGregorianCalendar(gc);
  }
}
