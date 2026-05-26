package Servlet;

import MySQL.PlayerDao;
import MySQL.Team;
import MySQL.TeamDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-22
 * Time: 21:52
 */
@MultipartConfig
@WebServlet("/team_enroll")
public class TeamEnrollServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        // 1. 如果是登录状态的话，退出登录状态
        Team team = Util.isLogin(req);
        if(team != null){
            resp.sendRedirect("loginout");
            return;
        }

        // 2. 校验 请求传递的信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String passwordAgain = req.getParameter("passwordAgain");
        String verification = req.getParameter("verification");
        String introduce = req.getParameter("introduce");

        // 处理文件
        Part part = req.getPart("MyFile");

        if(username == null || username.equals("") || password == null || password.equals("")){
            resp.getWriter().write("<h3>用户名或密码为空</h3>");
            return;
        }
        if(passwordAgain == null || passwordAgain.equals("")){
            resp.getWriter().write("<h3>再输入一次密码</h3>");
            return;
        }
        if(verification == null || verification.equals("")){
            resp.getWriter().write("<h3>验证码为空</h3>");
            return;
        }
        if(introduce == null || introduce.equals("")){
            resp.getWriter().write("<h3>个人简介不能为空</h3>");
            return;
        }

        if(part == null){
            resp.getWriter().write("<h3>上传头像不可以为空</h3>");
            return;
        }

        // 处理文件
        String uppath = "";
        String fileFullName = part.getSubmittedFileName();

        String dataName = String.valueOf(System.currentTimeMillis());

        String type = fileFullName.substring(fileFullName.lastIndexOf(".") + 1);


        uppath = "/root/apache-tomcat-8.5.76/webapps/ManagePlayer/image/" + dataName + "." + type;
        part.write(uppath);

        // 3. 判断能否注册
        Team teamF = TeamDao.selectByName("username");
        if(teamF != null){
            resp.getWriter().write("<h3>此用户名已经存在，请重新输入<h3>");
            return;
        }
        if(!password.equals(passwordAgain)){
            resp.getWriter().write("<h3>两次输入的密码不一致</h3>");
            return;
        }
        String verifica = "青岛科技大学";
        if(!verification.equals(verifica)){
            resp.getWriter().write("<h3>验证码错误</h3>");
            return;
        }

        // 4. 注册
        Team teamNew = new Team();
        teamNew.setTeamName(username);
        teamNew.setPassword(password);
        teamNew.setIntroduce(introduce);

        String path = "image/" + dataName + "." + type;
        teamNew.setPhotoPath(path);

        TeamDao.insert(teamNew);

        // 5. 重定向到 player_goto.html
        resp.sendRedirect("player_goto.html");
    }
}
