package common.util;/**
 * @author Dantence
 * @description:
 * @date 2023/3/23
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: p2pFileSystem
 * @package: common.util
 * @className: FileScanner
 * @author: Dantence
 * @description: TODO
 * @date: 2023/3/23 13:29
 * @version: 1.0
 */
public class FileScanner {
    public static List<String> getAllFiles(String dirName) {
        List<String> files = new ArrayList<>();

        File file = new File(dirName);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                File file2 = new File(dirName + "\\" + list[i]);
                if (!file2.isDirectory()) {
                    files.add(list[i]);
                }
            }
        } else {
            System.out.println(dirName + "is not a directory");
        }
        return files;
    }
}
