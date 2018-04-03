import java.io.File;
import java.util.ArrayList;


/**
 * 用递归方法获取一个目录下的所有特定文件（指定正则表达式）
 * Created by VectorLu on 3/19/18.
 * Email: vector_lu@foxmail.com
 */
public class AllFilesGetterTest {
    private static String folderPath = "../../testCases";
    private static final String targetFileRegEx = "(\\w)+.[ch]";
    // 不读隐藏文件和带空格的文件
    private static final String folderRegEx = "(\\w)+";

    public static void getAllFiles(String pathName, String targetFileRegEx,
                                   String folderRegEx, ArrayList<File> fileList) {
        File tempFile = new File(pathName);
        if (!tempFile.exists()) {
            System.out.println("The file doesn't exist.");
            return;
        }
        if (tempFile.isFile()
                && tempFile.getName().matches(targetFileRegEx)) {
            fileList.add(tempFile);
            return;
        }
        else if (tempFile.isDirectory()
                && tempFile.getName().matches(folderRegEx)) {
            File[] allFiles = tempFile.listFiles();
            if (allFiles != null) {
                for (File f : allFiles) {
                    // 一个目录下有多个文件，就在一次调用中全部处理
                    // 以避免过多的递归
                    if (tempFile.isFile()
                            && tempFile.getName().matches(targetFileRegEx)) {
                        fileList.add(tempFile);
                    }
                    else if (tempFile.isDirectory()
                            && tempFile.getName().matches(folderRegEx)){
                        getAllFiles(f.getPath(), targetFileRegEx, folderRegEx, fileList);
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        ArrayList<File> targetFiles = new ArrayList<>();
        getAllFiles(folderPath, targetFileRegEx, folderRegEx, targetFiles);
        if (!targetFiles.isEmpty()) {
            for (File targetF : targetFiles) {
                System.out.println(targetF.getName());
                System.out.println(targetF.getPath());
            }
        }
    }
}
