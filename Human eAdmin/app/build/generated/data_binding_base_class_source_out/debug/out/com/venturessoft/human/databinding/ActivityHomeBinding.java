// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.venturessoft.human.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityHomeBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout activityMain;

  @NonNull
  public final FrameLayout fragmentContainer;

  @NonNull
  public final ViewLoadingNewBinding loadingAnimation;

  @NonNull
  public final MainActivityToolbarBinding toolBar;

  private ActivityHomeBinding(@NonNull RelativeLayout rootView,
      @NonNull RelativeLayout activityMain, @NonNull FrameLayout fragmentContainer,
      @NonNull ViewLoadingNewBinding loadingAnimation,
      @NonNull MainActivityToolbarBinding toolBar) {
    this.rootView = rootView;
    this.activityMain = activityMain;
    this.fragmentContainer = fragmentContainer;
    this.loadingAnimation = loadingAnimation;
    this.toolBar = toolBar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.activity_main;
      RelativeLayout activityMain = ViewBindings.findChildViewById(rootView, id);
      if (activityMain == null) {
        break missingId;
      }

      id = R.id.fragmentContainer;
      FrameLayout fragmentContainer = ViewBindings.findChildViewById(rootView, id);
      if (fragmentContainer == null) {
        break missingId;
      }

      id = R.id.loadingAnimation;
      View loadingAnimation = ViewBindings.findChildViewById(rootView, id);
      if (loadingAnimation == null) {
        break missingId;
      }
      ViewLoadingNewBinding binding_loadingAnimation = ViewLoadingNewBinding.bind(loadingAnimation);

      id = R.id.toolBar;
      View toolBar = ViewBindings.findChildViewById(rootView, id);
      if (toolBar == null) {
        break missingId;
      }
      MainActivityToolbarBinding binding_toolBar = MainActivityToolbarBinding.bind(toolBar);

      return new ActivityHomeBinding((RelativeLayout) rootView, activityMain, fragmentContainer,
          binding_loadingAnimation, binding_toolBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
