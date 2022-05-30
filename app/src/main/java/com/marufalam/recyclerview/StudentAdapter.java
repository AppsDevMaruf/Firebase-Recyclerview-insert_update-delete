package com.marufalam.recyclerview;

import static android.app.Activity.RESULT_OK;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ImageView imagePreview, cancelButton,uploadImage;
    private MaterialCardView UpdateImg;

    private Button addButton;
    private EditText name, department, number;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    StudentAdapter adapter;
    private DatabaseReference database;
    private ProgressDialog progressDialog;
    private final int REQ = 1;
    private Bitmap bitmap;
    private StorageReference storageReference;
    private String downloadUrl = "";
    private String fullName="";
    private String departName="";
    private String mobileNumber="";
    private Context context;
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
    public void onBindViewHolder(@NonNull StudentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //data set
// setting data over views
        StudentsModel models = studentList.get(position);
        if (models != null) {
            Glide.with(holder.profileImage.getContext())
                    .load(models.getProfileImg())
                    .centerCrop()
                    .placeholder(R.drawable.load_icon)
                    .into(holder.profileImage);
            holder.name.setText(models.getName());
            holder.department.setText(models.getDepartment());
            holder.number.setText(models.getNumber());
        }


      holder.delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                      .setTitle("Delete User")
                      .setIcon(R.drawable.ic_baseline_delete_24)
                      .setCancelable(false)
                      .setMessage("Are you sure?")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
                              assert models != null;
                              databaseReference.child(models.getId()).removeValue();
                              Toast.makeText(view.getContext(), "Delete Successfully"+models.getId(),Toast.LENGTH_LONG).show();

                             /* DatabaseReference itemRef = getRef(position);
                              final String myKeyItem = itemRef.getKey();*/
                          }
                      })
                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int i) {

                              dialog.dismiss();

                          }
                      });
              builder.show();

          }
      });

      holder.edit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(view.getContext(), "req", Toast.LENGTH_SHORT).show();
              Dialog dialog = new Dialog(view.getContext());

              dialog.setContentView(R.layout.updatedialogcontent);


              imagePreview = dialog.findViewById(R.id.imagePreview);
              name = dialog.findViewById(R.id.uname);
              department = dialog.findViewById(R.id.udepartment);
              number = dialog.findViewById(R.id.unumber);
              ShapeableImageView showPImage = dialog.findViewById(R.id.uproImg);
              number = dialog.findViewById(R.id.unumber);

              /*Glide.with(holder.profileImg.getContext())
                      .load(studentList.get(position).getProfileImg())
                      .centerCrop()
                      .placeholder(R.drawable.load_icon)
                      .into(showPImage);*/
              name.setText(studentList.get(position).getName());
              department.setText(studentList.get(position).getDepartment());
              number.setText(studentList.get(position).getNumber());

              addButton = dialog.findViewById(R.id.usubmit);
              addButton.setText("Update");
              cancelButton = dialog.findViewById(R.id.cancel_button);
              cancelButton.setOnClickListener(view1 -> {dialog.dismiss();});

              addButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      progressDialog = new ProgressDialog(view.getContext());
                      progressDialog.setTitle("Loading...");
                      progressDialog.setMessage("Update Data");
                      progressDialog.show();


                      String fullName = name.getText().toString().trim();
                      String departName = department.getText().toString().trim();
                      String mobileNumber = number.getText().toString().trim();

                      if(fullName.equalsIgnoreCase("")){
                          name.setError("Enter A Valid Name");
                      }
                      if(departName.equalsIgnoreCase("")){
                          department.setError("Enter A Valid Department");
                      }if(mobileNumber.equalsIgnoreCase("")){
                          number.setError("Enter A Valid Number");
                      }else {
                           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students").child(models.getId());
                            StudentsModel studentsModel = new StudentsModel(models.getId(),"url",fullName,departName,mobileNumber);
                            databaseReference.setValue(studentsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "Update Failed"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                }
                            });
                          dialog.dismiss();
                      }
                  }
              });


              dialog.setCancelable(false);
              dialog.show();
          }
      });






    }




    @Override
    public int getItemCount() {
        return studentList.size();
    }

    //viewHolder
    //initialize view
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView profileImage;
        ImageView edit,delete,uploadImage;
        ShapeableImageView profileImg;
        TextView name,department,number;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.proImg);
            name = itemView.findViewById(R.id.nameTv);
            department = itemView.findViewById(R.id.departmentTV);
            number = itemView.findViewById(R.id.phoneNumTv);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            profileImg = itemView.findViewById(R.id.uproImg);
            uploadImage = itemView.findViewById(R.id.uImg);


        }


    }


}
