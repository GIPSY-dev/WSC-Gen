1- Commenting ByzantineIntBroadcastNetworkProvider method at line 118
 because of this error: 

C:\Users\mrtnchps\Documents1\GitHub\WSC-Gen\src\org\dgpf\vm\net\byzantine\ByzantineIntBroadcastNetworkProvider.java:118: error: incompatible types: Class<ByzantineIntBroadcastNetwork> cannot be converted to Class<? extends ByzantineIntBroadcastNetwork<MT,?>>
        ((Class<? extends ByzantineIntBroadcastNetwork<MT, ?>>) (ByzantineIntBroadcastNetwork.class)),
  where MT is a type-variable:
    MT extends Serializable declared in class ByzantineIntBroadcastNetworkProvider

2-Commenting AgreementNetworkProvider method at line 90 because of this error:

error: incompatible types: Class<AgreementNetwork> cannot be converted to Class<? extends AgreementNetwork<MT,?>>
        ((Class<? extends AgreementNetwork<MT, ?>>) (AgreementNetwork.class)),
  where MT is a type-variable:
    MT extends Serializable declared in class AgreementNetworkProvider

3-Commenting CSNetworkProvider method at line 111 because of this error:

error: incompatible types: Class<CSNetwork> cannot be converted to Class<? extends CSNetwork<MT,?,?>>
    this(((Class<? extends CSNetwork<MT, ?, ?>>) (CSNetwork.class)),
  where MT is a type-variable:
    MT extends Serializable declared in class CSNetworkProvider

4- Commenting ByzantineIntBroadcastNetworkProvider method at line 136 because of this error:

error: no suitable constructor found for ByzantineIntBroadcastNetworkProvider(IVirtualMachineNetworkParameters<MT>,boolean,int,int)
    this(parameters, randomizeScenarios, scenarioCount, -1);
    constructor ByzantineIntBroadcastNetworkProvider.ByzantineIntBroadcastNetworkProvider(Class<? extends ByzantineIntBroadcastNetwork<MT,?>>,IVirtualMachineNetworkParameters<MT>,boolean,int,int) is not applicable
      (actual and formal argument lists differ in length)
    constructor ByzantineIntBroadcastNetworkProvider.ByzantineIntBroadcastNetworkProvider(IVirtualMachineNetworkParameters<MT>,boolean,int) is not applicable
      (actual and formal argument lists differ in length)
  where MT is a type-variable:
    MT extends Serializable declared in class ByzantineIntBroadcastNetworkProvider

5- Commenting NetworkProvider<?> doCreateSimulationProvider method at line 128 because of this error:

error: constructor AgreementNetworkProvider in class AgreementNetworkProvider<MT> cannot be applied to given types;
    return new AgreementNetworkProvider(PARAMETERS, randomized, sc);
  required: Class,IVirtualMachineNetworkParameters,boolean,int
  found: RBGPNetParameters,boolean,int
  reason: actual and formal argument lists differ in length
  where MT is a type-variable:
    MT extends Serializable declared in class AgreementNetworkProvider

6- Commenting NetworkProvider<?> doCreateSimulationProvider at line 148:

 error: constructor CSNetworkProvider in class CSNetworkProvider<MT> cannot be applied to given types;
    return new CSNetworkProvider(PARAMETERS, randomized, sc, BASE_CS_TIME);
  required: Class,IVirtualMachineNetworkParameters,boolean,int,int
  found: FragletNetParameters,boolean,int,int
  reason: actual and formal argument lists differ in length
  where MT is a type-variable:
    MT extends Serializable declared in class CSNetworkProvider


7-Commenting doCreateSimulationProvider at line 142:

error: constructor CSNetworkProvider in class CSNetworkProvider<MT> cannot be applied to given types;
    return new CSNetworkProvider(PARAMETERS, randomized, sc, BASE_CS_TIME);
  required: Class,IVirtualMachineNetworkParameters,boolean,int,int
  found: RBGPNetParameters,boolean,int,int
  reason: actual and formal argument lists differ in length
  where MT is a type-variable:
    MT extends Serializable declared in class CSNetworkProvider

8- Added key word abstract to AgreementERBGPTestSeries line 57
9- Added key word abstract to CSeRBGPTestSeries line56
10- Added key word abstract to CSFragletTestSeries line 57
11- Added key word abstract to CSRBGPTestSeries line 54
12- Commenting line 197 in AgreementERBGPTTestSeries
13- commented line 52 to 95 in ALLSCTestSeries
14- commented line 202 to 216 in CSeRBGPResultTest
15- commented line 181 to 185 in CSERBGPestSeries
16- commented line 68 to 86 in CSFragletResultTest
17- commented line 201 to 204 in CSFragletTestSeries
18- commented line 56 to 69 in CSRBGResultTest
19- commented line 199 to 202 in CSRBGPTestSeries
