#!/bin/bash
# This script shall print a string to stdout that can directly be used as a tag name for versioning.

echo "INFO: Call this script like this: $0 4 3 2 alpha"

# read arguments
majorOffset=$1
minorOffset=$2
patchOffset=$3
masterPostfix=$4

gitInfo=`git describe --tags --always --match "v[[:digit:]]*"`
rawBranchName=`git symbolic-ref --short HEAD`
branchName="${rawBranchName/\//-}"

# truncate leading `v`
oldVersionString=${gitInfo:1}

# separate version information
IFS=- # delimit on _
set -f # disable the glob part
oldVersionInfo=(${oldVersionString}) # invoke the split+glob operator
IFS=

commits=${oldVersionInfo[1]}
object=${oldVersionInfo[2]}

# separate version numbers
IFS=. # delimit on _
set -f # disable the glob part
oldVersionNumbers=(${oldVersionInfo[0]}) # invoke the split+glob operator
IFS=

major=$((${oldVersionNumbers[0]}+$majorOffset))
minor=$((${oldVersionNumbers[1]}+$minorOffset))
patch=$((${oldVersionNumbers[2]}+$patchOffset))

if [ "$branchName" == "master" ]; then
	if [ "$masterPostfix" == "" ]; then
		postfix=""
	else
		postfix="-$masterPostfix"
	fi
else
	postfix="-0.develop+$branchName.$commits.$object"
fi

version="$major.$minor.$patch$postfix"

# Tags which point to defined versions shall use the version name prefixed with a `v` as this is [common practice](https://github.com/semver/semver/issues/235#issuecomment-346477428) on GitHub.
tag="v$version"
echo ${tag}
