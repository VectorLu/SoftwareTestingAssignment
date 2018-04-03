import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The {@code WordCounter} class is a multi-purpose text file
 * counter, which can count char number, word number, line number,
 * comment line number and so on.
 * <br><br>
 *
 * Created by VectorLu on 3/19/18.
 * Email: vector_lu@foxmail.com
 * Copyright © 2018 VectorLu. All rights reserved.
 * <br>
 *
 * @author VectorLu
 * @since JDK1.7
 */

public class WordCounter {

    /** 域显示初始化，匹配 {@code .c} 和 {@code .h} 文件的正则表达式字符串 */
    private static final String COUNTABLE_FILE_REG_EX = "(\\w)+.[ch]";

    /** 匹配 .txt 文件的正则表达式字符串 （输出文件，停用词表）*/
    private static final String TXT_REG_EX = "(\\w)+.txt";

    /** 匹配只含有 {@code _A-Za-z0-9} 文件夹名的正则表达式字符串 */
    private static final String FOLDER_REG_EX = "(\\w)+";

    /**
     * 需要计算的所有文件名，支持多个文件，默认为空，而非 {@code null}。
     */
    private ArrayList<String> countFileNames = new ArrayList<>();

    /**
     * 需要计算的所有文件，默认为空，而非 {@code null}。
     */
    private ArrayList<File> countFiles = new ArrayList<>();

    /** 表示是否有命令参数 {@code -c}，即是否需要计算字符数，默认为否 */
    private boolean requestChar = false;

    /** 表示是否有命令参数 {@code -w}，即表示是否需要计算单词数，默认为否 */
    private boolean requestWord = false;

    /** 表示是否有命令参数 {@code -l}，即是否需要计算行数，默认为否 */
    private boolean requestLine = false;

    /**
     * 表示是否有命令参数 {@code -o}，即是否需要打印到指定的输出文件，默认为否
     */
    private boolean requestOut = false;

    /**
     * 指定的输出文件名
     * {@code -o} 命令参数和 outputFileName 必须成对出现
     */
    private String outputFileName = null;

    /**
     * 表示是否有命令参数 {@code -s}，默认为否
     * 是否需要递归处理目录下符合条件的文件
     */
    private boolean requestRecur = false;
    /**
     * 需要递归处理的文件夹名
     */
    private String folderPath = null;

    /**
     * 表示是否有有命令参数 {@code -a}，默认为否
     * 是否需要返回更复杂的数据（代码行 / 空行 / 注释行）
     */
    private boolean requestMore = false;

    /**
     * 表示是否有扩展功能 {@code -e}，默认为否
     * {@code -e stopList.txt} 是否需要提供文件，不计入单词统计
     * {@code -e} 命令参数和 stopList.txt 必须成对出现
     */
    private boolean requestIgnore = false;

    /**
     * 指定的停用词表名
     */
    private String stopListName = null;

    /** 停用词表 */
    private HashSet<String> stopList = null;

    // TODO: -x 图形化界面

    /**
     * 使用默认参数，保留的空构造方法
     */
    public WordCounter() {}


    public WordCounter(String[] args) {
//        System.out.println("test 1");
        this.parseCmdArgs(args);
//        System.out.println("test 2");
    }

    /**
     * 判断是否是各种单词分隔符 (eg. ',', ' ', '\t', '\n')
     * @param ch
     *        被检测的字符
     * @return {@code true} 如果 {@code ch} 是规定的分隔符, 否则返回 {@code false}
     */
    private static boolean isWordSep(char ch) {
        return (ch == ',' || ch == ' ' || ch == '\t' || ch == '\n');
    }

    /**
     * 判断是否是各种空白字符 (eg. ' ', '\t', '\n')
     * @param ch
     *        被检测的字符
     * @return {@code true} 如果 {@code ch} 是规定的空白字符, 否则返回 {@code false}
     */
    private static boolean isBlank(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\n');
    }

    /**
     * 判断文件名是否和要求的正则表达式相匹配
     *
     * @param fileName
     *        被判断的文件名
     * @param fileRegEx
     *        正则匹配格式
     *        eg. "(\\w)+.txt" *.txt, "(\\w)+.[ch]" *.c or *.h
     * @return 如果文件名和要求的正则表达式匹配，返回 {@code true} ，否则 {@code false}
     */
    private static boolean isFileMatch(String fileName, String fileRegEx) {
        // .c & .h 文件的文件名正则表达式的模式
        Pattern countablePattern = Pattern.compile(fileRegEx);
//        System.out.println(fileName);
        // 字符串为 null 或为空显然不匹配
        if (fileName == null || fileName.equals("")) {
            return false;
        }
        Matcher fileNameMacher = countablePattern.matcher(fileName);
        return fileNameMacher.matches();
    }

