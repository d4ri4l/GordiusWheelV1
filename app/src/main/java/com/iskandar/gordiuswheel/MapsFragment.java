package com.iskandar.gordiuswheel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.geojson.GeoJsonPointStyle;

import org.json.JSONException;

import java.io.IOException;


public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String urlrutas; //Almacena la URL por default de los archivos .kml

    private String name; //Almacena el nombre del archivo .kml
    private String idd;

    RequestQueue rq;
    JsonRequest jrq;
    GeoJsonLayer layer;
    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(24.124040, -110.311814);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        CameraUpdate update = CameraUpdateFactory.newLatLng(position);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

        try {
            layer = new GeoJsonLayer(mMap, R.raw.map, getContext());
            addColorsToMarkers(layer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(GeoJsonFeature geoJsonFeature) {
                Toast.makeText(getContext(), "Ruta Seleccionada: "+geoJsonFeature.getProperty("Name"), Toast.LENGTH_SHORT).show();
            }
        });

        mMap.moveCamera(update);
        mMap.animateCamera(zoom);
    }

    private void addColorsToMarkers(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : layer.getFeatures()) {
            // Check if the magnitude property exists
            if (feature.getProperty("stroke") != null) {

                // Create a new point style
                GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();

                // Set options for the point style
                // Assign the point style to the feature
                lineStringStyle.setColor(Color.parseColor(feature.getProperty("stroke")));
                feature.setLineStringStyle(lineStringStyle);
            }
        }
        layer.addLayerToMap();
    }
}
