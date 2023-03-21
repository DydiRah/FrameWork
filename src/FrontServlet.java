package etu1984.framework.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu1984.framework.Mapping;
import etu1984.framework.annot.UrlA;

public class FrontServlet extends HttpServlet{

    public void getUrl(PrintWriter out,HttpServletRequest req, HttpServletResponse resp) {
        String requete = req.getRequestURI();
        String values = req.getQueryString();
        String[] infoReq = requete.split("/");
        if(infoReq.length == 3){
            out.println("Page: " + infoReq[2]);
            out.println("Les donnees sont:");
        } 
        if(values != null){
            String[] vals = values.split("&&");
            if(vals.length != 0){
                for (String val : vals) {
                    out.println(val); 
                }
            }
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        try {        
            getUrl(out, req, resp);
        } catch (Exception e) {
            out.println(e.getMessage());
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}