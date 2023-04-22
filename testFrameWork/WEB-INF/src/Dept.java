package modele;

import etu1984.framework.annot.*;
import etu1984.framework.*;
public class Dept {
    @UrlA(url="dept-all")
    public ModelView findAll() {
        return new ModelView("allDept");
    }
}
