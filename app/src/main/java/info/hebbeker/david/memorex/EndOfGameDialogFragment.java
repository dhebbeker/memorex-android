package info.hebbeker.david.memorex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.io.Serializable;

public class EndOfGameDialogFragment extends DialogFragment
{
    public final static String keyLastScore = "KEY_LAST_SCORE";
    public final static String keyCurrentHighScore = "KEY_CURRENT_HIGH_SCORE";
    public final static String keyIsNewHighScore = "KEY_IS_NEW_HIGH_SCORE";
    private Serializable lastScore = null;
    private Serializable currentHighScore = null;
    private boolean isNewHighScore = false;

    @Override
    public void setArguments(Bundle args)
    {
        super.setArguments(args);
        this.lastScore = args.getSerializable(keyLastScore);
        this.currentHighScore = args.getSerializable(keyCurrentHighScore);
        this.isNewHighScore = args.getBoolean(keyIsNewHighScore);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Game Over!");
        String message = "Your score is: " + lastScore.toString() + '\n';
        if (isNewHighScore)
        {
            /* https://unicode.org/emoji/charts/full-emoji-list.html#1f389 */
            final String partyPopper = "\uD83C\uDF89";
            message += "Congratulations, this is a new high score! " + partyPopper;
        } else
        {
            message += "The current high score is: " + currentHighScore.toString();
        }
        builder.setMessage(message);
        return builder.create();
    }
}
