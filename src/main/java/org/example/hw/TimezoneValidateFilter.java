package org.example.hw;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");
        if (timezone == null || timezone.isEmpty()||validTime(timezone)){
            chain.doFilter(req,res);
        }else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("text/html");
            res.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
        }

    }
    private boolean validTime(String in){
        if (in.startsWith("UTC")){
            String hoursString = in.substring(3).trim();
            if (hoursString.isEmpty()){
                return true;
            }
                Integer.parseInt(hoursString);
                return true;
            } else {
            return false;
        }
    }
}
