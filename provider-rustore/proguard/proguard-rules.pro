-include ../../proguard/proguard-root-project.txt

-dontwarn kotlinx.coroutines.CoroutineScope

-repackageclasses 'io.appmetrica.analytics.push.provider.rustore.impl'

-keep public class !io.appmetrica.analytics.push.provider.rustore.impl.**, io.appmetrica.analytics.push.provider.rustore.** {
    public *;
    protected *;
}
