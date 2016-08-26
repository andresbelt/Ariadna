package com.oncreate.ariadna.ModelsVO;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private List<Glossary> glossary;
    private List<Group> groups;
    private int id;
    private ArrayList<Module> modules;
    private String name;
    private String tags;
    private int version;

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int value) {
        this.version = value;
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public void setModules(ArrayList<Module> value) {
        this.modules = value;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
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

    public String getTags() {
        return this.tags;
    }
}
