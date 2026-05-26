package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
import MyException.ReviewNotFiledException;
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

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-03
 * Time: 16:44
 */
/*
* 查看某个题目的详情
* url: "/reviewdetail?id="
*
*
*
* */

@WebServlet("/reviewdetail")
public class SelectReviewDetailServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReviewDao reviewDao = new ReviewDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }


        Review review = new Review();
        SelectReviewDetailResp selectReviewDetailResp = new SelectReviewDetailResp();
        // 1. 获取参数
        String param = req.getParameter("id");
        try {
            int id = Integer.parseInt(param);
            // 2. 查找评论
            review = reviewDao.selectReviewOne(id);
            if(review == null){
                throw new ReviewNotFiledException();
            }

            selectReviewDetailResp.setId(review.getId());
            selectReviewDetailResp.setUsername(review.getUsername());
            selectReviewDetailResp.setTitle(review.getTitle());
            selectReviewDetailResp.setContent(review.getContent());
            selectReviewDetailResp.setTime(review.getTime());

            UserDao userDao = new UserDao();
            User u = userDao.selectOne(review.getUsername());
            selectReviewDetailResp.setUserphoto(u.getPhoto());

        } catch (NumberFormatException e){
            resp.getWriter().write("输入格式错误!!!");
            return;
        } catch (ReviewNotFiledException e){
            resp.getWriter().write("该评论不存在!!!");
            return;
        }

        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(selectReviewDetailResp);
        resp.getWriter().write(body);

    }
}
