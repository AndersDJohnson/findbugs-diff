# [findbugs-diff](https://github.com/AndersDJohnson/findbugs-diff)

Report FindBugs diffs.

[![Build Status](https://travis-ci.org/AndersDJohnson/findbugs-diff.svg)](https://travis-ci.org/AndersDJohnson/findbugs-diff)

## Use

Note: Must be run on FindBugs XML reports generated with the `xml:withMessages` option,
so that `BugInstance` elements have `instanceHash` attributes, which is necessary for diffing.

### CLI

Download a JAR `findbugs-diff-${version}-all.jar` from [releases](https://github.com/AndersDJohnson/findbugs-diff/releases), e.g. [v0.1.0](https://github.com/AndersDJohnson/findbugs-diff/releases/download/v0.1.0/findbugs-diff-0.1.0-all.jar).

For CLI usage, including all available options, run:

```
java -jar findbugs-diff-0.1.0-all.jar
```

Here's a basic example:

```
java -jar findbugs-diff-0.1.0-all.jar "./path/to/1st/findbugs.xml" "./path/to/2nd/findbugs.xml"
```

## Support

* FindBugs 2, 3
* Java 1.6, 1.7

## Alternatives

* https://stackoverflow.com/questions/25442751/maven-findbugs-report-showing-difference-between-two-builds
* https://wiki.jenkins-ci.org/display/JENKINS/Static+Code+Analysis+Plug-ins#StaticCodeAnalysisPlug-ins-trend

## Other research
* https://mailman.cs.umd.edu/pipermail/findbugs-discuss/2009-January/002672.html
* https://issues.jenkins-ci.org/browse/JENKINS-2968
* http://www.sw-engineering-candies.com/blog-1/howtotransformtheresultsfromfindbugscheckstyleandpmdintoasinglehtmlreportwithxslt20andjava
* http://www.jayway.com/2013/04/09/serializing-groovy-util-slurpersupport-node-to-xml/

## Dev

Set default log level for slf4j simple:
```
-Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```
