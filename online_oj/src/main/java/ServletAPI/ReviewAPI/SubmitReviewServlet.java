package ServletAPI.ReviewAPI;

import Operation.ReviewOp.Review;
import Operation.ReviewOp.ReviewDao;
import Searcher.SearchReview.Index;
import MyException.*;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.ProblemDao;

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
 * Date: 2022-09-25
 * Time: 14:23
 */
/*
* 提交评论
* 方法: POST
* URL: /submitreview
* 请求: {
*           title:
*           content:
*           underId:
*
*       }
* 响应:
*     {
*        error:  0--成功 1--失败
*        reason:
*     }
*
* */
@WebServlet("/submitreview")
public class SubmitReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    // 没办法了，手动添加 id 了
    // 提交 Review时，我需要向 Index 中添加 新的Review，但是新增的Review的id，我不知道啊（数据库也没法返回），就出此下策，手动添加 Review 的id
    // 注意加锁哦
    static int reviewId = 10;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");

        ReviewDao reviewDao = new ReviewDao();
        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }
        UserDao userDao = new UserDao();

        SubmitReviewResp submitReviewResp = new SubmitReviewResp();

        // 1. 获取参数
        SubmitReviewReq submitReviewReq = mapper.readValue(req.getInputStream(), SubmitReviewReq.class);


        // 2. 校验参数
        try {
            // 校验 underId
            int underId = submitReviewReq.getUnderId();


            // 如果 underId == 0 的话, 说明提交的是一级评论; !=0，说明提交的是二级或三级评论
            if(underId != 0){
                Review review = reviewDao.selectReviewOne(underId);
                if(review == null){
                    throw new ReviewNotFiledException();
                }
            }

            // 校验 title 如果 underId == 0 的话，说明提交的一级评论，必须有题目；二三级评论题目可以是空
            if(underId == 0){
                if (submitReviewReq.getTitle() == null || submitReviewReq.getTitle().trim().equals("")) {
                    throw new TitleNotException();
                }
                if(submitReviewReq.getTitle().length() > 20){
                    throw new TitleLengthErrorException();
                }
            }


            // 校验 Content
            if(submitReviewReq.getContent() == null || submitReviewReq.getContent().trim().equals("")){
                throw new ContentNotException();
            }


            // 校验 sendTo

            int sendTo = submitReviewReq.getSendTo();
            if(underId == 0){
                sendTo = 0;
            } else {
                Review review = reviewDao.selectReviewOne(sendTo);
                if(review == null){
                    throw new ReviewNotFiledException();
                }
            }


            Review review = new Review();
            review.setUsername(user.getUsername());
            review.setTitle(submitReviewReq.getTitle());
            review.setContent(submitReviewReq.getContent());
            review.setTime(new Timestamp(System.currentTimeMillis()));
            review.setUnderId(underId);
            review.setSendTo(submitReviewReq.getSendTo());


            synchronized (this){
                review.setId(reviewId);
                // 2. 新增评论
                reviewDao.insert(review);
                reviewId++;
            }


            // 3. 添加索引
            Index index = new Index();
            if(underId == 0) {
                index.insertForwardIndex(review);
            }
        } catch (ContentNotException e) {
            submitReviewResp.setError(1);
            submitReviewResp.setReason("输入内容为空!!!");
        } catch (ReviewNotFiledException e) {
            submitReviewResp.setError(1);
            submitReviewResp.setReason("评论不存在!!!");
        } catch (TitleNotException e) {
            submitReviewResp.setError(1);
            submitReviewResp.setReason("题目为空!!!");
        } catch (TitleLengthErrorException e){
            submitReviewResp.setError(1);
            submitReviewResp.setReason("题目长度过长!!!");
        }

        // 3. 返回响应
        String body = mapper.writeValueAsString(submitReviewResp);
        resp.getWriter().write(body);

    }
}
