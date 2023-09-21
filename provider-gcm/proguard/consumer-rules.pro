-dontwarn io.appmetrica.analytics.push.provider.gcm.**
-keeppackagenames io.appmetrica.analytics.push.provider.gcm.**
-keep class io.appmetrica.analytics.push.provider.gcm.** { *; }
-keep enum io.appmetrica.analytics.push.provider.gcm.** { *; }


-keep public class com.google.android.gms.gcm.GcmReceiver {
    public *;
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