    /**
     * 递归遍历获取一个路径下的所有符合要求的文件，
     * 匹配 {@code .c} 和 {@code .h} 文件，
     * 匹配只有 {@code _A-Za-z0-9} 文件夹名。
     * @param pathName
     *        需要遍历的路径
     * @param fileList
     *        遍历获得的所有文件
     */
    public static void getAllFile(String pathName, ArrayList<File> fileList) {
        File tempFile = new File(pathName);
        if (!tempFile.exists()) {
            return;
        }
        else if (tempFile.isFile()
                && tempFile.getName().matches(COUNTABLE_FILE_REG_EX)) {
            fileList.add(tempFile);
            return;
        } 
        else if (tempFile.isDirectory() 
                && tempFile.getName().matches(FOLDER_REG_EX)) {
            File[] files = tempFile.listFiles();
            if (files != null) {
                for (File f : files) {
                    // 一个目录下有多个文件，在一次调用中全部处理
                    // 以免不必要的递归
                    if (tempFile.isFile()
                            && tempFile.getName().matches(COUNTABLE_FILE_REG_EX)) {
                        fileList.add(tempFile);
                        
                    }
                    else if (tempFile.isDirectory()
                            && tempFile.getName().matches(FOLDER_REG_EX)) {
                        getAllFile(f.getPath(), fileList);
                    }
                }
            }
        }
    }

    /**
     * 返回一个由给定的停用词表生成的 {@code HashSet<String>} 对象。
     * @param pathName 给定的停用词表的路径名
     * @return {@code null} 如果文件不存在或者不是 .txt 格式。{@code HashSet} 的对象，
     *         如果文件存在且合法，但是为空，返回一个空的 {@code HashSet} 对象，否则返回
     *         一个非空的 {@code HashSet} 对象。
     *
     * @since JDK1.7 使用了该版本才引进 {@code try with resources} 的语法
     *
     */
    private HashSet<String> getStopList(String pathName) {
        HashSet<String> stopList = new HashSet<>();
        File stopFile = new File(pathName);

        // 1. 如果文件不存在或者不是 .txt 格式，返回 null
        if (!stopFile.exists() || !isFileMatch(stopFile.getName(), TXT_REG_EX)) {
            return null;
        } else {
            try (Scanner sc = new Scanner(stopFile)){
                // 2.1 如果文件存在且合法，但是为空，返回一个空的 HashSet
                //     这区别于情况 1，逻辑意义不同
                // 2.2 否则返回一个非空的 HashSet
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String stopWords[] = line.split(" ");
                    for (String word : stopWords) {
                        stopList.add(word);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stopList;
        }
    }

    /**
     * 从一个字符串获取文件名
     * @param filePath 该字符串可能为一个文件路径
     * @return 如果是一个合法的文件路径字符串，会返回一个文件名/文件夹名，否则返回 {@code null}。
     */
    private static String getNameFromPath(String filePath) {
        String sysSep = "/";

        String[] tempStrings = filePath.split(sysSep);
        if (tempStrings.length > 1) {
            return tempStrings[tempStrings.length-1];
        }
        else {
            return null;
        }
    }

    /**
     * 解析命令行参数：<br><br>
     *
     * 基础功能：<br>
     *     {@code -c file.c}     返回文件 file.c 的字符数 <br>
     *     {@code -w file.c}     返回文件 file.c 的单词总数 <br>
     *     {@code -l file.c}     返回文件 file.c 的总行数 <br>
     *     {@code -o outputFile.txt}     //将结果输出到指定文件outputFile.txt <br>
     *
     * 扩展功能：<br>
     *     {@code -a} 返回更复杂的数据（代码行 / 空行 / 注释行）<br>
     *     {@code -e stopList.txt} 是否需要提供文件，不计入单词统计 <br>
     *     {@code -e} 命令参数和 stopList.txt 必须成对出现 <br>
     *     {@code -s} 递归遍历文件夹
     *
     * @param args 命令行参数数组
     */
    private void parseCmdArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            // 获取要计算的文件
            String possibleName = getNameFromPath(args[i]);
            if (possibleName != null) {
                if (isFileMatch(possibleName, COUNTABLE_FILE_REG_EX)) {
                    this.countFileNames.add(args[i]);
                }
                // 获取可能出现的文件夹名
                else if (isFileMatch(possibleName, FOLDER_REG_EX)) {
                    this.folderPath = args[i];
                }
            }

            // 基础功能
            else if (args[i].equals("-c")) {
                this.requestChar = true;
            }
            else if (args[i].equals("-w")) {
                this.requestWord = true;
            }
            else if (args[i].equals("-l")) {
                this.requestLine = true;
            }
            else if (args[i].equals("-o")) {
                i++;
//                System.out.println(i);
                String possibleOutput = getNameFromPath(args[i]);
                if (isFileMatch(possibleOutput, TXT_REG_EX)) {
//                    System.out.println("getNameFromPath() " + args[i]);
                    this.requestOut = true;
                    this.outputFileName = args[i];
                }
                else {
                    System.out.println("请指定合法的输出文件格式：*.txt");
                }
            }
            // 扩展功能
            else if (args[i].equals("-s")) {
                this.requestRecur = true;
            }
            else if (args[i].equals("-a")) {
                this.requestMore = true;
            }
            else if (args[i].equals("-e")) {
                i++;
//                System.out.println(args[i]);
                String possibleStopFileName = getNameFromPath(args[i]);
//                System.out.println(possibleStopFileName);
//                System.out.println(isFileMatch(possibleStopFileName, TXT_REG_EX));
                if (isFileMatch(possibleStopFileName, TXT_REG_EX)) {
                    this.requestIgnore = true;
                    this.stopListName = args[i];
                    this.stopList = this.getStopList(this.stopListName);
                }
                else {
                    System.out.println("-e 命令后需有合法的停用词表文件：*.txt");
                }
            }
        }

        // 解析命令行中要读取的文件/文件夹
        if (requestRecur) {
            // -s 命令参数遍历文件夹
            getAllFile(folderPath, this.countFiles);
        }
        else {
            // 读取多个普通文件
            if (this.countFileNames.isEmpty()) {
                System.out.println("无可计算的文件");
                return;
            }
            else {
                for (String fileName : this.countFileNames) {
                    this.countFiles.add(new File(fileName));
                }

            }
        }
    }

