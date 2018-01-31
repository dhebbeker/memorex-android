package info.hebbeker.david.memorex;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by dhebbeker on 21.01.18.
 */

final class SymbolButton extends android.support.v7.widget.AppCompatButton
{
    final static private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
    final private int symbol;
    final private int soundId;
    final private Animation animation;

    public SymbolButton(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SymbolButton);
        symbol = a.getInteger(R.styleable.SymbolButton_symbol, 0);
        soundId = soundPool.load(context, getResourceId(), 1);
        animation = AnimationUtils.loadAnimation(context, R.anim.shake);
    }

    private int getResourceId()
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
                return 0;
        }
    }

    final void signalSymbol()
    {
        startAnimation(animation);
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
    }

    final long getSignallingDuration()
    {
        return animation.getDuration();
    }

    public int getSymbol()
    {
        return symbol;
    }
}
