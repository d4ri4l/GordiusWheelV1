package com.iskandar.gordiuswheel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SchedulesFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    View vista;
    Spinner listRoutes;
    String[] rutas={"Rutas","R62","R71","R70","R69","R68"};
    private TableDynamic tableDynamic;
    private TableLayout tableLayout;
    private String[]header={"Inicio De Turno","Fin De Turno"};
    private int n;

    private String id;
    private String name;
    private String LastN;
    public String[]data;

    RequestQueue rq;
    JsonRequest jrq;

    public SchedulesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_schedules, container, false);
        listRoutes = (Spinner)vista.findViewById(R.id.listaRutas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rutas);
        listRoutes.setAdapter(adapter);

        rq = Volley.newRequestQueue(getContext());

        tableLayout=(TableLayout)vista.findViewById(R.id.tlTableSched);

        tableDynamic=new TableDynamic(tableLayout, getContext());
        tableDynamic.addHeader(header);
        tableDynamic.backgroundHeader(Color.rgb(11,0,151));
        /*for (n=1;n<=100;n++){
            RecuperarDatos();
        }*/

        listRoutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RecuperarDatos(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return vista;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        n=101;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("datos");
        JSONObject jsonObject1 = null;

        try {
            jsonObject1 = jsonArray.getJSONObject(0);
            name=jsonObject1.optString("Hora_Inicio");
            LastN=jsonObject1.optString("Hora_Termino");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data=new String[]{name,LastN};
        tableDynamic.addItems(data);
    }
    private void RecuperarDatos(int i){

        String url="https://gordiuswheelyae.000webhostapp.com/ShedSearch2.php?param="+i;

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
}
