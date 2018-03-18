package info.hebbeker.david.memorex;

/**
 * Does contain information about a score.
 * \todo Add the following attributes: username, speed, date, points
 */
class Score
{
    /**
     * Completed level.
     * <p>
     * If the player fails at level 2, the completed level is 1. If he fails at level 1, the
     * completed level is 0.
     */
    private final int level;

    Score(final int level)
    {
        this.level = level;
    }

    /**
     * @return true if this score is greater than other score
     */
    boolean isGreaterThan(final Score otherScore)
    {
        return this.level > otherScore.level;
    }
}
