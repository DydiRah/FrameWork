package etu1984.framework.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu1984.framework.Mapping;
import etu1984.framework.ModelView;
import etu1984.framework.annot.UrlA;

public class FrontServlet extends HttpServlet{

    HashMap<String,Mapping> mappingUrls = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            mappAllClass(getInitParameter("package"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void mappAllClass(String pack) throws Exception{ 
        File dir = new File(pack);
        File[] files = dir.listFiles();
        for (File file : files) {
            Mapping temp = new Mapping();
            String nameClass = file.getName().replace(".class","");
            Class<?> c = Class.forName("modele."+nameClass);
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                if(m.isAnnotationPresent(UrlA.class)) {
                    temp.setClassName(nameClass);
                    temp.setMethod(m.getName());
                    this.mappingUrls.put(m.getAnnotation(UrlA.class).url(), temp);
                }
            }
        }
    }

    public String getUrl(PrintWriter out,HttpServletRequest req, HttpServletResponse resp) {
        String requete = req.getRequestURI();
        String values = req.getQueryString();
        String[] infoReq = requete.split("/");
        if(infoReq.length == 3){
            return infoReq[2];
            //out.println("Page: " + infoReq[2]);
            //out.println("Les donnees sont:");
        } 
        return null;
        // if(values != null){
        //     String[] vals = values.split("&&");
        //     if(vals.length != 0){
        //         for (String val : vals) {
        //             out.println(val); 
        //         }
        //     }
        // }
    }


    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        try {        
            String url = getUrl(out, req, resp);
            this.mappingUrls.forEach((key, value) -> {
                try{
                    if(key.equals(url)){
                        Class<?> c = Class.forName("modele."+value.getClassName());
                        Method[] ms = c.getDeclaredMethods();
                        for (Method m : ms) {
                            if(m.getName().equals(value.getMethod())) {
                                ModelView nomView = (ModelView) m.invoke(c.newInstance());
                                out.println("view:"+nomView.getUrl());
                                RequestDispatcher rd = req.getRequestDispatcher(nomView.getUrl()+".jsp");
                                rd.forward(req,resp);   
                            }
                        }
                        out.println(key + " : " + value.getClassName() + " , " + value.getMethod());  
                    }
                }catch(Exception e){
                    out.println(e.getMessage());
                }
            });
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