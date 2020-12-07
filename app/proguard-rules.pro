# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#<<<<<<<<<<<<<<< proguard configuration for bugly begin <<<<<<<<<<<<<<<<
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#<<<<<<<<<<<<<<< proguard configuration for bugly end <<<<<<<<<<<<<<<<

#<<<<<<<<<<<<<<< proguard configuration for glide begin <<<<<<<<<<<<<<<<
#https://blog.csdn.net/shangmingchao/article/details/51125554
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#<<<<<<<<<<<<<<< proguard configuration for glide end <<<<<<<<<<<<<<<<

#<<<<<<<<<<<<<<< proguard configuration for PersistentCookieJar begin <<<<<<<<<<<<<<<<
-dontwarn com.franmontiel.persistentcookiejar.**
-keep class com.franmontiel.persistentcookiejar.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#<<<<<<<<<<<<<<< proguard configuration for PersistentCookieJar end <<<<<<<<<<<<<<<<

#<<<<<<<<<<<<<<< proguard configuration for eventbus begin <<<<<<<<<<<<<<<<
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#<<<<<<<<<<<<<<< proguard configuration for eventbus end <<<<<<<<<<<<<<<<

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#<<<<<<<<<<<<<<< proguard configuration for rxjava begin <<<<<<<<<<<<<<<<
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#<<<<<<<<<<<<<<< proguard configuration for rxjava end <<<<<<<<<<<<<<<<

#<<<<<<<<<<<<<<< proguard configuration for okhttp <<<<<<<<<<<<<<<<
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
#>>>>>>>>>>>>>>> proguard configuration for okhttp >>>>>>>>>>>>>>>

#<<<<<<<<<<<<<<< proguard configuration for Gson begin <<<<<<<<<<<<<<<<
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *;}
#<<<<<<<<<<<<<<< proguard configuration for Gson end <<<<<<<<<<<<<<<<



-keepattributes *JavascriptInterface*
-ignorewarnings

-keep class com.lyl.wanandroid.service.entity.** {*;} #com.haier.uhome.haiergoodairabroad.bean.** { *; }

-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }



-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }