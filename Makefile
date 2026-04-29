compileServer:
	javac Backend.java Frontend.java WebApp.java

startServer: compileServer
	java WebApp 8080

runAllTests: compileServer
	javac -cp ../junit5.jar:. BackendTests.java
	java -jar ../junit5.jar -cp . -c BackendTests

clean:
	rm -f *.class
