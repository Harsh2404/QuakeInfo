package com.example.quakeinfo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemview=convertView;
        if(listitemview == null){
            listitemview= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        EarthQuake currentEartquake=getItem(position);

        //amplitude
        DecimalFormat amplitudeformatter = new DecimalFormat("0.0");
        TextView amplitude=(TextView)listitemview.findViewById(R.id.amplitude);
        amplitude.setText(amplitudeformatter.format(currentEartquake.getMamplitude()));

        //amplitude-cicle-color
        GradientDrawable magnitudeCircle = (GradientDrawable) amplitude.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEartquake.getMamplitude());
        magnitudeCircle.setColor(magnitudeColor);

        //location
        final String LOCATION_SEPARATOR = " of ";

        String originalLocation = currentEartquake.getMplace();
        String primaryLocation;
        String locationOffset;

        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts=originalLocation.split(LOCATION_SEPARATOR);
            locationOffset=parts[0]+LOCATION_SEPARATOR;
            primaryLocation=parts[1];
        }else {
                locationOffset="near The";
                primaryLocation=originalLocation;
        }


        TextView placeoffset=(TextView)listitemview.findViewById(R.id.placeoffset);
        placeoffset.setText(locationOffset);
        TextView placeprimary=(TextView)listitemview.findViewById(R.id.placeprimary);
        placeprimary.setText(primaryLocation);


        //dateobject in miliseconds
        Date dateObject = new Date(currentEartquake.getMmtimeinms());

        //date
        TextView dateView = (TextView) listitemview.findViewById(R.id.date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate =  dateFormat. format(dateObject);

        dateView.setText(formattedDate);

        //time

        TextView timeView = (TextView) listitemview.findViewById(R.id.time);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedtime =  timeFormat. format(dateObject);
        timeView.setText(formattedtime);

        return listitemview;
    }

    public EarthQuakeAdapter(@NonNull Context context, @NonNull List<EarthQuake> objects) {
        super(context, 0, objects);
    }
}
