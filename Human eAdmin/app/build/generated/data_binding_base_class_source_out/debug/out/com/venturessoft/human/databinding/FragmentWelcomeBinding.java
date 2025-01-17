// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentWelcomeBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextInputLayout adminEmployeeTextFieldV;

  @NonNull
  public final LinearLayout adminFlowInfoLayout;

  @NonNull
  public final LinearLayout adminLayout;

  @NonNull
  public final LinearLayout adminTitle;

  @NonNull
  public final LinearLayout chartBar;

  @NonNull
  public final MaterialTextView checkHistoryText;

  @NonNull
  public final LinearLayout dateTimeLayout;

  @NonNull
  public final TextInputEditText etAdminEmployee;

  @NonNull
  public final BarChart graficChard;

  @NonNull
  public final LinearLayout lineCenter;

  @NonNull
  public final CardviewFreeTrailBinding linearFreeTrial;

  @NonNull
  public final LinearLayout linearLayout20;

  @NonNull
  public final ViewLoadingNewBinding loadingAnimationWelcome;

  @NonNull
  public final MaterialTextView messageRequiredAction;

  @NonNull
  public final MaterialButton okButton;

  @NonNull
  public final LinearLayout pieBarLayout;

  @NonNull
  public final PieChart pieChar;

  @NonNull
  public final LinearLayout pieLegent;

  @NonNull
  public final MaterialTextView razonTitleText;

  @NonNull
  public final MaterialTextView tvCosumidos;

  @NonNull
  public final TextView tvRemaining;

  @NonNull
  public final TextView userName;

  @NonNull
  public final LinearLayout welcomeAdmin;

  private FragmentWelcomeBinding(@NonNull ScrollView rootView,
      @NonNull TextInputLayout adminEmployeeTextFieldV, @NonNull LinearLayout adminFlowInfoLayout,
      @NonNull LinearLayout adminLayout, @NonNull LinearLayout adminTitle,
      @NonNull LinearLayout chartBar, @NonNull MaterialTextView checkHistoryText,
      @NonNull LinearLayout dateTimeLayout, @NonNull TextInputEditText etAdminEmployee,
      @NonNull BarChart graficChard, @NonNull LinearLayout lineCenter,
      @NonNull CardviewFreeTrailBinding linearFreeTrial, @NonNull LinearLayout linearLayout20,
      @NonNull ViewLoadingNewBinding loadingAnimationWelcome,
      @NonNull MaterialTextView messageRequiredAction, @NonNull MaterialButton okButton,
      @NonNull LinearLayout pieBarLayout, @NonNull PieChart pieChar,
      @NonNull LinearLayout pieLegent, @NonNull MaterialTextView razonTitleText,
      @NonNull MaterialTextView tvCosumidos, @NonNull TextView tvRemaining,
      @NonNull TextView userName, @NonNull LinearLayout welcomeAdmin) {
    this.rootView = rootView;
    this.adminEmployeeTextFieldV = adminEmployeeTextFieldV;
    this.adminFlowInfoLayout = adminFlowInfoLayout;
    this.adminLayout = adminLayout;
    this.adminTitle = adminTitle;
    this.chartBar = chartBar;
    this.checkHistoryText = checkHistoryText;
    this.dateTimeLayout = dateTimeLayout;
    this.etAdminEmployee = etAdminEmployee;
    this.graficChard = graficChard;
    this.lineCenter = lineCenter;
    this.linearFreeTrial = linearFreeTrial;
    this.linearLayout20 = linearLayout20;
    this.loadingAnimationWelcome = loadingAnimationWelcome;
    this.messageRequiredAction = messageRequiredAction;
    this.okButton = okButton;
    this.pieBarLayout = pieBarLayout;
    this.pieChar = pieChar;
    this.pieLegent = pieLegent;
    this.razonTitleText = razonTitleText;
    this.tvCosumidos = tvCosumidos;
    this.tvRemaining = tvRemaining;
    this.userName = userName;
    this.welcomeAdmin = welcomeAdmin;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentWelcomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentWelcomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_welcome, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentWelcomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.adminEmployeeTextFieldV;
      TextInputLayout adminEmployeeTextFieldV = ViewBindings.findChildViewById(rootView, id);
      if (adminEmployeeTextFieldV == null) {
        break missingId;
      }

      id = R.id.adminFlowInfoLayout;
      LinearLayout adminFlowInfoLayout = ViewBindings.findChildViewById(rootView, id);
      if (adminFlowInfoLayout == null) {
        break missingId;
      }

      id = R.id.adminLayout;
      LinearLayout adminLayout = ViewBindings.findChildViewById(rootView, id);
      if (adminLayout == null) {
        break missingId;
      }

      id = R.id.adminTitle;
      LinearLayout adminTitle = ViewBindings.findChildViewById(rootView, id);
      if (adminTitle == null) {
        break missingId;
      }

      id = R.id.chartBar;
      LinearLayout chartBar = ViewBindings.findChildViewById(rootView, id);
      if (chartBar == null) {
        break missingId;
      }

      id = R.id.checkHistoryText;
      MaterialTextView checkHistoryText = ViewBindings.findChildViewById(rootView, id);
      if (checkHistoryText == null) {
        break missingId;
      }

      id = R.id.dateTimeLayout;
      LinearLayout dateTimeLayout = ViewBindings.findChildViewById(rootView, id);
      if (dateTimeLayout == null) {
        break missingId;
      }

      id = R.id.et_adminEmployee;
      TextInputEditText etAdminEmployee = ViewBindings.findChildViewById(rootView, id);
      if (etAdminEmployee == null) {
        break missingId;
      }

      id = R.id.graficChard;
      BarChart graficChard = ViewBindings.findChildViewById(rootView, id);
      if (graficChard == null) {
        break missingId;
      }

      id = R.id.lineCenter;
      LinearLayout lineCenter = ViewBindings.findChildViewById(rootView, id);
      if (lineCenter == null) {
        break missingId;
      }

      id = R.id.linearFreeTrial;
      View linearFreeTrial = ViewBindings.findChildViewById(rootView, id);
      if (linearFreeTrial == null) {
        break missingId;
      }
      CardviewFreeTrailBinding binding_linearFreeTrial = CardviewFreeTrailBinding.bind(linearFreeTrial);

      id = R.id.linearLayout20;
      LinearLayout linearLayout20 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout20 == null) {
        break missingId;
      }

      id = R.id.loadingAnimationWelcome;
      View loadingAnimationWelcome = ViewBindings.findChildViewById(rootView, id);
      if (loadingAnimationWelcome == null) {
        break missingId;
      }
      ViewLoadingNewBinding binding_loadingAnimationWelcome = ViewLoadingNewBinding.bind(loadingAnimationWelcome);

      id = R.id.messageRequiredAction;
      MaterialTextView messageRequiredAction = ViewBindings.findChildViewById(rootView, id);
      if (messageRequiredAction == null) {
        break missingId;
      }

      id = R.id.okButton;
      MaterialButton okButton = ViewBindings.findChildViewById(rootView, id);
      if (okButton == null) {
        break missingId;
      }

      id = R.id.pieBarLayout;
      LinearLayout pieBarLayout = ViewBindings.findChildViewById(rootView, id);
      if (pieBarLayout == null) {
        break missingId;
      }

      id = R.id.pieChar;
      PieChart pieChar = ViewBindings.findChildViewById(rootView, id);
      if (pieChar == null) {
        break missingId;
      }

      id = R.id.pieLegent;
      LinearLayout pieLegent = ViewBindings.findChildViewById(rootView, id);
      if (pieLegent == null) {
        break missingId;
      }

      id = R.id.razonTitleText;
      MaterialTextView razonTitleText = ViewBindings.findChildViewById(rootView, id);
      if (razonTitleText == null) {
        break missingId;
      }

      id = R.id.tv_cosumidos;
      MaterialTextView tvCosumidos = ViewBindings.findChildViewById(rootView, id);
      if (tvCosumidos == null) {
        break missingId;
      }

      id = R.id.tv_remaining;
      TextView tvRemaining = ViewBindings.findChildViewById(rootView, id);
      if (tvRemaining == null) {
        break missingId;
      }

      id = R.id.userName;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      id = R.id.welcomeAdmin;
      LinearLayout welcomeAdmin = ViewBindings.findChildViewById(rootView, id);
      if (welcomeAdmin == null) {
        break missingId;
      }

      return new FragmentWelcomeBinding((ScrollView) rootView, adminEmployeeTextFieldV,
          adminFlowInfoLayout, adminLayout, adminTitle, chartBar, checkHistoryText, dateTimeLayout,
          etAdminEmployee, graficChard, lineCenter, binding_linearFreeTrial, linearLayout20,
          binding_loadingAnimationWelcome, messageRequiredAction, okButton, pieBarLayout, pieChar,
          pieLegent, razonTitleText, tvCosumidos, tvRemaining, userName, welcomeAdmin);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
