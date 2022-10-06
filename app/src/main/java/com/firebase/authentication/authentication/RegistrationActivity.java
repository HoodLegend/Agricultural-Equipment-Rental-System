package com.firebase.authentication.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.authentication.models.Data;
import com.firebase.authentication.models.User;
import com.firebase.authentication.ui.dashboard.Dashboard;
import com.firebase.authentication.ui.dashboard.EquipmentOwnerDashboard;
import com.firebase.authentication.R;
import com.firebase.authentication.ui.dashboard.SplashActivity;
import com.firebase.authentication.ui.maps.CustomerMapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private static final String TAG = "Email Password";
    private FirebaseAuth firebaseAuth;
    private MaterialButton register_btn;
    private TextInputEditText owner_username_field, owner_email_field_id, owner_password_field_id, owner_confirm_password_field_id, owner_phone_number_field_id;
    private TextView login_btn;
    private ProgressBar progressBar;
    private Spinner sp_user_type;
    private String user_type = "Owner";

    // method that takes the equipment owner back to the dashboard.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(EquipmentOwnerRegistrationActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_owner_registration);
        owner_username_field = findViewById(R.id.edit_owner_user_name);
        owner_email_field_id = findViewById(R.id.edit_owner_user_email);
        owner_password_field_id = findViewById(R.id.edit_owner_password);
        owner_confirm_password_field_id = findViewById(R.id.edit_owner_confirm_password);
        owner_phone_number_field_id = findViewById(R.id.edit_owner_phone_number);
        register_btn = findViewById(R.id.owner_register_button);
        login_btn = findViewById(R.id.owner_login_link_button);
        progressBar = findViewById(R.id.progressBar);

        // when clicked registers the users credentials in the real time database.
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String equipment_owner_username = Objects.requireNonNull(owner_username_field.getText()).toString().trim();
                String email = Objects.requireNonNull(owner_email_field_id.getText()).toString().trim();
                String password = Objects.requireNonNull(owner_password_field_id.getText()).toString().trim();
                String phone_number = Objects.requireNonNull(owner_phone_number_field_id.getText()).toString().trim();
                String confirm_password = Objects.requireNonNull(owner_confirm_password_field_id.getText()).toString().trim();

                if (!validate(equipment_owner_username, email, password, confirm_password, phone_number)) {
                    return;
                }
                createEquipmentOwnerAccount(equipment_owner_username, email, password, phone_number);
            }
        });

        sp_user_type = findViewById(R.id.et_occupation);
        sp_user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (sp_user_type.getSelectedItem().toString().equals("Equipment Owner")) {
                    user_type = "Owner";
                } else {
                    user_type = "Renter";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseApp.initializeApp(this);

//        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                        User user = snapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                        if (user != null) {
                            Data.user = user;
                            updateUI();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    // method that checks whether the credentials for the user are correct and valid.
    private boolean validate(String equipment_owner_username, String email, String password, String confirm_password, String phone_number) {
        if (TextUtils.isEmpty(equipment_owner_username)) {
            owner_username_field.setError("Username is required!");
            return false;
        }

        if (TextUtils.isEmpty(phone_number)) {
            owner_phone_number_field_id.setError("PhoneNumber is required!");
            return false;
        } else if (phone_number.length() < 10) {
            owner_phone_number_field_id.setError("Phone Number is too short");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            owner_email_field_id.setError("Email is required!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            owner_email_field_id.setError("Please enter a valid email address");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            owner_password_field_id.setError("Password is required");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            owner_password_field_id.setError("Password must have atleast one UPPERCASE Letter"
                    + " One Lowercase Letter" + " One special symbol");
            return false;
        } else if (password.length() < 8) {
            owner_password_field_id.setError("Password is too short. Password must be longer than 8 characters!");
            return false;
        }

        if (!confirm_password.equals(password)) {
            owner_confirm_password_field_id.setError("Passwords Do not Match! PLease make sure passwords much!");
            return false;
        }

        return true;
    }

    // Method that enables the equipment owner to create an account in the system.
    private void createEquipmentOwnerAccount(String equipment_owner_username, String email, String password, String phone_number) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            // method that stores users credentials in the database.
                            String owner_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(owner_id);
                            current_user_db.setValue(true);
                            // object that stores the values entered during registration of the equipment owner.
                            User equipmentOwner = new User(owner_id, equipment_owner_username, email, phone_number, user_type, Data.token);
                            DatabaseReference user_details_reference = FirebaseDatabase.getInstance().getReference().child("Users").child(owner_id);
                            user_details_reference.setValue(equipmentOwner);
                            FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())) {
                                        User user = snapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                                        if (user != null) {
                                            Data.user = user;
                                            updateUI();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationActivity.this, "Registration was not successful!!!",
                                    Toast.LENGTH_SHORT).show();

                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthUserCollisionException e) {
                                owner_email_field_id.setError("User Already Exists!");
                                owner_email_field_id.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    private void updateUI() {
        if (Data.user != null) {
            if (Data.user.getType().equals("Owner")) {
                startActivity(new Intent(RegistrationActivity.this, EquipmentOwnerDashboard.class));
                finish();
            } else if (Data.user.getType().equals("Renter")) {
                startActivity(new Intent(RegistrationActivity.this, Dashboard.class));
                finish();
            }
        } else {
            Toast.makeText(RegistrationActivity.this, "User is null", Toast.LENGTH_SHORT).show();
        }
    }
}