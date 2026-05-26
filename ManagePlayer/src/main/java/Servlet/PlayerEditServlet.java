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
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-22
 * Time: 15:25
 */
@WebServlet("/player_edit")
public class PlayerEditServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 1.判断是否为登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2. 获取标题和内容
        String playerName = req.getParameter("playerName");
        String content = req.getParameter("content");
        // 标题或内容为空不可以
        if(playerName == null || playerName.equals("") || content == null || content.equals("")){
            resp.getWriter().write("<h3>标题或内容为空，请修改</h3>");
            return;
        }
        // 3.创建球员对象，放入到数据库中
        // 此处不需要添加球员id，球员id是数据库自己处理的
        Player player = new Player();
        player.setPlayerName(playerName);
        player.setContent(content);
        player.setPostTime(new Timestamp(System.currentTimeMillis()));
        player.setUserId(team.getId());
        PlayerDao.insert(player);

        // 4.球队的 playerCount + 1
        TeamDao.addPlayerCount(team);

        // 5. 重定向到 player_list.html
        resp.sendRedirect("player_list.html");
    }
}
