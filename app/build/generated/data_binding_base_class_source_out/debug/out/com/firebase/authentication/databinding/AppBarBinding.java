// Generated by view binder compiler. Do not edit!
package com.firebase.authentication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.firebase.authentication.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class AppBarBinding implements ViewBinding {
  @NonNull
  private final Toolbar rootView;

  @NonNull
  public final Toolbar appBar;

  private AppBarBinding(@NonNull Toolbar rootView, @NonNull Toolbar appBar) {
    this.rootView = rootView;
    this.appBar = appBar;
  }

  @Override
  @NonNull
  public Toolbar getRoot() {
    return rootView;
  }

  @NonNull
  public static AppBarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AppBarBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
      boolean attachToParent) {
    View root = inflater.inflate(R.layout.app_bar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AppBarBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    Toolbar appBar = (Toolbar) rootView;

    return new AppBarBinding((Toolbar) rootView, appBar);
  }
}
