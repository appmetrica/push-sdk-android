-dontwarn io.appmetrica.analytics.push.coreutils.**
-keeppackagenames io.appmetrica.analytics.push.coreutils.**
-keep class io.appmetrica.analytics.push.coreutils.** { *; }
-keep enum io.appmetrica.analytics.push.coreutils.** { *; }

-keep,allowobfuscation @io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline class * { *; }
