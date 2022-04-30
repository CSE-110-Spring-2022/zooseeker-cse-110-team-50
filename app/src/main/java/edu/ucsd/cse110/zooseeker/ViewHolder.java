package edu.ucsd.cse110.zooseeker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder{

    private final TextView textView;
    private ExhibitListItem exhibitItem;

    public ViewHolder(@NonNull View itemView){
        super(itemView);
        this.textView = itemView.findViewById(R.id.exhibit_item_txt);
    }

    public ExhibitListItem getExhibitItem(){return exhibitItem;}

    public void setExhibitItem(ExhibitListItem exhibitItem){
        this.exhibitItem = exhibitItem;
        this.textView.setText(exhibitItem.name);
    }
}
