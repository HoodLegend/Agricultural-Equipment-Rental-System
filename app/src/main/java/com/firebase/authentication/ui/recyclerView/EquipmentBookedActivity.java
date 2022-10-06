package com.firebase.authentication.ui.recyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.authentication.R;
import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.ui.adapters.EquipmentBookedHistoryAdapter;
import com.firebase.authentication.ui.adapters.EquipmentOwnerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EquipmentBookedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Query display_equipment_ref;
    EquipmentBookedHistoryAdapter equipmentBookedHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_booked);


        recyclerView = findViewById(R.id.recycler_menu_4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Equipment> options = new FirebaseRecyclerOptions.Builder<Equipment>()
                .setQuery(display_equipment_ref = FirebaseDatabase.getInstance().getReference().child("equipment Booked History"), Equipment.class)
                .build();

        equipmentBookedHistoryAdapter = new EquipmentBookedHistoryAdapter(options);
        recyclerView.setAdapter(equipmentBookedHistoryAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        equipmentBookedHistoryAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        equipmentBookedHistoryAdapter.stopListening();
    }


    // method that filters the data that the user is searching for from the list of available equipment
    private void textSearch(String str)
    {
        FirebaseRecyclerOptions<Equipment> options =
                new FirebaseRecyclerOptions.Builder<Equipment>()
                        .setQuery(display_equipment_ref = FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment")
                                .orderByChild("equipment_name").startAt(str).endAt(str + "~"), Equipment.class)
                        .build();
        equipmentBookedHistoryAdapter = new EquipmentBookedHistoryAdapter(options);
        equipmentBookedHistoryAdapter.startListening();
        recyclerView.setAdapter(equipmentBookedHistoryAdapter);
    }
}