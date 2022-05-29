package com.marufalam.recyclerview;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fb;
    ImageView cancelButton;
    private Button addButton;
    private EditText imageUrl, name, department, number;
    DatabaseReference database;
    ProgressDialog progressDialog;


    private List<StudentsModel> studentsList = new ArrayList<>();
    private StudentAdapter adapter = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getActivity());
        database = FirebaseDatabase.getInstance().getReference();

        recyclerView = view.findViewById(R.id.recview);
        fb = view.findViewById(R.id.fadd);
        fb.setOnClickListener(view1 -> {
            Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            Dialog dialog = new Dialog(view.getContext());

            dialog.setContentView(R.layout.dialogcontent);


            imageUrl = dialog.findViewById(R.id.uimgurl);
            name = dialog.findViewById(R.id.uname);
            department = dialog.findViewById(R.id.udepartment);
            number = dialog.findViewById(R.id.unumber);

            addButton = dialog.findViewById(R.id.usubmit);
            cancelButton = dialog.findViewById(R.id.cancel_button);

            addButton.setOnClickListener(view22 -> {
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Add Data");
                progressDialog.show();

                String url = imageUrl.getText().toString().trim();
                String fullName = name.getText().toString().trim();
                String departName = department.getText().toString().trim();
                String mobileNumber = number.getText().toString().trim();

                if(fullName.equalsIgnoreCase("")){
                    name.setError("Enter A Valid Name");
                }if(url.equalsIgnoreCase("")){
                    imageUrl.setError("Enter A Valid URl");
                }
                if(departName.equalsIgnoreCase("")){
                    department.setError("Enter A Valid Department");
                }if(mobileNumber.equalsIgnoreCase("")){
                    number.setError("Enter A Valid Number");
                }else {
                    final String id = database.push().getKey();
                    StudentsModel model = new StudentsModel(id,url,fullName,departName,mobileNumber);

                    FirebaseDatabase.getInstance().getReference().child("students").child(id).setValue(model)
                            .addOnSuccessListener(unused -> {
                                name.setText("");
                                number.setText("");
                                department.setText("");
                                imageUrl.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(requireActivity(), "Data Added Successfully", Toast.LENGTH_SHORT).show();

                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(requireActivity(), "Insert Failed", Toast.LENGTH_SHORT).show();
                            });

                }
                dialog.dismiss();
            });


            dialog.setCancelable(false);
            dialog.show();

            cancelButton.setOnClickListener(view2 -> dialog.dismiss());
        });



        //1.LinearLayoutManager
        //2.GridLayoutManager:
        //3.StaggeredGridLayoutManager

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(llm);

        database = FirebaseDatabase.getInstance().getReference().child("students");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentsList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    StudentsModel studentsModel = dataSnapshot.getValue(StudentsModel.class);
                    studentsList.add(studentsModel);
                }
                adapter = new StudentAdapter(studentsList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireActivity(), "Error:"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }
 /*   private void processinsert()
    {
        Map<String,Object> map=new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("department",department.getText().toString());
        map.put("number",number.getText().toString());
        map.put("purl",imageUrl.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("students").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.setText("");
                        department.setText("");
                        number.setText("");
                        imageUrl.setText("");
                        Toast.makeText(requireActivity(),"Inserted Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(requireActivity(),"Could not insert",Toast.LENGTH_LONG).show();
                    }
                });

    }*/
}