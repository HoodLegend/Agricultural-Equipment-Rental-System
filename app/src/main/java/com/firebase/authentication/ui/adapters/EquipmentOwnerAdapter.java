package com.firebase.authentication.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EquipmentOwnerAdapter extends FirebaseRecyclerAdapter<Equipment, EquipmentOwnerAdapter.MyViewHolder> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    FirebaseRecyclerOptions<Equipment> options;
    public EquipmentOwnerAdapter(@NonNull FirebaseRecyclerOptions<Equipment> options) {
        super(options);
        this.options = options;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_entry,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Equipment equipment) {
            myViewHolder.name.setText(equipment.getEquipment_name());
            myViewHolder.usage.setText(equipment.getEquipment_usage());
            myViewHolder.price.setText(equipment.getEquipment_price());
            myViewHolder.details.setText(equipment.getEquipment_details());
            myViewHolder.location.setText(equipment.getEquipment_location());
            if (equipment.getDate_added() != null) {
                myViewHolder.date_added.setText(format.format(Objects.requireNonNull(equipment).getDate_added()));
            }
            equipment.setKey(getRef(myViewHolder.getAdapterPosition()).getKey());


            myViewHolder.edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(myViewHolder.edit_button.getContext())
                            .setContentHolder(new ViewHolder(R.layout.update_popup))
                            .setExpanded(true, 900)
                            .create();

                    View v = dialogPlus.getHolderView();
                    EditText equipment_name = v.findViewById(R.id.equipment_name_text);
                    EditText equipment_usage = v.findViewById(R.id.equipment_usage_text);
                    EditText equipment_details = v.findViewById(R.id.equipment_details_text);
                    EditText equipment_price = v.findViewById(R.id.equipment_price_text);
                    EditText equipment_location = v.findViewById(R.id.equipment_location_text);

                    Button btnEdit = v.findViewById(R.id.edit_button);

                    equipment_name.setText(equipment.getEquipment_name());
                    equipment_usage.setText(equipment.getEquipment_usage());
                    equipment_details.setText(equipment.getEquipment_details());
                    equipment_price.setText(equipment.getEquipment_price());
                    equipment_location.setText(equipment.getEquipment_location());


                    dialogPlus.show();

                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("equipment_name", equipment_name.getText().toString().trim());
                            map.put("equipment_usage", equipment_usage.getText().toString().trim());
                            map.put("equipment_details", equipment_details.getText().toString().trim());
                            map.put("equipment_price", equipment_price.getText().toString().trim());
                            map.put("equipment location", equipment_location.getText().toString().trim());


                            FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment").child(Objects.requireNonNull(getRef(myViewHolder.getAdapterPosition()).getKey())).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(myViewHolder.name.getContext(), "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.usage.getContext(), "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.details.getContext(), "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.price.getContext(), "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.location.getContext(), "Data Uodated Successfully!", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(myViewHolder.name.getContext(), "Error while updating.", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.usage.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.details.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.price.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(myViewHolder.location.getContext(), "Error while updating!", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    });
                }
            });

            myViewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolder.name.getContext());
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Deleted data can't be undone");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("equipmentOwnersEquipment")
                                    .child(Objects.requireNonNull(getRef(myViewHolder.getAdapterPosition()).getKey())).removeValue();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(myViewHolder.name.getContext(), "Canceled.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, usage, price, details, date_added, location;
        Button edit_button, delete_button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.equipment_name_results);
            usage = itemView.findViewById(R.id.equipment_usage_results);
            price = itemView.findViewById(R.id.equipment_price_results);
            details = itemView.findViewById(R.id.equipment_details_results);
            date_added = itemView.findViewById(R.id.date_equipment_was_created_results);
            location = itemView.findViewById(R.id.location_results);

            edit_button = itemView.findViewById(R.id.edit_equipment_button);
            delete_button = itemView.findViewById(R.id.delete_equipment_button);
        }
    }
}
