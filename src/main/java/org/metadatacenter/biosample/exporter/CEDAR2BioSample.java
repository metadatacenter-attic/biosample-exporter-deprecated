package org.metadatacenter.biosample.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import generated.TypeBioSample;

import java.io.File;
import java.io.IOException;

public class CEDAR2BioSample
{
  public static void main(String[] argc) throws IOException
  {
    ObjectMapper mapper = new ObjectMapper();
    File addressJSONFile = new File(CEDAR2BioSample.class.getClassLoader().getResource("./json/address.json").getFile());

    Address address1 = mapper.readValue("{\"post-office-box\": \"232\", \"extended-address\": \"an extended\",  " +
      "\"street-address\": \"123 Main St\", \"locality\": \"a locality\", " +
      "\"region\": \"a region\", \"postal-code\": \"666\", \"country-name\": \"USA\" }", Address.class);

    Address address2 = mapper.readValue(addressJSONFile, Address.class);

    System.out.println("Address: " + address1);
    System.out.println("Address: " + address2);

    TypeBioSample bioSample;
  }
}
