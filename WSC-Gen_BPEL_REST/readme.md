## WSC-Gen BPEL Process (REST service)  
BPEL process using the WSC-Gen REST service.
---
### current status
the BPEL process uses the generated method (`uri=http://localhost:8080/WSC-Gen/resources/restGenerator/gen`) 
and the getGeneratedFile(fileType) method (`uri=http://localhost:8080/WSC-Gen/resources/restGenerator/gen/{fileType}`)  
where the fileType is a given string by the Client

the current output in the composite project `WSC-Gen_REST_composite` is 
```
-<SOAP-ENV:Fault>
<faultcode>SOAP-ENV:Server</faultcode>
<faultstring>BPCOR-6136: Sending errors for the pending requests in the process scope since, the process instance has completed</faultstring>
<faultactor>sun-bpel-engine</faultactor>
-<detail>
<detailText>BPCOR-6136: Sending errors for the pending requests in the process scope since, the process instance has completed</detailText>
</detail>
</SOAP-ENV:Fault>
```

The reason for this output might be that the getGeneratedFile method returns DataHandler which cannot be received by the Client
