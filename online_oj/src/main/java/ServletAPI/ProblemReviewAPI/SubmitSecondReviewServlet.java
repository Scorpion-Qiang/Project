package ServletAPI.ProblemReviewAPI;

import Operation.ProblemReviewOp.ProblemReview;
import Operation.ProblemReviewOp.ProblemReviewDao;
import MyException.ContentNotException;
import MyException.ProblemNotFiledException;
import MyException.ReviewNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.Problem;
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
 * Time: 15:17
 */
/*
* 提交二级评论
* 方法: POST
* URL: /subproblemreview
* 请求: SubmitProblemReviewReq
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
@WebServlet("/submitproblemreview")
public class SubmitSecondReviewServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");

        ProblemReviewDao problemReviewDao = new ProblemReviewDao();
        ProblemDao problemDao = new ProblemDao();

        UserDao userDao = new UserDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            resp.sendRedirect("login.html");
            return;
        }


        // 1. 获取参数
        SubmitProblemReviewReq submitSecondReviewReq = mapper.readValue(req.getInputStream(), SubmitProblemReviewReq.class);


        SubmitProblemReviewResp submitSecondReviewResp = new SubmitProblemReviewResp();

        // 2. 校验参数
        try {
            // 校验 content
            if(submitSecondReviewReq.getContent() == null || submitSecondReviewReq.getContent().trim().equals("")){
                throw new ContentNotException();
            }

            // 校验 problemId
            Problem problem = problemDao.selectOne(submitSecondReviewReq.getProblemId());
            if(problem == null){
                throw new ProblemNotFiledException();
            }

            // 校验 underId, underId != 0, 说明是二级评论，不是一级评论，咱得看看一级评论 underId 在不在
            if(submitSecondReviewReq.getUnderId() != 0) {
                ProblemReview pr = problemReviewDao.selectOneProblemReview(submitSecondReviewReq.getUnderId());
                if (pr == null) {
                    throw new ReviewNotFiledException();
                }
            }



            // 3. 新增评论
            ProblemReview problemreview = new ProblemReview();
            problemreview.setUsername(user.getUsername());
            problemreview.setContent(submitSecondReviewReq.getContent());
            problemreview.setTime(new Timestamp(System.currentTimeMillis()));
            problemreview.setProblemId(submitSecondReviewReq.getProblemId());
            problemreview.setUnderId(submitSecondReviewReq.getUnderId());
            problemreview.setSendTo(submitSecondReviewReq.getSendTo());
            problemReviewDao.insertProblemReview(problemreview);

        } catch (ReviewNotFiledException e){
            submitSecondReviewResp.setError(1);
            submitSecondReviewResp.setReason("此上级目录不存在");
        } catch (ProblemNotFiledException e){
            submitSecondReviewResp.setError(1);
            submitSecondReviewResp.setReason("此题目不存在");
        } catch (ContentNotException e) {
            submitSecondReviewResp.setError(1);
            submitSecondReviewResp.setReason("内容为空");
        }


        // 4. 返回响应
        String body = mapper.writeValueAsString(submitSecondReviewResp);
        resp.getWriter().write(body);
    }
}
