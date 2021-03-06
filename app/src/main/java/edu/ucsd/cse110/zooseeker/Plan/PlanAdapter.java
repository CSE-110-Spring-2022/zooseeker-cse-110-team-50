package edu.ucsd.cse110.zooseeker.Plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.Place;
import edu.ucsd.cse110.zooseeker.Persistence.PlaceDao;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.R;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder>{
    private List<PlanItem> planItems = Collections.emptyList();

    MainDatabase db ;
    PlaceDao placeDao;

    @NonNull
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);
        db = MainDatabase.getSingleton(parent.getContext());
        placeDao = db.placeDao();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ViewHolder holder, int position) {
        holder.setPlanItem(planItems.get(position));
    }

    public void setPlanItems(List<PlanItem> planItems){
        this.planItems = planItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return planItems.size();
    }

    public int getItemCount(List<PlanItem> planItems) {
        return planItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private PlanItem plan;
        private TextView name;
        private double distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.plan_item);
        }

        public void setPlanItem(PlanItem plan){
            this.plan = plan;
            Place place = placeDao.get(plan.placeId);

            this.name.setText(place.name);
        }
        
    }
}
