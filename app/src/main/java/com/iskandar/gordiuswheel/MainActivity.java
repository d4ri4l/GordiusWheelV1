package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String OnDisplay="0";
    private String Accouttype;
    public String id;
    TextView name;

    SeleccionRutaFragment seleccionRutaFragment1=new SeleccionRutaFragment();
    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private Fragment fmm = getSupportFragmentManager().findFragmentById(R.id.mapfr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefe.edit();
            if (prefe.getString("OnDisplay","0").equals("1")){
                editor.putString("OnDisplay", "0");
                editor.commit();
                MapsFragment mapsFragment = new MapsFragment();
                SeleccionRutaFragment seleccionRutaFragment = new SeleccionRutaFragment();
                BlankFragment blankFragment= new BlankFragment();
                fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                fm.beginTransaction().remove(seleccionRutaFragment).commit();
                fm.beginTransaction().add(R.id.escenarioSeleccion,mapsFragment).commit();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        Accouttype=prefe.getString("UserType","");
        MapsFragment mapsFragment = new MapsFragment();
        SeleccionRutaFragment seleccionRutaFragment = new SeleccionRutaFragment();
        BlankFragment blankFragment= new BlankFragment();
        if (id == R.id.nav_cuenta) {
            editor.putString("OnDisplay", "0");
            editor.commit();
            fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
            fm.beginTransaction().remove(seleccionRutaFragment).commit();
            fm.beginTransaction().replace(R.id.escenarioSeleccion, new CuentaFragment()).commit();
        } else if (id == R.id.nav_rutas) {
            fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
            fm.beginTransaction().remove(seleccionRutaFragment).commit();
            fm.beginTransaction().replace(R.id.escenarioSeleccion, new MapsFragment()).commit();
        }
        else if (id == R.id.nav_user){
            if (!Accouttype.equals("5"))
            {
                Toast.makeText(this, "Solo Disponible Para Administradores", Toast.LENGTH_SHORT).show();
            }
            else {
                if (prefe.getString("OnDisplay","0").equals("1")){
                    editor.putString("OnDisplay", "0");
                    editor.commit();
                    fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                    fm.beginTransaction().remove(seleccionRutaFragment).commit();
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new UserFragment()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new UserFragment()).commit();
                }
            }
        }
        else if (id == R.id.nav_drivers){
            if (!Accouttype.equals("5"))
            {
                Toast.makeText(this, "Solo Disponible Para Administradores", Toast.LENGTH_SHORT).show();
            }
            else {
                if (prefe.getString("OnDisplay","0").equals("1")){
                    editor.putString("OnDisplay", "0");
                    editor.commit();
                    fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                    fm.beginTransaction().remove(seleccionRutaFragment);
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new DriversFragment()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new DriversFragment()).commit();
                }
            }

        }
        else if (id == R.id.nav_routes){
            if (!Accouttype.equals("5"))
            {
                Toast.makeText(this, "Solo Disponible Para Administradores", Toast.LENGTH_SHORT).show();
            }
            else {
                if (prefe.getString("OnDisplay","0").equals("1")){
                    editor.putString("OnDisplay", "0");
                    editor.commit();
                    fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                    fm.beginTransaction().remove(seleccionRutaFragment);
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new RoutesFragment()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new RoutesFragment()).commit();
                }
            }
        }else if (id == R.id.nav_sched) {
            if (!Accouttype.equals("5"))
            {
                Toast.makeText(this, "Solo Disponible Para Administradores", Toast.LENGTH_SHORT).show();
            }
            else {
                if (prefe.getString("OnDisplay","0").equals("1")){
                    editor.putString("OnDisplay", "0");
                    editor.commit();
                    fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                    fm.beginTransaction().remove(seleccionRutaFragment);
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new SchedulesFragment()).commit();
                }else {
                    fm.beginTransaction().replace(R.id.escenarioSeleccion, new SchedulesFragment()).commit();
                }
            }
        }
        else if (id == R.id.nav_favoritos) {
            if (prefe.getString("OnDisplay","0").equals("1")){
                editor.putString("OnDisplay", "0");
                editor.commit();
                fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                fm.beginTransaction().remove(seleccionRutaFragment);
                fm.beginTransaction().replace(R.id.escenarioSeleccion, new FavoritosFragment()).commit();
            }else {
                fm.beginTransaction().replace(R.id.escenarioSeleccion, new FavoritosFragment()).commit();
            }
        } else if (id == R.id.nav_ajustes) {
            if (prefe.getString("OnDisplay","0").equals("1")){
                editor.putString("OnDisplay", "0");
                editor.commit();
                fm.beginTransaction().replace(R.id.escenarioMapSeleccion,blankFragment).commit();
                fm.beginTransaction().remove(seleccionRutaFragment);
            }else {
                super.onBackPressed();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}