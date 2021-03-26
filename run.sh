javac  -classpath jars/jackson-annotations-2.9.0.jar:jars/jackson-core-2.9.8.jar:jars/jackson-databind-2.9.8.jar -sourcepath ./src -d bin src/JsonReaderRunner.java && 
java -classpath jars/jackson-annotations-2.9.0.jar:jars/jackson-core-2.9.8.jar:jars/jackson-databind-2.9.8.jar:./bin JsonReaderRunner &

