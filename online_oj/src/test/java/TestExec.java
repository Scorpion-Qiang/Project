import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-10
 * Time: 18:30
 */

/*
* 一个进程在启动的时候，会自动的打开三个文件
* 标准输入  对应到键盘
* 标准输出  对应到显示器
* 标准错误  对应到显示器
* 此处的文件，不是指磁盘上的文件，包括硬件设备和软件资源
* */
// 服务器进程，运行 servlet，接收用户请求，返回响应
// 用户提交的代码，也是一个独立的逻辑，要使用独立的进程执行代码
// 1. 实现并发编程，可以通过多线程编程或多进程编程
// 2. 进程具有独立性，操作系统同时运行多个进程，一个进程崩了，并不会影响其他进程
// 3. 多个线程共用一个进程的地址，一个线程崩了，可能会带走整个进程
// 4. 用户提交的代码，一定使用多进程执行（编译、运行代码），用户提交的代码五花八门，如果使用"多线程"，可能会导致该线程出问题，会影响到整个服务器进程，影响所有的用户这是不允许的
public class TestExec {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Runtime 在 JVM 中是一个单例
        Runtime runtime = Runtime.getRuntime();
        // Process就是进程
        // exec()方法表示进程启动，相当于在cmd中输入"javac", Windows会在相关的文件中查询此命令
        Process process = runtime.exec("javac");

        // javac 是一个控制台程序，它的输出都在 "输出文件" 和 "输出错误" 这两个特殊文件中，获取到这两个特殊文件的内容才能看到 javac 程序执行的效果
        // 虽然子进程启动，打开了这三个特殊的文件，但是子进程没有和IDEA终端关联，因此在IDEA看不到子进程的输出，我们需要手动获取到它的输出

        // 获取到子进程的 "标准输出" 和 "标准错误"，将它们分别写到两个文件中

        // 获取标准输出
        InputStream stdoutFrom = process.getInputStream();
        FileOutputStream stdoutTo = new FileOutputStream("stuout.txt");
        while (true){
            int b = stdoutFrom.read();
            if(b == -1){
                break;
            }
            stdoutTo.write(b);
        }
        stdoutFrom.close();
        stdoutTo.close();

        // 获取标准错误
        InputStream stderrFrom = process.getErrorStream();
        FileOutputStream stderrTo = new FileOutputStream("stderr.txt");
        while (true){
            int b = stderrFrom.read();
            if(b == -1){
                break;
            }
            stderrTo.write(b);
        }
        stderrFrom.close();
        stderrTo.close();

        // 用户提交代码，只有当代码编译、运行完毕后，父进程才能将响应结果返回给用户，所以要等待子进程结束之后，父进程才能继续运行

        // 父进程执行到这行代码，就会阻塞，一直到阻塞到子进程结束为止
        // exitCode 表示子进程的执行结果；如果子进程代码执行完了，正常退出，返回0；子进程异常退出，此时返回 非0
        int exitCode = process.waitFor();
        System.out.println(exitCode);
    }
}
