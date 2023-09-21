-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.provider.api.impl'

-keep public class !io.appmetrica.analytics.push.provider.api.impl.**, io.appmetrica.analytics.push.provider.api.** {
    public *;
    protected *;
}
