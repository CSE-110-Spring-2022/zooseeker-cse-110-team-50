package edu.ucsd.cse110.zooseeker.Plan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.ucsd.cse110.zooseeker.Persistence.MainDatabase;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItem;
import edu.ucsd.cse110.zooseeker.Persistence.PlanItemDao;
import edu.ucsd.cse110.zooseeker.R;

public class PlanFragment extends Fragment {

    public RecyclerView planList;
    private PlanViewModel planViewModel;

    public static PlanFragment newInstance(){ return new PlanFragment();}

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

        return view;
    }

    public RecyclerView getPlanView(){return planList;}
}