package com.firebase.authentication.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.authentication.authentication.LoginActivity;
import com.firebase.authentication.ui.maps.EquipmentOwnerMapsActivity;
import com.firebase.authentication.R;
import com.firebase.authentication.ui.recyclerView.AvailableEquipmentListActivity;
import com.firebase.authentication.ui.recyclerView.RequestedEquipmentActivity;
import com.firebase.authentication.ui.forms.UploadEquipmentActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class EquipmentOwnerDashboard extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EquipmentOwnerDashboard.this, EquipmentOwnerDashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_owner_dashboard);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NavigationView);
        toolbar = findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);



        // method that listens to click event when one of the navigation item is clicked.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.owner_nav_Home:
                        startActivity(new Intent(EquipmentOwnerDashboard.this, EquipmentOwnerDashboard.class));
                        return true;
                    case R.id.owner_nav_available_equipment:
                        startActivity(new Intent(EquipmentOwnerDashboard.this, AvailableEquipmentListActivity.class));
                        return true;

                    case R.id.owner_nav_upload_equipment:
                        startActivity(new Intent(EquipmentOwnerDashboard.this, UploadEquipmentActivity.class));
                        finish();
                        return true;

                    case R.id.owner_nav_current_requests:
                        startActivity(new Intent(EquipmentOwnerDashboard.this, RequestedEquipmentActivity.class));
                        finish();
                        return true;

                    case R.id.owner_nav_logout:
                        signOut();
                        Intent intent = new Intent(EquipmentOwnerDashboard.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(EquipmentOwnerDashboard.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // method that ends the user session and logs the user out of the application.
    public void signOut () {
        FirebaseAuth.getInstance().signOut();
    }
}
