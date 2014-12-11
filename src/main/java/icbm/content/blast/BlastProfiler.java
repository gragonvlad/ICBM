package icbm.content.blast;

import resonant.lib.debug.profiler.Profiler;

/**
 * Created by robert on 12/10/2014.
 */
public class BlastProfiler extends Profiler
{
    public BlastProfiler()
    {
        super("BlastBasicProfiler");
    }

    public BlastRunProfile run(BlastBasic blast)
    {
        BlastRunProfile profile = new BlastRunProfile(blast);
        this.profileRuns.put(profile.name, profile);
        return profile;
    }
}