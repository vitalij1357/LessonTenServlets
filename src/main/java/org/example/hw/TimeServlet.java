package org.example.hw;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        String tzPar = req.getParameter("timezone");
        ZoneId zoneId;
        String zoneLable;
        if (tzPar==null || tzPar.isEmpty()){
             zoneId = ZoneOffset.UTC;
             zoneLable = "UTC";
        }else {
            zoneId = parseZoneId(tzPar);
            zoneLable = tzPar;
        }
        LocalDateTime dataTime = LocalDateTime.now(zoneId);
        String dataTimeFormat = dataTime.format(DateTimeFormatter.ofPattern(" yyyy-MM-dd, hh:mm:ss "));
        resp.getWriter().println(dataTimeFormat + zoneLable);
    }
    private ZoneId parseZoneId(String in){
        if (in.startsWith("UTC")){
            String hourString  = in.substring(3).trim();
            if (hourString.isEmpty()){
                return ZoneOffset.UTC;
            }
            int parseHours = Integer.parseInt(hourString);
            return ZoneOffset.ofHours(parseHours);
        }else {
            return ZoneId.of(in);
        }
    }
}
