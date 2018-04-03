# compile .java to .class
javac WordCounter.java
# build .jar
jar cvfm ../out/WordCounter.jar WordCounter.mf *.class
# test .jar
java -jar ../out/WordCounter.jar 