package comp4521.group_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProteinSuggestion extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    WebView webView;


    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protein_suggestion);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        // Load the website URL
        webView.loadUrl("https://www.google.com/search?q=protein+suggestion");
    }



}