CEDAR 2 BioSample Converter
===========================

[![Build Status](https://travis-ci.org/metadatacenter/biosample-exporter.svg?branch=master)](https://travis-ci.org/metadatacenter/biosample-exporter)

This is an experimental CEDAR project to generate [BioSample](http://www.ncbi.nlm.nih.gov/biosample/) submissions.

This converter takes CEDAR BioSample submission instances and converts them into [BioSample](http://www.ncbi.nlm.nih.gov/biosample/) XML-based submissions.

The ```./src/main/resources/json-schema/``` directory contains a CEDAR BioSample template called 
[NCBIBioSampleSubmissionTemplate.json](https://github.com/metadatacenter/biosample-exporter/blob/develop/src/main/resources/json-schema/NCBIBioSampleSubmissionTemplate.json).
This template was generated using the CEDAR Template Designer.

The ```./src/main/resources/xsd/``` directory contains an XML Schema document describing a BioSample submission.
It is called [BioSampleSubmission.xsd](https://github.com/metadatacenter/biosample-exporter/blob/develop/src/main/resources/xsd/BioSampleSubmission.xsd). 
Two sub-schemas are defined in the files ```SP.common.xsd``` and ```biosample.xsd```.
The schema files were downloaded from the NCBI site. 

The CEDAR Metadata Editor can use the CEDAR submission template to generate a CEDAR instance of a BioSample submission. 
The ```./src/main/resources/json/``` directory contains an example instance created using this template.
It is called [NCBIBioSampleSubmissionInstance1.json](https://github.com/metadatacenter/biosample-exporter/blob/develop/src/main/resources/json/NCBIBioSampleSubmissionInstance1.json).
Other instances can be generated using the CEDAR Metadata Editor.

This converter takes these CEDAR BioSample submission instances and generates XML documents conforming to the
BioSample submission XML Schema.

These XML documents can then be validated using the [NCBI BioSample validator](http://www.ncbi.nlm.nih.gov/projects/biosample/validate/).

The following is an example ```curl``` command to submit XML to this validator:

    curl -X POST -d @<Submission XML>  http://www.ncbi.nlm.nih.gov/projects/biosample/validate/

Some example submissions can be found in the ```./examples``` directory.

Each submission requires a BioSample project identifier. Our identifier for testing is `PRJNA212117`.

An XML document is returned from the validator with the validation status.

A success could look as follows:

```
<?xml version="1.0" encoding="UTF-8"?>
<BioSampleValidate>
  <Action status="processed-ok" action_id="SUB123456-1" target_db="BioSample">
    <Response status="processed-ok">
      <Message error_code="34" severity="warning" error_source="data">Submission processing may be delayed due to necessary curator review. Please check spelling of organism, current information generated the following error message and will require a taxonomy consult: Organism not found, value 'Midi-chlorian'.</Message>
      <Object target_db="BioSample" object_id="" spuid="MIDI_ISO_9154" spuid_namespace="JEDI-MIDI"/>
    </Response>
  </Action>
</BioSampleValidate>
```

A failure could look as follows:

```
<?xml version="1.0" encoding="UTF-8"?>
<BioSampleValidate>
  <Action status="processed-error" action_id="SUB123456-1" target_db="BioSample">
    <Response status="processed-error">
      <Message error_code="62" severity="error-stop" error_source="data">Invalid BioProject accession: PRJNA212117XXXXX. Please provide a valid BioProject accession with format PRJxxxxx.</Message>
      <Object target_db="BioSample" object_id="" spuid="MIDI_ISO_9154" spuid_namespace="JEDI-MIDI"/>
    </Response>
    <Response status="processed-ok">
      <Message error_code="34" severity="warning" error_source="data">Submission processing may be delayed due to necessary curator review. Please check spelling of organism, current information generated the following error message and will require a taxonomy consult: Organism not found, value 'Midi-chlorian'.</Message>
      <Object target_db="BioSample" object_id="" spuid="MIDI_ISO_9154" spuid_namespace="JEDI-MIDI"/>
    </Response>
  </Action>
</BioSampleValidate>
```

## Notes

Information on the overall submission process can be found on the [NCBI Submission page](http://www.ncbi.nlm.nih.gov/home/submit.shtml)
and also [here](https://submit.ncbi.nlm.nih.gov/subs/) and [here](http://www.ncbi.nlm.nih.gov/biosample/docs/submission/faq/).
If needed, it is possible to log on to the system with Stanford institutional access.

A description of current BioSample attributes can be found [here](http://www.ncbi.nlm.nih.gov/biosample/docs/attributes/).
BioSample defines a set of packages that define attribute groups for certain domains.
These are described [here](http://www.ncbi.nlm.nih.gov/biosample/docs/packages/).

This converter does not have inbuilt support for packages. 
Similarly, the CEDAR BioSample submission template does not support them.
More work remains to correctly deal with packages.

## Building and Running

To build this library you must have the following items installed:

+ [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ A tool for checking out a [Git](http://git-scm.com/) repository.
+ Apache's [Maven](http://maven.apache.org/index.html).

Get a copy of the latest code:

    git clone https://github.com/metadatacenter/biosample-exporter.git

Change into the biosample-exporter directory:

    cd biosample-exporter 

Then build it with Maven:

    mvn clean install

To run:

    mvn exec:java


