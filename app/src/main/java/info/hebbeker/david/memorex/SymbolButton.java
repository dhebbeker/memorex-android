package info.hebbeker.david.memorex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import static java.lang.Math.max;

/**
 * Extends AppCompatButton with capabilities to signal a symbol.
 * <p>
 * Symbols can be one of the following: 0, 1, 2, 3
 */
final class SymbolButton extends android.support.v7.widget.AppCompatButton implements Symbol
{
    final static private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
    private static long lastSignallingDuration = 0;
    private static int maxSoundDuration = 0;
    final private int symbol;
    final private Animation animation;
    private final String preferenceKeySilent;
    private final String preferenceKeySpeed;
    private final SharedPreferences sharedPreferences;
    private int soundId = -1;

    public SymbolButton(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        {
            final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SymbolButton);
            symbol = styledAttributes.getInteger(R.styleable.SymbolButton_symbol, 0);
            styledAttributes.recycle();
        }
        animation = AnimationUtils.loadAnimation(context, R.anim.shake);

        try
        {
            final int rawSoundResourceId = getRawSoundResourceId();
            soundId = soundPool.load(context, rawSoundResourceId, 1);
            try
            {
                maxSoundDuration = max(maxSoundDuration, getSoundDuration(context, rawSoundResourceId));
            } catch (Exception exceptionGettingSoundDuration)
            {

                // can not do much in case getting sound duration fails. Fall back to default value.
                exceptionGettingSoundDuration.printStackTrace();
            }
        } catch (Exception exceptionLoadingSoundResource)
        {
            // can not do much in case getting sound duration fails. Fall back to default value.
            // An invalid soundId will not be played but does not lead to an exception.
            exceptionLoadingSoundResource.printStackTrace();
        }
        preferenceKeySilent = context.getString(R.string.preference_switch_sound);
        preferenceKeySpeed = context.getString(R.string.preference_speed_list);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    static long getLastSignallingDuration()
    {
        return lastSignallingDuration;
    }

    /**
     * @param rawId the raw resource id ({@code R.raw.<something>}) for the resource to use as sound source
     * @return the duration in milliseconds, if no duration is available exception is thrown
     */
    private int getSoundDuration(final Context context, final int rawId) throws Exception
    {
        final MediaPlayer mediaPlayer = MediaPlayer.create(context, rawId);
        final int duration = mediaPlayer.getDuration();
        mediaPlayer.release();
        if (duration < 0)
            throw new Exception("fetching duration of sound failed (raw resource id:" + rawId + ", duration:" + duration + ")");
        return duration;
    }

    /**
     * @return the raw resource id ({@code R.raw.<something>}) for the resource to use as sound source
     */
    private int getRawSoundResourceId() throws Exception
    {
        switch (symbol)
        {
            case 0:
                return R.raw.keyok1;
            case 1:
                return R.raw.keyok2;
            case 2:
                return R.raw.keyok3;
            case 3:
                return R.raw.keyok5;
            default:
                throw new Exception("No sound resource found for symbol '" + symbol + "'!");
        }
    }

    void signalSymbol()
    {
        animation.setDuration(Long.parseLong(sharedPreferences.getString(preferenceKeySpeed, "250")));
        lastSignallingDuration = getSignallingDuration();
        startAnimation(animation);
        if (!sharedPreferences.getBoolean(preferenceKeySilent, false))
        {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    long getSignallingDuration()
    {
        return max(animation.getDuration(), maxSoundDuration);
    }
}
