Thanks for considering to contribute to this project! :+1:

# About
This document shall give you some guidelines in order to help you to contribute to this project. It shall help the project contributors to work on common grounds. Contributions shall underlie as few restrictions as necessary in order to leverage productivity.

# General
* Stick to [the project's Code of Conduct](CODE_OF_CONDUCT.md)
* Don't be shy. Start to contribute *today*!

# Coding Rules
Before committing to this repository, please:

* Use automatic code formatting with the [project specific format](.idea/codeStyleSettings.xml).
* Make sure your code does not introduce warnings or errors.
* Test your contribution on the target device.
* We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/dhebbeker/memorex-android/tags). 
* Project build configuration is setup for [Android Studio](https://developer.android.com/studio/index.html) 

# Versioning
Versions in this project shall be specified according to the [Semantic Versioning](https://semver.org/) 2 specification (SemVer2). Additional rules specify details of the usage.

Tags which point to defined versions shall use the version name prefixed with a `v` as this is [common practice](https://github.com/semver/semver/issues/235#issuecomment-346477428) on GitHub.

## Development releases
A development release is defined as a release, which is only on a branch, which is not the normal release branch. A development release aims to be included within the normal release branch after development is completed.
The normal release branch is `master`.
The following rule does specify how to name versions in development releases using Backus–Naur form:

    <version>   ::= <X>"."<Y>"."<Z>"-0.develop+"<dev-id>
    <X>         ::= <X'> | <X'>+1
    <X'>        ::= mayjor version of the normal release this development is based on
    <Y>         ::= <Y'> | <Y'>+1
    <Y'>        ::= minor version of the normal release this development is based on
    <Z>         ::= <Z'> | <Z'>+1
    <Z'>        ::= patch version of the normal release this development is based on
    <dev-id>    ::= <branch>"."<commits>"."<object>
    <branch>    ::= branch name
    <commits>   ::= number of additional commits on top of the normal release this development is based on
    <object>    ::= abbreviated object name of the most recent commit

The major, minor or patch number are incremented according to SemVer2. Exactly one of major, minor or patch number must be incremented.
The branch name must satisfy the requirements for pre-release version identifiers of SemVer2. Slashes (`/`) in branch names must be replaced with hyphens (`-`).
The abbreviated object name of the most recent commit may be a tag, a revision number or a hash.

The following is a valid example of a version *tag*: `v6.6.5-0.develop+feature-update-notification.1.gf934dc7`