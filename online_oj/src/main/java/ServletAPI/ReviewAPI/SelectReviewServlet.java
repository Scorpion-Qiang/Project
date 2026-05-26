package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-22
 * Time: 0:01
 */

/*
* 显示所有的一级评论
* 方法: GET
* URL: /review
*
* */
@WebServlet("/review")
public class SelectReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ReviewDao reviewDao = new ReviewDao();

        // 1. 判断是否登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();


        List<SelectReviewResp> res = new ArrayList<>();
        // 2. 获取评论
        List<Review> list = reviewDao.selectAllReview(0);
        for (Review review : list) {
            SelectReviewResp selectReviewResp = new SelectReviewResp();
            selectReviewResp.setId(review.getId());
            selectReviewResp.setUsername(review.getUsername());
            selectReviewResp.setTitle(review.getTitle());
            selectReviewResp.setTime(review.getTime());

            String content = review.getContent();
            int end = Math.min(content.length(), 140);
            content = content.substring(0, end);
            selectReviewResp.setContent(content);

            User u = userDao.selectOne(review.getUsername());
            selectReviewResp.setUserphoto(u.getPhoto());

            res.add(selectReviewResp);
        }


        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}
