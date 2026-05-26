package ServletAPI.ProblemAPI;

import Operation.RecordOp.Record;
import Operation.RecordOp.RecordDao;
import MyException.NotMatchException;
import MyException.ProblemNotFiledException;
import MyException.RecordNotFiledException;
import ServletAPI.Util;
import Operation.UserOp.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import Operation.ProblemOp.Problem;
import Operation.ProblemOp.ProblemDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-09-13
 * Time: 16:15
 */

/*
* 返回题目列表 或 某个题目的详情页 或 某个提交记录的编辑页面
* 方法: GET
* URL: /problem?id=.. &preRecord=..
*      题目详情页，可以从题目列表页而来，也可以从提交记录详情页而来
*      id--题目id preRecode--提交记录的id
* 返回响应：如果是 返回题目列表页，就是 ProblemListResp
*         其它，返回Problem
*         错误信息放在响应头的ERRORREASON中
*
* */
@WebServlet("/problem")
public class ProblemServlet extends HttpServlet {
    // Jackson库的核心类
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProblemDao problemDao = new ProblemDao();
        RecordDao recordDao = new RecordDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }


        String param1 = req.getParameter("id");
        String param2 = req.getParameter("preRecord");
        Problem p = null;

        try {
            // 查询的
            if (param1 == null || param1.equals("")) {
                List<Problem> res = problemDao.selectAll();
                List<ProblemListResp> list = new ArrayList<>();

                System.out.println(res.size());
                for (Problem problem : res) {
                    ProblemListResp listResp = new ProblemListResp();
                    //  0--解答正确  1--做过但没通过  2--没做过
                    int status = recordDao.selectOneRe(problem.getId(), user.getId());
                    listResp.setId(problem.getId());
                    listResp.setLevel(problem.getLevel());
                    listResp.setTitle(problem.getTitle());
                    listResp.setStatus(status);
                    list.add(listResp);
                }

                resp.setContentType("application/json;charset=utf8");
                String respString = objectMapper.writeValueAsString(list);
                resp.getWriter().write(respString);
                return;
            } else {
                int id = Integer.parseInt(param1);
                p = problemDao.selectOne(id);
                // HTML 不认换行符\n\
                if (p == null) {
                    throw new ProblemNotFiledException();
                }

                if (param2 != null && !param2.equals("")) {
                    int preRecord = Integer.parseInt(param2);
                    Record record = recordDao.selectOneRe(preRecord);
                    if (record == null) {
                        throw new RecordNotFiledException();
                    }
                    if(record.getProblemId() != id){
                        throw new NotMatchException();
                    }
                    p.setTemplatecode(record.getContent());
                }
                p.setDescription(p.getDescription().replaceAll("\n", "<br/>"));
            }



        } catch (NumberFormatException e) {
            resp.setHeader("ERRORREASON", URLEncoder.encode("输入格式错误!!", "utf8"));
            return;
        } catch (ProblemNotFiledException e) {
            resp.setHeader("ERRORREASON", URLEncoder.encode("此题目不存在!!", "utf8"));
            return;
        } catch (RecordNotFiledException e){
            resp.setHeader("ERRORREASON", URLEncoder.encode("此提交记录不存在!!", "utf8"));
            return;
        } catch (NotMatchException e){
            resp.setHeader("ERRORREASON", URLEncoder.encode("此提交记录与题目不匹配!!", "utf8"));
            return;
        }


        resp.setContentType("application/json;charset=utf8");
        String respString = objectMapper.writeValueAsString(p);
        resp.getWriter().write(respString);
    }
}

