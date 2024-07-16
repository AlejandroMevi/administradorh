// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentRegisterEmployeeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputEditText apMaternoEmployee;

  @NonNull
  public final TextInputEditText apPaternoEmployee;

  @NonNull
  public final MaterialButton btnBackRegEmployee;

  @NonNull
  public final AppCompatImageView editphoto;

  @NonNull
  public final AppCompatImageView employePhotoEnrroll;

  @NonNull
  public final FrameLayout employeeAdmin;

  @NonNull
  public final AppCompatImageView employeeImage;

  @NonNull
  public final TextInputEditText estacionesSeleccinadas;

  @NonNull
  public final LinearLayout linearLayout7;

  @NonNull
  public final LinearLayout loadingAnimationEmployee;

  @NonNull
  public final ViewLoadingNewBinding loadingAnimationWelcome;

  @NonNull
  public final TextInputEditText nameEmployee;

  @NonNull
  public final TextInputEditText numEmployeRegister;

  @NonNull
  public final ViewLoadingNewBinding photoLoadingAnimation;

  @NonNull
  public final MaterialButton saveEmployee;

  @NonNull
  public final TextInputEditText zonasSeleccinadasR;

  private FragmentRegisterEmployeeBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextInputEditText apMaternoEmployee, @NonNull TextInputEditText apPaternoEmployee,
      @NonNull MaterialButton btnBackRegEmployee, @NonNull AppCompatImageView editphoto,
      @NonNull AppCompatImageView employePhotoEnrroll, @NonNull FrameLayout employeeAdmin,
      @NonNull AppCompatImageView employeeImage, @NonNull TextInputEditText estacionesSeleccinadas,
      @NonNull LinearLayout linearLayout7, @NonNull LinearLayout loadingAnimationEmployee,
      @NonNull ViewLoadingNewBinding loadingAnimationWelcome,
      @NonNull TextInputEditText nameEmployee, @NonNull TextInputEditText numEmployeRegister,
      @NonNull ViewLoadingNewBinding photoLoadingAnimation, @NonNull MaterialButton saveEmployee,
      @NonNull TextInputEditText zonasSeleccinadasR) {
    this.rootView = rootView;
    this.apMaternoEmployee = apMaternoEmployee;
    this.apPaternoEmployee = apPaternoEmployee;
    this.btnBackRegEmployee = btnBackRegEmployee;
    this.editphoto = editphoto;
    this.employePhotoEnrroll = employePhotoEnrroll;
    this.employeeAdmin = employeeAdmin;
    this.employeeImage = employeeImage;
    this.estacionesSeleccinadas = estacionesSeleccinadas;
    this.linearLayout7 = linearLayout7;
    this.loadingAnimationEmployee = loadingAnimationEmployee;
    this.loadingAnimationWelcome = loadingAnimationWelcome;
    this.nameEmployee = nameEmployee;
    this.numEmployeRegister = numEmployeRegister;
    this.photoLoadingAnimation = photoLoadingAnimation;
    this.saveEmployee = saveEmployee;
    this.zonasSeleccinadasR = zonasSeleccinadasR;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentRegisterEmployeeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentRegisterEmployeeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_register_employee, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentRegisterEmployeeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.apMaternoEmployee;
      TextInputEditText apMaternoEmployee = ViewBindings.findChildViewById(rootView, id);
      if (apMaternoEmployee == null) {
        break missingId;
      }

      id = R.id.apPaternoEmployee;
      TextInputEditText apPaternoEmployee = ViewBindings.findChildViewById(rootView, id);
      if (apPaternoEmployee == null) {
        break missingId;
      }

      id = R.id.btnBackRegEmployee;
      MaterialButton btnBackRegEmployee = ViewBindings.findChildViewById(rootView, id);
      if (btnBackRegEmployee == null) {
        break missingId;
      }

      id = R.id.editphoto;
      AppCompatImageView editphoto = ViewBindings.findChildViewById(rootView, id);
      if (editphoto == null) {
        break missingId;
      }

      id = R.id.employePhotoEnrroll;
      AppCompatImageView employePhotoEnrroll = ViewBindings.findChildViewById(rootView, id);
      if (employePhotoEnrroll == null) {
        break missingId;
      }

      id = R.id.employeeAdmin;
      FrameLayout employeeAdmin = ViewBindings.findChildViewById(rootView, id);
      if (employeeAdmin == null) {
        break missingId;
      }

      id = R.id.employeeImage;
      AppCompatImageView employeeImage = ViewBindings.findChildViewById(rootView, id);
      if (employeeImage == null) {
        break missingId;
      }

      id = R.id.estacionesSeleccinadas;
      TextInputEditText estacionesSeleccinadas = ViewBindings.findChildViewById(rootView, id);
      if (estacionesSeleccinadas == null) {
        break missingId;
      }

      id = R.id.linearLayout7;
      LinearLayout linearLayout7 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout7 == null) {
        break missingId;
      }

      id = R.id.loadingAnimationEmployee;
      LinearLayout loadingAnimationEmployee = ViewBindings.findChildViewById(rootView, id);
      if (loadingAnimationEmployee == null) {
        break missingId;
      }

      id = R.id.loadingAnimationWelcome;
      View loadingAnimationWelcome = ViewBindings.findChildViewById(rootView, id);
      if (loadingAnimationWelcome == null) {
        break missingId;
      }
      ViewLoadingNewBinding binding_loadingAnimationWelcome = ViewLoadingNewBinding.bind(loadingAnimationWelcome);

      id = R.id.nameEmployee;
      TextInputEditText nameEmployee = ViewBindings.findChildViewById(rootView, id);
      if (nameEmployee == null) {
        break missingId;
      }

      id = R.id.numEmployeRegister;
      TextInputEditText numEmployeRegister = ViewBindings.findChildViewById(rootView, id);
      if (numEmployeRegister == null) {
        break missingId;
      }

      id = R.id.photoLoadingAnimation;
      View photoLoadingAnimation = ViewBindings.findChildViewById(rootView, id);
      if (photoLoadingAnimation == null) {
        break missingId;
      }
      ViewLoadingNewBinding binding_photoLoadingAnimation = ViewLoadingNewBinding.bind(photoLoadingAnimation);

      id = R.id.saveEmployee;
      MaterialButton saveEmployee = ViewBindings.findChildViewById(rootView, id);
      if (saveEmployee == null) {
        break missingId;
      }

      id = R.id.zonasSeleccinadasR;
      TextInputEditText zonasSeleccinadasR = ViewBindings.findChildViewById(rootView, id);
      if (zonasSeleccinadasR == null) {
        break missingId;
      }

      return new FragmentRegisterEmployeeBinding((ConstraintLayout) rootView, apMaternoEmployee,
          apPaternoEmployee, btnBackRegEmployee, editphoto, employePhotoEnrroll, employeeAdmin,
          employeeImage, estacionesSeleccinadas, linearLayout7, loadingAnimationEmployee,
          binding_loadingAnimationWelcome, nameEmployee, numEmployeRegister,
          binding_photoLoadingAnimation, saveEmployee, zonasSeleccinadasR);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
