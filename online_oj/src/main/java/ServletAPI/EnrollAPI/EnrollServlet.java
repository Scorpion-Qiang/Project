package ServletAPI.EnrollAPI;

import MyException.*;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-18
 * Time: 21:02
 */
/*
* 注册功能
* 方法: POST
* URL: /enroll
* 请求:
*       {
*           username:
*           password:
*           passwordAgain:
*           myPhoto:
*           introduce:
*
*       }
*
*
*
* */
@MultipartConfig
@WebServlet("/enroll")
public class EnrollServlet extends HttpServlet {
    // 此处写的是绝对路径，云服务器上：/root/apache-tomcat-8.5.76/webapps/OnlineJudge/image/ --23.5.21
    private static final String USER_PHOTO_PATH = "C:\\Users\\86188\\Documents\\代码\\Java\\online_OJ\\src\\main\\webapp\\image\\";
    private static final String STOP_WORDS_PATH = "./punctuationmark.txt";

    private ObjectMapper mapper = new ObjectMapper();
    private HashSet<String> stopWords = new HashSet<>();


    @Override
    public void init(){
        // 加载 用户名禁用词表
        loadStopWords();
    }
    // 加载 用户名禁用词表
    private void loadStopWords() {
        try(BufferedReader reader = new BufferedReader(new FileReader(STOP_WORDS_PATH))) {
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    return;
                }
                stopWords.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");
        UserDao userDao = new UserDao();

        EnrollResp enrollResp = new EnrollResp();

        User user = new User();

        // 1. 获取参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String passwordAgain = req.getParameter("passwordAgain");
        String introduce = req.getParameter("introduce");
        String validatecode = req.getParameter("validatecode");

        // 2. 校验参数
        try {
            if(username == null || username.trim().equals("") || password == null || password.trim().equals("")){
                throw new UsernameOrPasswordEmptyException();
            }
            if(username.length() < 2 || username.length() > 8){
                throw new UserNameTooLongOrShortException();
            }

            // 校验用户名中是不是有禁用字
            List<Term> terms = ToAnalysis.parse(username).getTerms();
            for (Term term : terms) {
                String word = term.getName();
                if(stopWords.contains(word)){
                    throw new UsernameHasBanWordException();
                }
            }


            if(password.length() < 2 || password.length() > 12){
                throw new PasswordTooLongOrShortException();
            }

            if(passwordAgain == null){
                throw new PasswordAgainEmptyException();
            }
            if(!password.equals(passwordAgain)){
                throw new UsernameOrPasswordNotException();
            }
            User exitUser = userDao.selectOne(username);
            if(exitUser != null) {
                throw new UserExistException();
            }


            // 验证码
            if(validatecode == null || validatecode.trim().equals("")){
                throw new ValidatecodeNullException();
            }
            HttpSession session = req.getSession(false);
            String checkcode = String.valueOf(session.getAttribute("checkcode"));
            if(!checkcode.equals(validatecode)){
                throw new ValidatecodeErrorException();
            }

            // 3. 处理头像
            File file = new File(USER_PHOTO_PATH);
            if(!file.exists()){
                file.mkdirs();
            }
            // 不传图片，part != null, 是 org.apache.catalina.core.ApplicationPart@62a62fa4
            Part part = req.getPart("myfile");
            // 3-1. 获取part的绝对路径
            String prior = part.getSubmittedFileName();
            String path = null;
            if(prior != null){
                // 3-2. 获取图片名的后缀 .jpg .png .jiff
                System.out.println(prior);
                int index = prior.lastIndexOf('.');
                String type = prior.substring(index);
                // 3-3. 给新的照片命名(唯一)
                path = UUID.randomUUID().toString() + type;
                // 3-4. 设置其绝对路径
                String location = USER_PHOTO_PATH + path;
                // 3-5. 将文件写入
                part.write(location);
            } else {
                Random random = new Random();
                int ret = random.nextInt(20);
                path = ret + ".png";
            }
            user.setPhoto(path);

            // 4. 设置这个人简介
            if(introduce != null){
                if(introduce.length() > 50){
                    throw new IntroduceOutException();
                }
                user.setIntroduce(introduce);
            }

            // 5. 新增用户
            // 设置用户名和密码
            user.setUsername(username);
            user.setPassword(password);
            userDao.insert(user);


        } catch (UsernameOrPasswordEmptyException e) {
            enrollResp.setError(1);
            enrollResp.setReason("用户名或密码为空!!!");
        } catch (PasswordAgainEmptyException e){
            enrollResp.setError(1);
            enrollResp.setReason("第二次输入的密码为空!!!");
        } catch (UsernameOrPasswordNotException e){
            enrollResp.setError(1);
            enrollResp.setReason("两次输入的密码不一致!!!");
        } catch (UserExistException e){
            enrollResp.setError(1);
            enrollResp.setReason("此用户名已经存在!!!");
        } catch (IntroduceOutException e) {
            enrollResp.setError(1);
            enrollResp.setReason("个人简介过长!!!");
        } catch (ValidatecodeNullException e){
            enrollResp.setError(1);
            enrollResp.setReason("验证码为空!!!");
        } catch (ValidatecodeErrorException e) {
            enrollResp.setError(1);
            enrollResp.setReason("验证码错误!!!");
        } catch (UserNameTooLongOrShortException e){
            enrollResp.setError(1);
            enrollResp.setReason("用户名长度不合适(建议2~8个字符)!!!");
        } catch (PasswordTooLongOrShortException e){
            enrollResp.setError(1);
            enrollResp.setReason("密码长度不合适(建议2~12个字符)!!!");
        } catch (UsernameHasBanWordException e) {
            enrollResp.setError(1);
            enrollResp.setReason("用户名不可使用非法字符!!!");
        }


        // 6. 返回响应
        String res = mapper.writeValueAsString(enrollResp);
        resp.getWriter().write(res);

    }
}
