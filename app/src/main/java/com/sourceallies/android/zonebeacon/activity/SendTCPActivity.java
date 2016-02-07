package com.sourceallies.android.zonebeacon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.task.TcpAsyncTask;

public class SendTCPActivity extends AppCompatActivity {

    private TextView responseText;
    private EditText commandText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_tcp);
        responseText = (TextView) findViewById(R.id.response);
        commandText = (EditText) findViewById(R.id.command_text);
        sendButton = (Button) findViewById(R.id.send_command);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TcpAsyncTask.runOnTestServer(commandText.getText().toString(), new TcpAsyncTask.Callback() {
                    @Override
                    public void onResponse(String responseText) {
                        SendTCPActivity.this.responseText.setText(responseText);
                    }
                });
            }
        });
    }


}
