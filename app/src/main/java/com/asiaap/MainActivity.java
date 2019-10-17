package com.asiaap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.asiaap.REST.ApiClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static Context contextOfApplication;

    private SharedPreferences sp;
    private final String name = "myShared";
    public final int mode = Activity.MODE_PRIVATE;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = null;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contextOfApplication = getApplicationContext();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if(getFragmentManager().getBackStackEntryCount() > 0)
                getFragmentManager().popBackStackImmediate();
            else
                super.onBackPressed();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        sp = getSharedPreferences(name, mode);
        String jabatan = new String(sp.getString("jabatan", "Salah"));
        if (id == R.id.nav_admin) {
            if(sp != null) {
                if(jabatan.equalsIgnoreCase("Admin")) {
                    fragment = new HomeAdminFragment();
                    displaySelectedFragment(fragment);
                } else {
                    fragment = new LoginAdmin();
                    displaySelectedFragment(fragment);
                }
            }
        } else if (id == R.id.nav_cs) {
            if(sp != null) {
                if(jabatan.equalsIgnoreCase("Customer Service")) {
                    fragment = new HomeCS();
                    displaySelectedFragment(fragment);
                } else {
                    fragment = new LoginCustomerService();
                    displaySelectedFragment(fragment);
                }
            }
        } else if(id == R.id.nav_home) {
            fragment = new HomeFragment();
            displaySelectedFragment(fragment);
        } else if(id == R.id.info) {
            fragment = new InformasiBengkelFragment();
            displaySelectedFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

//    private void masukAdmin() {
//        sp = this.getSharedPreferences(name, mode);
//        if(sp != null) {
//            String jabatan = new String();
//            sp.getString("jabatan", null);
//            Boolean cek = new Boolean(jabatan.equals("Admin"));
//
//            if(jabatan.equals("Admin")) {
//                Toast.makeText(this, jabatan, Toast.LENGTH_SHORT).show();
//                fragment = new HomeAdminFragment();
//                displaySelectedFragment(fragment);
//            } else {
//                Toast.makeText(this, jabatan, Toast.LENGTH_SHORT).show();
//                fragment = new LoginAdmin();
//                displaySelectedFragment(fragment);
//            }
//        }
//    }

//    private void masukCS() {
//        sp = this.getSharedPreferences(name, mode);
//        if(sp != null) {
//            String jabatan = new String();
//            sp.getString("jabatan", null);
//            if(jabatan.equals("Customer Service")) {
//                Toast.makeText(this, jabatan, Toast.LENGTH_SHORT).show();
//                fragment = new PenjualanTampil();
//                displaySelectedFragment(fragment);
//            } else {
//                Toast.makeText(this, jabatan, Toast.LENGTH_SHORT).show();
//                displaySelectedFragment(fragment);
//            }
//        }
//
//    }
}
