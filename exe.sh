#compile java Framework
export CLASSPATH=/home/dylan/Documents/servlet.jar
javac -d ./Framework/bin/ ./Framework/src/*.java

#creer jar
cd Framework/bin/
jar -cf ./framework.jar ./

#copier jar dans Web-inf/lib
cp ./framework.jar ../../testFrameWork/WEB-INF/lib
cd ../../

#compile java testFramework
javac -d ./testFrameWork/WEB-INF/classes -cp ./testFrameWork/WEB-INF/lib/framework.jar ./testFrameWork/WEB-INF/src/*.java

#preparation repository
cp -R ./testFrameWork/WEB-INF ./test_fw_tomcat/
cp -R ./testFrameWork/*.jsp ./test_fw_tomcat/
rm -R ./test_fw_tomcat/WEB-INF/src

#creer war
cd test_fw_tomcat
jar -cf ./testFramework.war ./
cp testFramework.war /opt/tomcat/apache-tomcat-9.0.73/webapps/
cd ../
echo "Its all"