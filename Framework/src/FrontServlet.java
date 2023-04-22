package etu1984.framework.servlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Enumeration;

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
            String nameClass = file.getName().replace(".class","");
            Class<?> c = Class.forName("modele."+nameClass);
            Method[] ms = c.getDeclaredMethods();
            for (Method m : ms) {
                Mapping temp = new Mapping();
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
        } 
        return null;
    }


    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        try {        
            String url = getUrl(out, req, resp);
            this.mappingUrls.forEach((key, value) -> {
                try{
                    if(key.equals(url)){
                        Class<?> c = Class.forName("modele."+value.getClassName());

                        ///ajouter
                        Enumeration<String> parameterNames = req.getParameterNames();
                        while (parameterNames.hasMoreElements()) {
                            String paramName = parameterNames.nextElement();
                            Method[] ms = c.getDeclaredMethods();
                            for (Method m : ms) {
                                String nameV1 = m.getName().replaceAll("set","");
                                nameV1 = nameV1.toLowerCase();
                                if(nameV1.equals(paramName)) {
                                    String[] paramValues = req.getParameterValues(paramName);
                                    for (int i = 0; i < paramValues.length; i++) {
                                        String paramValue = paramValues[i];
                                        out.println("Param: "+paramName+" : "+paramValue); 
                                        m.invoke(c.newInstance(), paramValue);
                                    }
                                } 
                            }
                        }
                        
                        ///view
                        Method[] ms = c.getDeclaredMethods();
                        for (Method m : ms) {
                            if(m.getName().equals(value.getMethod()) && m.invoke(c.newInstance()) instanceof ModelView) {
                                ModelView nomView = (ModelView) m.invoke(c.newInstance());
                                HashMap<String,Object> dataView = nomView.getData();
                                out.println("View:"+nomView.getUrl());
                                dataView.forEach((attribut,valeur) -> {
                                    out.println(attribut+"   "+valeur);
                                    req.setAttribute(attribut, valeur);
                                });
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