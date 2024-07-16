// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.gms.maps.MapView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDetallesEstacionesLibresBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final HeaderUserEstacionesLibresBinding headerUser;

  @NonNull
  public final MapView mapView;

  @NonNull
  public final Toolbar toolbar;

  private FragmentDetallesEstacionesLibresBinding(@NonNull ConstraintLayout rootView,
      @NonNull HeaderUserEstacionesLibresBinding headerUser, @NonNull MapView mapView,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.headerUser = headerUser;
    this.mapView = mapView;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDetallesEstacionesLibresBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDetallesEstacionesLibresBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_detalles_estaciones_libres, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDetallesEstacionesLibresBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.header_user;
      View headerUser = ViewBindings.findChildViewById(rootView, id);
      if (headerUser == null) {
        break missingId;
      }
      HeaderUserEstacionesLibresBinding binding_headerUser = HeaderUserEstacionesLibresBinding.bind(headerUser);

      id = R.id.mapView;
      MapView mapView = ViewBindings.findChildViewById(rootView, id);
      if (mapView == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentDetallesEstacionesLibresBinding((ConstraintLayout) rootView,
          binding_headerUser, mapView, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}