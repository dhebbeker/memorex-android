package info.hebbeker.david.memorex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class LicenseView extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WebView webview = new WebView(this);
        webview.loadUrl("file:///android_asset/software-licenses.html");
        setContentView(webview);
    }
}
