-dontwarn io.appmetrica.analytics.push.provider.firebase.**
-keeppackagenames io.appmetrica.analytics.push.provider.firebase.**
-keep class io.appmetrica.analytics.push.provider.firebase.** { *; }
-keep enum io.appmetrica.analytics.push.provider.firebase.** { *; }

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
