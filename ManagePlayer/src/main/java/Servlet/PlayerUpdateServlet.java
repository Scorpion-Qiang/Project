package Servlet;

import MySQL.Player;
import MySQL.PlayerDao;
import MySQL.Team;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
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
 * Date: 2022-03-23
 * Time: 18:04
 */
@WebServlet("/player_update")
public class PlayerUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        ServletContext servletContext = req.getServletContext();
        TemplateEngine engine = (TemplateEngine) servletContext.getAttribute("TemplateEngine");

        // 1.检查是否是登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.sendRedirect("player_goto.html");
            return;
        }
        // 2. 获取传过来的球员id
        String id = req.getParameter("id");
        if(id == null || id.equals("")){
            resp.getWriter().write("<h3>球员id为空</h3>");
            return;
        }
        // 3.去数据库看 是否有这个球员
        Player player = PlayerDao.selectById(Integer.parseInt(id));
        if(player == null){
            resp.getWriter().write("<h3>没有这个球员</h3>");
            return;
        }
        // 4.再次校验有没有资格修改 球员，即验证登录的球队 和 球员的球队作者 是一个吗
        if(team.getId() != player.getUserId()){
            resp.getWriter().write("<h3>您不是这个球员的领导，没有资格修改球员信息</h3>");
            return;
        }

        // 5.模板渲染
        WebContext context = new WebContext(req, resp, servletContext);
        context.setVariable("player", player);
        engine.process("player_update", context, resp.getWriter());

    }
}
