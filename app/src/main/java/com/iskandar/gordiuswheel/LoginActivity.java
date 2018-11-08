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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BoxUser = (EditText) findViewById(R.id.etEmail);
        BoxPass = (EditText) findViewById(R.id.etPass);

        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("HMPrefs", MODE_PRIVATE);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        user=preferences.getString("USER","");
        pass=preferences.getString("PASS","");

        if(!user.equals("") && !user.equals("")){
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

        //ErrorPass.setText("Email o Contraseña Incorrecto");
        Toast.makeText(this, "Inicio De Sesion Fallido " + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        User usuario = new User();

        Toast.makeText(this, "Inicio De Sesion Exitoso " + BoxUser.getText().toString(), Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray jsonArray = jsonObject.optJSONArray("datos");
        JSONObject jsonObject1 = null;

        try {
            jsonObject1 = jsonArray.getJSONObject(0);
            editor.putString("USER", jsonObject1.optString("Usuario_Cuenta"));
            editor.putString("PASS",jsonObject1.optString("Contrasena_Cuenta"));
            editor.putString("NameUser",jsonObject1.optString("Nombre"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
