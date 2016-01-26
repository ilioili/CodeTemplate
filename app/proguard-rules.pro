# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android IDE\sdk/tools/proguard/proguard-android.txt
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
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify


# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keepattributes Signature
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep serializable classes and necessary members for serializable classes
# Copied from the ProGuard manual at http://proguard.sourceforge.net.
-keep class * implements java.io.Serializable

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't w about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn org.**
-dontwarn com.igexin.**
-dontwarn cn.evun.gap.**

-keep class * extends android.app.Activity
-keep class * extends android.view.View
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.preference.Preference


-keep public class cn.evun.gap.openapi.**{*;}
-keep public interface cn.evun.gap.openapi.**{*;}

-keep public class org.**{*;}
-keep public interface org.**{*;}

-keep public class com.fasterxml.**{*;}
-keep public interface com.fasterxml.**{*;}


-keep public interface com.igexin.**{*;}
-keep public class com.igexin.**{*;}

-keep public interface com.baidu.**{*;}
-keep public class com.baidu.**{*;}

-keep public interface com.umeng.**{*;}
-keep public class com.umeng.**{*;}

-keep public interface com.diegocarloslima.**{*;}
-keep public class com.diegocarloslima.**{*;}

-keep public class cn.evun.gap.bean.**{*;}





#-assumenosideeffects class android.com.unknown.com.taihe.template.app.base.util.Log {
#　　public static *** d(...);
#　　public static *** v(...);
#}
#-assumenosideeffects class android.com.unknown.com.taihe.template.app.base.util.Log {
#　　public static *** d(...);
#　　public static *** v(...);
#}



