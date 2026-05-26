package ServletAPI.LoginAPI;
import MyException.UsernameOrPasswordEmptyException;
import MyException.UsernameOrPasswordNotException;
import MyException.ValidatecodeErrorException;
import MyException.ValidatecodeNullException;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-20
 * Time: 21:17
 */
/*
* 登录功能
* 方法: POST
* URL: login
* 请求:
*        {
*           username:
*           password:
*        }
*
* 响应:
*        {
*           error:   0--成功 1--失败
*           reason:
*        }
*
* */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");

        UserDao userDao = new UserDao();

        // 返回的响应
        LoginResq loginResq = new LoginResq();


        // 1. 获取参数
        LoginReq loginReq = mapper.readValue(req.getInputStream(), LoginReq.class);
        String username = loginReq.getUsername();
        String password = loginReq.getPassword();
        String validatecode = loginReq.getValidatecode();

        // 2. 校验参数
        try {
            // 校验用户名或密码为空
            if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
                throw new UsernameOrPasswordEmptyException();
            }
            if(validatecode == null || validatecode.trim().equals("")){
                throw new ValidatecodeNullException();
            }

            // 校验验证码
            HttpSession session = req.getSession(true);
            if(!session.getAttribute("checkcode").equals(validatecode)){
                throw new ValidatecodeErrorException();
            }

            // 校验用户名是否存在及密码是否正确
            User user = userDao.selectOne(username);
            if (user == null || !user.getPassword().equals(password)) {
                throw new UsernameOrPasswordNotException();
            }

            session.setAttribute("user", user);


        } catch (UsernameOrPasswordEmptyException e) {
            loginResq.setError(1);
            loginResq.setReason("用户或密码为空!!!");
        } catch (UsernameOrPasswordNotException e){
            loginResq.setError(1);
            loginResq.setReason("用户名或密码错误!!!");
        } catch (ValidatecodeErrorException e) {
            loginResq.setError(1);
            loginResq.setReason("验证码错误!!!");
        } catch (ValidatecodeNullException e) {
            loginResq.setError(1);
            loginResq.setReason("验证码为空!!!");
        }


        // 3. 返回响应
        String res = mapper.writeValueAsString(loginResq);
        resp.getWriter().write(res);
    }

}
