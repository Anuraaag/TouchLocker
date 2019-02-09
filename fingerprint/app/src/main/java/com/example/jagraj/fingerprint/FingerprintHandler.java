package com.example.jagraj.fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
    private Context context;
    MqttAndroidClient client;
    String topic,clientId;
    MqttConnectOptions options;
    IMqttToken token;
    public FingerprintHandler(Context c)
    {
        this.context = c;
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, "tcp://m11.cloudmqtt.com:11093",clientId);
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
                    //Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void startAuthentication(FingerprintManager f , FingerprintManager.CryptoObject c)
    {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;;
            f.authenticate(c,cancellationSignal,0,this,null);
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult a)
    {

        super.onAuthenticationSucceeded(a);

        context.startActivity(new Intent(context,HomeActivity.class));
    }

    @Override
    public void onAuthenticationFailed()
    {
        super.onAuthenticationFailed();
        Toast.makeText(context,"Auth Failed",Toast.LENGTH_SHORT).show();
        topic = "FingerPrints";
        String payload = "0";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(false);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
