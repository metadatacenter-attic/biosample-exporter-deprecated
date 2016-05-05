CEDAR 2 BioSample Convertor
===========================

[![Build Status](https://travis-ci.org/metadatacenter/biosample-exporter.svg?branch=master)](https://travis-ci.org/metadatacenter/biosample-exporter)

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

#### Submitting Generated XML to BioSample Validator

curl -X POST -d @<Submission XML>  http://www.ncbi.nlm.nih.gov/projects/biosample/validate/

Some example submissions can be found in the ./examples directory.

#### NCBI Submission Site

Log on with Stanford institutional access.

http://www.ncbi.nlm.nih.gov/home/submit.shtml

Submissions:

https://submit.ncbi.nlm.nih.gov/subs/
