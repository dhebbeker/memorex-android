package info.hebbeker.david.memorex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class DisplayAboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about);
        final String aboutText = getResources().getString(R.string.about_text, BuildConfig.VERSION_NAME);
        final TextView textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(aboutText));
    }
}
