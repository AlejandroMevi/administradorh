// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentConfirmCancellationBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnAcept;

  @NonNull
  public final TextView btnContinue;

  @NonNull
  public final CheckBox check;

  @NonNull
  public final TextView checkTxt;

  @NonNull
  public final LinearLayout linearLayout21;

  @NonNull
  public final LinearLayout linearTxt;

  @NonNull
  public final TextView textView14;

  @NonNull
  public final TextView textView15;

  private FragmentConfirmCancellationBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnAcept, @NonNull TextView btnContinue, @NonNull CheckBox check,
      @NonNull TextView checkTxt, @NonNull LinearLayout linearLayout21,
      @NonNull LinearLayout linearTxt, @NonNull TextView textView14, @NonNull TextView textView15) {
    this.rootView = rootView;
    this.btnAcept = btnAcept;
    this.btnContinue = btnContinue;
    this.check = check;
    this.checkTxt = checkTxt;
    this.linearLayout21 = linearLayout21;
    this.linearTxt = linearTxt;
    this.textView14 = textView14;
    this.textView15 = textView15;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentConfirmCancellationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentConfirmCancellationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_confirm_cancellation, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentConfirmCancellationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_acept;
      Button btnAcept = ViewBindings.findChildViewById(rootView, id);
      if (btnAcept == null) {
        break missingId;
      }

      id = R.id.btnContinue;
      TextView btnContinue = ViewBindings.findChildViewById(rootView, id);
      if (btnContinue == null) {
        break missingId;
      }

      id = R.id.check;
      CheckBox check = ViewBindings.findChildViewById(rootView, id);
      if (check == null) {
        break missingId;
      }

      id = R.id.checkTxt;
      TextView checkTxt = ViewBindings.findChildViewById(rootView, id);
      if (checkTxt == null) {
        break missingId;
      }

      id = R.id.linearLayout21;
      LinearLayout linearLayout21 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout21 == null) {
        break missingId;
      }

      id = R.id.linearTxt;
      LinearLayout linearTxt = ViewBindings.findChildViewById(rootView, id);
      if (linearTxt == null) {
        break missingId;
      }

      id = R.id.textView14;
      TextView textView14 = ViewBindings.findChildViewById(rootView, id);
      if (textView14 == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      return new FragmentConfirmCancellationBinding((ConstraintLayout) rootView, btnAcept,
          btnContinue, check, checkTxt, linearLayout21, linearTxt, textView14, textView15);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
