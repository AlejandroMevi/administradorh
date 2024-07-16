# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/airtouchnewmedia08/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keep class com.venturessoft.human.services.** { *; }
-dontwarn com.venturessoft.human.webservices.**

-keep class com.venturessoft.human.models.** { *; }
-dontwarn com.venturessoft.human.models.**

-keep class com.venturessoft.human.repository.** { *; }
-dontwarn com.venturessoft.human.repository.**

-keep class com.venturessoft.human.views.ui.fragments.CameraAdmin.** { *; }
-dontwarn com.venturessoft.human.views.ui.fragments.CameraAdmin.**

-keep class com.venturessoft.human.views.ui.fragments.Camera.** { *; }
-dontwarn com.venturessoft.human.views.ui.fragments.Camera.**

-keep class io.fotoapparat.facedetector.** { *; }
-dontwarn io.**

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**


-keep class com.venturessoft.human.Camera.** { *; }
-dontwarn com.venturessoft.human.Camera.**
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory {*;}

-keep class * {
    public private *;
}
