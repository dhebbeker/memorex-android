package info.hebbeker.david.memorex;

import android.content.SharedPreferences;

import java.io.IOError;
import java.io.IOException;

import info.hebbeker.david.util.StringSerialization;

class HighScoreContainer
{
    final private SharedPreferences sharedPref;
    final private String highScorePreferenceKey = "HighScoreStorage";
    private Score currentHighScore;

    HighScoreContainer(final SharedPreferences sharedPref)
    {
        this.sharedPref = sharedPref;
        loadHighScore();
    }

    private void loadHighScore()
    {
        try
        {
            String highScoreSerializedObject = this.sharedPref.getString(highScorePreferenceKey, "");
            currentHighScore = (Score) StringSerialization.deserialize(highScoreSerializedObject);
            if (currentHighScore == null)
            {
                throw new Exception("Getting stored high score failed!");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            currentHighScore = new Score(0);
            saveHighScore();
        }
    }

    boolean setNewHighScore(final Score newScore)
    {
        final boolean isNewHighScore = newScore.isGreaterThan(currentHighScore);
        if (isNewHighScore)
        {
            currentHighScore = newScore;
            saveHighScore();
        }
        return isNewHighScore;
    }

    private void saveHighScore()
    {
        try
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(highScorePreferenceKey, StringSerialization.serialize(currentHighScore));
            editor.apply();
        } catch (IOException e)
        {
            throw new IOError(e); // nothing I can do about it...
        }
    }
}
