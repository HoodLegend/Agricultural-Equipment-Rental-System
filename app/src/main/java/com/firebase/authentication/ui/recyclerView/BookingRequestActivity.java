package com.firebase.authentication.ui.recyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.authentication.ui.adapters.BookingRequestAdapter;
import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.R;
import com.firebase.authentication.ui.dashboard.Dashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BookingRequestActivity extends AppCompatActivity {

    BookingRequestAdapter bookingRequestAdapter;
    RecyclerView recyclerView;
    Query display_equipment_ref;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BookingRequestActivity.this, Dashboard.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_request);

        recyclerView = findViewById(R.id.recycler_menu_1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Equipment> options = new FirebaseRecyclerOptions.Builder<Equipment>()
                .setQuery(display_equipment_ref = FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment"), Equipment.class)
                .build();

        bookingRequestAdapter = new BookingRequestAdapter(options);
        recyclerView.setAdapter(bookingRequestAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bookingRequestAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookingRequestAdapter.stopListening();
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
        bookingRequestAdapter= new BookingRequestAdapter(options);
        bookingRequestAdapter.startListening();
        recyclerView.setAdapter(bookingRequestAdapter);
    }
}