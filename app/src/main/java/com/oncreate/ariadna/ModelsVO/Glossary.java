package com.oncreate.ariadna.ModelsVO;

import java.util.List;

public class Glossary {
    private String name;
    private List<GlossaryTerm> terms;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GlossaryTerm> getTerms() {
        return this.terms;
    }

    public void setTerms(List<GlossaryTerm> terms) {
        this.terms = terms;
    }
}
