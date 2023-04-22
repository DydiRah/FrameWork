package modele;

import etu1984.framework.ModelView;
import etu1984.framework.annot.*;
public class Emp {
    @UrlA(url="emp-all")
    public ModelView findAll() {
        return new ModelView("allEmp");
    }

    @UrlA(url = "emp-add")
    public ModelView addEmp() {
        return new ModelView("addEmp");
    }
}
