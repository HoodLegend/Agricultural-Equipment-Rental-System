// Generated by view binder compiler. Do not edit!
package com.firebase.authentication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.firebase.authentication.R;
import com.google.android.material.navigation.NavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEquipmentOwnerDashboardBinding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final NavigationView NavigationView;

  @NonNull
  public final DrawerLayout drawerLayout;

  @NonNull
  public final TextView equipmentOwnerDashboard;

  private ActivityEquipmentOwnerDashboardBinding(@NonNull DrawerLayout rootView,
      @NonNull NavigationView NavigationView, @NonNull DrawerLayout drawerLayout,
      @NonNull TextView equipmentOwnerDashboard) {
    this.rootView = rootView;
    this.NavigationView = NavigationView;
    this.drawerLayout = drawerLayout;
    this.equipmentOwnerDashboard = equipmentOwnerDashboard;
  }

  @Override
  @NonNull
  public DrawerLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEquipmentOwnerDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEquipmentOwnerDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_equipment_owner_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEquipmentOwnerDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.NavigationView;
      NavigationView NavigationView = ViewBindings.findChildViewById(rootView, id);
      if (NavigationView == null) {
        break missingId;
      }

      DrawerLayout drawerLayout = (DrawerLayout) rootView;

      id = R.id.equipment_owner_dashboard;
      TextView equipmentOwnerDashboard = ViewBindings.findChildViewById(rootView, id);
      if (equipmentOwnerDashboard == null) {
        break missingId;
      }

      return new ActivityEquipmentOwnerDashboardBinding((DrawerLayout) rootView, NavigationView,
          drawerLayout, equipmentOwnerDashboard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}