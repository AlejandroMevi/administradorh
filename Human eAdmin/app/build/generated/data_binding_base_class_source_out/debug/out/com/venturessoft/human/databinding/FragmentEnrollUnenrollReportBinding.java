// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentEnrollUnenrollReportBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBackMenuReport;

  @NonNull
  public final MaterialButton btnDownloadReport;

  @NonNull
  public final TextInputEditText dateFrom;

  @NonNull
  public final TextInputEditText dateUp;

  @NonNull
  public final ConstraintLayout loadingAnimationEnroll;

  @NonNull
  public final MaterialRadioButton radioEmpEnroll;

  @NonNull
  public final MaterialRadioButton radioEmpUnenroll;

  @NonNull
  public final RadioGroup radioGroup;

  @NonNull
  public final TextInputLayout textDateUp;

  @NonNull
  public final TextInputLayout textView3;

  @NonNull
  public final AppCompatTextView txtReport;

  private FragmentEnrollUnenrollReportBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnBackMenuReport, @NonNull MaterialButton btnDownloadReport,
      @NonNull TextInputEditText dateFrom, @NonNull TextInputEditText dateUp,
      @NonNull ConstraintLayout loadingAnimationEnroll, @NonNull MaterialRadioButton radioEmpEnroll,
      @NonNull MaterialRadioButton radioEmpUnenroll, @NonNull RadioGroup radioGroup,
      @NonNull TextInputLayout textDateUp, @NonNull TextInputLayout textView3,
      @NonNull AppCompatTextView txtReport) {
    this.rootView = rootView;
    this.btnBackMenuReport = btnBackMenuReport;
    this.btnDownloadReport = btnDownloadReport;
    this.dateFrom = dateFrom;
    this.dateUp = dateUp;
    this.loadingAnimationEnroll = loadingAnimationEnroll;
    this.radioEmpEnroll = radioEmpEnroll;
    this.radioEmpUnenroll = radioEmpUnenroll;
    this.radioGroup = radioGroup;
    this.textDateUp = textDateUp;
    this.textView3 = textView3;
    this.txtReport = txtReport;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentEnrollUnenrollReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentEnrollUnenrollReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_enroll__unenroll__report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentEnrollUnenrollReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnBackMenuReport;
      Button btnBackMenuReport = ViewBindings.findChildViewById(rootView, id);
      if (btnBackMenuReport == null) {
        break missingId;
      }

      id = R.id.btnDownloadReport;
      MaterialButton btnDownloadReport = ViewBindings.findChildViewById(rootView, id);
      if (btnDownloadReport == null) {
        break missingId;
      }

      id = R.id.dateFrom;
      TextInputEditText dateFrom = ViewBindings.findChildViewById(rootView, id);
      if (dateFrom == null) {
        break missingId;
      }

      id = R.id.dateUp;
      TextInputEditText dateUp = ViewBindings.findChildViewById(rootView, id);
      if (dateUp == null) {
        break missingId;
      }

      id = R.id.loadingAnimationEnroll;
      ConstraintLayout loadingAnimationEnroll = ViewBindings.findChildViewById(rootView, id);
      if (loadingAnimationEnroll == null) {
        break missingId;
      }

      id = R.id.radio_emp_enroll;
      MaterialRadioButton radioEmpEnroll = ViewBindings.findChildViewById(rootView, id);
      if (radioEmpEnroll == null) {
        break missingId;
      }

      id = R.id.radio_emp_unenroll;
      MaterialRadioButton radioEmpUnenroll = ViewBindings.findChildViewById(rootView, id);
      if (radioEmpUnenroll == null) {
        break missingId;
      }

      id = R.id.radioGroup;
      RadioGroup radioGroup = ViewBindings.findChildViewById(rootView, id);
      if (radioGroup == null) {
        break missingId;
      }

      id = R.id.textDateUp;
      TextInputLayout textDateUp = ViewBindings.findChildViewById(rootView, id);
      if (textDateUp == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextInputLayout textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.txtReport;
      AppCompatTextView txtReport = ViewBindings.findChildViewById(rootView, id);
      if (txtReport == null) {
        break missingId;
      }

      return new FragmentEnrollUnenrollReportBinding((ConstraintLayout) rootView, btnBackMenuReport,
          btnDownloadReport, dateFrom, dateUp, loadingAnimationEnroll, radioEmpEnroll,
          radioEmpUnenroll, radioGroup, textDateUp, textView3, txtReport);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
