package com.example.quakeinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    private EarthQuakeAdapter adapter;
    private String requestURL;
    public TextView empty_view;
    public ProgressBar progressBar;
    public ListView earthquakelistview;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","main activity created");

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        empty_view=(TextView)findViewById(R.id.empty_view);
        earthquakelistview=(ListView)findViewById(R.id.list);
        progressBar=(ProgressBar)findViewById(R.id.loading_icon);
        progressBar.setVisibility(View.VISIBLE);

        requestURL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";
        //data fetching using AsyncTask
//        EarthQuakeAsyncTask fetchdata=new EarthQuakeAsyncTask();
//        fetchdata.execute(requestURL);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected!=false){
            LoaderManager.getInstance(this).initLoader(1,null,this);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            empty_view.setText("No Internet Connection");
        }
//        getSupportLoaderManager().initLoader(1,null,this).forceLoad();


    }

    //Impliment data fetching using AsyncTask
//   private class EarthQuakeAsyncTask extends AsyncTask<String, Void, List<EarthQuake>> {
//
//        @Override
//        protected List<EarthQuake> doInBackground(String... strings) {
//            String requestURL=strings[0];
//            URL url=createurl(requestURL);
//            String jsonResponse = "";
//            try {
//                jsonResponse = MakeHttpRequest(url);
//            } catch (IOException e) {
//                // TODO Handle the IOException
//            }
//
//            ArrayList<EarthQuake> earthquakes= QueryUtils.extractEarthquakes(jsonResponse);
//
//            return earthquakes;
//        }
//
//       @Override
//        protected void onPostExecute(List<EarthQuake> earthquakes) {
//
//            if (earthquakes == null) {
//                return;
//            }
//
//            updateui(earthquakes);
//        }
//
//  }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("LoaderCreated","Loader created.....");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("starttime", "2014-01-01");
        uriBuilder.appendQueryParameter("endtime", "2014-12-01");
        uriBuilder.appendQueryParameter("limit", "25");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");


        return new EarthQuakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        progressBar.setVisibility(View.INVISIBLE);
        if (data == null) {
                return;
            }
        if(adapter!=null){
            adapter.clear();
        }
        Log.i("OnLoadFinished","OnLoadFinished......");
        earthquakelistview.setEmptyView(empty_view);
        empty_view.setText("No EarthQuake Found");
        updateui(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
        Log.i("OnLoadReset","Onload reset called....");
        adapter.clear();
    }


    public void updateui(final List<EarthQuake> earthquakes){


        adapter=new EarthQuakeAdapter(MainActivity.this,earthquakes);

        earthquakelistview.setAdapter(adapter);

        earthquakelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake currentearthuquak=earthquakes.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(currentearthuquak.getMdetail()));
                startActivity(browserIntent);


            }
        });
    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
