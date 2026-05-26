package Servlet;

import MySQL.Team;
import MySQL.TeamDao;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-25
 * Time: 20:12
 */
@WebServlet("/team_list.html")
public class TeamListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        ServletContext servletContext = req.getServletContext();
        TemplateEngine engine = (TemplateEngine) servletContext.getAttribute("TemplateEngine");

        // 1.判断登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2. 获取全部的 team
        List<Team> teams = TeamDao.selectAll();
        WebContext context = new WebContext(req, resp, servletContext);
        context.setVariable("teams", teams);
        engine.process("team_list", context, resp.getWriter());
    }
}
