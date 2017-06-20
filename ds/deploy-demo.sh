#!/bin/sh
#
# Deploy to demo server


scp pap/target/pap.war dziegler@sunfish-test.iaik.tugraz.at:~
scp pdp/target/pdp.war dziegler@sunfish-test.iaik.tugraz.at:~
scp pep/target/pep.war dziegler@sunfish-test.iaik.tugraz.at:~
scp pip/target/pip.war dziegler@sunfish-test.iaik.tugraz.at:~
scp prp/target/prp.war dziegler@sunfish-test.iaik.tugraz.at:~
scp ri/target/ri.war  dziegler@sunfish-test.iaik.tugraz.at:~
scp demoapp/target/demo-app.war dziegler@sunfish-test.iaik.tugraz.at:~
scp pep-proxy/target/pep-proxy-jar-with-dependencies.jar dziegler@sunfish-test.iaik.tugraz.at:~/pep-proxy.jar
