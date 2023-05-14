package comp4521.group_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleWebsite extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar4;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_article_website);


        toolbar4 = findViewById(R.id.toolbar4);

        toolbar4.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        webView = findViewById(R.id.BBCFood);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.fitnessprofessionalonline.com/articles/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}