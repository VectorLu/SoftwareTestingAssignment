import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 测试 {@code isFileMatch()} 是否正确有效。
 * <br><br>
 * Created by VectorLu on 3/19/18.
 * <br>
 * Email: vector_lu@foxmail.com
 */
public class IsFileMatchTest {

    /** 域显示初始化，匹配 {@code .c} 和 {@code .h} 文件的正则表达式字符串 */
    private static final String COUNTABLE_FILE_REG_EX = "(\\w)+.[ch]";

    /** 匹配 .txt 文件的正则表达式字符串 （输出文件，停用词表）*/
    private static final String TXT_REG_EX = "(\\w)+.txt";

    /** 匹配只含有 {@code _A-Za-z0-9} 文件夹名的正则表达式字符串 */
    private static final String FOLDER_REG_EX = "(\\w)+";

    /**
     * 白盒测试，重要的域和私有方法单独拿出来测试
     *
     * @param fileName
     *        待检测是否符合要求的文件的名字
     * @param fileRegEx
     *        正则表达式检测规则
     * @return {@code true} 如果文件名符合要求，否则 {@code false}
     */
    private static boolean isFileMath(String fileName, String fileRegEx) {
        // .c & .h 文件的文件名正则表达式的模式
        Pattern countablePattern = Pattern.compile(fileRegEx);
        // 字符串为 null 或为空显然不匹配
        if (fileName == null || fileName.equals("")) {
            return false;
        }
        Matcher fileNameMacher = countablePattern.matcher(fileName);
        return fileNameMacher.matches();
    }

    public static void main(String[] args) {
        boolean fileMatch = isFileMath("hello.c", COUNTABLE_FILE_REG_EX);
        System.out.println("fileMatch: " + fileMatch);

        boolean fileNotMatch = isFileMath("hello", COUNTABLE_FILE_REG_EX);
        System.out.println("fileNotMatch: " + fileNotMatch);

        boolean folderMatch = isFileMath("hello", FOLDER_REG_EX);
        System.out.println("folderMatch: " + folderMatch);

        boolean folderNotMatch = isFileMath("hello.txt", FOLDER_REG_EX);
        System.out.println("folderNotMatch: " + folderNotMatch);

        boolean txtMatch = isFileMath("emptyTestOutput.txt", TXT_REG_EX);
        System.out.println("txtMatch: " + txtMatch);
    }
}
