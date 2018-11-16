package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
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


public class DriversFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    View vista;

    RequestQueue rq;
    JsonRequest jrq;

    RadioButton rbID;
    RadioButton rbName;
    RadioButton rbLastN;

    Button btnSearch;
    Button btnUpdate;
    Button btnDelete;

    EditText etParam;

    private String id;
    private String name;
    private String LastN;
    public String[]data;
    public Boolean NameValid, delete=false;

    private String idd;

    private TableLayout tableLayout;
    private String[]header={"ID","Nombre","Apellido"};

    private TableDynamic tableDynamic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista=inflater.inflate(R.layout.fragment_drivers, container, false);

        rq = Volley.newRequestQueue(getContext());

        rbID=(RadioButton) vista.findViewById(R.id.rbIDDriver);
        rbName=(RadioButton) vista.findViewById(R.id.rbNameDrivers);
        rbLastN=(RadioButton) vista.findViewById(R.id.rbLastNameDriver);

        btnSearch=(Button) vista.findViewById(R.id.btnSearchDriver);
        btnUpdate=(Button) vista.findViewById(R.id.btnUpdateDriver);
        btnDelete=(Button) vista.findViewById(R.id.btnDeleteDriver);

        etParam = (EditText)vista.findViewById(R.id.etSearchD) ;

        tableLayout=(TableLayout)vista.findViewById(R.id.tlTableDrivers);

        tableDynamic=new TableDynamic(tableLayout, getContext());
        tableDynamic.addHeader(header);
        tableDynamic.backgroundHeader(Color.rgb(11,0,151));

        etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
        etParam.setHint("ID");

        btnSearch.setOnClickListener(new View.OnClickListener() { // hago clic en el botón

            @Override
            public void onClick(View v) {
                if(rbID.isChecked()){
                    RecuperarDatos();
                }
                if(rbLastN.isChecked()){
                    NameValid=isNameValid(etParam.getText().toString());
                    if (NameValid){
                        RecuperarDatos();
                    }else {
                        Toast.makeText(getContext(), "Por favor Ingrese Un Nombre Valido", Toast.LENGTH_SHORT).show();
                    }
                }
                if(rbName.isChecked()){
                    NameValid=isNameValid(etParam.getText().toString());
                    if (NameValid){
                        RecuperarDatos();
                    }else {
                        Toast.makeText(getContext(), "Por favor Ingrese Un Nombre Valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePFragment updatePFragment = new UpdatePFragment();
                FragmentManager manager = getFragmentManager();
                if(rbID.isChecked()){
                    if (!etParam.getText().toString().equals("")){
                        SharedPreferences prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefe.edit();
                        editor.putString("MiID", etParam.getText().toString());
                        editor.putString("ORIGIN", "D");
                        editor.commit();
                        manager.beginTransaction().replace(R.id.escenarioSeleccion, updatePFragment).addToBackStack(null).commit();
                    }
                    else {
                        Toast.makeText(getContext(), "Ingrese Un ID", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Opcion Valida Solo para Busqueda Por ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbID.isChecked()){
                    if (!etParam.getText().toString().equals("")){
                        delete=true;
                        DeleteUser();
                    }
                    else {
                        Toast.makeText(getContext(), "Ingrese Un ID", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Opcion Valida Solo para Busqueda Por ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rbID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbID.isChecked()){
                    etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etParam.setHint("ID");
                }
                if(rbLastN.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Apellido");
                }
                if(rbName.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Nombre");
                }
            }
        });
        rbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbID.isChecked()){
                    etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etParam.setHint("ID");
                }
                if(rbLastN.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Apellido");
                }
                if(rbName.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Nombre");
                }
            }
        });
        rbLastN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbID.isChecked()){
                    etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etParam.setHint("ID");
                }
                if(rbLastN.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Apellido");
                }
                if(rbName.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    etParam.setHint("Nombre");
                }            }
        });

        return vista;
    }

    private void DeleteUser() {
        String url="https://gordiuswheelyae.000webhostapp.com/DeleteDriver.php?user="+etParam.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if(!delete){
            Toast.makeText(getContext(), "No Se Encontro El Chofer", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "El Chofer Se Elimino Correctamente", Toast.LENGTH_SHORT).show();
            delete=false;
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if(!delete){
            JSONArray jsonArray = jsonObject.optJSONArray("datos");
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = jsonArray.getJSONObject(0);
                id=jsonObject1.optString("idChoferes");
                name=jsonObject1.optString("Nombre");
                LastN=jsonObject1.optString("ApPaterno");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "Se Encontro El Usuario", Toast.LENGTH_SHORT).show();
            data=new String[]{id,name,LastN};
            tableDynamic.addItems(data);
        }
    }

    private void RecuperarDatos(){

        String url="https://gordiuswheelyae.000webhostapp.com/Drivers.php?param="+etParam.getText().toString();

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastN(String lastN) {
        LastN = lastN;
    }
    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }
}
