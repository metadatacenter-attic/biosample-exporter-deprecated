package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import generated.ObjectFactory;
import generated.TypeSubmission;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CEDAR2BioSample
{
  public static void main(String[] argc) throws IOException, JAXBException
  {
    ObjectFactory objectFactory = new ObjectFactory();
    ObjectMapper mapper = new ObjectMapper();
    File bioSampleJSONFile = new File(
      CEDAR2BioSample.class.getClassLoader().getResource("./json/BioSample1.json").getFile());
    File ncbiSubmissionDescriptionJSONFile = new File(
      CEDAR2BioSample.class.getClassLoader().getResource("./json/NCBISubmissionDescription1.json").getFile());

    BioSample bioSample = mapper.readValue(bioSampleJSONFile, BioSample.class);
    NCBISubmissionDescription ncbiSubmissionDescription = mapper
      .readValue(bioSampleJSONFile, NCBISubmissionDescription.class);

    System.out.println("Comment: " + ncbiSubmissionDescription.getComment().getValue());
    System.out.println("Release Date: " + ncbiSubmissionDescription.getReleaseDate().getValue());

    NCBIOrganization ncbiOrganization = ncbiSubmissionDescription.getNCBIOrganization();
    System.out.println("Institution Name: " + ncbiOrganization.getInstitutionName().getValue());

    NCBIContact ncbiContact = ncbiOrganization.getNCBIContact();
    System.out.println("Email: " + ncbiContact.getEmail().getValue());

    NCBIName ncbiName = ncbiContact.getNCBIName();
    System.out.println("First Name: " + ncbiName.getFirstName().getValue());
    System.out.println("Middle Initial: " + ncbiName.getMiddleInitial().getValue());
    System.out.println("Last Name: " + ncbiName.getLastName().getValue());

    System.out.println("BioProjectID: " + bioSample.getBioProjectID().getValue());
    System.out.println("Package: " + bioSample.getPackage().getValue());

    BioSampleSampleID bioSampleSampleID = bioSample.getBioSampleSampleID();
    System.out.println("Label: " + bioSampleSampleID.getLabel().getValue());
    System.out.println("Display: " + bioSampleSampleID.getDisplay().getValue());

    NCBISPUID ncbiSPUID = bioSampleSampleID.getNCBISPUID();
    System.out.println("SubmitterID: " + ncbiSPUID.getSubmitterID().getValue());
    System.out.println("Namespace: " + ncbiSPUID.getNamespace().getValue());
    System.out.println("Value: " + ncbiSPUID.getValue().getValue());

    BioSampleDescriptor bioSampleDescriptor = bioSample.getBioSampleDescriptor();
    System.out.println("Title: " + bioSampleDescriptor.getTitle().getValue());
    System.out.println("Description: " + bioSampleDescriptor.getDescription().getValue());
    System.out.println("External Link: " + bioSampleDescriptor.getExternalLink().getValue());

    NCBIOrganism ncbiOrganism = bioSample.getNCBIOrganism();
    System.out.println("Organism Name: " + ncbiOrganism.getOrganismName().getValue());
    System.out.println("Label: " + ncbiOrganism.getLabel().getValue());
    System.out.println("Strain: " + ncbiOrganism.getStrain().getValue());
    System.out.println("Isolate Name: " + ncbiOrganism.getIsolateName().getValue());
    System.out.println("Breed: " + ncbiOrganism.getBreed().getValue());
    System.out.println("Cultivar: " + ncbiOrganism.getCultivar().getValue());

    BioSamplePathogenCl10Attributes bioSamplePathogenCl10Attributes = bioSample.getBioSamplePathogenCl10Attributes();
    System.out.println("Strain: " + bioSamplePathogenCl10Attributes.getStrain().getValue());
    System.out.println("Collection Date: " + bioSamplePathogenCl10Attributes.getCollectionDate().getValue());
    System.out.println("Collected By: " + bioSamplePathogenCl10Attributes.getCollectedBy().getValue());
    System.out.println("GEO Location Name: " + bioSamplePathogenCl10Attributes.getGEOLocationName().getValue());
    System.out.println("Isolation Source: " + bioSamplePathogenCl10Attributes.getIsolationSource().getValue());
    System.out.println("Latitude/longitude: " + bioSamplePathogenCl10Attributes.getLatitudeLongitude().getValue());
    System.out.println("Host: " + bioSamplePathogenCl10Attributes.getHost().getValue());
    System.out.println("Host Disease: " + bioSamplePathogenCl10Attributes.getHostDisease().getValue());

    List<BioSampleAttribute> bioSampleAttributes = bioSample.getBioSampleAttribute();
    for (BioSampleAttribute bioSampleAttribute : bioSampleAttributes) {
      System.out.println("Attribute Name: " + bioSampleAttribute.getAttributeName().getValue());
      System.out.println("Attribute Value: " + bioSampleAttribute.getAttributeValue().getValue());
    }

    TypeSubmission submission = objectFactory.createTypeSubmission();
    JAXBElement<TypeSubmission> submissionRoot = objectFactory.createSubmission(submission);

    JAXBContext ctx = JAXBContext.newInstance(TypeSubmission.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.marshal(submissionRoot, System.out);
  }
}
