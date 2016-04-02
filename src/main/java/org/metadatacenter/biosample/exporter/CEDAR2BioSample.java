package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import generated.ObjectFactory;
import generated.TypeBioSample;

import javax.xml.bind.JAXBContext;
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

    Address address1 = mapper.readValue("{\"post-office-box\": \"232\", \"extended-address\": \"an extended\",  " +
      "\"street-address\": \"123 Main St\", \"locality\": \"a locality\", " +
      "\"region\": \"a region\", \"postal-code\": \"666\", \"country-name\": \"USA\" }", Address.class);

    Address address2 = mapper.readValue(addressJSONFile, Address.class);

    System.out.println("Address: " + address1);
    System.out.println("Address: " + address2);

    TypeBioSample bioSample = objectFactory.createTypeBioSample();

    JAXBContext ctx = JAXBContext.newInstance(TypeBioSample.class);
    Marshaller marshaller = ctx.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    marshaller.marshal(bioSample, System.out);
  }
}
