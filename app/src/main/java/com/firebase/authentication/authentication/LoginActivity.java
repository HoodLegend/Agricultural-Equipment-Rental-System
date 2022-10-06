package com.firebase.authentication.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Email Password Sign in";
    private FirebaseAuth firebaseAuth;
    private TextInputEditText email_field, password_field;
    private TextView new_here_link;
    private MaterialButton login_btn;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(EquipmentOwnerLoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_owner_login);

        email_field = findViewById(R.id.edit_equipment_owner_email);
        password_field = findViewById(R.id.edit_equipment_owner_password);
        login_btn = findViewById(R.id.equipment_owner_login_button);
        new_here_link = findViewById(R.id.equipment_owner_signup_link_button);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_field.getText().toString().trim();
                String password = password_field.getText().toString().trim();

                if (!validate(email, password)) {
                    return;
                }
                signInAccount(email, password);
            }
        });

        new_here_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return;
            }
        });

        FirebaseApp.initializeApp(this);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // Method that performs validation checks on the user information input to the system by the user.
    private boolean validate(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            email_field.setError("Email is required.");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            password_field.setError("password is required");
            return false;
        }
        if (password.length() < 8) {
            password_field.setError("password too short! password must be longer than 8 characters.");
            return false;
        }

        return true;
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


    // method that enables the equipment owner login in to the system.
    private void signInAccount(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
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
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Please try Again Later!",
                                    Toast.LENGTH_SHORT).show();

                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                password_field.setError(getString(R.string.error_weak_password));
                                password_field.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                email_field.setError(getString(R.string.error_invalid_email));
                                email_field.requestFocus();
                            } catch (FirebaseAuthUserCollisionException | FirebaseAuthInvalidUserException e) {
                                email_field.setError(getString(R.string.error_user_exists));
                                email_field.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                });
    }

    public void updateUI() {
        if (Data.user.getType().equals("Owner")) {

            startActivity(new Intent(LoginActivity.this, EquipmentOwnerDashboard.class));
            finish();
        } else {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
            //redirect to login page

        }
    }
}

