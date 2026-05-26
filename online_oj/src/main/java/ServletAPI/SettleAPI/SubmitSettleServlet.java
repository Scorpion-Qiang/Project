package ServletAPI.SettleAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import MyException.TitleLengthErrorException;
import MyException.ContentNotException;
import MyException.ProblemNotFiledException;
import MyException.TitleNotException;
import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;
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
 * Date: 2022-10-18
 * Time: 16:16
 */
/*
* 提交题解
* url: '/submitsettle'
* 方法: 'post'
* 请求：SubmitSettleReq
* 响应：SubmitSettleResp
*
* */
@WebServlet("/submitsettle")
public class SubmitSettleServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf8");
        req.setCharacterEncoding("utf8");

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }

        UserDao userDao = new UserDao();


        SubmitSettleResp submitSettleResp = new SubmitSettleResp();
        // 1. 读取参数
        SubmitSettleReq submitSettleReq = mapper.readValue(req.getInputStream(), SubmitSettleReq.class);
        // 2. 校验参数
        try {
            // 校验题目
            String title = submitSettleReq.getTitle();
            if(title == null || title.trim().equals("")) {
                throw new TitleNotException();
            }
            if(title.length() > 20){
                throw new TitleLengthErrorException();
            }

            // 校验内容
            String content = submitSettleReq.getContent();
            if(content == null || content.trim().equals("")) {
                throw new ContentNotException();
            }

            // 校验problemId
            ProblemDao problemDao = new ProblemDao();
            Problem problem = problemDao.selectOne(submitSettleReq.getProblemId());
            if(problem == null){
                throw new ProblemNotFiledException();
            }

            SettleDao settleDao = new SettleDao();
            Settle settle = new Settle();
            settle.setUsername(user.getUsername());
            settle.setTitle(submitSettleReq.getTitle());
            settle.setContent(submitSettleReq.getContent());
            settle.setTime(new Timestamp(System.currentTimeMillis()));
            settle.setProblemId(problem.getId());
            settleDao.insert(settle);

            // 牛逼，也可以自己维护一个主键，像 新增评论，添加到 Index 那样
            int id = settleDao.selectUserSettleOneId(user.getUsername());
            submitSettleResp.setId(id);

        } catch (TitleNotException e) {
            submitSettleResp.setError(1);
            submitSettleResp.setReason("错误: 题目不可以为空!!!");
        } catch (ContentNotException e) {
            submitSettleResp.setError(1);
            submitSettleResp.setReason("错误: 内容不可以为空!!!");
        } catch (ProblemNotFiledException e) {
            submitSettleResp.setError(1);
            submitSettleResp.setReason("错误: 此题目不存在!!!");
        } catch (TitleLengthErrorException e){
            submitSettleResp.setError(1);
            submitSettleResp.setReason("错误: 题目长度过长(20个字符以内)!!!");
        }

        // 3. 返回响应
        String body = mapper.writeValueAsString(submitSettleResp);
        resp.getWriter().write(body);

    }
}
