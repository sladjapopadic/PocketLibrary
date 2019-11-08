package com.example.pocketlibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.pocketlibrary.data.Book;
import com.example.pocketlibrary.fragments.FinishedFragment;
import com.example.pocketlibrary.fragments.InfoFragment;
import com.example.pocketlibrary.fragments.LibraryFragment;
import com.example.pocketlibrary.fragments.LoginFragment;
import com.example.pocketlibrary.fragments.ProfileFragment;
import com.example.pocketlibrary.fragments.RegisterFragment;
import com.example.pocketlibrary.fragments.ToReadFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.onLoggedUser {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        updateNavDraw(false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_to_read:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToReadFragment()).commit();
                break;
            case R.id.nav_finished:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FinishedFragment()).commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
            case R.id.nav_library:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LibraryFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void updateNavDraw(Boolean isLogged) {
        if (!isLogged) {
            navigationView.getMenu().findItem(R.id.nav_finished).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_to_read).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_library).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_info).setVisible(true);

        } else {
            navigationView.getMenu().findItem(R.id.nav_finished).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_to_read).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_library).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_info).setVisible(true);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ProfileFragment) {
            ProfileFragment profileFragment = (ProfileFragment) fragment;
            profileFragment.setOnLoggedUserListener(this);
        }
    }

    @Override
    public void getUserEmail(String email) {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView headerUserEmail = navHeaderView.findViewById(R.id.nav_email);
        headerUserEmail.setText(email);
    }

    public void openBrowser(View view) {

        String url = (String) view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        intent.setData(Uri.parse(url));

        startActivity(intent);
    }


}
