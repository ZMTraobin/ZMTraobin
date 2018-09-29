

#!/bin/bash
echo "------update csp server begin------"
cd /gitRepertory/csp/
pwd
git pull origin dev
mvn clean package -Psit
cp /gitRepertory/csp/core/target/core.war /var/www/tomcat8hap/webapps/ROOT/
cd /var/www/tomcat8hap/webapps/ROOT/
jar -xf core.war
if [ `ps -ef | grep '/var/www/tomcat8hap/bin' | grep -v 'grep' | awk  '{print $2}'` != "" ]; then
 kill `ps -ef | grep '/var/www/tomcat8hap/bin' | grep -v 'grep' | awk  '{print $2}'`
else 
 echo "no tomcat8hap process"
fi
cd /var/www/tomcat8hap/bin
./startup.sh
echo "------update csp server end------"
