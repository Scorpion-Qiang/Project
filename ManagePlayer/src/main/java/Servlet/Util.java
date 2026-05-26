package Servlet;

import MySQL.Team;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-21
 * Time: 18:50
 */
// 登录监测功能 监测是否登录过
public class Util {
    public static Team isLogin(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session == null){
            return null;
        }
        Team team = (Team) session.getAttribute("team");
        return team;
    }
}
