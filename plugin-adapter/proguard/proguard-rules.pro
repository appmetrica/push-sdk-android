-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.plugin.adapter.impl'

-keep public class !io.appmetrica.analytics.push.plugin.adapter.impl.**, io.appmetrica.analytics.push.plugin.adapter.** {
    public *;
    protected *;
}
