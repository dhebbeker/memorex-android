package info.hebbeker.david.memorex;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by dhebbeker on 21.01.18.
 */

public class SymbolButton extends android.support.v7.widget.AppCompatButton
{
    public int symbol = 0;

    public SymbolButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SymbolButton);
        symbol = a.getInteger(R.styleable.SymbolButton_symbol, 0);
    }
}
