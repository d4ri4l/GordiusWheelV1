package com.iskandar.gordiuswheel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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

import java.util.ArrayList;


public class RoutesFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    View vista;

    RequestQueue rq;
    JsonRequest jrq;

    RadioButton rbID;
    RadioButton rbName;

    Button btnSearch;
    Button btnUpdate;
    Button btnDelete;

    EditText etParam;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    private String id;
    private String name;

    private StringBuilder allowedChars = new StringBuilder("abcdefghijklmnopqrstuvwxyz");

    public String[]data;

    public int n=1;

    private TableLayout tableLayout;
    private String[]header={"ID","Ruta"};
    private ArrayList<String[]>rows=new ArrayList<>();

    private TableDynamic tableDynamic;

    private int status=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista=inflater.inflate(R.layout.fragment_routes, container, false);

        rq = Volley.newRequestQueue(getContext());

        rbID=(RadioButton) vista.findViewById(R.id.rbIDRoutes);
        rbName=(RadioButton) vista.findViewById(R.id.rbNameRoutes);

        btnSearch=(Button) vista.findViewById(R.id.btnSearchRoutes);
        btnUpdate=(Button) vista.findViewById(R.id.btnUpdateRoutes);
        btnDelete=(Button) vista.findViewById(R.id.btnDeleteRoutes);

        etParam = (EditText)vista.findViewById(R.id.etSearchR) ;

        tableLayout=(TableLayout)vista.findViewById(R.id.tlTableRoutes);

        tableDynamic=new TableDynamic(tableLayout, getContext());
        tableDynamic.addHeader(header);
        tableDynamic.backgroundHeader(Color.rgb(11,0,151));

        if(rbID.isChecked()){
            etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if(rbName.isChecked()){
            etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            //etParam.setFilters(new InputFilter[] { filter });
        }

        btnSearch.setOnClickListener(new View.OnClickListener() { // hago clic en el botón

            @Override
            public void onClick(View v) {
                RecuperarDatos();
            }
        });
        rbID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbID.isChecked()){
                    etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if(rbName.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    //etParam.setFilters(new InputFilter[] { filter });
                }
            }
        });
        rbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(rbID.isChecked()){
                    etParam.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                if(rbName.isChecked()){
                    etParam.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    //etParam.setFilters(new InputFilter[] { filter });
                }
            }
        });

        return vista;
    }

    private ArrayList<String[]>getDrivers(){
        rows.add(new String[]{id,name});
        Toast.makeText(getContext(), "hofer", Toast.LENGTH_SHORT).show();

        return rows;
    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "1234567890!@#$%^&*()_-=+{[}]|;:.>?/<,`~";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    @Override
    public void onErrorResponse(VolleyError volleyError) {

        Toast.makeText(getContext(), "No Se Encontro La Ruta", Toast.LENGTH_SHORT).show();
        n=101;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("datos");
        JSONObject jsonObject1 = null;

        try {
            jsonObject1 = jsonArray.getJSONObject(0);
            id=jsonObject1.optString("idRutas");
            name=jsonObject1.optString("Nombre");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), "Se Encontro La Ruta", Toast.LENGTH_SHORT).show();
        data=new String[]{id,name};
        tableDynamic.addItems(data);
        n=101;
    }

    private void RecuperarDatos(){

        String url="https://gordiuswheelyae.000webhostapp.com/RutasSearch.php?param="+etParam.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

}
