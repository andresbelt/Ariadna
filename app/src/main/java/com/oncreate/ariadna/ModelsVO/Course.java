package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private List<Glossary> glossary;
    private String id;
    private ArrayList<Module> modules;
    private String titulo;

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String value) {
        this.titulo = value;
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public void setModules(ArrayList<Module> value) {
        this.modules = value;
    }

    public List<Glossary> getGlossary() {
        return this.glossary;
    }

    public void setGlossary(List<Glossary> glossary) {
        this.glossary = glossary;
    }

    public boolean isGlossaryAvailable() {
        return this.glossary != null && this.glossary.size() > 0;
    }

    public Module getModule(int index) {
        return (Module) this.modules.get(index);
    }

    public int getModuleCount() {
        return this.modules.size();
    }

}
