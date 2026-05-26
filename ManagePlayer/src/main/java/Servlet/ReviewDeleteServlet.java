package Servlet;

import MySQL.Review;
import MySQL.ReviewDao;
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
 * Date: 2022-03-25
 * Time: 15:16
 */
@WebServlet("/review_delete")
public class ReviewDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 1.判断是否是登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2.判断id teamAuthor.id 是否为空
        String id = req.getParameter("id");
        String teamAuthorId = req.getParameter("teamAuthorId");
        if(id == null || id.equals("") || teamAuthorId == null || teamAuthorId.equals("")){
            resp.getWriter().write("<h3>id或teamAuthorId为空</h3>");
            return;
        }

        // 3. 判断这个 review 是否存在   teamAuthor 是否存在
        Review review = ReviewDao.selectById(Integer.parseInt(id));
        if(review == null){
            resp.getWriter().write("<h3>这个评论不存在</h3>");
            return;
        }
        Team teamAuthor = TeamDao.selectById(Integer.parseInt(teamAuthorId));
        if(teamAuthor == null){
            resp.getWriter().write("<h3>这个球队领导不存在</h3>");
            return;
        }

        // 4. 判断team.id 和 teamAuthor.id 是否相同  如果登录者就是这个球员的 球队领导，直接删就行
        if(team.getId() == teamAuthor.getId()){
            ReviewDao.delete(Integer.parseInt(id));
            resp.sendRedirect("player_detail.html?id=" + review.getPlayerId());
        }else{
            // 5. 判断review的postAuthor 和 team.teamName 是否相同   登录者不是这个球员的球队领导，判断这条评论是不是这个登录者发的，如果是的话，删；不是的话，不删；
            if(review.getPostAuthor().equals(team.getTeamName())){
                ReviewDao.delete(Integer.parseInt(id));
                resp.sendRedirect("player_detail.html?id=" + review.getPlayerId());
            }else {
                resp.getWriter().write("<h3>这条评论不是您发的，你不可以删除的</h3>");
                return;
            }
        }

    }
}
