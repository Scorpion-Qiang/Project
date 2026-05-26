package complieAndRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-10
 * Time: 20:47
 */
// 创建子进程，用于编译或运行
public class CommandUtil {
    // 1. Runtime创建、启动子进程
    // 2. 获取子进程标准输出内容，写入到指定文件中 -stdout
    // 3. 获取子进程标准错误内容，写入到指定文件中 -stderr
    // 4. 等待子进程结束，返回子进程状态码

    public static int run(String cmd, String stdout, String stderr){
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            // 获取标准输出
            if(stdout != null){
                // TODO 子进程 process 的标准输出内容的字节输出流 读
                InputStream inputStream = process.getInputStream();
                // TODO 获取 stdout 这个文件的字节输入流 写
                FileOutputStream fileOutputStream = new FileOutputStream(stdout);

                while (true){
                    int b = inputStream.read();
                    if(b == -1){
                        break;
                    }
                    fileOutputStream.write(b);
                }

                // 一定不要忘记关掉啊！！！
                inputStream.close();
                fileOutputStream.close();
            }

            // 获取标准错误
            if(stderr != null){
                // TODO 子进程 process 的标准错误内容的字节输出流 读
                InputStream inputStream = process.getErrorStream();
                FileOutputStream fileOutputStream = new FileOutputStream(stderr);

                while (true){
                    int b = inputStream.read();
                    if(b == -1){
                        break;
                    }
                    fileOutputStream.write(b);
                }

                inputStream.close();
                fileOutputStream.close();
            }

            // 等待子进程结束，返回子进程状态码
            int exitCode = process.waitFor();
            return exitCode;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
