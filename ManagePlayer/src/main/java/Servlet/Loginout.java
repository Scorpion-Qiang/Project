package Servlet;

import javax.servlet.ServletContext;
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
 * Date: 2022-03-21
 * Time: 21:49
 */
// 注销操作
// 1. 删除HttpSession中的 team对象
// 2. 重定向到 登录页面（player_goto.html）
@WebServlet("/loginout")
public class Loginout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1.
        HttpSession session = req.getSession(false);
        if(session == null){
            // 当前已经是未登录的状态，直接跳转到登录页面即可
            resp.sendRedirect("player_goto.html");
            return;
        }

        //  删除之前登录成功后保存的Team
        session.removeAttribute("team");
        //  2. 重定向
        resp.sendRedirect("player_goto.html");

    }
}
