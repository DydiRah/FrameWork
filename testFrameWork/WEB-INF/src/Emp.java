package modele;

import java.util.HashMap;

import etu1984.framework.ModelView;
import etu1984.framework.annot.*;
public class Emp {
    @UrlA(url = "emp-all")
    public ModelView findAll() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("nom", "Emp1");
        data.put("salaire", 10000);
        return new ModelView("allEmp",data);
    }

    @UrlA(url = "emp-add")
    public ModelView addEmp() {
        return new ModelView("addEmp");
    }
}
