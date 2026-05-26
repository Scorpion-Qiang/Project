package Servlet;

import MySQL.*;

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
 * Date: 2022-03-25
 * Time: 10:34
 */
@WebServlet("/player_review")
public class UpReview extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        // 1.判断是不是登录状态
        Team team = Util.isLogin(req);
        if(team == null){
            resp.sendRedirect("player_goto.html");
            return;
        }

        // 2.获取 被评论的playerId 和 评论的内容content
        String id = req.getParameter("playerId");
        System.out.println(id);
        if(id == null || id.equals("")){
            resp.getWriter().write("<h3>id不能为空</h3>");
            return;
        }
        String content = req.getParameter("upcontent");
        if(content == null || content.equals("")){
            resp.getWriter().write("<h3>评论内容不能为空</h3>");
            return;
        }

        boolean ret = true;
        for (int i = 0; i < content.length(); i++) {
            ret = content.charAt(i) == ' ';
        }
        if(ret){
            resp.getWriter().write("<h3>评论内容不能为空，请填写文字</h3>");
            return;
        }

        // 3.判断有没有这个球员
        Player player = PlayerDao.selectById(Integer.parseInt(id));
        if(player == null){
            resp.getWriter().write("<h3>没有这个球员</h3>");
            return;
        }

        // 4.新增评论
        Review review = new Review();
        review.setContent(content);
        review.setPostTime(new Timestamp(System.currentTimeMillis()));
        review.setPostAuthor(team.getTeamName());
        review.setPlayerId(Integer.parseInt(id));
        ReviewDao.insert(review);

        // 5.跳转到 player_detail.html 页面
        resp.sendRedirect("player_detail.html?id=" + Integer.parseInt(id));
    }
}
