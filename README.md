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
