package ServletAPI.SettleReviewAPI;

import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;

import MyException.ContentNotException;
import MyException.SettleNotFiledException;
import MyException.ReviewNotFiledException;

import Operation.SettleReviewOp.SettleReview;
import Operation.SettleReviewOp.SettleReviewDao;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

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
 * Time: 15:17
 */

/*
* 提交二级评论
* 方法: POST
* URL: /subsettlereview
* 请求: SubmitSettleReviewReq
*      校验参数
*
* 响应:
*      {
*          error: 0--成功 1--失败
*          reason:
*      }
*
* */

// 提交一级评论
//    underId == 0 sendTo == 0
// 提交二级评论
//    underId != 0 sendTo != 0
@WebServlet("/submitsettlereview")
public class SubmitSettleReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");

        SettleReviewDao settleReviewDao = new SettleReviewDao();
        SettleDao settleDao = new SettleDao();

        UserDao userDao = new UserDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }



        // 1. 获取参数
        SubmitSettleReviewReq submitSettleReviewReq = mapper.readValue(req.getInputStream(), SubmitSettleReviewReq.class);


        SubmitSettleReviewResp submitSettleReviewResp = new SubmitSettleReviewResp();

        // 2. 校验参数
        try {
            // 校验 content
            if(submitSettleReviewReq.getContent() == null || submitSettleReviewReq.getContent().trim().equals("")){
                throw new ContentNotException();
            }

            // 校验 settleId
            Settle settle = settleDao.selectOneSettle(submitSettleReviewReq.getSettleId());
            if(settle == null){
                throw new SettleNotFiledException();
            }

            // 校验 underId, underId != 0, 说明是二级评论，不是一级评论，咱得看看一级评论 underId 在不在
            if(submitSettleReviewReq.getUnderId() != 0) {
                SettleReview s = settleReviewDao.selectOneSettleReview(submitSettleReviewReq.getUnderId());
                if (s == null) {
                    throw new ReviewNotFiledException();
                }
            }



            // 3. 新增评论
            SettleReview settlereview = new SettleReview();
            settlereview.setUsername(user.getUsername());
            settlereview.setContent(submitSettleReviewReq.getContent());
            settlereview.setTime(new Timestamp(System.currentTimeMillis()));
            settlereview.setSettleId(submitSettleReviewReq.getSettleId());
            settlereview.setUnderId(submitSettleReviewReq.getUnderId());
            settlereview.setSendTo(submitSettleReviewReq.getSendTo());

            settleReviewDao.insertSettleReview(settlereview);

        } catch (ReviewNotFiledException e){
            submitSettleReviewResp.setError(1);
            submitSettleReviewResp.setReason("此上级目录不存在");
        } catch (SettleNotFiledException e){
            submitSettleReviewResp.setError(1);
            submitSettleReviewResp.setReason("此题目不存在");
        } catch (ContentNotException e) {
            submitSettleReviewResp.setError(1);
            submitSettleReviewResp.setReason("内容为空");
        }


        // 4. 返回响应
        String body = mapper.writeValueAsString(submitSettleReviewResp);
        resp.getWriter().write(body);
    }
}
