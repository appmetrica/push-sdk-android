-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.provider.gcm.impl'

-keep public class !io.appmetrica.analytics.push.provider.gcm.impl.**, io.appmetrica.analytics.push.provider.gcm.** {
    public *;
    protected *;
}
