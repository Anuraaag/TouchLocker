package com.example.jagraj.fingerprint;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class HomeActivity extends AppCompatActivity {
    MqttAndroidClient client;
    String topic,clientId;
    MqttConnectOptions options;
    IMqttToken token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this, "tcp://m11.cloudmqtt.com:11093",clientId);
        options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("dpuhuqmn");
        options.setPassword("J6rvrL_4Ggq_".toCharArray());
        try {
            token = client.connect(options);
        }
        catch (Exception e)
        {

        }
        try
        {
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(HomeActivity.this, "Connected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(HomeActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        Button b1  = (Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = "FingerPrints";
                String payload = "1";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(false);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button b2  = (Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topic = "FingerPrints";
                String payload = "0";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(false);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
