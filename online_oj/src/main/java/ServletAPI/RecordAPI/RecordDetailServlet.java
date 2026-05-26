package ServletAPI.RecordAPI;

import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;
import ServletAPI.Util;
import MyException.RecordNotFiledException;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.RecordOp.Record;
import Operation.RecordOp.RecordDao;

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
 * Date: 2022-09-24
 * Time: 19:00
 */

/*
* 获取提交记录的详细信息
* 方法: GET
* URL: /recordDetail?id=..
* 返回响应:
*          error = 1;  id == null || id 为空 || id 指定的记录不存在 || 不是此用户提交的记录, 无权查看
*
*          id 没有问题，error = 0; 更新其他的信息
*          {
*               error:           // 0--查询成功  1--查询失败
*               reason:
*               problemtitle:
*               status:          // 0--未通过 1--已通过
*               time:
*               content:
*           }
*
* */

@WebServlet("/recordDetail")
public class RecordDetailServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RecordDao recordDao = new RecordDao();

        ProblemDao problemDao = new ProblemDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            resp.sendRedirect("login.html");
            return;
        }

        UserDao userDao = new UserDao();

        // 返回的响应
        RecordDetailResp recordDetailResp = new RecordDetailResp();

        // 1. 获取参数
        String param = req.getParameter("id");
        try {
            // 2. 校验参数
            // 校验 recordId 格式是否合法
            int recordId = Integer.parseInt(param);
            // 校验 recordId 对应的记录是否存在
            Record record = recordDao.selectOneRe(recordId);
            if(record == null){
                throw new RecordNotFiledException();
            }
            Problem p = problemDao.selectOne(record.getProblemId());

            recordDetailResp.setProblemId(p.getId());
            recordDetailResp.setProblemtitle(p.getTitle());
            recordDetailResp.setStatus(record.getStatus());
            recordDetailResp.setTime(record.getTime());
            recordDetailResp.setContent(record.getContent());

            recordDetailResp.setErrorReason(record.getErrorReason());
            recordDetailResp.setAllExample(p.getTestExample());
            recordDetailResp.setPassExample(record.getPassExample());
            recordDetailResp.setFinalInput(record.getFinalInput());


        } catch (NumberFormatException e){
            resp.getWriter().write("输入格式错误!!!");
            return;
        } catch (RecordNotFiledException e){
            resp.getWriter().write("此提交记录不存在!!!");
            return;
        }

        // 3. 返回响应
        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(recordDetailResp);
        resp.getWriter().write(body);
    }
}
