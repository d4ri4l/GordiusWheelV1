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


public class UpdatePFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private String name1;
    private String Last1;
    private String LastN1;
    private Boolean NameValid, LastValid, LastNValid;

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

        rq = Volley.newRequestQueue(getContext());

        name = (EditText)vista.findViewById(R.id.etNameUpdate);
        last = (EditText)vista.findViewById(R.id.etLastUpdate);
        lastName = (EditText)vista.findViewById(R.id.etLastNUpdate);

        update = (Button)vista.findViewById(R.id.btnUpdate);
        RecuperarDatos();

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
        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

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



    }

    private void RecuperarDatos(){

        String id;
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        id=sharedPreferences.getString("MiID", "1");

        String url="https://gordiuswheelyae.000webhostapp.com/RecData.php?param=" + id;

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
