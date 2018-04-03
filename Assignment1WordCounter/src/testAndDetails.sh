# 1. 测试空集
java WordCounter -c -w -l ../test/testCases/emptyTest.c -o ../test/testOutput/emptyTestOutput.txt

# 2. 测试普通文件和命令行参数顺序
java WordCounter -l -c -w ../test/testCases/helloTest.c -o ../test/testOutput/helloTestOutput.txt

# 3. 测试大文件
java WordCounter -c -w -l ../test/testCases/lparser.c -o ../test/testOutput/bigFileTestOutput.txt


# 4 quoteTest1.c // 4 单独成行的注释测试
# 5 quoteTest2.c // 5 多行代码注释
# 6 quoteInString.c // 6 当注释符号出现在字符串中，并不认为它是注释？ 未规定，故没有这样实现
# 7 newLine.c // 7 单个空行测试
# 8 newLines.c // 8 多个空行测试
# 4.&5.&6.&7.&8. 
java WordCounter -l -c -w -a ../test/testCases/commentInStringTest.c -o ../test/testOutput/commentInStringTestOutput.txt

# 9. 难以在一台电脑上完成，与 Windows 的小伙伴们交流了一下，他们的字符数每一次换行会多 1

# 10. 测试非空停用词表
java WordCounter -c -w -l ../test/testCases/lparser.c -e ../test/testCases/notEmptyStopWordList.txt -o ../test/testOutput/notEmptyStopWordListTest.txt

# 11. 测试空停用词表
java WordCounter -c -w -l ../test/testCases/lparser.c -e ../test/testCases/emptyStopWordList.txt -o ../test/testOutput/emptyStopWordListTest.txt

# 12. 测试非法停用词表
java WordCounter -c -w -l ../test/testCases/lparser.c -e ../test/testCases/iLegalStopWordListTest.txt -o ../test/testOutput/iLegalStopWordListTestOutput.txt


# 13. 测试递归遍历文件夹，被测文件是 sqlite 的源代码
java WordCounter -c -w -l -s ../test/testCases/sqlite -o ../test/testOutput/recurTestOutput.txt

# 14. 深目录递归测试
java WordCounter -c -w -l -s ../test/testCases/1 -o ../test/testOutput/deepRecurTestOutput.txt