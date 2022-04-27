package com.marufalam.recyclerview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

             View view =    inflater.inflate(R.layout.fragment_home, container, false);

             recyclerView = view.findViewById(R.id.recyclerView);

             StudentAdapter adapter = new StudentAdapter(StudentsModel.getAllStudents());
             //1.LinearLayoutManager
            //2.GridLayoutManager:
            //3.StaggeredGridLayoutManager

             LinearLayoutManager llm = new GridLayoutManager(getActivity(),2);

             recyclerView.setLayoutManager(llm);

             recyclerView.setAdapter(adapter);


             return  view;
    }
}