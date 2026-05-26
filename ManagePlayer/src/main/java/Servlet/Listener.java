package Servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 强仔
 * Date: 2022-03-21
 * Time: 10:54
 */
@WebListener
public class Listener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent scr) {
        ServletContext context = scr.getServletContext();

        // 模板引擎初始化
        TemplateEngine engine = new TemplateEngine();
        // 模板解析器
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(context);
        resolver.setPrefix("WEB-INF/template/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("utf-8");

        engine.setTemplateResolver(resolver);
        // 把模板引擎放到上下文对象中
        context.setAttribute("TemplateEngine", engine);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
