CEDAR 2 BioSample Converter
===========================

[![Build Status](https://travis-ci.org/metadatacenter/biosample-exporter.svg?branch=master)](https://travis-ci.org/metadatacenter/biosample-exporter)

This converter takes a CEDAR BioSample template instance and converts it into a BioSample XML submission.

The ```./src/main/resources/json-schema/``` directory contains a CEDAR BioSample template called ```NCBIBioSampleSubmissionTemplate.json```.
This template was generated using the CEDAR template editor.

The ```./src/main/resources/xsd/``` directory contains an XML Schema document describing a BioSample submission.
It is called ```BioSampleSubmission.xsd```. 
Two sub-schemas are defined in the files ```SP.common.xsd``` and ```biosample.xsd```.

The CEDAR template editor can use this template to generate a CEDAR instance of a BioSample submission. 
The ```./src/main/resources/json/``` directory contains an example instance created using this template.
It is called ```NCBIBioSampleSubmissionInstance1.json```.

This converter takes CEDAR BioSample submission instances and generates XML documents conforming to the
BioSample submission XML Schema.

These XML documents can be validated using the [NCBI BioSample validator](http://www.ncbi.nlm.nih.gov/projects/biosample/validate/).

The following is an example ```curl``` command to submit XML to this validator:

    curl -X POST -d @<Submission XML>  http://www.ncbi.nlm.nih.gov/projects/biosample/validate/

Some example submissions can be found in the ```./examples``` directory.

Information on the overall subnmission process can be found on the [NCBI Submission page](http://www.ncbi.nlm.nih.gov/home/submit.shtml)
and also [here](https://submit.ncbi.nlm.nih.gov/subs/).
If needed, it is possible to log on to the system with Stanford institutional access.

#### Building and Running

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


