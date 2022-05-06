package edu.ucsd.cse110.zooseeker.Plan;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.ucsd.cse110.zooseeker.MainActivity;
import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;
import edu.ucsd.cse110.zooseeker.RouteActivity;


public class PlanFragment extends Fragment implements View.OnClickListener {

    private Button startRoute;
    public RecyclerView planList;
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

        //planAdapter.setPlanItems(PlanItemDao.getAll());

        startRoute = view.findViewById(R.id.start_route_button);
        startRoute.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), RouteActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        startActivity(intent);
    }
}