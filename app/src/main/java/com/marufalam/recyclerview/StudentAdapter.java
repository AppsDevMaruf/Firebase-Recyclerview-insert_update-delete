package com.marufalam.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
        int counter =0;
    List<StudentsModel> studentList = new ArrayList<>();

    public StudentAdapter(List<StudentsModel> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view Inflate
        counter++;
        Log.e("TAG", "onCreateViewHolder: "+counter);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_row,parent,false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        //data set
      holder.profileImage.setImageResource(studentList.get(position).getProfileImg());
      holder.name.setText(studentList.get(position).getName());
      holder.department.setText(studentList.get(position).getDepartment());
      holder.number.setText(studentList.get(position).getNumber());



    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    //viewHolder
    //initialize view
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView name,department,number;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.proImg);
            name = itemView.findViewById(R.id.nameTv);
            department = itemView.findViewById(R.id.departmentTV);
            number = itemView.findViewById(R.id.phoneNumTv);
        }
    }
}
