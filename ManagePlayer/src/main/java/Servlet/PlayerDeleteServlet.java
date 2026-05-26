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

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-22
 * Time: 20:38
 */
@WebServlet("/player_delete")
public class PlayerDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 1.判断用户是否已经登录
        Team team = Util.isLogin(req);
        if(team == null){
            System.out.println("用户未登录");
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2.获取到 要删除的球员的 id
        String id = req.getParameter("id");
        if(id == null || id.equals("")){
            resp.getWriter().write("<h3>id不可以为空哦<h3>");
            return;
        }

        // 3.读出球员的id后，去数据库查看是否有这个球员
        Player player = PlayerDao.selectById(Integer.parseInt(id));
        if(player == null){
            resp.getWriter().write("<h3>要删除的球员不存在<h3>");
            return;
        }

        // 4. 再次校验 登录的球队 和 要删除的球员所属的球队 是不是同一个  double check 前端校验 + 后端校验
        if(team.getId() != player.getUserId()){
            resp.getWriter().write("<h3>您不是此球员的领导，无法删除</h3>");
            return;
        }

        // 5. 删除球员
        PlayerDao.delete(Integer.parseInt(id));

        // 6.跳转到 player_list.html
        resp.sendRedirect("player_list.html");
    }
}
