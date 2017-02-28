mvn deploy:deploy-file \
	-DartifactId=test \
	-DgroupId=test \
	-Dversion=1 \
	-Dfile=$(pwd)/target/fwapp-test-deploy-0.0.1-SNAPSHOT.jar \
	-Durl=http://192.168.0.13:8584/launcher/maven/deploy