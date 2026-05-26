package Util;

import java.io.*;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-11
 * Time: 18:54
 */
// 对读写文件的操作进行封装
// 二进制文件只能使用字节流读写，文本文件可以使用字节流或字符流读写
// 要读写的文件在 complieAndRun.Task 类中已经给出，都是文本文档，所以可以使用字符流来读写文件，可以使用字节流读写文本文件，但是要手动处理编码方式，尤其是涉及中文
public class FileOperation {
    // 1. 读文件，将文件内容以字符串的形式返回
    public static String readFile(String path){
        // 由于这个变量是局部变量，是在这个线程自己的栈中，每个线程都有自己的栈，不会访问其他进程的独立的栈，所以此处是线程安全的，不必使用 StringBuffer
        StringBuilder res = new StringBuilder();
        // 自动关闭资源
        try (FileReader fileReader = new FileReader(path)){
            while (true){
                // 一次读一个字符，以 int 的形式返回
                int b = fileReader.read();
                if(b == -1){
                    break;
                }
                res.append((char)b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    // 2. 写文件，将字符串写到文件中
    public static void writeFile(String content, String path){
        try (FileWriter fileWriter = new FileWriter(path)){
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
