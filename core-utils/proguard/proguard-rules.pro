-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.coreutils.impl'

-keep public class !io.appmetrica.analytics.push.coreutils.impl.**, io.appmetrica.analytics.push.coreutils.** {
    public *;
    protected *;
}

-keep,allowobfuscation @io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline class * {
    *;
}
