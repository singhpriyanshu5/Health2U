package com.example.priyanshu.health2u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ClinicSearch extends AppCompatActivity {

    AutoCompleteTextView auto_textView = null;
    ListView clinic_list;
    String user_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        user_name = getIntent().getStringExtra("user_name");

        clinic_list = (ListView)findViewById(R.id.clinic_list);
//        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,)
        final ArrayList<String> clinic_names = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            JSONArray features_arr = obj.getJSONArray("features");

            for (int i = 0; i < features_arr.length(); i++) {
                JSONObject j_obj = features_arr.getJSONObject(i);
                JSONObject properties_obj = j_obj.getJSONObject("properties");
                String name = properties_obj.getString("name");
                clinic_names.add(i,name);

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, clinic_names);
        auto_textView = (AutoCompleteTextView) findViewById(R.id.auto1);
        clinic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                auto_textView.setText(clinic_names.get((int) position));
            }
        });
        auto_textView.setAdapter(adapter);
        clinic_list.setAdapter(adapter);
        auto_textView.setThreshold(1);
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("clinicsjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clinic_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            Intent i = new Intent(this, ClinicDetail.class);
            i.putExtra("title", auto_textView.getText().toString());
            i.putExtra("address", "962 Jurong West Street 91, Singapore 640962");
            i.putExtra("user_name",user_name);
            Log.d("ClinicSearch", auto_textView.getText().toString());
            startActivity(i);
            return true;
        }

        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

