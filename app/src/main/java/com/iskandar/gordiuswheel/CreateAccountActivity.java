package com.iskandar.gordiuswheel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue rq1;
    JsonRequest jrq1;

    EditText etName, etLastName1, etLastName2,
            etUser, etPass, etConfirmPass,
            etBirthday, etPhone, etEmail;

    ImageView imageView;
    Button btnCreateAccount;

    private Boolean NameValid, LastNValid, LastN1Valid,MailValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etName = (EditText)findViewById(R.id.etName);
        etLastName1 = (EditText)findViewById(R.id.etLastName1);
        etLastName2 = (EditText)findViewById(R.id.etLastName2);
        etUser = (EditText)findViewById(R.id.etUser);
        etPass = (EditText)findViewById(R.id.etPass);
        etConfirmPass = (EditText)findViewById(R.id.etConfirmPass);
        etBirthday = (EditText)findViewById(R.id.etBirthday);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etEmail = (EditText)findViewById(R.id.etEmail);

        btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);

        rq1 = Volley.newRequestQueue(this);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MailValid=isEmailValid(etEmail.getText().toString());
                NameValid=isNameValid(etName.getText().toString());
                LastNValid=isNameValid(etLastName1.getText().toString());
                LastN1Valid=isNameValid(etLastName2.getText().toString());
                if(!etName.getText().toString().equals("") && !etLastName1.getText().toString().equals("") && !etLastName2.getText().toString().equals("") && !etUser.getText().toString().equals("") && !etPass.getText().toString().equals("") && !etConfirmPass.getText().toString().equals("") && !etEmail.getText().toString().equals("")){
                    if (NameValid){
                        if (LastNValid){
                            if(LastN1Valid){
                                if (etPass.getText().toString().equals(etConfirmPass.getText().toString()))
                                {
                                    if (MailValid){
                                        createAccount();
                                    }else {
                                        Toast.makeText(CreateAccountActivity.this, "Correo Invalido", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(CreateAccountActivity.this, "Las Contrasenas No Coinciden", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(CreateAccountActivity.this, "Apellido Materno No Valido", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(CreateAccountActivity.this, "Apellido Paterno No Valido", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CreateAccountActivity.this, "Nombre No Valido", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CreateAccountActivity.this, "Verifique Los Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //ErrorPass.setText("Email o Contraseña Incorrecto");
        Toast.makeText(this, "Registro Fallido " + error.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

        Toast.makeText(this, "Registro Exitoso " + etUser.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void createAccount(){

        String url="http://192.168.43.207/login/register.php?user=";

        //String url="https://semilio9818.000webhostapp.com/sesion.php?email="+BoxUser.getText().toString()+"&pass="+BoxPass.getText().toString();

        jrq1 = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq1.add(jrq1);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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

}

