# Archer

Conditional Implicit Call aware static analyzer for Android apps.

:warning: :warning: :warning:
:loudspeaker: All artifacts are available in Zenodo, **<ins>LINK BELOW</ins>**.

## Link to the Zenodo archive

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.10474310.svg)](https://doi.org/10.5281/zenodo.10474310)


:link: Link: https://doi.org/10.5281/zenodo.10474310

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

### Example

We display below an execution example of Archer on the Scylla malware, sample: 2D46A76C94DFF80992615354C713E1552A1F55F3EB0C4D5297D52571180D6402

```
Results:
 - App name: 2D46A76C94DFF80992615354C713E1552A1F55F3EB0C4D5297D52571180D6402
 - Analysis elapsed time: 133
 - Instrumentation elapsed time: 31
 - Taint Analysis elapsed time: 0
 - Number of new Edges in call graph: 48
 - Number of new Nodes in call graph: 48
 - Number of Edges in call graph: 30595
 - Number of Nodes in call graph: 7017
 - Number of extra statement covered: 870
 - Proportion of extra code covered: 0.28%
 - Reached timeout: no
 - Constraints:
   - Method call "schedule" in methods <com.ironsource.mediationsdk.RewardedVideoManager: void scheduleFetchTimer()>:
    - Potential targets:
     - com.ironsource.mediationsdk.RvAuctionTigger$1.run
     - com.ironsource.mediationsdk.RvAuctionTigger$3.run
     - com.ironsource.mediationsdk.RvAuctionTigger$2.run
     - com.ironsource.mediationsdk.InterstitialSmash$2.run
     - com.ironsource.mediationsdk.ProgIsSmash$1.run
     - com.ironsource.mediationsdk.ProgRvSmash$1.run
     - com.ironsource.mediationsdk.InterstitialSmash$1.run
     - com.ironsource.mediationsdk.ProgRvManager$1.run
     - com.ironsource.mediationsdk.BannerSmash$1.run
     - com.ironsource.mediationsdk.RewardedVideoManager$1.run
     - com.ironsource.mediationsdk.RewardedVideoSmash$1.run
     - com.ironsource.mediationsdk.BannerManager$1.run
    - No constraints
 - Constraints:
   - Method call "scheduleWithFixedDelay" in methods <rx.internal.schedulers.CachedThreadScheduler$CachedWorkerPool: void <init>(java.util.concurrent.ThreadFactory,long,java.util.concurrent.TimeUnit)>:
    - Potential targets:
     - com.moat.analytics.mobile.iro.h$4.run
     - rx.internal.schedulers.CachedThreadScheduler$CachedWorkerPool$2.run
     - com.moat.analytics.mobile.iro.h$1.run
     - com.moat.analytics.mobile.vng.i$1.run
     - com.moat.analytics.mobile.vng.i$2.run
     - com.moat.analytics.mobile.tjy.q.run
     - com.moat.analytics.mobile.tjy.s.run
    - No constraints
 - Constraints:
   - Method call "schedule" in methods <com.ironsource.mediationsdk.utils.DailyCappingManager: void scheduleTimer()>:
    - Potential targets:
     - com.ironsource.mediationsdk.utils.DailyCappingManager$1.run
    - No constraints
 - Constraints:
   - Method call "schedule" in methods <okhttp3.internal.ws.RealWebSocket: boolean writeOneFrame()>:
    - Potential targets:
     - com.moat.analytics.mobile.vng.o$2.run
     - okhttp3.internal.ws.RealWebSocket$CancelRunnable.run
     - com.moat.analytics.mobile.tjy.o.run
     - com.moat.analytics.mobile.iro.k$1.run
     - bolts.Task$1.run
     - bolts.CancellationTokenSource$1.run
    - No constraints
 - Constraints:
   - Method call "schedule" in methods <com.Talentnewdev.gjxFfCOcq.KbLDfknMveZUxWuT: void startRunTimer()>:
    - Potential targets:
     - com.Talentnewdev.gjxFfCOcq.KbLDfknMveZUxWuT$2.run
    - Potential constraints:
     - Period after next execution in ms: 1000L
     - Delay of execution in ms: 1000L
 - Constraints:
   - Method call "execute" in methods <okhttp3.internal.http2.Http2Connection$ReaderRunnable: void applyAndAckSettings(okhttp3.internal.http2.Settings)>:
    - Potential targets:
     - okhttp3.internal.http2.Http2Connection$1.run
     - com.tonyodev.fetch.FetchService$1.run
     - com.unity.purchasing.googleplay.BillingServiceManager$2.run
     - okhttp3.internal.http2.Http2Connection$ReaderRunnable$3.run
     - okhttp3.internal.http2.Http2Connection$6.run
     - com.unity.purchasing.googleplay.BillingServiceManager$1$2.run
     - bolts.Task$14.run
     - bolts.Task$15.run
     - bolts.Task$4.run
     - okhttp3.internal.http2.Http2Connection$2.run
     - okhttp3.internal.http2.Http2Connection$5.run
     - okhttp3.internal.http2.Http2Connection$3.run
     - retrofit2.ExecutorCallAdapterFactory$ExecutorCallbackCall$1$2.run
     - com.tapjoy.internal.dd$1$1.run
     - okhttp3.internal.http2.Http2Connection$ReaderRunnable$1.run
     - okhttp3.internal.http2.Http2Connection$7.run
     - retrofit2.ExecutorCallAdapterFactory$ExecutorCallbackCall$1$1.run
     - okhttp3.internal.http2.Http2Connection$ReaderRunnable$2.run
     - com.unity.purchasing.googleplay.BillingServiceManager$1$1.run
     - okhttp3.internal.http2.Http2Connection$4.run
     - com.tonyodev.fetch.FetchService$2.run
    - No constraints
 - Constraints:
   - Method call "submit" in methods <com.tapjoy.internal.cf: void a(com.tapjoy.internal.ck,java.util.concurrent.ExecutorService)>:
    - Potential targets:
     - com.moat.analytics.mobile.tjy.av.run
     - com.tapjoy.internal.gw$2.run
     - com.tapjoy.internal.ch.run
     - com.tapjoy.internal.gw$1.run
    - No constraints
 - Constraints:
   - Method call "scheduleAtFixedRate" in methods <okhttp3.internal.ws.RealWebSocket: void initReaderAndWriter(java.lang.String,long,okhttp3.internal.ws.RealWebSocket$Streams)>:
    - Potential targets:
     - rx.internal.util.ObjectPool$1.run
     - rx.internal.schedulers.NewThreadWorker$1.run
     - okhttp3.internal.ws.RealWebSocket$PingRunnable.run
    - No constraints
 - Constraints:
   - Method call "submit" in methods <com.tapjoy.TapjoyCache: java.util.concurrent.Future startCachingThread(java.net.URL,java.lang.String,long)>:
    - Potential targets:
     - com.tapjoy.TapjoyCache$CacheAssetThread.call
    - No constraints
 - Constraints:
   - Method call "schedule" in methods <com.Talentnewdev.gjxFfCOcq.KbLDfknMveZUxWuT: void useJobServiceForKeepAlive()>:
    - Potential targets:
     - com.Talentnewdev.gjxFfCOcq.ScheduleService.onStartJob
    - Potential constraints:
     - Periodic: 2000L
     - Persistent: 1
     - Network type: 1
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the GNU LESSER GENERAL PUBLIC LICENSE 2.1 - see the [LICENSE](LICENSE) file for details

## Contact

For any question regarding this study, please contact us at:
[Jordan Samhi](mailto:jordan.samhi@uni.lu)
