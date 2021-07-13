package com.example.businesscardscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import java.util.ArrayList;

public class DisplayDatabase extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    private DataBaseReturn db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaydb);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved Cards");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.displaydb_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(this, SearchOnDB.class);
                this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
