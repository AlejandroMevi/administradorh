// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CardviewFreeTrailBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final AppCompatButton actualizarAhora;

  @NonNull
  public final ImageView backgroundImage;

  @NonNull
  public final CardView cardFree;

  @NonNull
  public final Guideline guideline12;

  @NonNull
  public final ImageView icClose;

  @NonNull
  public final ImageView icFreeTrial;

  @NonNull
  public final LinearLayout linearLayout18;

  @NonNull
  public final LinearLayout linearPeriodo;

  @NonNull
  public final TextView txtFreeTrial;

  @NonNull
  public final TextView txtFreeTrialContacto;

  @NonNull
  public final TextView txtFreeTrialDuration;

  private CardviewFreeTrailBinding(@NonNull CardView rootView,
      @NonNull AppCompatButton actualizarAhora, @NonNull ImageView backgroundImage,
      @NonNull CardView cardFree, @NonNull Guideline guideline12, @NonNull ImageView icClose,
      @NonNull ImageView icFreeTrial, @NonNull LinearLayout linearLayout18,
      @NonNull LinearLayout linearPeriodo, @NonNull TextView txtFreeTrial,
      @NonNull TextView txtFreeTrialContacto, @NonNull TextView txtFreeTrialDuration) {
    this.rootView = rootView;
    this.actualizarAhora = actualizarAhora;
    this.backgroundImage = backgroundImage;
    this.cardFree = cardFree;
    this.guideline12 = guideline12;
    this.icClose = icClose;
    this.icFreeTrial = icFreeTrial;
    this.linearLayout18 = linearLayout18;
    this.linearPeriodo = linearPeriodo;
    this.txtFreeTrial = txtFreeTrial;
    this.txtFreeTrialContacto = txtFreeTrialContacto;
    this.txtFreeTrialDuration = txtFreeTrialDuration;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static CardviewFreeTrailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CardviewFreeTrailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.cardview_free_trail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CardviewFreeTrailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.actualizarAhora;
      AppCompatButton actualizarAhora = ViewBindings.findChildViewById(rootView, id);
      if (actualizarAhora == null) {
        break missingId;
      }

      id = R.id.backgroundImage;
      ImageView backgroundImage = ViewBindings.findChildViewById(rootView, id);
      if (backgroundImage == null) {
        break missingId;
      }

      CardView cardFree = (CardView) rootView;

      id = R.id.guideline12;
      Guideline guideline12 = ViewBindings.findChildViewById(rootView, id);
      if (guideline12 == null) {
        break missingId;
      }

      id = R.id.ic_close;
      ImageView icClose = ViewBindings.findChildViewById(rootView, id);
      if (icClose == null) {
        break missingId;
      }

      id = R.id.ic_freeTrial;
      ImageView icFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (icFreeTrial == null) {
        break missingId;
      }

      id = R.id.linearLayout18;
      LinearLayout linearLayout18 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout18 == null) {
        break missingId;
      }

      id = R.id.linearPeriodo;
      LinearLayout linearPeriodo = ViewBindings.findChildViewById(rootView, id);
      if (linearPeriodo == null) {
        break missingId;
      }

      id = R.id.txtFreeTrial;
      TextView txtFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrial == null) {
        break missingId;
      }

      id = R.id.txtFreeTrialContacto;
      TextView txtFreeTrialContacto = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrialContacto == null) {
        break missingId;
      }

      id = R.id.txtFreeTrialDuration;
      TextView txtFreeTrialDuration = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrialDuration == null) {
        break missingId;
      }

      return new CardviewFreeTrailBinding((CardView) rootView, actualizarAhora, backgroundImage,
          cardFree, guideline12, icClose, icFreeTrial, linearLayout18, linearPeriodo, txtFreeTrial,
          txtFreeTrialContacto, txtFreeTrialDuration);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
