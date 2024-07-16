// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CardviewFreeTrailListBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final MaterialButton actualizarAhora;

  @NonNull
  public final CardView cardFree;

  @NonNull
  public final AppCompatImageView icFreeTrial;

  @NonNull
  public final LinearLayout linearLayout18;

  @NonNull
  public final MaterialTextView txtFreeTrial;

  private CardviewFreeTrailListBinding(@NonNull CardView rootView,
      @NonNull MaterialButton actualizarAhora, @NonNull CardView cardFree,
      @NonNull AppCompatImageView icFreeTrial, @NonNull LinearLayout linearLayout18,
      @NonNull MaterialTextView txtFreeTrial) {
    this.rootView = rootView;
    this.actualizarAhora = actualizarAhora;
    this.cardFree = cardFree;
    this.icFreeTrial = icFreeTrial;
    this.linearLayout18 = linearLayout18;
    this.txtFreeTrial = txtFreeTrial;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static CardviewFreeTrailListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CardviewFreeTrailListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.cardview_free_trail_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CardviewFreeTrailListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.actualizarAhora;
      MaterialButton actualizarAhora = ViewBindings.findChildViewById(rootView, id);
      if (actualizarAhora == null) {
        break missingId;
      }

      CardView cardFree = (CardView) rootView;

      id = R.id.ic_freeTrial;
      AppCompatImageView icFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (icFreeTrial == null) {
        break missingId;
      }

      id = R.id.linearLayout18;
      LinearLayout linearLayout18 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout18 == null) {
        break missingId;
      }

      id = R.id.txtFreeTrial;
      MaterialTextView txtFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (txtFreeTrial == null) {
        break missingId;
      }

      return new CardviewFreeTrailListBinding((CardView) rootView, actualizarAhora, cardFree,
          icFreeTrial, linearLayout18, txtFreeTrial);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
