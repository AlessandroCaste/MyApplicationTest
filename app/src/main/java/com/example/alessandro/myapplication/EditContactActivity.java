package com.example.alessandro.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
    }

    @Override
    protected void onResume() {

        super.onResume();

        int id = getIntent().getExtras().getInt("idContatto");

        if(id > 0) {
            /* Tutto è andato al meglio e i dati sono stati recuperati*/
            Intent i = new Intent();
            i.putExtra("nome","Paolo");
            i.putExtra("cognome","Preite");

            setResult(RESULT_OK,i);
        } else
            setResult(RESULT_CANCELED);


    }
}
