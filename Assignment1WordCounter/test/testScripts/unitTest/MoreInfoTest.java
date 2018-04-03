import java.io.File;
import java.util.Scanner;
import java.io.IOException;
/**
 * 用递归方法获取一个目录下的所有特定文件（指定正则表达式）
 * Created by VectorLu on 3/19/18.
 * Email: vector_lu@foxmail.com
 */

public class MoreInfoTest {
    /**
     * 判断是否是各种空白字符 (eg. ' ', '\t', '\n')
     * @param ch
     *        被检测的字符
     * @return {@code true} 如果 {@code ch} 是规定的空白字符, 否则返回 {@code false}
     */
    private static boolean isBlank(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\n');
    }

    public long[] countMoreInfo(File f) {
        long[] details = { 0L, 0L, 0L };
        long codeLine0 = 0L;
        long blankLine1 = 0L;
        long commentLine2 = 0L;
        boolean inComment = false;
        boolean isCommentLine = false;
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
                                isCommentLine = true;
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
                                    isCommentLine = true;
                                    isCodeLine = false;
                                    commentLine2++;
                                }
                                else {codeLine0++;}
                                // 已经确定这一行是代码行还是注释行，跳出这一行字符的 for 循环
                                break;                                
                            } 
                            else {
                                isCommentLine = false;
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
    public static void main(String[] args) {
        MoreInfoTest moreInfo = new MoreInfoTest();
        String parentPath = "../../testCases";
        File helloTest = new File(parentPath, "helloTest.c");
        long[] counts = moreInfo.countMoreInfo(helloTest);
        String countDetails = "代码行/空行/注释行：" + counts[0] + "/" + counts[1] + "/" + counts[2];
        System.out.println(countDetails);
    }
}