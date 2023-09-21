-dontwarn io.appmetrica.analytics.push.provider.rustore.**
-keeppackagenames io.appmetrica.analytics.push.provider.rustore.**
-keep class io.appmetrica.analytics.push.provider.rustore.** { *; }
-keep enum io.appmetrica.analytics.push.provider.rustore.** { *; }

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
