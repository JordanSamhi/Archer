# Archer

Conditional Implicit Call aware static analyzer for Android apps.

:warning: :warning: :warning:
:loudspeaker: All artifacts are available in Zenodo, **<ins>LINK BELOW</ins>**.

## Link to the Zenodo archive

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.7568685.svg)](https://doi.org/10.5281/zenodo.7568685)



:link: Link: https://doi.org/10.5281/zenodo.7568685

## Artifacts

The `artifacts` folder contains all the artifacts, i.e., datasets, scripts, results, source code, etc., to reproduce our study.

## Getting started

### Downloading the tool

<pre>
git clone https://github.com/JordanSamhi/Archer.git
</pre>

### Installing the tool

<pre>
cd Archer
mvn clean install:install-file -Dfile=libs/soot-infoflow-android-classes.jar -DgroupId=lu.uni.trux -DartifactId=soot-infoflow-android-modified -Dversion=2.7.1 -Dpackaging=jar
mvn clean install
</pre>

OR

<pre>
cd Archer
sh build.sh
</pre>

### Issues

If you stumble upon a stack overflow error while building Archer, increase memory available with this command:

<pre>
export MAVEN_OPTS=-Xss32m
</pre>

Then, try to rebuild.

### Using the tool

<pre>
java -jar Archer/target/Archer-1.0-jar-with-dependencies.jar <i>options</i>
</pre>

Options:

* ```-a``` : The path to the APK to process.
* ```-p``` : The path to Android platofrms folder.
* ```-t``` : Perform taint analysis.
* ```-r``` : Provides raw results.
* ```-to``` : Sets a timeout (in minutes).
* ```-c``` : Sets the call graph algorithm (CHA, RTA, VTA, SPARK)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the GNU LESSER GENERAL PUBLIC LICENSE 2.1 - see the [LICENSE](LICENSE) file for details

## Contact

For any question regarding this study, please contact us at:
[Jordan Samhi](mailto:jordan.samhi@uni.lu)
