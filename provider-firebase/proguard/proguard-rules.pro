-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.provider.firebase.impl'

-keep public class !io.appmetrica.analytics.push.provider.firebase.impl.**, io.appmetrica.analytics.push.provider.firebase.** {
    public *;
    protected *;
}
