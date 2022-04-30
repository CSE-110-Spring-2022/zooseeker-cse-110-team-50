package edu.ucsd.cse110.zooseeker.ExhibitList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.zooseeker.R;

public class ExhibitListAdapter extends RecyclerView.Adapter<ExhibitListAdapter.ViewHolder> {
    private List<ExhibitListItem> exhibitItems = Collections.emptyList();
    private Consumer<ExhibitListItem> onCheckBoxClicked;

    public void setExhibitListItems(List<ExhibitListItem> newExhibitItems) {
        this.exhibitItems.clear();
        this.exhibitItems = newExhibitItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.exhibit_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibitItem(exhibitItems.get(position));
    }

    @Override
    public int getItemCount() {
        return exhibitItems.size();
    }

    @Override
    public long getItemId(int position) { return exhibitItems.get(position).id; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
//        private final CheckBox checkBox;
        private ExhibitListItem exhibitItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.textView = itemView.findViewById(R.id.exhibit_item_txt);
//            this.checkBox = itemView.findViewById(R.id.checked);
        }

//        this.checkBox.setOnClickedListener(view -> {
//            if (onCheckBoxClicked)
//        })

        public ExhibitListItem getExhibitItem(){ return exhibitItem; }

        public void setExhibitItem(ExhibitListItem exhibitItem){
            this.exhibitItem = exhibitItem;
            this.textView.setText(exhibitItem.name);
        }
    }
}
