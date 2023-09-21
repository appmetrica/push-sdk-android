-dontwarn io.appmetrica.analytics.push.provider.hms.**
-keeppackagenames io.appmetrica.analytics.push.provider.hms.**
-keep class io.appmetrica.analytics.push.provider.hms.** { *; }
-keep enum io.appmetrica.analytics.push.provider.hms.** { *; }

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
