#!/bin/sh
#
# Deploy to server


scp pap/target/pap.war ubuntu@cyber-hyper.com:~/tomcat/webapps
scp pdp/target/pdp.war ubuntu@cyber-hyper.com:~/tomcat/webapps
scp pep/target/pep.war ubuntu@cyber-hyper.com:~/tomcat/webapps
scp pip/target/pip.war ubuntu@cyber-hyper.com:~/tomcat/webapps
scp prp/target/prp.war ubuntu@cyber-hyper.com:~/tomcat/webapps
scp benchmark/target/resource.war ubuntu@cyber-hyper.com:~/tomcat/webapps