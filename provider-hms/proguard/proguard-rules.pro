-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.provider.hms.impl'

-keep public class !io.appmetrica.analytics.push.provider.hms.impl.**, io.appmetrica.analytics.push.provider.hms.** {
    public *;
    protected *;
}
