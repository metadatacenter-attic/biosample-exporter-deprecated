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

public class CEDAR2BioSample
{
  public static void main(String[] argc) throws IOException, JAXBException
  {
    ObjectFactory objectFactory = new ObjectFactory();
    ObjectMapper mapper = new ObjectMapper();
    File addressJSONFile = new File(CEDAR2BioSample.class.getClassLoader().getResource("./json/address.json").getFile());

    Address address2 = mapper.readValue(addressJSONFile, Address.class);

    System.out.println("Address: " + address2);

    TypeSubmission submission = objectFactory.createTypeSubmission();
    JAXBElement<TypeSubmission> submissionRoot = objectFactory.createSubmission(submission);

    JAXBContext ctx = JAXBContext.newInstance(TypeSubmission.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.marshal(submissionRoot, System.out);
  }
}
