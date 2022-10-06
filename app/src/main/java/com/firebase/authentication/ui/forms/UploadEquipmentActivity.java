package com.firebase.authentication.ui.forms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.authentication.models.Data;
import com.firebase.authentication.ui.recyclerView.AvailableEquipmentListActivity;
import com.firebase.authentication.ui.dashboard.EquipmentOwnerDashboard;
import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadEquipmentActivity extends AppCompatActivity {

    EditText equipment_name, equipment_usage, equipment_price, equipment_details, equipment_location;
    Button upload_button, back_to_dashboard;
    private DatabaseReference upload_equipment_ref;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadEquipmentActivity.this, EquipmentOwnerDashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_equipment);

        equipment_name = findViewById(R.id.name_of_equipment);
        equipment_usage = findViewById(R.id.usage_of_equipment);
        equipment_price = findViewById(R.id.price_of_equipment);
        equipment_details = findViewById(R.id.details_of_equipment);
        equipment_location = findViewById(R.id.location_of_equipment);
        upload_button = findViewById(R.id.upload_button);
        back_to_dashboard = findViewById(R.id.back_to_dashboard_button);


        upload_equipment_ref = FirebaseDatabase.getInstance().getReference();

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                startActivity(new Intent(UploadEquipmentActivity.this, AvailableEquipmentListActivity.class));
                finish();
            }
        });

        back_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadEquipmentActivity.this , EquipmentOwnerDashboard.class));
                finish();
            }
        });
    }

    // Method that performs validation checks and also
    // allows the equipment Owner to Enter data about their equipment into the firebase realtime database.
    private void insertData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String equipment_name_value = equipment_name.getText().toString().trim();
        if (TextUtils.isEmpty(equipment_name_value)) {
            equipment_name.setError("Field cannot be Empty!");
            return;
        }

        String equipment_usage_value = equipment_usage.getText().toString().trim();
        if (TextUtils.isEmpty(equipment_usage_value)) {
            equipment_usage.setError("Field cannot be Empty!");
            return;
        }

        String equipment_price_value = equipment_price.getText().toString().trim();
        if (TextUtils.isEmpty(equipment_price_value)) {
            equipment_price.setError("Field cannot be Empty!");
            return;
        }

        String equipment_details_value = equipment_details.getText().toString().trim();
        if (TextUtils.isEmpty(equipment_details_value)) {
            equipment_details.setError("Field cannot be Empty!");
            return;
        }


        String equipment_location_value = equipment_location.getText().toString().trim();
        if (TextUtils.isEmpty(equipment_location_value)) {
            equipment_location.setError("Field cannot be Empty!");
            return;
        }
        
        Date currentDate = Calendar.getInstance().getTime();

        Equipment equipment = new Equipment(uid, equipment_name_value, equipment_usage_value, equipment_price_value,equipment_details_value,equipment_location_value, Data.token,null, currentDate);

        upload_equipment_ref.child("equipmentOwnersEquipment").push().setValue(equipment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UploadEquipmentActivity.this, "You have successfully uploaded your equipment....", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadEquipmentActivity.this, "Error adding Equipment..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}