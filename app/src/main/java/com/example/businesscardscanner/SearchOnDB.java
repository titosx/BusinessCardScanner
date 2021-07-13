package com.example.businesscardscanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SearchOnDB extends AppCompatActivity {

    Toolbar toolbar;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchondb);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchBtn = findViewById(R.id.search);;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.cardName)).getText().toString();
                String phone = ((EditText) findViewById(R.id.cardPhone)).getText().toString();
                String address = ((EditText) findViewById(R.id.cardAddress)).getText().toString();
                String city = ((EditText) findViewById(R.id.cardCity)).getText().toString();
                String email = ((EditText) findViewById(R.id.cardEmail)).getText().toString();
                String query = "";
                int atLeastOneIsNotEmpty = 0;

                if (!(name.isEmpty() && phone.isEmpty() && address.isEmpty() && city.isEmpty() && email.isEmpty())) {
                    if (!name.isEmpty()) {
                        query = "SELECT * FROM cards WHERE name LIKE '" + name + "%'";
                        atLeastOneIsNotEmpty = 1;
                    }

                    if (!phone.isEmpty() && atLeastOneIsNotEmpty==0) {
                        query = "SELECT * FROM cards WHERE phone LIKE '" + phone + "%'";
                        atLeastOneIsNotEmpty = 1;
                    }
                    else if (!phone.isEmpty() && atLeastOneIsNotEmpty==1) {
                        query = query + " AND phone LIKE '" + phone + "%'";
                    }

                    if (!address.isEmpty() && atLeastOneIsNotEmpty==0) {
                        query = "SELECT * FROM cards WHERE address LIKE '" + address + "%'";
                        atLeastOneIsNotEmpty = 1;
                    }
                    else if (!address.isEmpty() && atLeastOneIsNotEmpty==1) {
                        query = query + " AND address LIKE '" + address + "%'";
                    }

                    if (!city.isEmpty() && atLeastOneIsNotEmpty==0) {
                        query = "SELECT * FROM cards WHERE city LIKE '" + city + "%'";
                        atLeastOneIsNotEmpty = 1;
                    }
                    else if (!city.isEmpty() && atLeastOneIsNotEmpty==1) {
                        query = query + " AND city LIKE '" + city + "%'";
                    }

                    if (!email.isEmpty() && atLeastOneIsNotEmpty==0) {
                        query = "SELECT * FROM cards WHERE email LIKE '" + email + "%'";
                        atLeastOneIsNotEmpty = 1;
                    }
                    else if (!email.isEmpty() && atLeastOneIsNotEmpty==1) {
                        query = query + " AND email LIKE '" + email + "%'";
                    }
                    query = query + ";";

                    System.out.println(query);

                    DBSearchBackground DBSearchBackground = new DBSearchBackground(getApplicationContext());
                    DBSearchBackground.execute(query);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
