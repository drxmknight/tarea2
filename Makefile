all: clean out remoteInterfaces remoteObjects process

out:
	mkdir out
	cp -r src/* out/

# Compile the packet with the remote interfaces.
# Generates a jar with the files to add it to the rmiregistry afterwards.
remoteInterfaces:
	cd out && javac remoteInterfaces/*.java
	cd out && jar cvf remoteInterfaces.jar remoteInterfaces/*.class
	rm -rf out/remoteInterfaces

# Compile the remoteObjects packet with the jar files.
remoteObjects:
	cd out && javac -cp remoteInterfaces.jar remoteObjects/*.java
	cd out && jar cvf remoteObjects.jar remoteObjects/*.class
	rm -f out/remoteObjects/*.java

process:
	cd out && javac -cp remoteInterfaces.jar:remoteObjects.jar process/*.java
	rm -f out/process/*.java

run-rmiregistry:
	CLASSPATH=out/remoteInterfaces.jar rmiregistry

run-remoteObjects:
	cd out && java -cp .:remoteInterfaces.jar\
	    -Djava.security.policy=security.policy\
	    remoteObjects.remoteObject


id = 0
n = 1
initialDelay = 100
bearer = false

run-process:
	cd out && java -cp .:remoteInterfaces.jar:remoteObjects.jar\
	    -Djava.security.policy=security.policy\
	    process.Process 2 1 100 false

clean:
	rm -rf out

test:
	cd out && java -cp .:remoteInterfaces.jar:remoteObjects.jar\
    	    -Djava.security.policy=security.policy\
    	    process.Process 0 2 100 true &\
    cd out && java -cp .:remoteInterfaces.jar:remoteObjects.jar\
        	    -Djava.security.policy=security.policy\
        	    process.Process 1 2 100 false &\
