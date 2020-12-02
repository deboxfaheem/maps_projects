package com.example.map_projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.map_projects.Adapter.SendAdapter;
import com.example.map_projects.model.Demo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetRecyclerview_data_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    SendAdapter sendAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<Demo> demolis;
    ArrayList<Demo> olddata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_recyclerview_data_);
        recyclerView = findViewById(R.id.recycleview);
        demolis = new ArrayList<>();
        olddata = new ArrayList<>();
        demolis= (ArrayList<Demo>) getIntent().getSerializableExtra("selectedList");
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        sendAdapter = new SendAdapter(this, demolis);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sendAdapter);
        olddata.addAll(demolis);
    }
}