# sbt-search-maven-plugin

[![Build Status](https://travis-ci.org/blstream/sbt-search-maven-plugin.svg?branch=master)](https://travis-ci.org/blstream/sbt-search-maven-plugin)

Plugin to search maven artifacts. It uses [search.maven.org](http://search.maven.org/) for querying.

## Installation
It's not yet in Maven central, so manual build using sbt is needed. `Test-project` subproject is a perfect way to test functionality during development.

## Directory structure

* `src/sbt-test` plugin tests using [scripted](https://github.com/sbt/sbt/tree/1.0.x/scripted)
* `test-project` allows quick tests in repl of this plugin functionality (just `reload`)

## Future work

* [~~Filter by scala version~~](https://github.com/blstream/sbt-search-maven-plugin/issues/1)
    * search.maven.org let searching by keywords, but also by tags (i.e. scalaVersion), but not by both of them in one query
* Add one of found dependencies to `build.sbt`
* ~~Search in other sources than search.maven.org, i.e. bintray.com (where typesafe repositories are)~~
    * Bintray allows searching only logged in users (and without limits only to users that paid for that account)
* ~~Add tests~~
* ~~Add continues deployment~~
* Add plugin to maven central

## Contribution
You can pick feature from future work section of this readme, issue with feature proposal (if any) or fix some bug. Pull requests are very welcome.

## Thanks to all contributors of:

* [sbt](https://github.com/sbt/sbt)
* [sbt-scalariform](https://github.com/sbt/sbt-scalariform)
* [lift-json](https://github.com/lift/lift/tree/master/framework/lift-base/lift-json/)
* [specs2](https://github.com/etorreborre/specs2)

## License
see `LICENSE` file
