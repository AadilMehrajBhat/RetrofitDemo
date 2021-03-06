package com.example.aadilmehraj.ideamanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.aadilmehraj.ideamanager.services.MessageService;
import com.example.aadilmehraj.ideamanager.services.ServiceBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        MessageService messageService = ServiceBuilder.buildService(MessageService.class);
        Call<String> messageCall;
        messageCall = messageService.getMessage();
//        messageCall = messageService.getMessage("http://10.0.2.2:7000/messages");

        messageCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ((TextView) findViewById(R.id.message)).setText(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ((TextView) findViewById(R.id.message)).setText(R.string.request_failed);
            }
        });
       }

    public void GetStarted(View view){
        Intent intent = new Intent(this, IdeaListActivity.class);
        startActivity(intent);
    }
}
