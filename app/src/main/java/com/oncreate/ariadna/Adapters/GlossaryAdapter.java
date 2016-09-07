//package com.oncreate.ariadna.Adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView
//
//import com.oncreate.ariadna.ModelsVO.Glossary;
//import com.oncreate.ariadna.ModelsVO.GlossaryTerm;
//import com.oncreate.ariadna.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GlossaryAdapter extends SectionAdapter<ViewHolder> {
//    private static final int TYPE_HEADER = 0;
//    private static final int TYPE_ITEM = 1;
//    protected Context context;
//    protected List<Glossary> glossary;
//
//    public class NormalViewHolder extends ViewHolder {
//        private TextView glossaryTerm;
//        private TextView glossaryText;
//
//        public NormalViewHolder(View itemView) {
//            super(itemView);
//            this.glossaryTerm = (TextView) itemView.findViewById(R.id.glossary_item_term);
//            this.glossaryText = (TextView) itemView.findViewById(R.id.glossary_item_text);
//        }
//
//        public void bind(GlossaryTerm term) {
//            this.glossaryTerm.setText(term.getTerm());
//            this.glossaryText.setText(term.getText());
//        }
//    }
//
//    public static class Section extends SectionAdapter.Section<GlossaryTerm> {
//        private List<GlossaryTerm> items;
//        private String name;
//
//        public String getName() {
//            return this.name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public List<GlossaryTerm> getItems() {
//            return this.items;
//        }
//
//        public Section(String name, List<GlossaryTerm> items) {
//            this.items = new ArrayList();
//            this.name = name;
//            this.items = items;
//        }
//    }
//
//    public class SectionViewHolder extends ViewHolder {
//        private TextView sectionName;
//
//        public SectionViewHolder(View itemView) {
//            super(itemView);
//            this.sectionName = (TextView) itemView.findViewById(R.id.glossary_section_name);
//        }
//
//        public void bind(Section section) {
//            this.sectionName.setText(section.getName());
//        }
//    }
//
//    public GlossaryAdapter(Context context, List<Glossary> list) {
//        this.glossary = list;
//        this.context = context;
//        for (Glossary group : list) {
//            addSection(new Section(group.getName(), group.getTerms()));
//        }
//    }
//
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == 0) {
//            return new SectionViewHolder(LayoutInflater.from(this.context).inflate(R.layout.glossary_section_item, parent, false));
//        }
//        return new NormalViewHolder(LayoutInflater.from(this.context).inflate(R.layout.glossary_item, parent, false));
//    }
//
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        if (holder instanceof NormalViewHolder) {
//            ((NormalViewHolder) holder).bind((GlossaryTerm) getItemAtPosition(position));
//        } else if (holder instanceof SectionViewHolder) {
//            ((SectionViewHolder) holder).bind((Section) getItemAtPosition(position));
//        }
//    }
//
//    public int getItemViewType(int position) {
//        if (getItemAtPosition(position) instanceof Section) {
//            return TYPE_HEADER;
//        }
//        return TYPE_ITEM;
//    }
//}
