package Servlet;

import MySQL.Player;
import MySQL.PlayerDao;
import MySQL.Team;
import MySQL.TeamDao;

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
 * Time: 17:58
 */
@WebServlet("/login")
public class PlayerGotoServlet extends HttpServlet {

    // 登录操作 固定套路
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");


        // 1.获取用户名和密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 2.校验用户名和密码
        if(username == null || username.equals("") || password == null || password.equals("")){
            resp.getWriter().write("<h3>用户名或密码不能为空<h3>");
            return;
        }

        // 3.判断是否登录成功
        Team team = TeamDao.selectByName(username);

        if(team == null || !password.equals(team.getPassword())){
            resp.getWriter().write("<h3>用户名或密码错误<h3>");
            return;
        }

        // 4.创建会话，储存用户信息
        HttpSession session = req.getSession(true);
        session.setAttribute("team", team);

        // 5.重定向到主页
        resp.sendRedirect("player_list.html");
    }

}
