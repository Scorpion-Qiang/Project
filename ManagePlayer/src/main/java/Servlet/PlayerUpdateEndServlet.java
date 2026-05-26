package Servlet;

import MySQL.Player;
import MySQL.PlayerDao;
import MySQL.Team;

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
 * Time: 19:24
 */
@WebServlet("/player_updateEnd")
public class PlayerUpdateEndServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 1.判断是否是登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.getWriter().write("<h3>球队管理者未登录</h3>");
            return;
        }
        // 2.获取到id，playerName，content
        String id = req.getParameter("playerId");
        String playerName = req.getParameter("playerName");
        String content = req.getParameter("content");
        if(id == null || id.equals("") || playerName == null || playerName.equals("") ||
           content == null || content.equals("")){
            resp.getWriter().write("<h3>id或playerName或content为空");
            return;
        }
        // 3. 看看数据库中有没有这个球员（根据id）
        Player player = PlayerDao.selectById(Integer.parseInt(id));
        if(player == null){
            resp.getWriter().write("<h3>没有这个球员</h3>");
            return;
        }

        // 4.再次检验 登录者 与 球员领导者 是不是同一个
        if(team.getId() != player.getUserId()){
            resp.getWriter().write("<h3>您不是这个球员的领导者，无法修改</h3>");
            return;
        }

        // 5. 修改
        PlayerDao.Update(Integer.parseInt(id), playerName, content);

        // 6.重定向到 player_list.html
        resp.sendRedirect("player_list.html");
    }
}
