package info.hebbeker.david.memorex;

/**
 * Does contain information about a score.
 * \todo Add the following attributes: username, speed, date, points
 */
public class Score
{
    /**
     * Completed level.
     * <p>
     * If the player fails at level 2, the completed level is 1. If he fails at level 1, the
     * completed level is 0.
     */
    private final int level;

    public Score(final int level)
    {
        this.level = level;
    }
}
