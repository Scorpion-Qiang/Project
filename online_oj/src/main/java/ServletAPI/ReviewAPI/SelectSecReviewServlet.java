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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-06
 * Time: 21:26
 */
/*
* 返回一级评论下面的二级评论
* url: "/secreview?underId=..."
* method: get
* 请求:     如果 underId 格式不对，或 没有这个review，抛出异常
*                          正确，返回
* 返回响应: SelectSecReviewResp
*         如果 sendTo 和 preContent 是null，说明这个评论是发给这个一级评论的，不需要指定接收的用户名和引用的内容
* */
@WebServlet("/secreview")
public class SelectSecReviewServlet extends HttpServlet {
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


        // 2. 获取参数
        String s = req.getParameter("underId");
        List<Review> list = new ArrayList<>();
        List<SelectSecReviewResp> res = new ArrayList<>();
        // 3. 获取评论
        try {
            int underId = Integer.parseInt(s);
            Review re = reviewDao.selectReviewOne(underId);
            if (re == null) {
                throw new ReviewNotFiledException();
            }

            list = reviewDao.selectAllReview(underId);

            for (Review review : list) {
                SelectSecReviewResp selectSecReviewResp = new SelectSecReviewResp();
                selectSecReviewResp.setId(review.getId());
                selectSecReviewResp.setUsername(review.getUsername());
                selectSecReviewResp.setTime(review.getTime());
                selectSecReviewResp.setContent(review.getContent());


                // 如果 sendTo == underId, 说明是发给这个一级评论的，不需要添加接收的用户名，也不需要添加 preContent
                if (review.getSendTo() != underId) {
                    Review reviewOne = reviewDao.selectReviewOne(review.getSendTo());
                    selectSecReviewResp.setSendTo(reviewOne.getUsername());

                    selectSecReviewResp.setPreContent(reviewOne.getContent());
                }

                User u = userDao.selectOne(review.getUsername());
                selectSecReviewResp.setUserphoto(u.getPhoto());

                res.add(selectSecReviewResp);
            }

        } catch (NumberFormatException e) {
            resp.getWriter().write("输入格式有误!!!");
            return;
        } catch (ReviewNotFiledException e) {
            resp.getWriter().write("该评论不存在!!!");
            return;
        }

        // 4. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(res);
        resp.getWriter().write(body);
    }
}
