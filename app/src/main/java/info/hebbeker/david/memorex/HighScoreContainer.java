package info.hebbeker.david.memorex;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

class HighScoreContainer
{
    final private SharedPreferences sharedPref;
    final private String highScorePreferenceKey = "HighScoreStorage";
    final private Gson gson = new Gson();
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
            currentHighScore = gson.fromJson(highScoreSerializedObject, Score.class);
            if (currentHighScore == null) // this may be overcautious
            {
                throw new IOException("Getting stored high score failed!");
            }
        }
        catch (JsonSyntaxException|IOException e)
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
        SharedPreferences.Editor editor = sharedPref.edit();
        String highScoreSerializedObject = gson.toJson(currentHighScore);
        editor.putString(highScorePreferenceKey, highScoreSerializedObject);
        editor.apply();
    }

    public Score getCurrentHighScore()
    {
        return currentHighScore;
    }
}
