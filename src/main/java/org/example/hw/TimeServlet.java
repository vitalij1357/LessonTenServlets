package org.example.hw;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.thymeleaf.context.WebContext;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tzPar = req.getParameter("timezone");
        if (tzPar == null || tzPar.isEmpty()) {
            // Якщо timezone не передано — шукаємо в cookie
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("lastTimezone")) {
                        tzPar = c.getValue();
                        break;
                    }
                }
            }
        }

        if (tzPar==null || tzPar.isEmpty()) {
            tzPar = "UTC";
        }

        ZoneId zoneId;

        try {
            zoneId = ZoneId.of(tzPar.replace(" ", "+"));
        } catch (Exception e) {
            zoneId = ZoneId.of("UTC");
            tzPar = "UTC";
        }
        LocalDateTime dataTime = LocalDateTime.now(zoneId);
        String dataTimeFormat = dataTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss"));

        Cookie timezoneCookie = new Cookie("lastTimezone",tzPar );
        timezoneCookie.setMaxAge(60 * 60 * 24 * 30);
        resp.addCookie(timezoneCookie);

        Context context = new Context(req.getLocale());
        context.setVariable("formattedTime",dataTimeFormat);
        context.setVariable("timezone", tzPar);
        engine.process("test", context, resp.getWriter());
    }
}
