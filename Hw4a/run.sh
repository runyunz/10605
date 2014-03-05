#!/bin/sh

hadoop fs -rmr hw4a/output
time hadoop jar /usr/local/hadoop-1.0.1/contrib/streaming/hadoop-streaming-1.0.1.jar \
-input hw4a/input -output hw4a/output \
-mapper 'java -cp ./lib/nb.jar NBTrainMapper' \
-reducer 'java -cp ./lib/nb.jar NBTrainReducer' \
-file nb.jar