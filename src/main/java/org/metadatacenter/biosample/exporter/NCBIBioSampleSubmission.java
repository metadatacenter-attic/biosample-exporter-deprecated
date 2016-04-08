package org.metadatacenter.biosample.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NCBIBioSampleSubmission
{
  private String comment = "";
  private String releaseDate = "";
  private String organizationRole = "";
  private String organizationType = "";
  private String organizationName = "";
  private String organizationContactEmailAddress = "";
  private String organizationContactFirstName = "";
  private String organizationContactMiddleInitial = "";
  private String organizationContactLastName = "";
  private final String targetDatabase = "BioSample";
  private final String bioSampleSubmissionContentType = "XML";
  private final String bioSampleSchemaVersion = "2.0";
  private final String bioSampleSchemaLocation = "http://www.ncbi.nlm.nih.gov/viewvc/v1/trunk/submit/public-docs/biosample/biosample.xsd?view=co";
  private String bioSampleSampleIDSPUIDSubmitterIdentifier = "";
  private final String bioSampleSampleIDSPUIDNamespace = "Institute Name";
  private String bioSampleDescriptorDescription = "";
  private String bioSampleDescriptorExternalLinkLabel = "";
  private String bioSampleDescriptorExternalLinkURL = "";
  private String bioSampleDescriptorTitle = "";
  private String bioSampleOrganismName = "";
  private final String bioSampleBioProjectPrimaryIDDatabase = "BioProject";
  private String bioSampleBioProjectPrimaryID = "";
  private String bioSamplePackageID = "";
  private final List<Attribute> attributes = new ArrayList<>();

  public void setBioSampleBioProjectPrimaryID(String bioSampleBioProjectPrimaryID)
  {
    this.bioSampleBioProjectPrimaryID = bioSampleBioProjectPrimaryID;
  }

  public void setBioSamplePackageID(String bioSamplePackageID)
  {
    this.bioSamplePackageID = bioSamplePackageID;
  }

  public void addAttribute(String attributeName, String attributeValue)
  {
    attributes.add(new Attribute(attributeName, attributeValue));
  }

  public List<Attribute> getAttributes()
  {
    return Collections.unmodifiableList(attributes);
  }

  public String getBioSamplePackageID()
  {
    return bioSamplePackageID;
  }

  public String getBioSampleBioProjectPrimaryID()
  {
    return bioSampleBioProjectPrimaryID;
  }

  public String getBioSampleBioProjectPrimaryIDDatabase()
  {
    return bioSampleBioProjectPrimaryIDDatabase;
  }

  public void setBioSampleSampleIDSPUIDSubmitterIdentifier(String bioSampleSampleIDSPUIDSubmitterIdentifier)
  {
    this.bioSampleSampleIDSPUIDSubmitterIdentifier = bioSampleSampleIDSPUIDSubmitterIdentifier;
  }

  public void setBioSampleDescriptorTitle(String bioSampleDescriptorTitle)
  {
    this.bioSampleDescriptorTitle = bioSampleDescriptorTitle;
  }

  public void setBioSampleDescriptorDescription(String bioSampleDescriptorDescription)
  {
    this.bioSampleDescriptorDescription = bioSampleDescriptorDescription;
  }

  public void setBioSampleDescriptorExternalLinkLabel(String bioSampleDescriptorExternalLinkLabel)
  {
    this.bioSampleDescriptorExternalLinkLabel = bioSampleDescriptorExternalLinkLabel;
  }

  public void setBioSampleDescriptorExternalLinkURL(String bioSampleDescriptorExternalLinkURL)
  {
    this.bioSampleDescriptorExternalLinkURL = bioSampleDescriptorExternalLinkURL;
  }

  public String getBioSampleOrganismName()
  {
    return bioSampleOrganismName;
  }

  public void setBioSampleOrganismName(String bioSampleOrganismName)
  {
    this.bioSampleOrganismName = bioSampleOrganismName;
  }

  public String getBioSampleDescriptorTitle()
  {
    return bioSampleDescriptorTitle;
  }

  public String getBioSampleDescriptorDescription()
  {
    return bioSampleDescriptorDescription;
  }

  public String getBioSampleDescriptorExternalLinkLabel()
  {
    return bioSampleDescriptorExternalLinkLabel;
  }

  public String getBioSampleDescriptorExternalLinkURL()
  {
    return bioSampleDescriptorExternalLinkURL;
  }

  public String getBioSampleSampleIDSPUIDNamespace()
  {
    return bioSampleSampleIDSPUIDNamespace;
  }

  public String getBioSampleSampleIDSPUIDSubmitterIdentifier()
  {
    return bioSampleSampleIDSPUIDSubmitterIdentifier;
  }

  public String getBioSampleSchemaVersion()
  {
    return bioSampleSchemaVersion;
  }

  public String getBioSampleSchemaLocation()
  {
    return bioSampleSchemaLocation;
  }

  public String getBioSampleSubmissionContentType()
  {
    return bioSampleSubmissionContentType;
  }

  public String getTargetDatabase()
  {
    return targetDatabase;
  }

  public String getOrganizationContactEmailAddress()
  {
    return organizationContactEmailAddress;
  }

  public void setOrganizationContactEmailAddress(String organizationContactEmailAddress)
  {
    this.organizationContactEmailAddress = organizationContactEmailAddress;
  }

  public String getOrganizationContactFirstName()
  {
    return organizationContactFirstName;
  }

  public void setOrganizationContactFirstName(String organizationContactFirstName)
  {
    this.organizationContactFirstName = organizationContactFirstName;
  }

  public String getOrganizationContactMiddleInitial()
  {
    return organizationContactMiddleInitial;
  }

  public void setOrganizationContactMiddleInitial(String organizationContactMiddleInitial)
  {
    this.organizationContactMiddleInitial = organizationContactMiddleInitial;
  }

  public String getOrganizationContactLastName()
  {
    return organizationContactLastName;
  }

  public void setOrganizationContactLastName(String organizationContactLastName)
  {
    this.organizationContactLastName = organizationContactLastName;
  }

  public String getOrganizationName()
  {
    return organizationName;
  }

  public void setOrganizationName(String organizationName)
  {
    this.organizationName = organizationName;
  }

  public String getOrganizationType()
  {
    return organizationType;
  }

  public void setOrganizationType(String organizationType)
  {
    this.organizationType = organizationType;
  }

  public String getOrganizationRole()
  {
    return organizationRole;
  }

  public void setOrganizationRole(String organizationRole)
  {
    this.organizationRole = organizationRole;
  }

  public String getReleaseDate()
  {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate)
  {
    this.releaseDate = releaseDate;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public static class Attribute
  {
    private final String attributeName;
    private final String attributeValue;

    public Attribute(String attributeName, String attributeValue)
    {
      this.attributeName = attributeName;
      this.attributeValue = attributeValue;
    }

    public String getAttributeName()
    {
      return attributeName;
    }

    public String getAttributeValue()
    {
      return attributeValue;
    }
  }
}
