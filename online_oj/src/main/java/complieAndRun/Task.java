package complieAndRun;

import Util.FileOperation;
import java.io.File;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-10
 * Time: 21:48
 */

/*
* 编译运行结果如下：
* 如果编译错误，COMPLIEERROR_PATH 不为空，存放编译信息
* 如果运行错误或解答错误，STDOUT_PATH文件中最后是 finally;2;[2,7,11] 9;[0,0];[0,1]  分别是通过的测试用例数；最后一次输入的测试用例；实际输出结果；预期结果, 用 ; 分割
*             如果是运行错误，STDERR_PATH 不为空，放错误信息；
*             如果是编译运行成功（解答错误或解答正确），STDERR_PATH 为空
* 如果解答正确，STDOUT_PATH文件中最后是 success
* */


/**
 * 关于临时文件的处理？
 * 如果多个用户同一时间提交不同的题目或代码，会出现多个请求同时共用一个临时文件的情况
 * 比如请求1是第1题的代码，请求2是第2题的代码，两个请求同时提交，请求1先将代码写入到Solution.java文件中；请求2又将代码写入到Solution.java，然后请求1编译运行Solution.java文件，此时Solution.java中是请求2的代码了
 * 解决方法：
 * 1. 使用加锁，不太靠谱，如果用户比较多的话，需要等待前面的用户提交-得到响应，等待时间太长
 * 2. 每个请求都有自己独有的存放临时文件的目录（使用 UUID（全世界唯一的ID）命名的目录）
 * 3. 临时文件很小，也不必担心请求很多，磁盘会满了的，而且我们会定期删除一些临时文件的
 *
 * */

/**
 *  关于java项目的路径与SmartTomcat的路径
 *  1. tmp目录所在的路径是相对路径，是有一个基准的
 *  2. main()方法中执行 complieAndRun()方法，WORK_PATH是基于 java项目所在的目录的相对路径，所以临时文件放在 java项目所在的目录中
 *  3. 启动 SmartTomcat，web收到请求，执行 complieAndRun()方法，WORK_PATH是基于 SmartTomcat所在的目录的相对路径，所以临时文件放在 SmartTomcat所在的目录中
 *  4. 启动 SmartTomcat，在java项目中创建的本地文件是不会部署到 SmartTomcat 上的（我自己觉得）
 *  5. SmartTomcat所在的路径：C:\Users\86188\.SmartTomcat
 *
 * */

// 编译+运行的过程，就称为一个 complieAndRun.Task
public class Task {

    // 临时文件的作用：进程间通信； 所有的进程都可以访问到这些临时文件，所以各个进程之间可以获取信息，服务器进程接收用户提交的代码，javac 进程就通过临时文件获取到这些代码，java 就通过这些临时文件获取到字节码文件
    // 1. 某一批临时文件路径(文件夹)
    private String WORK_PATH;
    // 约定代码的类名，即 .java 文件名是 Solution.java
    private static final String CLASS_NAME = "Solution";
    // 2. 存放用户提交的代码 .java文件
    private String CODE_PATH;
    // 3. 存放编译错误信息的临时文件  TODO 即子进程编译 的标准错误内容
    private String COMPLIEERROR_PATH;
    // 4. 存放运行子进程标准输出的临时文件
    private String STDOUT_PATH;
    // 5. 存放运行子进程标准错误的临时文件
    private String STDERR_PATH;

    // 每一次编译运行代码，都生成新的临时文件
    public Task(){
        WORK_PATH = "./tmp/" + UUID.randomUUID().toString() + "/";

        // WORK_PATH = "./" + UUID.randomUUID().toString() + "/";
        CODE_PATH = WORK_PATH + CLASS_NAME + ".java";
        COMPLIEERROR_PATH = WORK_PATH + "complieErr.txt";
        STDOUT_PATH = WORK_PATH + "stdout.txt";
        STDERR_PATH = WORK_PATH + "stderr.txt";
    }

    // complieAndRun.Task 的核心方法
    // 参数：要编译运行的代码
    // 返回结果：编译运行此代码的结果
    public Answer complieAndRun(Question question){
        Answer answer = new Answer();
        // 如果没有 WORK_PATH 目录，要创建 WORK_PATH 目录
        File file = new File(WORK_PATH);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("创建临时文件夹");
        }
        // 1. 将 question 中的 code 写到一个 .java 文件中，由于类名和文件名要一致，所以所以我们约定类名是 Solution，文件名 Solution.java
        FileOperation.writeFile(question.getCode(), CODE_PATH);
        // 2. 创建子进程，调用 javac 编译 .java 文件
        //        如果编译出错，错误信息就放在标准错误中；如果编译没错，标准错误就为空. 可以用一个专门的文件（complieErr.txt）来保存编译子进程的标准错误内容
        //        不用关注编译子进程的标准输出，因此我们在 (2) 的时候，并没有指定文件存放标准输出，指定 COMPLIEERROR_PATH 存放标准错误
        // (1). 构造编译命令， " javac -encoding utf8 [.java文件路径] -d [存放.class文件的目录] "
        String complieCmd = String.format("javac -encoding utf8 %s -d %s", CODE_PATH, WORK_PATH);
        System.out.println("编译命令: " + complieCmd);
        // (2). 启动编译子进程
        CommandUtil.run(complieCmd, null, COMPLIEERROR_PATH);
        // (3). 读取 complieErr.txt 中的标准错误内容，判断是否编译错误
        String complieError = FileOperation.readFile(COMPLIEERROR_PATH);
        if(!complieError.equals("")){
            System.out.println("编译出错!!!");
            // complieError不为空，编译出错!!!
            answer.setError(1);
            answer.setReason(complieError);
            return answer;
        }
        // 编译成功，继续运行程序!!!

        // 3. 创建子进程，调用 java 运行 刚才编译好的 .class 文件
        //        运行程序时，会把 java 子进程的标准输出和标准错误放到 stdout.txt stderr.txt
        // (1). 构造运行命令，" java -classpath [.class文件所在的目录] [类名] "
        String runCmd = String.format("java -classpath %s %s", WORK_PATH, CLASS_NAME);
        System.out.println("运行命令: " + runCmd);
        // (2). 启动运行子程序，将标准输出放到 STOUT_PATH文件中，将标准错误放到 STDERR_PATH文件中
        CommandUtil.run(runCmd, STDOUT_PATH, STDERR_PATH);
        // (3). 判断运行是否出现异常
        //         运行出现异常，异常信息和调用的栈将放在标准错误中（出现在 STDERR_PATH文件中）
        // TODO    子进程要打印的信息（System.out.println("")）就放在标准输出中
        String runErr = FileOperation.readFile(STDERR_PATH);

        String stdOut = FileOperation.readFile(STDOUT_PATH);
        answer.setStdout(stdOut);

        if(!runErr.equals("")){
            System.out.println("运行异常!!!");
            // 运行出现异常!!!
            answer.setError(2);
            answer.setReason(runErr);
            return answer;
        }
        // 运行正常!!!

        // 4. 父进程获取到编译执行的结果，打包称Answer
        //        通过读这几个文件，获取到编译执行的结果
        System.out.println("编译运行成功!!!");
        answer.setError(0);
        return answer;
    }


    // 测试
    public static void main(String[] args) {
        Question question = new Question();
        question.setCode("public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello world\");\n" +
                "    }\n" +
                "}");
        Task task = new Task();
        Answer answer = task.complieAndRun(question);
        System.out.println(answer);

    }
}
