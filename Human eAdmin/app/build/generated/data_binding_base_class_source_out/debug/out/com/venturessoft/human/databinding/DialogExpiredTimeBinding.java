// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogExpiredTimeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView backgroundImage;

  @NonNull
  public final CardView cardV;

  @NonNull
  public final ImageView closeDialog;

  @NonNull
  public final ImageView icFreeTrial;

  @NonNull
  public final Button payNow;

  @NonNull
  public final TextView txtFreeTrial;

  @NonNull
  public final TextView txtFreeTrialDuration;

  private DialogExpiredTimeBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView backgroundImage, @NonNull CardView cardV, @NonNull ImageView closeDialog,
      @NonNull ImageView icFreeTrial, @NonNull Button payNow, @NonNull TextView txtFreeTrial,
      @NonNull TextView txtFreeTrialDuration) {
    this.rootView = rootView;
    this.backgroundImage = backgroundImage;
    this.cardV = cardV;
    this.closeDialog = closeDialog;
    this.icFreeTrial = icFreeTrial;
    this.payNow = payNow;
    this.txtFreeTrial = txtFreeTrial;
    this.txtFreeTrialDuration = txtFreeTrialDuration;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogExpiredTimeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogExpiredTimeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_expired_time, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogExpiredTimeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backgroundImage;
      ImageView backgroundImage = ViewBindings.findChildViewById(rootView, id);
      if (backgroundImage == null) {
        break missingId;
      }

      id = R.id.cardV;
      CardView cardV = ViewBindings.findChildViewById(rootView, id);
      if (cardV == null) {
        break missingId;
      }

      id = R.id.closeDialog;
      ImageView closeDialog = ViewBindings.findChildViewById(rootView, id);
      if (closeDialog == null) {
        break missingId;
      }

      id = R.id.ic_freeTrial;
      ImageView icFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (icFreeTrial == null) {
        break missingId;
      }

      id = R.id.payNow;
      Button payNow = ViewBindings.findChildViewById(rootView, id);
      if (payNow == null) {
        break missingId;
      }

      id = R.id.txtFreeTrial;
      TextView txtFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrial == null) {
        break missingId;
      }

      id = R.id.txtFreeTrialDuration;
      TextView txtFreeTrialDuration = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrialDuration == null) {
        break missingId;
      }

      return new DialogExpiredTimeBinding((ConstraintLayout) rootView, backgroundImage, cardV,
          closeDialog, icFreeTrial, payNow, txtFreeTrial, txtFreeTrialDuration);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
