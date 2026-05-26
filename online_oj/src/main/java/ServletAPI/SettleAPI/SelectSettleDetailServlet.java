package ServletAPI.SettleAPI;

import MyException.SettleNotFiledException;
import ServletAPI.Util;
import Operation.SettleOp.Settle;
import Operation.SettleOp.SettleDao;
import Operation.UserOp.User;
import Operation.UserOp.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-10-18
 * Time: 20:04
 */
/*
* 查看某个题解的详细信息
* url: '/settledetail?id=....'
* 方法；'get'
* 响应：SelectSettleDetailResp
*
* */
@WebServlet("/settledetail")
public class SelectSettleDetailServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        SettleDao settleDao = new SettleDao();

        // 监测登录
        User user = Util.isLogin(req);
        if(user == null){
            Util.sendRedirect(req, resp);
            return;
        }
        UserDao userDao = new UserDao();


        // 1. 获取参数
        String param = req.getParameter("id");

        SelectSettleDetailResp selectSettleDetailResp = new SelectSettleDetailResp();
        // 2. 校验参数
        try {
            int id = Integer.parseInt(param);

            Settle settle = settleDao.selectOneSettle(id);
            if (settle == null) {
                throw new SettleNotFiledException();
            }

            selectSettleDetailResp.setId(settle.getId());
            selectSettleDetailResp.setUsername(settle.getUsername());

            User u = userDao.selectOne(settle.getUsername());
            selectSettleDetailResp.setUserphoto(u.getPhoto());

            selectSettleDetailResp.setTitle(settle.getTitle());
            selectSettleDetailResp.setContent(settle.getContent());

            selectSettleDetailResp.setTime(settle.getTime());


        } catch (NumberFormatException e){
            resp.setHeader("ERRORREASON", URLEncoder.encode("输入格式错误!!!", "utf8"));
            return;
        } catch (SettleNotFiledException e) {
            resp.setHeader("ERRORREASON", URLEncoder.encode("此题解不存在!!!", "utf8"));
            return;
        }

        resp.setContentType("application/json;charset=utf8");
        String body = mapper.writeValueAsString(selectSettleDetailResp);
        resp.getWriter().write(body);
    }
}
