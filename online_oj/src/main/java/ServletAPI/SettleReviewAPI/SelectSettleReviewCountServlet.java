package ServletAPI.SettleReviewAPI;


import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;

import MyException.SettleNotFiledException;
import Operation.SettleReviewOp.SettleReviewDao;
import Operation.UserOp.User;
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
 * Date: 2022-10-11
 * Time: 18:17
 */
/*
* 获取某个题题解的评论总数
* url: "./settlereviewcount?settleId=.."
* 响应: SelectSettleReviewCountResp
*
* */
@WebServlet("/settlereviewcount")
public class SelectSettleReviewCountServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        SettleReviewDao settleReviewDao = new SettleReviewDao();
        SettleDao settleDao = new SettleDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        SelectSettleReviewCountResp selectSettleReviewCountResp = new SelectSettleReviewCountResp();

        // 1. 获取参数
        String param = req.getParameter("settleId");

        try {
            // 校验参数 settleId
            int settleId = Integer.parseInt(param);
            Settle settle = settleDao.selectOneSettle(settleId);
            if(settle == null){
                throw new SettleNotFiledException();
            }

            int count = settleReviewDao.selectSettleReviewCount(settleId);

            selectSettleReviewCountResp.setCount(count);
        } catch (NumberFormatException e){
            selectSettleReviewCountResp.setError(1);
            selectSettleReviewCountResp.setReason("输入格式有误!!!");
        } catch (SettleNotFiledException e) {
            selectSettleReviewCountResp.setError(1);
            selectSettleReviewCountResp.setReason("此题目不存在!!!");
        }

        // 3. 返回响应
        String body = mapper.writeValueAsString(selectSettleReviewCountResp);
        resp.getWriter().write(body);
    }
}
