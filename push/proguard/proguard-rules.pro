-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.impl'

-keep public class !io.appmetrica.analytics.push.impl.**, io.appmetrica.analytics.push.** {
    public *;
    protected *;
}
