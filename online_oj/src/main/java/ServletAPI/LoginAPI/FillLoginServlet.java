package ServletAPI.LoginAPI;

import ServletAPI.Util;
import Operation.UserOp.User;
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
 * Date: 2022-09-20
 * Time: 21:42
 */
/*
* 填充用户登录页面
* 方法: /fill_login
* 响应:
*      {
*         error:       0--没登录过     1--登陆过
*         usename:
*         password:
*      }
*
*
* */
@WebServlet("/fill_login")
public class FillLoginServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        FillLoginResp fillLoginResp = new FillLoginResp();

        // 1. 监测是否登录过
        User user = Util.isLogin(req);
        if(user != null){
            fillLoginResp.setError(1);
            fillLoginResp.setUsername(user.getUsername());
            fillLoginResp.setPassword(user.getPassword());
        }
        // 2. 返回响应
        String body = mapper.writeValueAsString(fillLoginResp);
        resp.getWriter().write(body);
    }
}
