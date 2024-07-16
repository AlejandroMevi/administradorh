// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textview.MaterialTextView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentItemFailsGeneralErrorBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBackFailGeneralError;

  @NonNull
  public final MaterialTextView dateCheck;

  @NonNull
  public final MaterialTextView device;

  @NonNull
  public final ImageView employePhoto;

  @NonNull
  public final ImageView employeeImage;

  @NonNull
  public final MaterialTextView idNumCompany;

  @NonNull
  public final MaterialTextView idNumEmp;

  @NonNull
  public final TextView movText;

  @NonNull
  public final MaterialTextView photoTake;

  @NonNull
  public final TextView sumUpTitle;

  @NonNull
  public final TextView textView4;

  @NonNull
  public final MaterialTextView textView5;

  @NonNull
  public final MaterialTextView textView6;

  @NonNull
  public final MaterialTextView txtCodigo;

  @NonNull
  public final MaterialTextView txtDateCheck;

  @NonNull
  public final MaterialTextView txtDevice;

  @NonNull
  public final MaterialTextView txtET032;

  @NonNull
  public final FrameLayout userImage;

  @NonNull
  public final MaterialTextView usernameText;

  private FragmentItemFailsGeneralErrorBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnBackFailGeneralError, @NonNull MaterialTextView dateCheck,
      @NonNull MaterialTextView device, @NonNull ImageView employePhoto,
      @NonNull ImageView employeeImage, @NonNull MaterialTextView idNumCompany,
      @NonNull MaterialTextView idNumEmp, @NonNull TextView movText,
      @NonNull MaterialTextView photoTake, @NonNull TextView sumUpTitle,
      @NonNull TextView textView4, @NonNull MaterialTextView textView5,
      @NonNull MaterialTextView textView6, @NonNull MaterialTextView txtCodigo,
      @NonNull MaterialTextView txtDateCheck, @NonNull MaterialTextView txtDevice,
      @NonNull MaterialTextView txtET032, @NonNull FrameLayout userImage,
      @NonNull MaterialTextView usernameText) {
    this.rootView = rootView;
    this.btnBackFailGeneralError = btnBackFailGeneralError;
    this.dateCheck = dateCheck;
    this.device = device;
    this.employePhoto = employePhoto;
    this.employeeImage = employeeImage;
    this.idNumCompany = idNumCompany;
    this.idNumEmp = idNumEmp;
    this.movText = movText;
    this.photoTake = photoTake;
    this.sumUpTitle = sumUpTitle;
    this.textView4 = textView4;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.txtCodigo = txtCodigo;
    this.txtDateCheck = txtDateCheck;
    this.txtDevice = txtDevice;
    this.txtET032 = txtET032;
    this.userImage = userImage;
    this.usernameText = usernameText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentItemFailsGeneralErrorBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentItemFailsGeneralErrorBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_item_fails_general_error, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentItemFailsGeneralErrorBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnBackFailGeneralError;
      Button btnBackFailGeneralError = ViewBindings.findChildViewById(rootView, id);
      if (btnBackFailGeneralError == null) {
        break missingId;
      }

      id = R.id.dateCheck;
      MaterialTextView dateCheck = ViewBindings.findChildViewById(rootView, id);
      if (dateCheck == null) {
        break missingId;
      }

      id = R.id.device;
      MaterialTextView device = ViewBindings.findChildViewById(rootView, id);
      if (device == null) {
        break missingId;
      }

      id = R.id.employePhoto;
      ImageView employePhoto = ViewBindings.findChildViewById(rootView, id);
      if (employePhoto == null) {
        break missingId;
      }

      id = R.id.employeeImage;
      ImageView employeeImage = ViewBindings.findChildViewById(rootView, id);
      if (employeeImage == null) {
        break missingId;
      }

      id = R.id.idNumCompany;
      MaterialTextView idNumCompany = ViewBindings.findChildViewById(rootView, id);
      if (idNumCompany == null) {
        break missingId;
      }

      id = R.id.idNumEmp;
      MaterialTextView idNumEmp = ViewBindings.findChildViewById(rootView, id);
      if (idNumEmp == null) {
        break missingId;
      }

      id = R.id.movText;
      TextView movText = ViewBindings.findChildViewById(rootView, id);
      if (movText == null) {
        break missingId;
      }

      id = R.id.photoTake;
      MaterialTextView photoTake = ViewBindings.findChildViewById(rootView, id);
      if (photoTake == null) {
        break missingId;
      }

      id = R.id.sumUpTitle;
      TextView sumUpTitle = ViewBindings.findChildViewById(rootView, id);
      if (sumUpTitle == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      id = R.id.textView5;
      MaterialTextView textView5 = ViewBindings.findChildViewById(rootView, id);
      if (textView5 == null) {
        break missingId;
      }

      id = R.id.textView6;
      MaterialTextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      id = R.id.txtCodigo;
      MaterialTextView txtCodigo = ViewBindings.findChildViewById(rootView, id);
      if (txtCodigo == null) {
        break missingId;
      }

      id = R.id.txtDateCheck;
      MaterialTextView txtDateCheck = ViewBindings.findChildViewById(rootView, id);
      if (txtDateCheck == null) {
        break missingId;
      }

      id = R.id.txtDevice;
      MaterialTextView txtDevice = ViewBindings.findChildViewById(rootView, id);
      if (txtDevice == null) {
        break missingId;
      }

      id = R.id.txtET032;
      MaterialTextView txtET032 = ViewBindings.findChildViewById(rootView, id);
      if (txtET032 == null) {
        break missingId;
      }

      id = R.id.userImage;
      FrameLayout userImage = ViewBindings.findChildViewById(rootView, id);
      if (userImage == null) {
        break missingId;
      }

      id = R.id.usernameText;
      MaterialTextView usernameText = ViewBindings.findChildViewById(rootView, id);
      if (usernameText == null) {
        break missingId;
      }

      return new FragmentItemFailsGeneralErrorBinding((ConstraintLayout) rootView,
          btnBackFailGeneralError, dateCheck, device, employePhoto, employeeImage, idNumCompany,
          idNumEmp, movText, photoTake, sumUpTitle, textView4, textView5, textView6, txtCodigo,
          txtDateCheck, txtDevice, txtET032, userImage, usernameText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}