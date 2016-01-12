# sbt-search-maven-plugin

Plugin to search maven artifacts. It uses [search.maven.org](http://search.maven.org/) for querying.

## Installation
It's not yet in Maven central, so manual build using sbt is needed. `Test-project` subproject is a perfect way to test functionality during development.

## Directory structure

* `src/sbt-test` plugin tests using [scripted](https://github.com/sbt/sbt/tree/1.0.x/scripted)
* `test-project` allows quick tests in repl of this plugin functionality (just `reload`)

## Future work

* [Filter by scala version](https://github.com/blstream/sbt-search-maven-plugin/issues/1)
* Add one of found dependencies to `build.sbt`
* Search in other sources than search.maven.org, i.e. bintray.com (where typesafe repositories are)
* Add tests
* Add continues deployment

## Contribution
You can pick a feature from roadmap, remove some TODO or fix some bug. Pull requests are very welcome.

## Thanks to all contributors of:

* [sbt](https://github.com/sbt/sbt)
* [sbt-scalariform](https://github.com/sbt/sbt-scalariform)
* [lift-json](https://github.com/lift/lift/tree/master/framework/lift-base/lift-json/)

## License
see `LICENSE` file
