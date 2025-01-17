// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class AppBarNavDrawerBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final MaterialButton botonModoColaboradorr;

  @NonNull
  public final MaterialTextView title;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final ImageView venturesLogo;

  private AppBarNavDrawerBinding(@NonNull CoordinatorLayout rootView,
      @NonNull MaterialButton botonModoColaboradorr, @NonNull MaterialTextView title,
      @NonNull Toolbar toolbar, @NonNull ImageView venturesLogo) {
    this.rootView = rootView;
    this.botonModoColaboradorr = botonModoColaboradorr;
    this.title = title;
    this.toolbar = toolbar;
    this.venturesLogo = venturesLogo;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AppBarNavDrawerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AppBarNavDrawerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.app_bar_nav_drawer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AppBarNavDrawerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.botonModoColaboradorr;
      MaterialButton botonModoColaboradorr = ViewBindings.findChildViewById(rootView, id);
      if (botonModoColaboradorr == null) {
        break missingId;
      }

      id = R.id.title;
      MaterialTextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.venturesLogo;
      ImageView venturesLogo = ViewBindings.findChildViewById(rootView, id);
      if (venturesLogo == null) {
        break missingId;
      }

      return new AppBarNavDrawerBinding((CoordinatorLayout) rootView, botonModoColaboradorr, title,
          toolbar, venturesLogo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
