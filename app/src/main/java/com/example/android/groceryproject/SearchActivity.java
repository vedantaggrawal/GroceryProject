package com.example.android.groceryproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    private final String JSON_REQUEST_URL="http://14.142.72.13/mandi/test.php";

    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar SearchToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(SearchToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView)findViewById(R.id.listview_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length() >2){

                    Toast.makeText(SearchActivity.this,newText,Toast.LENGTH_SHORT).show();
                    makeVolleyRequest(newText);


                }
                else{
                    listView.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

    }
    public void makeVolleyRequest(final String newText)
    {


        final List<String> searchList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.list_textview,searchList);

        listView.setAdapter(adapter);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_REQUEST_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listView.setVisibility(View.VISIBLE);

                //searchList.clear();
                Log.i("tagconvertstr", "*" + response + "*");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    Log.i("boolean", "*" + success + "*");
                    if(success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("search");
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String searchItemName = object.getString("name");
                            String searchItemCategory = object.getString("category");
                            String result = searchItemName+" in "+searchItemCategory;
                            searchList.add(result);

                        }

                    }
                    else
                    {
                        Toast.makeText(SearchActivity.this,"No result found",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();




            }
        }, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("item",newText);
                Log.i("params",""+params+"");
                return params;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);

    }

}
