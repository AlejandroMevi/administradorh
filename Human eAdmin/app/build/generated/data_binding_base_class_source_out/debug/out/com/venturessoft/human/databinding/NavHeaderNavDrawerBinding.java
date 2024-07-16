// Generated by view binder compiler. Do not edit!
package com.venturessoft.human.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.divider.MaterialDivider;
import com.venturessoft.human.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NavHeaderNavDrawerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout headerDrawer;

  @NonNull
  public final MaterialDivider imageView;

  @NonNull
  public final CircleImageView imgAvatar;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final TextView userName;

  private NavHeaderNavDrawerBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout headerDrawer, @NonNull MaterialDivider imageView,
      @NonNull CircleImageView imgAvatar, @NonNull LinearLayout linearLayout,
      @NonNull TextView userName) {
    this.rootView = rootView;
    this.headerDrawer = headerDrawer;
    this.imageView = imageView;
    this.imgAvatar = imgAvatar;
    this.linearLayout = linearLayout;
    this.userName = userName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NavHeaderNavDrawerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NavHeaderNavDrawerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.nav_header_nav_drawer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NavHeaderNavDrawerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout headerDrawer = (ConstraintLayout) rootView;

      id = R.id.imageView;
      MaterialDivider imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.imgAvatar;
      CircleImageView imgAvatar = ViewBindings.findChildViewById(rootView, id);
      if (imgAvatar == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.userName;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      return new NavHeaderNavDrawerBinding((ConstraintLayout) rootView, headerDrawer, imageView,
          imgAvatar, linearLayout, userName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}