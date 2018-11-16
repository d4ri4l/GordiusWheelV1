package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue rq;
    JsonRequest jrq;

    EditText BoxPass, BoxUser;
    Button btnIniciar, btnCrearCuenta;
    TextView ErrorPass;

    String user, pass;

    String USER1;

    public String getUSER1() {
        return USER1;
    }

    public void setUSER1(String USER1) {
        this.USER1 = USER1;
    }

    public String getPASS1() {
        return PASS1;
    }

    public void setPASS1(String PASS1) {
        this.PASS1 = PASS1;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getIDUser() {
        return IDUser;
    }

    public void setIDUser(String IDUser) {
        this.IDUser = IDUser;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    String PASS1;
    String NameUser;
    String IDUser;
    String UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BoxUser = (EditText) findViewById(R.id.etEmail);
        BoxPass = (EditText) findViewById(R.id.etPass);

        SharedPreferences prefe=getSharedPreferences("datos",Context.MODE_PRIVATE);

        user=prefe.getString("USER","");
        pass=prefe.getString("PASS","");

        if(!user.equals("") && !pass.equals("")){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        btnIniciar = (Button) findViewById(R.id.btnLogin);
        btnCrearCuenta = (Button) findViewById(R.id.btnCreateAccount);

        ErrorPass = (TextView) findViewById(R.id.tvPassIncorrect);

        rq = Volley.newRequestQueue(this);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesion();
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        ErrorPass.setText("Usuario o Contraseña Incorrecto");

    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        User usuario = new User();

        Toast.makeText(this, "Inicio De Sesion Exitoso " + BoxUser.getText().toString(), Toast.LENGTH_SHORT).show();

        SharedPreferences prefe=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        JSONArray jsonArray = jsonObject.optJSONArray("datos");
        JSONObject jsonObject1 = null;

        try {
            jsonObject1 = jsonArray.getJSONObject(0);
            setIDUser(jsonObject1.optString("idClientes"));
            setNameUser(jsonObject1.optString("Nombre"));
            setUSER1(jsonObject1.optString("Usuario_Cuenta"));
            setPASS1(jsonObject1.optString("Contrasena_Cuenta"));
            setUserType(jsonObject1.optString("Tipo_Usuario_idTipo_Usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString("USER", getUSER1());
        editor.putString("PASS", getPASS1());
        editor.putString("NameUser", getNameUser());
        editor.putString("IDUser", getIDUser());
        editor.putString("UserType", getUserType());
        editor.commit();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void IniciarSesion(){

        String url="https://gordiuswheelyae.000webhostapp.com/login.php?user="+BoxUser.getText().toString()+"&pwd="+BoxPass.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

}
