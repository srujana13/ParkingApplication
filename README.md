This project has been transfered from an enterprise github therefore has missing commits. 

Team members involved: 

Ayush Khot  
Rajshree Jain  
Srujana Rachakonda  

# parking-app-dbms

### PREREQ:
1. Maven - (for dependency management) in your system. (https://maven.apache.org/install.html)
2. Java - Preferred JDK11

After installing maven, you'd need to manually add ojdbc jar to your local maven repo.
Download the ojdbc jar from Oracle website, and run the below command. - 

mvn install:install-file -Dfile=ojdbc6.jar  -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar


###Execution

Run the jar as:

java -jar parking-0.0.1-SNAPSHOT.jar

