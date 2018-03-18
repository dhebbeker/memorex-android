package info.hebbeker.david.memorex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import java.io.Serializable;

public class DisplayHighScore extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_high_score);
        final Serializable currentHighScore = getIntent().getSerializableExtra(MainActivity.HIGH_SCORE_DATA);
        final String aboutText = getResources().getString(R.string.high_score_display, currentHighScore.toString());
        final TextView textView = findViewById(R.id.textViewHighScore);
        textView.setText(Html.fromHtml(aboutText));
    }
}
