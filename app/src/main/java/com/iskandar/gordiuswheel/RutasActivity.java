package com.iskandar.gordiuswheel;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.google.maps.android.kml.KmlLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RutasActivity extends FragmentActivity implements OnMapReadyCallback, Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String TAG = RutasActivity.class.getSimpleName();

    private GoogleMap mMap;
    private String urlrutas; //Almacena la URL por default de los archivos .kml

    private String name; //Almacena el nombre del archivo .kml
    private String idd;

    RequestQueue rq;
    JsonRequest jrq;
    GeoJsonLayer layer;


    public int x=0, i=1, ii=0;

    public RutasActivity() throws IOException, JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        rq = Volley.newRequestQueue(this);

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(24.124040, -110.311814);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        CameraUpdate update = CameraUpdateFactory.newLatLng(position);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

        try {
            layer = new GeoJsonLayer(mMap, R.raw.map, getApplicationContext());
            addColorsToMarkers(layer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(GeoJsonFeature geoJsonFeature) {
                Toast.makeText(RutasActivity.this, "Ruta Seleccionada: "+geoJsonFeature.getProperty("Name"), Toast.LENGTH_SHORT).show();
            }
        });

        mMap.moveCamera(update);
        mMap.animateCamera(zoom);
        //retrieveFileFromResource();


        for(i=1;i<100;i++)
        {
            //etName(i);
        }
    }



    private void etName(int id){

        String url="https://gordiuswheelyae.000webhostapp.com/Rutas.php?id="+id;

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        i=101;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("datos");
        JSONObject jsonObject1 = null;

        try {
            jsonObject1 = jsonArray.getJSONObject(0);
            setName(jsonObject1.optString("Archivo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        urlrutas="https://gordiuswheelyae.000webhostapp.com/Rutas/"+name;
        retrieveFileFromUrl();
    }

    public void setName(String name) {
        this.name = name;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void retrieveFileFromUrl() {
        new DownloadKmlFile(urlrutas).execute();
    }

    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
        private final String mUrl;

        public DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected byte[] doInBackground(String... params) {
            try {
                InputStream is =  new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(byte[] byteArr) {
            try {
                KmlLayer kmlLayer1 = new KmlLayer(mMap, new ByteArrayInputStream(byteArr), getApplicationContext());
                kmlLayer1.addLayerToMap();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static float magnitudeToColor(double magnitude) {
        if (magnitude < 1.0) {
            return BitmapDescriptorFactory.HUE_CYAN;
        } else if (magnitude < 2.5) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (magnitude < 4.5) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return BitmapDescriptorFactory.HUE_RED;
        }
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
    public static int[] getRGB(final String rgb)
    {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++)
        {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    private void retrieveFileFromResource() {
        try {
            KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.ruta_37, getApplicationContext());
            kmlLayer.addLayerToMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    */
}
