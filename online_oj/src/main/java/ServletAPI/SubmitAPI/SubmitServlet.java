package ServletAPI.SubmitAPI;

import MyException.ProblemNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import complieAndRun.Answer;
import complieAndRun.Question;
import complieAndRun.Task;
import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import Operation.RecordOp.Record;
import Operation.RecordOp.RecordDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-13
 * Time: 16:54
 */

/*
* 用户提交代码
* 方法: POST
* URL: /submit
* body:
*        id:  (题目id)
*        code:  (用户提交的代码)
* 返回响应:
*         ComplieResp
* 过程: 1. 编译运行
*      2. 更新用户的提交次数, 通过的次数, 通过的题目数
*      3. 新增提交记录
*
*  */
@WebServlet("/submit")
public class SubmitServlet extends HttpServlet {
    // Jackson库的核心类
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProblemDao op = new ProblemDao();
        UserDao userDao = new UserDao();
        RecordDao recordDao = new RecordDao();

        //  判断是否登录过
        User user = Util.isLogin(req);
        if(user == null){
            resp.sendRedirect("login.html");
            return;
        }


        // 临时加一下，获取 SmartTomcat 的工作目录
//        System.out.println("获取用户当前所在的目录: " + System.getProperty("user.dir"));

        resp.setContentType("application/json;charset=utf8");

        // ComplieResp 作为 web响应
        ComplieResp complieResp = new ComplieResp();
        ComplieReq complieReq = null;

        // 这里用 try catch() 就是为了处理 id 不存在的情况；当然了也可以用 if else，抛异常也可以
        try {
            // 1. ObjectMapper 类的 readValue（）方法可以读取请求 req 的 body 内容，转化为 ComplieReq 的对象
            // readVaule（）第一个参数可以是 body的流对象，也可以是body内容组成的String；第二个参数表示类对象

            // ComplieReq complieReq = objectMapper.readValue(req.getInputStream(), ComplieReq.class);
            // TODO 自行创建方法，读取body内容，将req请求中的 body 内容转换为字符串
            String body = readBody(req);
            // TODO 将body内容转化为 类ComlieReq 的对象，（类名.class表示获取类对象）
            complieReq = objectMapper.readValue(body, ComplieReq.class);



            // 2. 得到该题的测试用例代码（main() 方法）
            int problemId = complieReq.getId();
            Problem p = op.selectOne(problemId);

            // 如果 id 不存在，返回的 p 就是 null，此时抛出异常，不执行下面的代码了，交给 catch() 处理
            if (p == null) {
                throw new ProblemNotFiledException();
            }

            // TODO 测试代码
            String testCode = p.getTestcode();

            // 3. 用户提交的代码
            String code = complieReq.getCode();
            // 4. 拼接成为完整、可运行的代码
            String completeCode = splicing(testCode, code);


            // 5. 编译 + 运行
            Task task = new Task();
            Question question = new Question();
            question.setCode(completeCode);
            Answer answer = task.complieAndRun(question);



            // 编译结果
            int status = 0;       // 0--解答成功  1--编译错误  2--运行错误  3--解答错误  4--题目未找到
            String reason = null; // status == 1 || 2, 放编译运行的错误
            int passExampleCount = 0; // 通过的测试用例数
            String finalInput = null; // 最后一次输入的测试用例
            String output = null;     // 实际输出
            String preOutput = null;  // 预期输出


            if(answer.getError() == 1){
                status = 1;
                int index = answer.getReason().indexOf("java");
                reason = answer.getReason().substring(index);
            } else {
                if(answer.getError() == 0 && answer.getStdout().endsWith("success")){
                    // success
                    passExampleCount = p.getTestExample();
                } else {
                    // 处理 "finally;通过测试用例数;最后一次的输入用例;实际输出的结果;预期的结果"
                    String stdout = answer.getStdout();
                    int index = stdout.lastIndexOf("finally;");


                    String result = stdout.substring(index + 8);
                    String[] arr = result.split(";");


                    passExampleCount = Integer.parseInt(arr[0]);
                    finalInput = arr[1];

                    if(answer.getError() == 0){
                        output = arr[2];
                        preOutput = arr[3];
                        status = 3;
                    } else {
                        status = 2;
                        reason = answer.getReason();
                    }
                }
            }


            // 更新用户的提交、通过次数
            int passcount = user.getPasscount();
            int submitcount = user.getSubmitcount() + 1;
            int passproblemcount = user.getPassproblemcount();
            if(status == 0){
                passcount++;
                int recordStatus = recordDao.selectOneRe(problemId, user.getId());
                if(recordStatus != 0){
                    passproblemcount++;
                }
            }
            user.setPasscount(passcount);
            user.setSubmitcount(submitcount);
            user.setPassproblemcount(passproblemcount);
            userDao.updateII(user);

            // 新增提交记录
            Record record = new Record();
            record.setUserId(user.getId());
            record.setProblemId(p.getId());
            record.setTime(new Timestamp(System.currentTimeMillis()));
            record.setContent(code);
            record.setErrorReason(reason);
            record.setStatus(status);
            record.setPassExample(passExampleCount);
            record.setFinalInput(finalInput);

            recordDao.insert(record);

            // 6. 将编译运行的结果打包返回给前端
            complieResp.setError(status);
            complieResp.setReason(reason);
            complieResp.setPassExampleCount(passExampleCount);
            complieResp.setFinalInput(finalInput);
            complieResp.setOutput(output);
            complieResp.setPreOutput(preOutput);

        } catch (ProblemNotFiledException e) {
            complieResp.setError(4);     // error为3，表示找不到指定的题目
            complieResp.setReason("没有指定的题目, id=" + complieReq.getId());
        }


        // 将ComplieResp转变为JSON格式的字符串
        String resqString = objectMapper.writeValueAsString(complieResp);
        resqString = resqString.replaceAll("\\\\n", "<br/>");
        resp.getWriter().write(resqString);
    }

    // 拼接测试用例的代码和用户提交的代码
    private String splicing(String testCode, String code){
        // 找到最后一个 ")" 的位置，不用 code.length() - 1，是因为可能有的用户在最后一个 ")" 后面有空格或换行，code.length() - 1就不是最后一个 ")"的位置了
        int index = code.lastIndexOf('}');
        StringBuilder stringBuilder = new StringBuilder(code);
        // 即使是没有 "}"，也把 testCode 拼接上，大不了报编译异常呗
        if(index == -1){
            index++;
        }
        stringBuilder.insert(index, testCode);
        return stringBuilder.toString();
    }

    // 将请求的 body内容转为字符串
    private String readBody(HttpServletRequest req){
        // 1. 先根据请求头获取 body 的长度（单位: 字节）
        int contentLength = req.getContentLength();
        // 2. 创建字节数组
        byte[] bytes = new byte[contentLength];
        // 3. 获取 body 的字节流对象
        try(InputStream inputStream = req.getInputStream()) {
            // 4. 读取 body 的内容，放到字节数组中
            inputStream.read(bytes);
            // 5. 将字节数组转成 String
            String body = new String(bytes, "utf8");
            // 需要说明的是，此处字节数组(二进制数据)以字节为单位，字符串(文本数据)以字符为单位，要指定字符集，说明字节数组当前的编码方式，用这种编码方式来解析字节数组，转变成字符串
            // java 使用Unicode字符集编码，一个字符是两个字节；但是此处的从body中读入的字节数组可不一定是使用Unicode编码的
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
