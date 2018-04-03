@echo off
wc.exe -c -w -l ../test/testCases/emptyTest.c -o ../test/testOutput/emptyTestOutput.txt
wc.exe -l -c -w ../test/testCases/helloTest.c -o ../test/testOutput/helloTestOutput.txt
wc.exe -c -w -l ../test/testCases/lparser.c -o ../test/testOutput/bigFileTestOutput.txt
wc.exe  -l -c -w -a ../test/testCases/commentInStringTest.c -o ../test/testOutput/commentInStringTestOutput.txt
wc.exe -c -w -l ../test/testCases/lparser.c -e ../test/testCases/notEmptyStopWordList.txt -o ../test/testOutput/notEmptyStopWordListTest.txt
wc.exe  -c -w -l ../test/testCases/lparser.c -e ../test/testCases/emptyStopWordList.txt -o ../test/testOutput/emptyStopWordListTest.txt
wc.exe  -c -w -l ../test/testCases/lparser.c -e ../test/testCases/iLegalStopWordListTest.txt -o ../test/testOutput/iLegalStopWordListTestOutput.txt
wc.exe  -c -w -l -s ../test/testCases/sqlite -o ../test/testOutput/recurTestOutput.txt
wc.exe  -c -w -l -s ../test/testCases/1 -o ../test/testOutput/deepRecurTestOutput.txt 