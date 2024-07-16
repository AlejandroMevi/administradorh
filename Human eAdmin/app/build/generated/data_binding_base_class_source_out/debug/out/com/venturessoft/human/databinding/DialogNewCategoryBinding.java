// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogNewCategoryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnAdd;

  @NonNull
  public final Button btnCancel;

  @NonNull
  public final TextInputEditText descriptionZones;

  @NonNull
  public final TextInputLayout tilZona;

  @NonNull
  public final TextView titleZones;

  private DialogNewCategoryBinding(@NonNull LinearLayout rootView, @NonNull Button btnAdd,
      @NonNull Button btnCancel, @NonNull TextInputEditText descriptionZones,
      @NonNull TextInputLayout tilZona, @NonNull TextView titleZones) {
    this.rootView = rootView;
    this.btnAdd = btnAdd;
    this.btnCancel = btnCancel;
    this.descriptionZones = descriptionZones;
    this.tilZona = tilZona;
    this.titleZones = titleZones;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogNewCategoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogNewCategoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_new_category, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogNewCategoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAdd;
      Button btnAdd = ViewBindings.findChildViewById(rootView, id);
      if (btnAdd == null) {
        break missingId;
      }

      id = R.id.btnCancel;
      Button btnCancel = ViewBindings.findChildViewById(rootView, id);
      if (btnCancel == null) {
        break missingId;
      }

      id = R.id.descriptionZones;
      TextInputEditText descriptionZones = ViewBindings.findChildViewById(rootView, id);
      if (descriptionZones == null) {
        break missingId;
      }

      id = R.id.tilZona;
      TextInputLayout tilZona = ViewBindings.findChildViewById(rootView, id);
      if (tilZona == null) {
        break missingId;
      }

      id = R.id.titleZones;
      TextView titleZones = ViewBindings.findChildViewById(rootView, id);
      if (titleZones == null) {
        break missingId;
      }

      return new DialogNewCategoryBinding((LinearLayout) rootView, btnAdd, btnCancel,
          descriptionZones, tilZona, titleZones);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}