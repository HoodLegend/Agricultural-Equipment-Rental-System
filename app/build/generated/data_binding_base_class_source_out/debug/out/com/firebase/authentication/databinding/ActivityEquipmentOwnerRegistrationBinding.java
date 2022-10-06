// Generated by view binder compiler. Do not edit!
package com.firebase.authentication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.firebase.authentication.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEquipmentOwnerRegistrationBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextInputEditText editOwnerConfirmPassword;

  @NonNull
  public final TextInputEditText editOwnerPassword;

  @NonNull
  public final TextInputEditText editOwnerPhoneNumber;

  @NonNull
  public final TextInputEditText editOwnerUserEmail;

  @NonNull
  public final TextInputEditText editOwnerUserName;

  @NonNull
  public final Spinner etOccupation;

  @NonNull
  public final TextInputLayout ownerConfirmPasswordId;

  @NonNull
  public final TextView ownerLoginLinkButton;

  @NonNull
  public final TextInputLayout ownerPasswordId;

  @NonNull
  public final TextInputLayout ownerPhoneNumberId;

  @NonNull
  public final MaterialButton ownerRegisterButton;

  @NonNull
  public final TextInputLayout ownerUserEmailId;

  @NonNull
  public final TextInputLayout ownerUserNameId;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final MaterialTextView registrationPage;

  private ActivityEquipmentOwnerRegistrationBinding(@NonNull RelativeLayout rootView,
      @NonNull TextInputEditText editOwnerConfirmPassword,
      @NonNull TextInputEditText editOwnerPassword, @NonNull TextInputEditText editOwnerPhoneNumber,
      @NonNull TextInputEditText editOwnerUserEmail, @NonNull TextInputEditText editOwnerUserName,
      @NonNull Spinner etOccupation, @NonNull TextInputLayout ownerConfirmPasswordId,
      @NonNull TextView ownerLoginLinkButton, @NonNull TextInputLayout ownerPasswordId,
      @NonNull TextInputLayout ownerPhoneNumberId, @NonNull MaterialButton ownerRegisterButton,
      @NonNull TextInputLayout ownerUserEmailId, @NonNull TextInputLayout ownerUserNameId,
      @NonNull ProgressBar progressBar, @NonNull MaterialTextView registrationPage) {
    this.rootView = rootView;
    this.editOwnerConfirmPassword = editOwnerConfirmPassword;
    this.editOwnerPassword = editOwnerPassword;
    this.editOwnerPhoneNumber = editOwnerPhoneNumber;
    this.editOwnerUserEmail = editOwnerUserEmail;
    this.editOwnerUserName = editOwnerUserName;
    this.etOccupation = etOccupation;
    this.ownerConfirmPasswordId = ownerConfirmPasswordId;
    this.ownerLoginLinkButton = ownerLoginLinkButton;
    this.ownerPasswordId = ownerPasswordId;
    this.ownerPhoneNumberId = ownerPhoneNumberId;
    this.ownerRegisterButton = ownerRegisterButton;
    this.ownerUserEmailId = ownerUserEmailId;
    this.ownerUserNameId = ownerUserNameId;
    this.progressBar = progressBar;
    this.registrationPage = registrationPage;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEquipmentOwnerRegistrationBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEquipmentOwnerRegistrationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_equipment_owner_registration, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEquipmentOwnerRegistrationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.edit_owner_confirm_password;
      TextInputEditText editOwnerConfirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (editOwnerConfirmPassword == null) {
        break missingId;
      }

      id = R.id.edit_owner_password;
      TextInputEditText editOwnerPassword = ViewBindings.findChildViewById(rootView, id);
      if (editOwnerPassword == null) {
        break missingId;
      }

      id = R.id.edit_owner_phone_number;
      TextInputEditText editOwnerPhoneNumber = ViewBindings.findChildViewById(rootView, id);
      if (editOwnerPhoneNumber == null) {
        break missingId;
      }

      id = R.id.edit_owner_user_email;
      TextInputEditText editOwnerUserEmail = ViewBindings.findChildViewById(rootView, id);
      if (editOwnerUserEmail == null) {
        break missingId;
      }

      id = R.id.edit_owner_user_name;
      TextInputEditText editOwnerUserName = ViewBindings.findChildViewById(rootView, id);
      if (editOwnerUserName == null) {
        break missingId;
      }

      id = R.id.et_occupation;
      Spinner etOccupation = ViewBindings.findChildViewById(rootView, id);
      if (etOccupation == null) {
        break missingId;
      }

      id = R.id.owner_confirm_password_id;
      TextInputLayout ownerConfirmPasswordId = ViewBindings.findChildViewById(rootView, id);
      if (ownerConfirmPasswordId == null) {
        break missingId;
      }

      id = R.id.owner_login_link_button;
      TextView ownerLoginLinkButton = ViewBindings.findChildViewById(rootView, id);
      if (ownerLoginLinkButton == null) {
        break missingId;
      }

      id = R.id.owner_password_id;
      TextInputLayout ownerPasswordId = ViewBindings.findChildViewById(rootView, id);
      if (ownerPasswordId == null) {
        break missingId;
      }

      id = R.id.owner_phone_number_id;
      TextInputLayout ownerPhoneNumberId = ViewBindings.findChildViewById(rootView, id);
      if (ownerPhoneNumberId == null) {
        break missingId;
      }

      id = R.id.owner_register_button;
      MaterialButton ownerRegisterButton = ViewBindings.findChildViewById(rootView, id);
      if (ownerRegisterButton == null) {
        break missingId;
      }

      id = R.id.owner_user_email_id;
      TextInputLayout ownerUserEmailId = ViewBindings.findChildViewById(rootView, id);
      if (ownerUserEmailId == null) {
        break missingId;
      }

      id = R.id.owner_user_name_id;
      TextInputLayout ownerUserNameId = ViewBindings.findChildViewById(rootView, id);
      if (ownerUserNameId == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.registration_page;
      MaterialTextView registrationPage = ViewBindings.findChildViewById(rootView, id);
      if (registrationPage == null) {
        break missingId;
      }

      return new ActivityEquipmentOwnerRegistrationBinding((RelativeLayout) rootView,
          editOwnerConfirmPassword, editOwnerPassword, editOwnerPhoneNumber, editOwnerUserEmail,
          editOwnerUserName, etOccupation, ownerConfirmPasswordId, ownerLoginLinkButton,
          ownerPasswordId, ownerPhoneNumberId, ownerRegisterButton, ownerUserEmailId,
          ownerUserNameId, progressBar, registrationPage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
