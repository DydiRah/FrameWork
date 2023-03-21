package modele;

import etu1984.framework.annot.*;
public class Emp {
    @UrlA(url="emp-all")
    public String findAll() {
        return "findAll";
    }
}
