package com.marufalam.recyclerview;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fb;
    private ImageView imagePreview, cancelButton;
    private Button addButton;
    private EditText name, department, number;
    private MaterialCardView addImg;
    private DatabaseReference database;
    private ProgressDialog progressDialog;
    private final int REQ = 1;
    private Bitmap bitmap;
    private StorageReference storageReference;
    private String downloadUrl = "";
    private String fullName;
    private String departName;
    private String mobileNumber;
    private Dialog dialog;


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
        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = view.findViewById(R.id.recview);
        fb = view.findViewById(R.id.fadd);
        fb.setOnClickListener(view1 -> {
            Toast.makeText(requireActivity(), "Clicked", Toast.LENGTH_SHORT).show();
           dialog = new Dialog(view.getContext());

            dialog.setContentView(R.layout.dialogcontent);


            addImg = dialog.findViewById(R.id.uploadImage);
            name = dialog.findViewById(R.id.uname);
            department = dialog.findViewById(R.id.udepartment);
            number = dialog.findViewById(R.id.unumber);

            addButton = dialog.findViewById(R.id.usubmit);
            addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGallery();
                }
            });
            cancelButton = dialog.findViewById(R.id.cancel_button);
            imagePreview = dialog.findViewById(R.id.imagePreview);

            addButton.setOnClickListener(view22 -> {
                //String url = imageUrl.getText().toString().trim();
                fullName = name.getText().toString().trim();
                departName = department.getText().toString().trim();
                mobileNumber = number.getText().toString().trim();

                if (fullName.equalsIgnoreCase("")) {
                    name.setError("Enter A Valid Name");
                }
                if (departName.equalsIgnoreCase("")) {
                    department.setError("Enter A Valid Department");
                }
                if (mobileNumber.equalsIgnoreCase("")) {
                    number.setError("Enter A Valid Number");
                } else if (bitmap == null) {
                    uploadData();
                } else {
                    uploadImage();
                }

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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                Toast.makeText(requireActivity(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

    private void uploadData() {

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Add Data");
        progressDialog.show();


        final String id = database.push().getKey();
        StudentsModel model = new StudentsModel(id, downloadUrl, fullName, departName, mobileNumber);

        FirebaseDatabase.getInstance().getReference().child("students").child(id).setValue(model)
                .addOnSuccessListener(unused -> {
                    name.setText("");
                    number.setText("");
                    department.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Data Added Successfully", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Insert Failed", Toast.LENGTH_SHORT).show();
                });

        dialog.dismiss();
    }


    private void uploadImage() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        //compress image size
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("image").child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        //pick Image from Gallery
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePreview.setImageBitmap(bitmap);

        }
    }


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