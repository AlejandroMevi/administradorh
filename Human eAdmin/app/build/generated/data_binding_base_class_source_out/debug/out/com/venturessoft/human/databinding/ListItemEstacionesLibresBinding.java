// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItemEstacionesLibresBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView bssid;

  @NonNull
  public final TextView date;

  @NonNull
  public final LinearLayoutCompat linearDate;

  @NonNull
  public final ImageView login;

  @NonNull
  public final ImageView logout;

  @NonNull
  public final TextView name;

  @NonNull
  public final TextView numEmployee;

  private ListItemEstacionesLibresBinding(@NonNull CardView rootView, @NonNull TextView bssid,
      @NonNull TextView date, @NonNull LinearLayoutCompat linearDate, @NonNull ImageView login,
      @NonNull ImageView logout, @NonNull TextView name, @NonNull TextView numEmployee) {
    this.rootView = rootView;
    this.bssid = bssid;
    this.date = date;
    this.linearDate = linearDate;
    this.login = login;
    this.logout = logout;
    this.name = name;
    this.numEmployee = numEmployee;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItemEstacionesLibresBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItemEstacionesLibresBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item_estaciones_libres, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItemEstacionesLibresBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bssid;
      TextView bssid = ViewBindings.findChildViewById(rootView, id);
      if (bssid == null) {
        break missingId;
      }

      id = R.id.date;
      TextView date = ViewBindings.findChildViewById(rootView, id);
      if (date == null) {
        break missingId;
      }

      id = R.id.linearDate;
      LinearLayoutCompat linearDate = ViewBindings.findChildViewById(rootView, id);
      if (linearDate == null) {
        break missingId;
      }

      id = R.id.login;
      ImageView login = ViewBindings.findChildViewById(rootView, id);
      if (login == null) {
        break missingId;
      }

      id = R.id.logout;
      ImageView logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.numEmployee;
      TextView numEmployee = ViewBindings.findChildViewById(rootView, id);
      if (numEmployee == null) {
        break missingId;
      }

      return new ListItemEstacionesLibresBinding((CardView) rootView, bssid, date, linearDate,
          login, logout, name, numEmployee);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
