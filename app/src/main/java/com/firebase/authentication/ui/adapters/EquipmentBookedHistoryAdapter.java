package com.firebase.authentication.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.authentication.R;
import com.firebase.authentication.models.Equipment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;

public class EquipmentBookedHistoryAdapter extends FirebaseRecyclerAdapter<Equipment, EquipmentBookedHistoryAdapter.MyViewHolder> {

    FirebaseRecyclerOptions<Equipment> options;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EquipmentBookedHistoryAdapter(@NonNull FirebaseRecyclerOptions<Equipment> options) {
        super(options);
        this.options = options;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Equipment equipment) {
        myViewHolder.name.setText(equipment.getEquipment_name());
        myViewHolder.usage.setText(equipment.getEquipment_usage());
        myViewHolder.price.setText(equipment.getEquipment_price());
        myViewHolder.details.setText(equipment.getEquipment_details());
        if(equipment.getDate_added() != null) {
            myViewHolder.date_added.setText(new SimpleDateFormat("dd MM yyyy").format(equipment.getDate_added()));
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_booked, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, usage, price, details, date_added;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.equipment_name_results);
            usage = itemView.findViewById(R.id.equipment_usage_results);
            price = itemView.findViewById(R.id.equipment_price_results);
            details = itemView.findViewById(R.id.equipment_details_results);
            date_added = itemView.findViewById(R.id.date_equipment_was_created_results);
        }
    }
}
