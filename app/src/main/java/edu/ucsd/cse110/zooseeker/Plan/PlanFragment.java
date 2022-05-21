package edu.ucsd.cse110.zooseeker.Plan;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.Route.RouteActivity;

public class PlanFragment extends Fragment{

    private FloatingActionButton startRoute;
    public RecyclerView planList;
    public TextView planAmount;
    private PlanViewModel planViewModel;

    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_plan, container, false);

        //Sets up planviewmodel and the planlist to be looked at when plan is hit
        planViewModel = new ViewModelProvider(requireActivity()).get(PlanViewModel.class);
        PlanAdapter planAdapter = new PlanAdapter();
        planAdapter.setHasStableIds(true);



        //PlanItemDao planItemDao = MainDatabase.getSingleton(this).planItemDao();
        //List<PlanItem> planListItems = PlanItemDao.getAll();


        //planAdapter.setPlanItems(planListItems);

        planViewModel.getPlanItems().observe(getViewLifecycleOwner(), planAdapter::setPlanItems);

        planList = view.findViewById(R.id.plan);
        planList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        planList.setAdapter(planAdapter);
        FloatingActionButton startPlan = view.findViewById(R.id.start_route_button);

        Button deletePlan = view.findViewById(R.id.delete_all_plans_button);

        startPlan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startPlan(v);
            }
        });

        deletePlan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deletePlan(v);
            }
        });



        planAmount = view.findViewById(R.id.plan_amount);
        int amount = planViewModel.getPlanCount();
        //int amount = planViewModel.getPlanItems().observe(getViewLifecycleOwner(), planAdapter::getItemCount);
//        if(amount == 1){
//            planAmount.setText(amount + " Exhibit in Plan");
//        }
        //else{
        planAmount.setText("Exhibit Count: " + amount);
        //}
        //planAmount.setText("Setting the text of the view, this is a test");
        //planAdapter.setPlanItems(PlanItemDao.getAll());

        return view;
    }

    public RecyclerView getPlanView(){return planList;}

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        final View view = inflater.inflate(R.layout.fragment_plan, container, false);
//
//        startRoute = view.findViewById(R.id.start_route_button);
//        startRoute.setOnClickListener(this);
//        return view;
//    }


    public void startPlan(View view){
        Intent intent = new Intent(getActivity(), RouteActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        Log.d("HI", "HIHI");
        startActivity(intent);
    }

    public void deletePlan(View view){
        planViewModel.nukePlanItems();
        planAmount.setText("Exhibit Count: 0");
    }
}