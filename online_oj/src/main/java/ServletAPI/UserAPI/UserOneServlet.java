package ServletAPI.UserAPI;

import MyException.UserNotFiledException;
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
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-30
 * Time: 21:38
 */
/*
* 获取用户详细信息
* url: "/userdetail?username=..."
*
*
* */

@WebServlet("/userdetail")
public class UserOneServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 判断是否登录过
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }
        UserDao userDao = new UserDao();



        String username = req.getParameter("username");
        UserDetailResp userDetailResp = new UserDetailResp();

        try {
            User u = null;
            if(username == null || username.trim().equals("")) {
                u = user;
            } else {
                u = userDao.selectOne(username);
            }
            if(u == null){
                throw new UserNotFiledException();
            }
            userDetailResp.setUsername(u.getUsername());
            userDetailResp.setPhoto(u.getPhoto());
            userDetailResp.setIntroduce(u.getIntroduce());
            userDetailResp.setSubmit(u.getSubmitcount());
            userDetailResp.setPassproblemcount(u.getPassproblemcount());
        } catch (UserNotFiledException e){
            resp.setHeader("ERRORREASON", URLEncoder.encode("此用户不存在!!", "utf8"));
            return;
        }

        resp.setContentType("application/json;charset=utf-8");
        String body = mapper.writeValueAsString(userDetailResp);
        resp.getWriter().write(body);
    }
}
