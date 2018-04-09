2018 - Webservices application made by Jonathan Desormais @jdesorm1, Marie Gobran @mariegobran and Martine Chapuis @mchapuis

--------------------------------------------------------
          HOW TO OPEN/USE THE PROJECT IN OPENESB
--------------------------------------------------------

notes:
1-libraries needed for this project is under src/lib ( manually add dom4j-1.6.1.jar and jdom.jar )
2-Files created are located in ca/concordia/cse/gipsy/ws/soap for SOAP service and ca/concordia/cse/gipsy/ws/rest for REST service

--------------------------------------------------------
	        	SOAP service
--------------------------------------------------------
1- Run Main.java in ca.concordia.cse.gipsy.ws.soap
2- Enter a number to run the service
3- Once the files are created server side, enter the command getFileGenerated to retreive them
4- GeneratorWS.java is the generator wrapper based on the GeneratorGUI.java in de.vs.unikassel.generator.gui

--------------------------------------------------------
	        	REST service
--------------------------------------------------------

In ca.concordia.cse.gipsy.ws.rest:

1- RestGenerator.java is the generator wrapper based on the GeneratorGUI.java in de.vs.unikassel.generator.gui
2- All files are created at the same time using different threads
3- Using RestJSClient.html in `Web Pages` folder, the client can request files in the browser


------- HOW TO: Javascript REST Client for WS-Gen ------

1- Client can click `Generate Files` without entering any inputs
2- The files takes a moment to be generated
3- Once the files are generated, buttons will appear.
4- The client can click each `Download` buttons to get the files independantly from the server

