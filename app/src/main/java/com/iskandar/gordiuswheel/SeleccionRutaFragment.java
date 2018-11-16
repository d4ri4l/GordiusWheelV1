package com.iskandar.gordiuswheel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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


public class SeleccionRutaFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private boolean recRoute=false;
    private boolean fav;
    private boolean noFav;
    private boolean ifFav=false;
    private String Route;
    private String idRoute;

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
    public boolean isNoFav() {
        return noFav;
    }

    public void setNoFav(boolean noFav) {
        this.noFav = noFav;
    }

    public boolean isIfFav() {
        return ifFav;
    }

    public void setIfFav(boolean ifFav) {
        this.ifFav = ifFav;
    }

    private String idUser;

    public boolean isRecRoute() {
        return recRoute;
    }
    public void setRecRoute(boolean recRoute) {
        this.recRoute = recRoute;
    }
    public String getIdRoute() {
        return idRoute;}
    public void setIdRoute(String idRoute) {
        this.idRoute = idRoute;
    }


    TextView tvNameRoute;
    Switch showRoute;
    Button addFav;
    View vista;
    RequestQueue rq;
    JsonRequest jrq;
    public SeleccionRutaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rq = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_seleccion_ruta, container, false);
        tvNameRoute=(TextView)vista.findViewById(R.id.tvNameRoute);
        showRoute=(Switch)vista.findViewById(R.id.swShowRoute);
        addFav = (Button)vista.findViewById(R.id.btnAddFav);

        final AlertDialog.Builder builderHiden = new AlertDialog.Builder(getActivity());
        builderHiden.setTitle("Ocultar Ruta");
        builderHiden.setMessage("Ocultar Esta Ruta?");
        builderHiden.setPositiveButton("Ocultar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefe.edit();
                tvNameRoute.setText(prefe.getString("IDROUTE",""));
                HidenRoutes();
            }
        });
        builderHiden.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        setRecRoute(true);
        RecRoutes();
        CheckIfFav();
        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        setRoute(prefe.getString("NameRoute",""));


        showRoute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                builderHiden.show();
            }
        });
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefe.edit();
                if (!ifFav){
                    AddToFav();
                    ifFav=true;
                    addFav.setBackgroundResource(android.R.drawable.btn_star_big_on);
                }else {
                    RemoveToFav();
                    ifFav=false;
                    addFav.setBackgroundResource(android.R.drawable.btn_star_big_off);
                }
            }
        });
        return vista;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }
    @Override
    public void onResponse(JSONObject jsonObject) {
        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        if (isRecRoute()){
            JSONArray jsonArray = jsonObject.optJSONArray("datos");
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = jsonArray.getJSONObject(0);
                editor.putString("IDROUTE",jsonObject1.optString("idRutas"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setRecRoute(false);

        }
    }
    private void RecRoutes(){
        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        setRoute(prefe.getString("NameRoute",""));
        String url="https://gordiuswheelyae.000webhostapp.com/RecRoute.php?param=" +prefe.getString("NameRoute","");

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void HidenRoutes(){

        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);

        String url="https://gordiuswheelyae.000webhostapp.com/HidenRoutes.php?names="+prefe.getString("NameRoute","")+"&user="+prefe.getString("IDUser","")+"&route="+prefe.getString("IDROUTE","");

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void AddToFav(){

        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String url="https://gordiuswheelyae.000webhostapp.com/AddFav.php?names="+prefe.getString("NameRoute","")+"&user="+prefe.getString("IDUser","")+"&route="+prefe.getString("IDROUTE","");

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void RemoveToFav(){

        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String url="https://gordiuswheelyae.000webhostapp.com/DeleteFav.php?iduser="+prefe.getString("IDUser","")+"&route="+prefe.getString("NameRoute","");

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void CheckIfFav(){

        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        String url="https://gordiuswheelyae.000webhostapp.com/CheckFav.php?iduser="+prefe.getString("IDUser","")+"&route="+prefe.getString("NameRoute","");

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
}
