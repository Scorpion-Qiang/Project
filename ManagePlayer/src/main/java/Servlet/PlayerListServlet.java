package Servlet;

import MySQL.Player;
import MySQL.PlayerDao;
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
 * Date: 2022-03-21
 * Time: 10:52
 */
// 此处使用的 servlet path 可以让前端不用区分静态页面 还是 动态页面
// 查看全部的球员信息（不限于球队）
@WebServlet("/player_list.html")
public class PlayerListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 监测登录情况
        Team team = Util.isLogin(req);
        if(team == null){
            System.out.println("用户未登录");
            resp.sendRedirect("player_goto.html");
            return;
        }

        ServletContext servletContext = getServletContext();
        TemplateEngine engine = (TemplateEngine) servletContext.getAttribute("TemplateEngine");


        WebContext context = new WebContext(req, resp, servletContext);



        // 所有球队的数目
        context.setVariable("teamCount", TeamDao.teamCount());
        // 看是否是 查看 “我的球员”
        String teamId = req.getParameter("id");

        if(teamId != null && !teamId.equals("")){
            Team teamTo = TeamDao.selectById(Integer.parseInt(teamId));
            context.setVariable("team", teamTo);

            List<Player> players = PlayerDao.selectForTeam(teamTo.getId());
            context.setVariable("players", players);
            context.setVariable("isMyPlayers","我的球员列表");
        }else {
            context.setVariable("team", team);

            List<Player> players = PlayerDao.selectAll();
            context.setVariable("players", players);
            context.setVariable("isMyPlayers","所有球员列表");
        }

        engine.process("player_list", context, resp.getWriter());
    }
}
