package com.oncreate.ariadna.Adapters;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SectionAdapter<VH extends ViewHolder> extends Adapter<VH> {
    private boolean isUpdating;
    private int itemCount;
    private ArrayList<Section> sections;

    public static abstract class Section<T> {
        private int position;

        public abstract List<T> getItems();
    }

    public SectionAdapter() {
        this.sections = new ArrayList();
    }

    public final int getItemCount() {
        return this.itemCount;
    }

    public Object getItemAtPosition(int position) {
        Iterator it = this.sections.iterator();
        while (it.hasNext()) {
            Section section = (Section) it.next();
            if (position == section.position) {
                return section;
            }
            if (position > section.position && position <= section.position + section.getItems().size()) {
                return section.getItems().get((position - section.position) - 1);
            }
        }
        return null;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        int index = this.itemCount;
        section.position = index;
        int newItems = section.getItems().size() + 1;
        this.itemCount += newItems;
        if (!this.isUpdating) {
            notifyItemRangeInserted(index, newItems);
        }
    }

    public void removeLastSection() {
        if (!this.sections.isEmpty()) {
            Section section = (Section) this.sections.get(this.sections.size() - 1);
            this.sections.remove(section);
            int removedItems = section.getItems().size() + 1;
            this.itemCount -= removedItems;
            if (!this.isUpdating) {
                notifyItemRangeRemoved(section.position, removedItems);
            }
        }
    }

    public void clearSections() {
        this.sections.clear();
        this.itemCount = 0;
        if (!this.isUpdating) {
            notifyDataSetChanged();
        }
    }

    protected void startUpdate() {
        this.isUpdating = true;
    }

    protected void finishUpdate() {
        if (this.isUpdating) {
            this.isUpdating = false;
            notifyDataSetChanged();
        }
    }
}
