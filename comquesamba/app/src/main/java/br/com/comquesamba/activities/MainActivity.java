package br.com.comquesamba.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import br.com.comquesamba.HomeData;
import br.com.comquesamba.R;
import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.factories.FragmentFactory;
import br.com.comquesamba.models.SambaBean;
import br.com.comquesamba.services.FirebaseDatabaseCallback;
import br.com.comquesamba.services.FirebaseService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;

    private ContentLoadingProgressBar progressBar;

    private FirebaseAuth.AuthStateListener mAuthState;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.show();

        readSambas();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_on);
        } else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_off);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int id){
        Fragment fragment = new FragmentFactory().getFragment(id);
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment){
        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    private void readSambas(){
        final FragmentFactory factory = new FragmentFactory();

        FirebaseDatabaseCallback callback = new FirebaseDatabaseCallback() {
            @Override
            public void success(ArrayList<SambaBean> sambas) {
                progressBar.hide();
                HomeData.getInstance().setSambas(sambas);
                showFragment(factory.getFragment(Constants.HOME_SCREEN));
            }
        };

        FirebaseService firebaseService = new FirebaseService(callback);
        firebaseService.readData();
    }

}
