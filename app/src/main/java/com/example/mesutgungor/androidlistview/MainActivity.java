package com.example.mesutgungor.androidlistview;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;



import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    ArrayList<Haber> haberlistesi = new ArrayList<Haber>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        ArrayList<Haber> haberlistesi = new ArrayList<Haber>();

        //Activity oluşturulduğunda haberleri getirmesi için arka plan görevini çağırıyoruz..
        new getirHaberleri().execute();


        //Listview in başındayken aşağı doğru swipe yaptığımızda haberleri güncellemesini sağlar.
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                       new getirHaberleri().execute();
                    }
                }
        );

        //SwipeRefresh güncelleme yaparken renk değiştirmesi için eklenen kod.

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);



    }






    public class getirHaberleri extends AsyncTask<Void, Integer, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            int i=0;
            StringBuilder result = new StringBuilder();

            //Try bloğunda hurriyet apisine bağlanıyoruz ve haberleri içeren json stringini oluşturuyoruz.
            try {
                URL url = new URL(Config.BaseUrl + "articles?apikey=" + Config.ApiKey);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    i=i+100;
                    publishProgress(i);
                  // sleep(10000);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();

        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            if (result != null) {

                //Try Bloğunda json stringini parse ediyoruz.

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    haberlistesi.removeAll(haberlistesi);

                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if (c != null) {

                                String habertarihi = c.getString("CreatedDate");
                                String habericerigi = c.getString("Description");
                                String haberkategorisi = c.getString("Path");
                                String haberbasligi = c.getString("Title");
                                String haberurl = c.getString("Url");

                                JSONArray files = c.getJSONArray("Files");
                                String haberresmiurl = "";
                                if (files.length() != 0) {
                                    JSONObject joFiles = files.getJSONObject(0);
                                    if (joFiles != null) {

                                        haberresmiurl = joFiles.getString("FileUrl");
                                    }
                                } else {

                                    haberresmiurl ="@drawable/sondakika";

                                }

                                Haber haberin = new Haber(haberresmiurl,  haberkategorisi,  habericerigi, haberurl, habertarihi, haberbasligi);
                                haberlistesi.add(haberin);

                            }
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Internet Bağlantı Hatası" ,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Internet Bağlantı Hatası.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

        if(haberlistesi.size()>0)
        {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
            ListView lv = (ListView)findViewById(R.id.listView);
            HaberAdapter ha = new HaberAdapter(getApplicationContext(),R.layout.list_view_row_model,haberlistesi);
            lv.setAdapter(ha);

            swipeRefreshLayout.setRefreshing(false);

            lv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String haberurl = haberlistesi.get(position).getHaberUrl();
                            Intent intent = new Intent(getApplicationContext(),HaberDetay.class);
                            intent.putExtra("HABERURL",haberurl);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);

                        }
                    }
            );


        }

        // 5 Dakikada bir haberleri getirmek için eklenen kod.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getirHaberleri().execute();
                }
            }, 5*60*1000);


        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

    }


}
