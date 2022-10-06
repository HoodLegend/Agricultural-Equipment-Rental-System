package com.firebase.authentication.ui.recyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.authentication.ui.adapters.EquipmentOwnerAdapter;
import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.R;
import com.firebase.authentication.ui.dashboard.Dashboard;
import com.firebase.authentication.ui.dashboard.EquipmentOwnerDashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AvailableEquipmentListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Query display_equipment_ref;
    EquipmentOwnerAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AvailableEquipmentListActivity.this, EquipmentOwnerDashboard.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_equipment_list);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Equipment> options = new FirebaseRecyclerOptions.Builder<Equipment>()
                .setQuery(display_equipment_ref = FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment"), Equipment.class)
                .build();


                adapter = new EquipmentOwnerAdapter(options);
                recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_equipment, menu);
        MenuItem menuItem = menu.findItem(R.id.search_equipment_id);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                textSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    // method that filters the data that the user is searching for from the list of available equipment
    private void textSearch(String str)
    {
        FirebaseRecyclerOptions<Equipment> options =
                new FirebaseRecyclerOptions.Builder<Equipment>()
                .setQuery(display_equipment_ref = FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment")
                        .orderByChild("equipment_name").startAt(str).endAt(str + "~"), Equipment.class)
                .build();
        adapter = new EquipmentOwnerAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}