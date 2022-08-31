# Archer

Conditional Implicit Call aware static analyzer for Android apps.

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

## Data

### Algorithm 1
<img src="data/algorithm.png" alt="Transfer functions for class literals data flow analysis" width="500"/>
An edge $\langle s_0, d_0 \rangle \rightarrow \langle n, d \rangle$ means
that, according to the analysis, data flow value $d$ holds at statement
$n$ if and only if data flow value $d_0$ holds at statement $s_0$.
workList temporarily stores edges that serve to propagate data flow values.
pathEdges stores the edges from the initial node to reachable nodes in the exploded super graph.
callToBase is a set of methods taking class literals as parameter that we manually vetted which generate new dataflow values for caller objects (e.g., $a.f(c)$ would generate a new dataflow value $a \mapsto \{c\}$).
callToReceiver is a set of methods that we manually vetted which propagate dataflow values held by caller object to a potential receiver (e.g., $a = b.f()$ would propagate any dataflow value held by $b$ to $a$).


### Table 1

![Different constraints that can be set to executor classes to trigger CI calls](data/constraints.png)
Different constraints that can be set to executor classes to trigger CI calls (NS = Network Status, NT = Network Type, BL = Battery Level, CS = Charging Status, IS = Idle Status, SL = Storage Level).

## License

This project is licensed under the GNU LESSER GENERAL PUBLIC LICENSE 2.1 - see the [LICENSE](LICENSE) file for details

## Contact

For any question regarding this study, please contact us at:
[Jordan Samhi](mailto:jordan.samhi@uni.lu)
