all: clean out token remoteInterfaces suzukiKasami process

out:
	mkdir out
	cp -r src/* out/

token:
	cd out && javac token/*.java
	cd out && jar cvf token.jar token/*.class
	rm -rf out/token

# Compile the packet with the remote interfaces.
# Generates a jar with the files to add it to the rmiregistry afterwards.
remoteInterfaces:
	cd out && javac remoteInterfaces/*.java
	cd out && jar cvf remoteInterfaces.jar remoteInterfaces/*.class
	rm -rf out/remoteInterfaces

# Compile the suzukiKasami packet with the jar files.
suzukiKasami:
	cd out && javac -cp remoteInterfaces.jar:token.jar suzukiKasami/*.java
	cd out && jar cvf suzukiKasami.jar suzukiKasami/*.class
	rm -f out/suzukiKasami/*.java

process:
	cd out && javac -cp remoteInterfaces.jar:suzukiKasami.jar process/*.java
	rm -f out/process/*.java

run-rmiregistry:
	CLASSPATH=out/remoteInterfaces.jar rmiregistry

run-suzukiKasami:
	cd out && java -cp .:remoteInterfaces.jar\
	    -Djava.security.policy=security.policy\
	    suzukiKasami.remoteObject


id = 0
n = 1
initialDelay = 100
bearer = false

run-process:
	cd out && java -cp .:remoteInterfaces.jar:suzukiKasami.jar\
	    -Djava.security.policy=security.policy\
	    process.Process 2 1 100 false

clean:
	rm -rf out

test:
	cd out && java -cp .:remoteInterfaces.jar:suzukiKasami.jar:token.jar\
    	    -Djava.security.policy=security.policy\
    	    process.Process 0 2 100 true &\
    cd out && java -cp .:remoteInterfaces.jar:suzukiKasami.jar:token.jar\
        	    -Djava.security.policy=security.policy\
        	    process.Process 1 2 100 false &\
