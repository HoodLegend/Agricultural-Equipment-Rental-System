package com.firebase.authentication.ui.adapters;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.authentication.apis.ApiCalls;
import com.firebase.authentication.apis.PushNotificationRequest;
import com.firebase.authentication.apis.PushNotificationResponse;
import com.firebase.authentication.apis.RetrofitService;
import com.firebase.authentication.models.Equipment;
import com.firebase.authentication.models.Request;
import com.firebase.authentication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedRequestAdapter extends FirebaseRecyclerAdapter<Equipment, ReceivedRequestAdapter.MyViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    FirebaseDatabase updatesDatabase = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
    FirebaseRecyclerOptions<Equipment> options;


    public ReceivedRequestAdapter(@NonNull FirebaseRecyclerOptions<Equipment> options) {
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
        myViewHolder.location.setText(equipment.getEquipment_location());
        if(equipment.getDate_added() != null) {
            myViewHolder.date_added.setText(format.format(Objects.requireNonNull(equipment.getDate_added())));
        }

        equipment.setKey(getRef(myViewHolder.getAdapterPosition()).getKey());

        if(equipment.getUid() != null){
            if (equipment.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                myViewHolder.accept_request_btn.setVisibility(View.VISIBLE);
                myViewHolder.reject_request_btn.setVisibility(View.VISIBLE);
            } else {

                if (equipment.getRequest() == null) {
                    equipment.setRequest(new Request(null, false));
                }

                if(equipment.getRequest().isHasBeenRequested()){
                    myViewHolder.accept_request_btn.setVisibility(View.VISIBLE);

                    if(Objects.equals(equipment.getRequest().getRequester_uid(), user.getUid())){
                        myViewHolder.accept_request_btn.setVisibility(View.VISIBLE);
                        myViewHolder.reject_request_btn.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.reject_request_btn.setVisibility(View.VISIBLE);
                        myViewHolder.reject_request_btn.setEnabled(false);
                        myViewHolder.reject_request_btn.setText("Requested.");
                    }
                } else {

                    myViewHolder.reject_request_btn.setVisibility(View.VISIBLE);
                }
            }
        }

        DatabaseReference bookingRef = updatesDatabase.getReference().child("Received Requests").child(equipment.getUid()).child(equipment.getKey());
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Equipment.class) != null) {
                    Equipment equipment = snapshot.getValue(Equipment.class);

                    assert equipment != null;

                    if (equipment.getRequest() == null) {
                        equipment.setRequest(new Request(null, false));
                    }

                    if (equipment.getRequest().isHasRequestBeenGranted()) {
                        if (equipment.getRequest().getRequester_uid() == user.getUid()) {
                            myViewHolder.accept_request_btn.setText("Already Accepted");
                            myViewHolder.accept_request_btn.setEnabled(false);
                            myViewHolder.accept_request_btn.setVisibility(View.VISIBLE);
                        } else {
                            myViewHolder.accept_request_btn.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final String TOPIC = "/topics/" + equipment.getUid();
        myViewHolder.accept_request_btn.setOnClickListener(v -> {
            if (true) {

                Request request = new Request();
                request.setRequester_uid(user.getUid());
                request.setHasBeenRequested(true);
                equipment.setRequest(request);
                getRef(myViewHolder.getAdapterPosition()).setValue(equipment);

                equipment.setDone(true);
                bookingRef.setValue(equipment).addOnSuccessListener(unused -> {
                    Toast.makeText(v.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                });
                RetrofitService retrofitService = new RetrofitService();

                ApiCalls apiCalls = retrofitService.getRetrofit().create(ApiCalls.class);
                PushNotificationRequest pushNotificationRequest = new PushNotificationRequest("Request to Book Item", "Someone would like your machine",  equipment.getTokenRequest(), equipment.getTokenRequest());
                apiCalls.sendToken(pushNotificationRequest).enqueue(new Callback<PushNotificationResponse>() {
                    @Override
                    public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d("onResponse: ", response.body().toString());
                            Request request = new Request();
                            request.setRequester_uid(user.getUid());
                            request.setHasBeenRequested(false);
                            equipment.setRequest(request);
                            equipment.setDone(true); bookingRef.setValue(equipment).addOnSuccessListener(unused -> {
                                Toast.makeText(v.getContext(), "Client Informed", Toast.LENGTH_SHORT).show();
                                myViewHolder.accept_request_btn.setText("Informed");
//                            Notification notification = new Notification(new PushNotification(new NotificationData("Booking Request Update: Hi, " + equipment.getEquipment_name() + "!","The booking request, you made on " + format.format(equipment.getDate_added()) + ". was accepted by the equipment owner. Have a wonderful day. \n\nYours faithfully, \n" + user.getDisplayName(),  "equipment booking"), TOPIC));
//                            notification.send();
                            });

                        }
                        else {
                            Log.d("onResponse: ", response.errorBody().toString());
                            myViewHolder.accept_request_btn.setText("Request Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                        Toast.makeText(v.getContext(), "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        myViewHolder.accept_request_btn.setText("Network Error");
                    }
                });
            }
        });

        myViewHolder.reject_request_btn.setOnClickListener(v -> {
            equipment.setDone(false);
            bookingRef.setValue(equipment).addOnSuccessListener(unused -> {
                Toast.makeText(v.getContext(), "rejected", Toast.LENGTH_SHORT).show();
            });
            Request request = new Request();
            request.setRequester_uid(user.getUid());
            request.setHasBeenRequested(true);
            equipment.setRequest(request);
            getRef(myViewHolder.getAdapterPosition()).child(equipment.getKey()).setValue(equipment);
        });
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_requested, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, usage, price, details, date_added, owner, location;
        Button accept_request_btn, reject_request_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.equipment_name_results);
            usage = itemView.findViewById(R.id.equipment_usage_results);
            price = itemView.findViewById(R.id.equipment_price_results);
            details = itemView.findViewById(R.id.equipment_details_results);
            date_added = itemView.findViewById(R.id.date_equipment_was_created_results);
            owner = itemView.findViewById(R.id.equipment_owner);
            location = itemView.findViewById(R.id.location_results_1);
            accept_request_btn = itemView.findViewById(R.id.accept_request_button);
            reject_request_btn = itemView.findViewById(R.id.reject_request_button);

        }
    }
}
