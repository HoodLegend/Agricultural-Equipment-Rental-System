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
import com.firebase.authentication.models.Data;
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

public class BookingRequestAdapter extends FirebaseRecyclerAdapter<Equipment, BookingRequestAdapter.MyViewHolder> {

    FirebaseDatabase updatesDatabase = FirebaseDatabase.getInstance();
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
    public BookingRequestAdapter(@NonNull FirebaseRecyclerOptions<Equipment> options) {
        super(options);
        this.options = options;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.book_equipment,parent,false);
        return new MyViewHolder(v);
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
            myViewHolder.date_added.setText(format.format(equipment.getDate_added()));
        }

        equipment.setKey(getRef(myViewHolder.getAdapterPosition()).getKey());

        if (equipment.getUid() != null) {
            if (equipment.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                myViewHolder.send_request.setVisibility(View.VISIBLE);
                myViewHolder.cancel_request.setVisibility(View.VISIBLE);
                myViewHolder.return_equipment.setVisibility(View.GONE);
            }else{

                if (equipment.getRequest() == null) {
                    equipment.setRequest(new Request(null, false));
                }

                if (equipment.getRequest().isHasBeenRequested()) {
                    myViewHolder.send_request.setVisibility(View.GONE);
                    myViewHolder.cancel_request.setVisibility(View.GONE);
                    myViewHolder.return_equipment.setVisibility(View.GONE);

                    if (Objects.equals(equipment.getRequest().getRequester_uid(), user.getUid())) {
                        myViewHolder.cancel_request.setVisibility(View.VISIBLE);
                        myViewHolder.return_equipment.setVisibility(View.VISIBLE);
                    }else{
                        myViewHolder.cancel_request.setVisibility(View.VISIBLE);
                        myViewHolder.cancel_request.setEnabled(false);
                        myViewHolder.cancel_request.setText("Requested.");
                    }
                } else {
                    myViewHolder.cancel_request.setVisibility(View.VISIBLE);
                    myViewHolder.return_equipment.setVisibility(View.GONE);
                }
            }
        }

        DatabaseReference bookingRef = updatesDatabase.getReference().child("Booking Requests").child(equipment.getUid()).child(equipment.getKey());
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Equipment.class) != null){
                  Equipment now = snapshot.getValue(Equipment.class);

                    assert now != null;
                    if (now.getRequest() == null) {
                        now.setRequest(new Request(null, false));
                    }

                    if(now.getRequest().isHasBeenRequested()){
                        if(now.getRequest().getRequester_uid() == user.getUid()){
                            myViewHolder.send_request.setText("Pending");
                            myViewHolder.send_request.setEnabled(false);
                            myViewHolder.send_request.setVisibility(View.VISIBLE);
                        } else {
                            myViewHolder.send_request.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        myViewHolder.send_request.setOnClickListener(v -> {
            myViewHolder.send_request.setText("Please Wait");
            equipment.setTokenRequest(Data.token);
            FirebaseDatabase.getInstance().getReference().child("Booking Requests").child(equipment.getUid()).child(equipment.getKey()).child("tokenRequest").setValue(Data.token);
            RetrofitService retrofitService = new RetrofitService();
            ApiCalls apiCalls = retrofitService.getRetrofit().create(ApiCalls.class);
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest("Request to Book Item", "Someone would like your machine",  equipment.getToken(), equipment.getToken());
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
                            Toast.makeText(v.getContext(), "Requested successfully!", Toast.LENGTH_SHORT).show();
                            myViewHolder.send_request.setText("Requested");
                            myViewHolder.cancel_request.setVisibility(View.VISIBLE);
//                            Notification notification = new Notification(new PushNotification(new NotificationData("Booking Request Update: Hi, " + equipment.getEquipment_name() + "!","The booking request, you made on " + format.format(equipment.getDate_added()) + ". was accepted by the equipment owner. Have a wonderful day. \n\nYours faithfully, \n" + user.getDisplayName(),  "equipment booking"), TOPIC));
//                            notification.send();
                        });

                    }
                    else {
                        Log.d("onResponse: ", response.errorBody().toString());
                        myViewHolder.send_request.setText("Failed");
                    }
                }

                @Override
                public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    myViewHolder.send_request.setText("Network Error");
                }
            });
        });

        myViewHolder.cancel_request.setOnClickListener(v->{
//            equipment.setDone(false);
//            bookingRef.setValue(equipment).addOnSuccessListener(unused -> {
//                Toast.makeText(v.getContext(), "Cancelled booking successfully!", Toast.LENGTH_SHORT).show();
//            });
//            Request request = new Request();
//            request.setRequester_uid(user.getUid());
//            request.setHasBeenRequested(false);
//            equipment.setRequest(request);
//            getRef(myViewHolder.getAdapterPosition()).child(equipment.getKey()).setValue(equipment);

            myViewHolder.cancel_request.setText("Please Wait");
            equipment.setTokenRequest(Data.token);
            FirebaseDatabase.getInstance().getReference().child("Booking Requests").child(equipment.getUid()).child(equipment.getKey()).child("tokenRequest").setValue(Data.token);
            RetrofitService retrofitService = new RetrofitService();
            ApiCalls apiCalls = retrofitService.getRetrofit().create(ApiCalls.class);
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest("Request Cancelled", "Requester Cancelled Request!",  equipment.getToken(), equipment.getToken());
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
                            Toast.makeText(v.getContext(), "Cancelled successfully!", Toast.LENGTH_SHORT).show();
                            myViewHolder.cancel_request.setText("Cancelled");
                            myViewHolder.send_request.setVisibility(View.VISIBLE);
//                            Notification notification = new Notification(new PushNotification(new NotificationData("Booking Request Update: Hi, " + equipment.getEquipment_name() + "!","The booking request, you made on " + format.format(equipment.getDate_added()) + ". was accepted by the equipment owner. Have a wonderful day. \n\nYours faithfully, \n" + user.getDisplayName(),  "equipment booking"), TOPIC));
//                            notification.send();
                        });
                    }
                    else {
                        Log.d("onResponse: ", response.errorBody().toString());
                        myViewHolder.cancel_request.setText("Failed");
                    }
                }

                @Override
                public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    myViewHolder.cancel_request.setText("Network Error");
                }
            });
        });

        myViewHolder.return_equipment.setOnClickListener(v->{
//            equipment.setDone(false);
//            bookingRef.setValue(equipment).addOnSuccessListener(unused -> {
//                Toast.makeText(v.getContext(), "Returned successfully", Toast.LENGTH_SHORT).show();
//            });
//            Request request = new Request();
//            request.setRequester_uid(user.getUid());
//            request.setHasBeenRequested(true);
//            equipment.setRequest(request);
//            getRef(myViewHolder.getAdapterPosition()).child(equipment.getKey()).setValue(equipment);

            myViewHolder.return_equipment.setText("Please Wait");
            equipment.setTokenRequest(Data.token);
            FirebaseDatabase.getInstance().getReference().child("Booking Requests").child(equipment.getUid()).child(equipment.getKey()).child("tokenRequest").setValue(Data.token);
            RetrofitService retrofitService = new RetrofitService();
            ApiCalls apiCalls = retrofitService.getRetrofit().create(ApiCalls.class);
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest("Returned Equipment", "Client returned equipment",  equipment.getToken(), equipment.getToken());
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
                            Toast.makeText(v.getContext(), "Returned successfully!", Toast.LENGTH_SHORT).show();
                            myViewHolder.return_equipment.setText("Returned");
                            myViewHolder.send_request.setVisibility(View.VISIBLE);
                            myViewHolder.cancel_request.setVisibility(View.VISIBLE);
//                            Notification notification = new Notification(new PushNotification(new NotificationData("Booking Request Update: Hi, " + equipment.getEquipment_name() + "!","The booking request, you made on " + format.format(equipment.getDate_added()) + ". was accepted by the equipment owner. Have a wonderful day. \n\nYours faithfully, \n" + user.getDisplayName(),  "equipment booking"), TOPIC));
//                            notification.send();
                        });

                    }
                    else {
                        Log.d("onResponse: ", response.errorBody().toString());
                        myViewHolder.return_equipment.setText("Failed");
                    }
                }

                @Override
                public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                    Toast.makeText(v.getContext(), "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    myViewHolder.return_equipment.setText("Network Error");
                }
            });
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, usage, price, details, date_added, location;
        Button send_request, cancel_request, return_equipment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.equipment_name_results_2);
            usage = itemView.findViewById(R.id.equipment_usage_results_2);
            price = itemView.findViewById(R.id.equipment_price_results_2);
            details = itemView.findViewById(R.id.equipment_details_results_2);
            date_added = itemView.findViewById(R.id.date_equipment_was_created_results_2);
            location = itemView.findViewById(R.id.location_results);

            send_request = itemView.findViewById(R.id.send_request_for_equipment_button);
            cancel_request = itemView.findViewById(R.id.cancel_request_equipment_button);
            return_equipment = itemView.findViewById(R.id.return__equipment_button);
        }
    }
}
