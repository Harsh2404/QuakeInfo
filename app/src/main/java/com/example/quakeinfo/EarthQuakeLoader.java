package com.example.quakeinfo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public  class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {
    private String murl;

    public EarthQuakeLoader(@NonNull Context context,String url) {
        super(context);
        murl=url;
        System.out.println(murl);
    }

    @Override
    protected void onStartLoading() {
        Log.i("StartLoading","OnstartLoading of EarthQuakeLoader called");

        forceLoad();
    }


    @Nullable
    @Override
    public List<EarthQuake> loadInBackground() {
        Log.i("loadinBackground","load in background.....");

       //String requestURL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";
        URL url=createurl(murl);
        String jsonResponse = "";
        try {
            jsonResponse = MakeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        ArrayList<EarthQuake> earthquakes= QueryUtils.extractEarthquakes(jsonResponse);

        return earthquakes;
    }
    public String MakeHttpRequest(URL url) throws IOException {

        String JSONParsingString=null;
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;

        try {

            //generet HTTP request
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            //connect with network
            urlConnection.connect();

            //get inputStream from url connection
            inputStream=urlConnection.getInputStream();
            JSONParsingString=readFromStream(inputStream);


        }catch (IOException e){

        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }

            return JSONParsingString;
        }

    }
    public URL createurl(String urlstring){


        URL url = null;
        try {
            url=new URL(urlstring);

        }catch (MalformedURLException e){
            Log.e("MalformedException","error in creating url");
            return null;
        }
        return  url;
    }
    public String readFromStream(InputStream inputStream) throws IOException {


        StringBuilder output=new StringBuilder();

        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }

        return output.toString();
    }
}