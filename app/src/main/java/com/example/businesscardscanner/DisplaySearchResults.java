package com.example.businesscardscanner;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DisplaySearchResults extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    DataBaseReturn db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaydb);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Results");
        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DataBaseReturn db = (DataBaseReturn) this.getIntent().getSerializableExtra("File");
        ArrayList<CardItem> cardList = new ArrayList<CardItem>();
        int i=0;
        while (i!=db.names.size()) {
            cardList.add(new CardItem(db.names.get(i), db.phones.get(i), db.addresses.get(i), db.cities.get(i), db.emails.get(i)));
            i++;
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CardAdapter(cardList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}