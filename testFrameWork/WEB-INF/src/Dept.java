package modele;

import etu1984.framework.annot.*;
public class Dept {
    @UrlA(url="dept-all")
    public String findAll() {
        return "findAll";
    }
}
