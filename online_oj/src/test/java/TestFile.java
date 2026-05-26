import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-10
 * Time: 16:52
 */
public class TestFile {

    // 1. 字节流读写
    public static void mainI(String[] args) throws IOException {
        // 将一个文件中内容读取出来，写到另一个文件中
        String srcPath = "d:/test1.txt";
        String destPath = "d:/test2.txt";

        // 在读文件之前一定要打开 !!!
        // 打开用于读数据的文件
        FileInputStream fileInputStream = new FileInputStream(srcPath);
        // 打开用于写数据的文件
        FileOutputStream fileOutputStream = new FileOutputStream(destPath);

        while (true) {
            // 1. 读操作，一次读一个字节
            // 为什么使用的是 int 类型来接受一个字节
            // 1. Java中的数据类型都是有符号的，byte类型的数据范围是 -128 ~ 127，
            //    实际上在按照字节读取数据时，并不要它用于算数计算，所以返回负数的意义不大
            //    因此希望读到的是一个 "无符号的数字"， 0 => 255
            // 2. read如果读到末尾了，就返回 EOF，用 -1 表示
            // byte类型是8个bit位，-128~127中的任意一个数表示8个bit位中0和1存在的一种情况
            // int类型是32个bit为，0~255中的任意一个数表示8个bit位中0和1存在的一种情况，此时就没有负数了
            int b = fileInputStream.read();
            if (b == -1) {
                break;
            }
            // 2. 写操作，一次写入一个字节
            fileOutputStream.write(b);
        }

        // 一定要关闭文件，否则会造成 "文件资源泄露"
        // 一个进程中能够打开文件的数目是有限的，取决于操作系统内核的实现，Linux中，进程PCB存在一个属性，叫做 "文件描述符表"，这个文件描述符表的大小是有限的
        fileInputStream.close();
        fileOutputStream.close();
    }


    // 2. 使用字符流读写!!!!
    public static void mainII(String[] args) {
        String srcPath = "d:/test1.txt";
        String destPath = "d:/test2.txt";
        StringBuilder res = new StringBuilder();

        try (FileReader fileReader = new FileReader(srcPath); FileWriter fileWriter = new FileWriter(destPath)) {
            while (true) {
                // 一次读一个字符，以 int 的形式返回
                int b = fileReader.read();
                if (b == -1) {
                    break;
                }
                fileWriter.write(b);
                res.append((char) b);
            }
            System.out.println(res.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
