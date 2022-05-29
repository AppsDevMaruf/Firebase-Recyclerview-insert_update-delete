package com.marufalam.recyclerview;

import java.util.ArrayList;
import java.util.List;

public class StudentsModel {
    private String id;
    private String profileImg;
    private  String name;
    private  String department;
    private  String number;

    public StudentsModel() {
    }

    public StudentsModel(String id, String profileImg, String name, String department, String number) {
        this.id = id;
        this.profileImg = profileImg;
        this.name = name;
        this.department = department;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static List<StudentsModel> getAllStudents(){

        List<StudentsModel> studentList = new ArrayList<>();

       /* studentList.add(new StudentsModel(R.drawable.mypiccircle,"Maruf Alam","CSE","+8801687422**"));
        studentList.add(new StudentsModel(R.drawable.maruf,"Maruf Alam Porosh ","BBA","+8801757422**"));
        studentList.add(new StudentsModel(R.drawable.doctor,"Lima","FN","+8801487422**"));
        studentList.add(new StudentsModel(R.drawable.doctor2,"Nuha","BBA","+8801387422**"));
        studentList.add(new StudentsModel(R.drawable.doctor3,"Miraz","ENG","+8801987422**"));
        studentList.add(new StudentsModel(R.drawable.doctor4,"Murad","BBA","+8801987422**"));
        studentList.add(new StudentsModel(R.drawable.ic_doctor,"Sharif","CSE","+8801787422**"));
        studentList.add(new StudentsModel(R.drawable.ic_profile,"Tayba","BBA","+8801687422**"));
        studentList.add(new StudentsModel(R.drawable.plant_doctor,"Fatema","CSE","+8809887422**"));
        studentList.add(new StudentsModel(R.drawable.krisokherhasi,"Zerin","BBA","+8803487422**"));
        studentList.add(new StudentsModel(R.drawable.krisokherhasi2,"Wafa","CSE","+8801687422**"));
        studentList.add(new StudentsModel(R.drawable.ic_user,"Rafi","EBG","+8801587422**"));
        studentList.add(new StudentsModel(R.drawable.ic_doctor_3,"Mafi","CSE","+8801587422**"));
        studentList.add(new StudentsModel(R.drawable.profile5,"Rayhan","MBA","+88019522**"));
        studentList.add(new StudentsModel(R.drawable.mypiccircle,"Morsed","CSE","+88013587422**"));
        studentList.add(new StudentsModel(R.drawable.mypiccircle,"Mostak Ahmed","JM","+8801587422**"));
        studentList.add(new StudentsModel(R.drawable.mypiccircle,"Nayon","EEE","+8801987422**"));*/

        return studentList;



    }

}
