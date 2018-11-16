package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;


public class UpdatePFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private String name1;
    private String Last1;
    private String LastN1;
    private Boolean NameValid, LastValid, LastNValid;

    public Boolean getUpdate() {
        return Update;
    }

    public void setUpdate(Boolean update) {
        Update = update;
    }

    private Boolean Update=false;

    View vista;

    EditText name, last, lastName;
    Button update;

    RequestQueue rq;
    JsonRequest jrq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_update, container, false);

        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);

        rq = Volley.newRequestQueue(getContext());
        name = (EditText)vista.findViewById(R.id.etNameUpdate);
        last = (EditText)vista.findViewById(R.id.etLastUpdate);
        lastName = (EditText)vista.findViewById(R.id.etLastNUpdate);

        update = (Button)vista.findViewById(R.id.btnUpdate);
        if(prefe.getString("ORIGIN","").equals("U")){
            RecuperarDatosUser();
        }
        if (prefe.getString("ORIGIN","").equals("D")){
            RecuperarDatosDriver();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("") && !last.getText().toString().equals("") && !lastName.getText().toString().equals("")){
                    NameValid=isNameValid(name.getText().toString());
                    LastValid=isNameValid(last.getText().toString());
                    LastNValid=isNameValid(lastName.getText().toString());
                    if(NameValid){
                        if(LastValid){
                            if (LastNValid){
                                setUpdate(true);
                                SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                                if(prefe.getString("ORIGIN","").equals("U")){
                                    UpdateDataUser();
                                }
                                if (prefe.getString("ORIGIN","").equals("D")){
                                    UpdateDataDriver();
                                }
                            }else {
                                Toast.makeText(getContext(), "Ingrese Un Apellido Materno Valido", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Ingrese Un Apellido Paterno Valido", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Ingrese Un Nombre Valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return vista;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if(!Update){
            Toast.makeText(getContext(), "Error Al Recuperar Los Datos", Toast.LENGTH_SHORT).show();
            exit(0);
        }else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if(!getUpdate())
        {
            JSONArray jsonArray = jsonObject.optJSONArray("datos");
            JSONObject jsonObject1 = null;

            try {
                jsonObject1 = jsonArray.getJSONObject(0);
                setName1(jsonObject1.optString("Nombre"));
                setLast1(jsonObject1.optString("ApPaterno"));
                setLastN1(jsonObject1.optString("ApMaterno"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            name.setText(name1);
            last.setText(Last1);
            lastName.setText(LastN1);
        }else {
            Toast.makeText(getContext(), "All Okay", Toast.LENGTH_SHORT).show();
        }
    }

    private void RecuperarDatosUser(){

        String id;
        SharedPreferences prefe=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        id=prefe.getString("MiID", "");

        String url="https://gordiuswheelyae.000webhostapp.com/RecDataUser.php?param=" + id;

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void UpdateDataUser(){

        String id;
        SharedPreferences prefe=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        id=prefe.getString("MiID", "");

        String url="https://gordiuswheelyae.000webhostapp.com/UpdateUser.php?user=" + prefe.getString("MiID", "") +"&nombre="+ name.getText().toString()+"&appaterno="+last.getText().toString()+"&apmaterno="+lastName.getText().toString();

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    private void RecuperarDatosDriver(){

        String id;
        SharedPreferences prefe=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        id=prefe.getString("MiID", "");

        String url="https://gordiuswheelyae.000webhostapp.com/RecDataDriver.php?param=" + id;

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
    private void UpdateDataDriver(){

        String id;
        SharedPreferences prefe=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        id=prefe.getString("MiID", "");

        String url="https://gordiuswheelyae.000webhostapp.com/UpdateDriver.php?user=" + prefe.getString("MiID", "") +"&nombre="+ name.getText().toString()+"&appaterno="+last.getText().toString()+"&apmaterno="+lastName.getText().toString();

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    public static boolean isNameValid(String email) {
        boolean isValid = false;

        String expression = "^[a-zA-Z]*$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getLastN1() {
        return LastN1;
    }

    public void setLastN1(String lastN1) {
        LastN1 = lastN1;
    }
    public String getLast1() {
        return Last1;
    }

    public void setLast1(String last1) {
        Last1 = last1;
    }
}
