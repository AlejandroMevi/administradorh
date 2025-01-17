// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.codesgood.views.JustifiedTextView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentAboutUsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView advetencia;

  @NonNull
  public final Button btnBackAboutUsF;

  @NonNull
  public final TextView compilacion;

  @NonNull
  public final TextView contetcopy;

  @NonNull
  public final TextView copy;

  @NonNull
  public final TextView corporation;

  @NonNull
  public final Guideline endTitleLine;

  @NonNull
  public final JustifiedTextView miadvertencia;

  @NonNull
  public final TextView numcompilacion;

  @NonNull
  public final TextView numversion;

  @NonNull
  public final TextView tituloapp;

  @NonNull
  public final TextView version;

  private FragmentAboutUsBinding(@NonNull ConstraintLayout rootView, @NonNull TextView advetencia,
      @NonNull Button btnBackAboutUsF, @NonNull TextView compilacion, @NonNull TextView contetcopy,
      @NonNull TextView copy, @NonNull TextView corporation, @NonNull Guideline endTitleLine,
      @NonNull JustifiedTextView miadvertencia, @NonNull TextView numcompilacion,
      @NonNull TextView numversion, @NonNull TextView tituloapp, @NonNull TextView version) {
    this.rootView = rootView;
    this.advetencia = advetencia;
    this.btnBackAboutUsF = btnBackAboutUsF;
    this.compilacion = compilacion;
    this.contetcopy = contetcopy;
    this.copy = copy;
    this.corporation = corporation;
    this.endTitleLine = endTitleLine;
    this.miadvertencia = miadvertencia;
    this.numcompilacion = numcompilacion;
    this.numversion = numversion;
    this.tituloapp = tituloapp;
    this.version = version;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentAboutUsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentAboutUsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_about_us, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentAboutUsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.advetencia;
      TextView advetencia = ViewBindings.findChildViewById(rootView, id);
      if (advetencia == null) {
        break missingId;
      }

      id = R.id.btnBackAboutUsF;
      Button btnBackAboutUsF = ViewBindings.findChildViewById(rootView, id);
      if (btnBackAboutUsF == null) {
        break missingId;
      }

      id = R.id.compilacion;
      TextView compilacion = ViewBindings.findChildViewById(rootView, id);
      if (compilacion == null) {
        break missingId;
      }

      id = R.id.contetcopy;
      TextView contetcopy = ViewBindings.findChildViewById(rootView, id);
      if (contetcopy == null) {
        break missingId;
      }

      id = R.id.copy;
      TextView copy = ViewBindings.findChildViewById(rootView, id);
      if (copy == null) {
        break missingId;
      }

      id = R.id.corporation;
      TextView corporation = ViewBindings.findChildViewById(rootView, id);
      if (corporation == null) {
        break missingId;
      }

      id = R.id.end_title_line;
      Guideline endTitleLine = ViewBindings.findChildViewById(rootView, id);
      if (endTitleLine == null) {
        break missingId;
      }

      id = R.id.miadvertencia;
      JustifiedTextView miadvertencia = ViewBindings.findChildViewById(rootView, id);
      if (miadvertencia == null) {
        break missingId;
      }

      id = R.id.numcompilacion;
      TextView numcompilacion = ViewBindings.findChildViewById(rootView, id);
      if (numcompilacion == null) {
        break missingId;
      }

      id = R.id.numversion;
      TextView numversion = ViewBindings.findChildViewById(rootView, id);
      if (numversion == null) {
        break missingId;
      }

      id = R.id.tituloapp;
      TextView tituloapp = ViewBindings.findChildViewById(rootView, id);
      if (tituloapp == null) {
        break missingId;
      }

      id = R.id.version;
      TextView version = ViewBindings.findChildViewById(rootView, id);
      if (version == null) {
        break missingId;
      }

      return new FragmentAboutUsBinding((ConstraintLayout) rootView, advetencia, btnBackAboutUsF,
          compilacion, contetcopy, copy, corporation, endTitleLine, miadvertencia, numcompilacion,
          numversion, tituloapp, version);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
