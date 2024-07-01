-include ../../proguard/proguard-root-project.txt

-repackageclasses 'io.appmetrica.analytics.push.logger.impl'

-keep public class !io.appmetrica.analytics.push.logger.impl.**, io.appmetrica.analytics.push.logger.** {
    public *;
    protected *;
}
