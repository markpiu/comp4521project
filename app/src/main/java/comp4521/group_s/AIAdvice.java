package comp4521.group_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIAdvice extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<AIMessage> messageList;
    AIMessageAdapter messageAdapter;
    androidx.appcompat.widget.Toolbar toolbar;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiadvice);

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }


        //setup recycler view
        messageAdapter = new AIMessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = messageEditText.getText().toString().trim();
                addToChat(question, AIMessage.SENT_BY_ME);
                messageEditText.setText("");
                callAPI(question);
                welcomeTextView.setVisibility(View.GONE);
            }
        });
    }



        void addToChat(String message,String sentBy){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageList.add(new AIMessage(message,sentBy));
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            });
        }

        void addResponse(String response){
            messageList.remove(messageList.size()-1);
            addToChat(response,AIMessage.SENT_BY_BOT);
        }


        void callAPI(String question){
            //okhttp
            messageList.add(new AIMessage("Typing... ",AIMessage.SENT_BY_BOT));

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("model","text-davinci-003");
                jsonBody.put("prompt",question);
                jsonBody.put("max_tokens",4000);
                jsonBody.put("temperature",0.8);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/completions")
                    .header("Authorization","Bearer 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")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    addResponse("Failed to load response due to "+e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        JSONObject  jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            String result = jsonArray.getJSONObject(0).getString("text");
                            addResponse(result.trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{
                        addResponse("Failed to load response due to "+response.body().toString());
                    }
                }
            });


        }

    }