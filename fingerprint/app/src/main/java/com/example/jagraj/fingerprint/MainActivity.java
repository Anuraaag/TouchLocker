package com.example.jagraj.fingerprint;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private KeyStore keyStore;
    private final String KEY_NAME = "Jagraj";
    private Cipher cipher;
    //private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("One Touch");
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        if(!fingerprintManager.isHardwareDetected())
            Toast.makeText(this,"No hardware found",Toast.LENGTH_SHORT).show();
        else
            {
                if(!fingerprintManager.hasEnrolledFingerprints())
                    Toast.makeText(this,"No fingerprint found",Toast.LENGTH_SHORT).show();
                else
                    if(!keyguardManager.isKeyguardSecure())
                        Toast.makeText(this,"Lock screen sec not enabled",Toast.LENGTH_SHORT).show();
                    else
                        genkey();
                    if(cipherInit())
                    {
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler helper = new FingerprintHandler(this);
                        helper.startAuthentication(fingerprintManager,cryptoObject);
                    }
            }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getSupportActionBar().setTitle("One Touch");
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        if(!fingerprintManager.isHardwareDetected())
            Toast.makeText(this,"No hardware found",Toast.LENGTH_SHORT).show();
        else
        {
            if(!fingerprintManager.hasEnrolledFingerprints())
                Toast.makeText(this,"No fingerprint found",Toast.LENGTH_SHORT).show();
            else
            if(!keyguardManager.isKeyguardSecure())
                Toast.makeText(this,"Lock screen sec not enabled",Toast.LENGTH_SHORT).show();
            else
                genkey();
            if(cipherInit())
            {
                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintHandler helper = new FingerprintHandler(this);
                helper.startAuthentication(fingerprintManager,cryptoObject);
            }
        }

    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        }
            try {
                keyStore.load(null);
                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return false;
            } catch (CertificateException e1) {
                e1.printStackTrace();
                return false;
            } catch (UnrecoverableKeyException e1) {
                e1.printStackTrace();
                return false;
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
                return false;
            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
                return false;
            }




    }



    private void genkey()
    {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }

    }
}
