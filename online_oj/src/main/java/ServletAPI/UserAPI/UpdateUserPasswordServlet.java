package ServletAPI.UserAPI;

import MyException.PasswordAgainEmptyException;
import MyException.PasswordAgainErrorException;
import MyException.UsernameOrPasswordNotException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-23
 * Time: 10:35
 */
/*
* 修改用户密码
* url: 'updateuserpassword'
* 请求: UpdateUserPasswordReq
* 响应: UpdateUserPasswordResp
*
*
* */
@WebServlet("/updateuserpassword")
public class UpdateUserPasswordServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        req.setCharacterEncoding("utf8");

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UpdateUserPasswordReq updateReq = mapper.readValue(req.getInputStream(), UpdateUserPasswordReq.class);
        UpdateUserPasswordResp updateResp = new UpdateUserPasswordResp();

        // 1. 获取参数
        String priorPassword = updateReq.getPriorPassword();
        String newPassword = updateReq.getNewPassword();
        String againPassword = updateReq.getAgainPassword();

        try {
            // 2. 校验参数
            if(priorPassword == null || priorPassword.trim().equals("") ||
                    newPassword == null || newPassword.trim().equals("") ||
                    againPassword == null || againPassword.trim().equals("")) {
                throw new PasswordAgainEmptyException();
            }

            if(!user.getPassword().equals(priorPassword)){
                throw new UsernameOrPasswordNotException();
            }

            if(!newPassword.equals(againPassword)){
                throw new PasswordAgainErrorException();
            }

            user.setPassword(newPassword);
            UserDao userDao = new UserDao();
            userDao.update(user);

        } catch (PasswordAgainEmptyException e) {
            updateResp.setError(1);
            updateResp.setReason("密码不可以为空!!!");
        } catch (UsernameOrPasswordNotException e) {
            updateResp.setError(1);
            updateResp.setReason("原密码错误!!!");
        } catch (PasswordAgainErrorException e){
            updateResp.setError(1);
            updateResp.setReason("两次输入的密码不一致!!!");
        }

        // 3. 返回响应
        String body = mapper.writeValueAsString(updateResp);
        resp.getWriter().write(body);
    }
}
