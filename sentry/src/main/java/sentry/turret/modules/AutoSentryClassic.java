package icbm.sentry.turret.modules;

import icbm.sentry.turret.SentryTypes;
import icbm.sentry.turret.block.TileSentry;

public class AutoSentryClassic extends AutoSentry
{
    public AutoSentryClassic(TileSentry host)
    {
        super(host);
        maxHealth = 200;
        this.centerOffset.y = 0.65;
    }
    
}