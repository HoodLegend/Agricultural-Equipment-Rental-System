package com.firebase.authentication.ui.dashboard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.authentication.R;
import com.firebase.authentication.authentication.LoginActivity;
import com.firebase.authentication.ui.maps.CustomerMapsActivity;
import com.firebase.authentication.ui.recyclerView.BookingRequestActivity;
import com.firebase.authentication.ui.recyclerView.EquipmentBookedActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    CardView home, available_equipment, current_orders, sign_out;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Dashboard.this, Dashboard.class ));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        home = findViewById(R.id.home_item);
        available_equipment = findViewById(R.id.equipment_available_item);
        sign_out = findViewById(R.id.sign_out);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Dashboard.class));
            }
        });

        available_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, BookingRequestActivity.class));
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(new Intent(Dashboard.this, LoginActivity.class));
                finish();
                Toast.makeText(Dashboard.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // method that ends the user session and logs the user out of the application.
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

}