    /**
     * 返回代码总行数
     * @param countL
     *        待测的文件
     * @return 被统计文件的总行数
     */
    public long countLine(File countL) {
        long lineN = 0L;
        try (Scanner sc = new Scanner(countL)){
            if (sc != null) {
                while (sc.hasNextLine()) {
                    lineN++;
                    sc.nextLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lineN;
    }

    /**
     * 返回 代码行 / 空行 / 注释行
     * @param f
     *        待测文件
     * @return 一个长度为 3 的 {@code long} 数组，按顺序分别表示待测文件的代码行数 / 空行数 / 注释行数
     */
    public long[] countMoreInfo(File f) {
        long[] details = { 0L, 0L, 0L };
        long codeLine0 = 0L;
        long blankLine1 = 0L;
        long commentLine2 = 0L;
        boolean inComment = false;
        boolean isCodeLine = false;
        int codeCharNum = 0;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (inComment) {    // 如果该行属于多行注释
                    int i = 0;
                    for (; i < line.length() - 1; i++) {
                        if (line.substring(i, i+2).equals("*/")) {
                            inComment = false;    // 多行注释的结束
                            break;    // 跳出当前检测注释结束的 for 循环
                        }
                    }
                    if (i < (line.length()-2)) {
                        // 本行包括多于一个字符的代码算作代码行，即使本行包含一个多行注释块的结尾部分
                        codeLine0++;
                    }
                    else {commentLine2++;}
                    // 已经确定这一行是代码行还是注释行，读取下一行
                    continue;
                }

                int i = 0;
                char ch = '\0';
                while (i < line.length() && isBlank(ch)) {
                    // 消耗行首的空白字符
                    System.out.println("BlankTest");
                    i++;
                    ch = line.charAt(i);
                }
                // 如果已经读到行尾 length()-1 或者行尾前一位 length()-2，则为空行
                if (i >= (line.length() - 2)) {
                    blankLine1++;
                    // 已知为空行，跳过本次 while 循环的其余步骤，读取下一行
                    continue;
                }
                // 否则继续判断其他情况
                for (; i < line.length(); i++) {
                    ch = line.charAt(i);
                    // System.out.println(ch + " " + i);
                    // 判断注释
                    if ((codeCharNum < 1) && (!isCodeLine) && (ch == '/')) {
                        i++;
                        if (i < line.length()) {
                            ch = line.charAt(i);
                            if (ch == '/') {
                                isCodeLine = false;
                                commentLine2++;
                                // 单行注释，跳出 for 循环，读取下一行
                                break;
                            }
                            // 判断是否是多行注释
                            else if (ch == '*') {
                                inComment = true;
                                i++;
                                while ((i+1) < line.length()) {
                                    if (line.substring(i, i+2).equals("*/")) {
                                        inComment = false;    // 多行注释的结束
                                        break;    // 跳出当前检测注释结束的 while 循环
                                    }
                                    i++;
                                }
                                if (i >= (line.length()-2)) {
                                    // 本行包括多于一个字符的代码算作代码行
                                    // 照上述规定，即使本行包含一个多行注释块，
                                    // 但是还含有多于一个字符的代码，即算为代码行
                                    isCodeLine = false;
                                    commentLine2++;
                                }
                                else {codeLine0++;}
                                // 已经确定这一行是代码行还是注释行，跳出这一行字符的 for 循环
                                break;
                            }
                            else {
                                isCodeLine = true;
                                codeCharNum+=2;
                            }
                        }
                    }
                    else if (codeCharNum > 1 || isCodeLine) {
                        // 已确定是代码行，跳出这一行字符的 for 循环
                        codeLine0++;
                        break;
                    }
                    else {
                        codeCharNum++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        details[0] = codeLine0;
        details[1] = blankLine1;
        details[2] = commentLine2;
        return details;
    }

    /**
     * 返回按特定规则计算的一个文件中的单词数，支持停用词表。
     * <br>
     * 使用了 {@code try with resources} 的特性，需要 JDK1.7+ 的支持。
     * @since JDK1.7
     * @param countW
     *        用于计算的文件
     *
     * @return 一个文件中的单词数
     */
    public long countWord(File countW) {
        long wordN = 0L;
        HashSet<String> ignoreWords = null;
        if (requestIgnore && this.stopListName != null) {
            ignoreWords = this.getStopList(this.stopListName);
        }
        try(FileReader reader = new FileReader(countW)) {
            int unicodeNo = reader.read();
            char ch = (char) unicodeNo;
            while (unicodeNo != -1) {
                String word = "";
                // 去掉多余的空格或逗号
                while (unicodeNo != -1 && isWordSep(ch)) {
                    unicodeNo = reader.read();
                    ch = (char) unicodeNo;
                }
                while (unicodeNo != -1 && !isWordSep(ch)) {
                    word += String.valueOf(ch);
                    unicodeNo = reader.read();
                    ch = (char) unicodeNo;
                }
                if (this.requestIgnore && ignoreWords != null &&
                        !ignoreWords.isEmpty() && ignoreWords.contains(word)) {
                    ;  // do nothing
                } else {
                    wordN++;
                }
                if (unicodeNo == -1) {
                    break;
                }
                unicodeNo = reader.read();
                ch = (char) unicodeNo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordN;
    }

    /**
     * 返回指定文件的总字符数
     * @param countC 被测文件
     * @return 总字符数
     */
    public long countChar(File countC) {
        long charN = 0L;
        try (FileReader reader = new FileReader(countC)){
            int unicodeNo = reader.read();
            while (unicodeNo != -1) {
                charN++;
                unicodeNo = reader.read();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return charN;
    }

    /**
     * 将统计出来的信息写入指定文件。
     * <br>
     * 使用了 {@code try with resources} 的特性，需要 JDK1.7+ 的支持。
     * @since JDK1.7
     * @param outputName 指定的文件路径
     */
    public void writeOutput(String outputName) {
        ArrayList<String> fileCounters = new ArrayList<>();
        for (File file : this.countFiles) {
            String aFileCounter = "";
            if (requestChar) {
                aFileCounter += ("字符数：" + this.countChar(file) + "\n");
            }
            if (requestWord) {
                aFileCounter += ("单词数：" + this.countWord(file) + "\n");
            }
            if (requestLine) {
                aFileCounter += ("行数：" + this.countLine(file) + "\n");
            }
            if (requestMore) {
                long[] counts = this.countMoreInfo(file);
                aFileCounter += ("代码行/空行/注释行：" + counts[0] + "/" +
                                      counts[1] + "/" + counts[2] + "\n");
            }
            fileCounters.add(aFileCounter);
        }
        try (PrintWriter output = new PrintWriter(outputName, "UTF-8")) {
            for (String str : fileCounters) {
                output.print(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        WordCounter wc = new WordCounter(args);
        if (wc.requestOut) {
            wc.writeOutput(wc.outputFileName);
        } else {
            System.out.println("请输入 -o [outputName].txt 命令参数以便查看结果");
        }
    }

}
