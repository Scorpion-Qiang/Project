package Servlet;

import MySQL.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.Servlet;
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
 * Time: 15:41
 */

@WebServlet("/player_detail.html")
public class PlayerDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        // 1. 监察登录信息
        Team team = Util.isLogin(req);
        if(team == null){
            System.out.println("用户未登录");
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2. 根据id找到该球员
        String id = req.getParameter("id");
        if(id == null || id.equals("")){
            resp.getWriter().write("<h3>id不可以为空</h3>");
            return;
        }
        Player player = PlayerDao.selectById(Integer.parseInt(id));
        if(player == null){
            resp.getWriter().write("<h3>没有这个球员</h3>");
            return;
        }

        // 3. 找到球员所属的球队
        Team teamAuthor = TeamDao.selectById(player.getUserId());
        if(teamAuthor == null){
            resp.getWriter().write("<h3>指定的球队不存在<h3>");
            return;
        }

        // 4. 获取 所有对本球员的 评论
        List<Review> reviews = ReviewDao.selectAll(player.getId());



        ServletContext servletContext = req.getServletContext();
        TemplateEngine engine = (TemplateEngine) servletContext.getAttribute("TemplateEngine");

        WebContext context = new WebContext(req, resp, servletContext);

        //  此处写 "players" 和 players，虽然前面没有直接出现 $players
        context.setVariable("player", player);

        // 修改左侧的球队信息
        context.setVariable("teamAuthor", teamAuthor);
        context.setVariable("teamCount", TeamDao.teamCount());

        // 是否显示删除、修改按钮
        context.setVariable("showDeleteButton", team.getId() == teamAuthor.getId());
        context.setVariable("showUpdateButton", team.getId() == teamAuthor.getId());

        // 评论
        context.setVariable("reviews", reviews);
        context.setVariable("teamName", team.getTeamName());
        engine.process("player_detail", context, resp.getWriter());
    }
}
