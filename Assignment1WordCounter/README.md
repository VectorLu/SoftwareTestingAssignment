# WordCounter
Software assignment 1 -- A simple word counter in Java.

# Requirement

JRE 1.8+

# Usage
预置一键测试脚本。如果是自己写的脚本测试，Windows 平台，请使用相对路径，且路径中不要含有空格。

## Windows
在 `Assignment1WordCounter/bin/` 目录下，双击运行 `windowsTest.bat` 这个脚本文件即可。对应的测试说明详见 `Assignment1WordCounter/src/testAndDetails.sh`。

## macOS or Linux or PowerShell
在 `Assignment1WordCounter/src` 目录下，直接用 `bash` 或者 `zsh` 运行脚本文件 `testAndDetails.sh`。

```
$ bash testAndDetails.sh
```


| option | description                        |                         |
| ------ | ---------------------------------- | ----------------------- |
| -c     | 统计字符数                         |                         |
| -w     | 统计单词书                         |                         |
| -l     | 统计行数                           |                         |
| -a     | 统计代码行/空行/注释行             |                         |
| -s     | 递归统计文件夹下所有符合条件的文件 | 如 *.c, *.h               |
| -o     | 指定输出文件                       |                         |
| -e     | 统计字符时忽略该文件列表中的词     | 以空格分隔 |

其中参数c、w、l、a、s与filename的顺序任意，输出顺序始终为 字符、单词、行数等。

# Details

- Assignment1WordCounter
    - bin    // 存放 Windows 平台下可运行的二进制文件
        - wc.exe
        - windowsTest.bat // 一键运行程序及测试的脚本文件（双击此文件，或用管理员身份运行此文件）
    - docs   // 核心程序 WordCounter.java 的 Javadoc
        - index.html 导航页
        - ...
    - src   // 核心程序和运行脚本
        - WordCounter.java   // 核心功能源程序
        - WordCounter.class  // 可直接运行的字节码文件（运行脚本依赖于此，勿删）
        - testAndDetails.sh  // 用来一键测试的脚本
        - jarBuild.sh        // 用来一键打包 `.jar` 文件的脚本
    - test  // 测试目录
        - testCases    // 测试用例，测试说明详见 testAndDetails.sh 源文件
        - testOutput   // 测试结果
        - testScript   // Java 测试脚本
            - unitTest    // 部分单元测试，用 `java className` 直接运行测试
                - AllFileGetterTest.java/class
                - IsFileMatchTest.java/class
                - MoreInfoTest.java/class
            
    

