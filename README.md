# ia-dev-tools
Tools used by the development team

### CCD Definition to JSON Tool
#### Purpose
There is a need to quickly see in readable format and compare two CCD Definition Excel files in various environments (Win, MacOS, Linux).

#### Usages

Converter
```
./gradlew ccdToJson --args="[ccd_file_path]"
```

Comparator
```
./gradlew ccdToCompare --args="[ccd_file_path_1] [ccd_file_path_2]"
```

Results are displayed on standard output.

<hr>

### PRD Wiremock Transformer
#### Purpose
There is a need to dynamically transform the Wiremock response from the Professional Reference Data endpoint used in ```ia-docker```. 
This is needed to simulate a Wiremock response for (solicitor) users from different organisations.

#### Usages

Clean the build directory containing the current jar file:
```
./gradlew clean
```

Create the fat jar file:
```
./gradlew fatJar
```

The resulting ```ia-wiremock-transformer.jar``` file is created into the ```ia-prd-wiremock-transformer/build/libs``` folder.

This jar file needs to be placed into the ```libs``` directory inside ```ia-docker```.
