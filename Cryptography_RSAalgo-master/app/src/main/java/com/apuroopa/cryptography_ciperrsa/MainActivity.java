package com.apuroopa.cryptography_ciperrsa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "AsymmetricAlgorithmRSA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// Original text
        String theTestText = "This is just a simple test!";
        TextView tvorig = (TextView)findViewById(R.id.tvorig);
        tvorig.setText("\n[ORIGINAL]:\n" + theTestText + "\n");

        // Generate key pair for 1024-bit RSA encryption and decryption
        /*Key publicKey = null;
        Key privateKey = null;*/
        PublicKey pubKey=null;
        PrivateKey priKey=null;
        String RSA_mode="RSA/ECB/OAEPwithSHA-512andMGF1Padding";
        String modulusAsString="ycN3OQPiSTBQo5lHvMfVj5TVy/SjXfcDBYc9p+kbXAqn9yQdyGmg3Uq6eIoX316BM62cU6p9Km61PTjH7lXYvgvyERik7bjwr+hCl6fYyW5CWpOzOJuqKPNd83mSNRBEvPm128OYdIn+CXZtg1svelmurc6kl1DXcSlwn9DaTCjy6a5QUVKatRosN9vDhShWOhyWgNHrrmPQpx2ZcVLh5ejoTkZghgR76wHMEHZaTZAyz2ODuLxuxCFC6DbzLvUb7M9z9zNDzojYsPYysUUambdFyN7N6WiUipmvuJZ6M8g8PBiC99wYMhUSdXj3vJ8n7hKCrTa5SMIjiF2FuujX4Q==";
        String exponentAsString="AQAB";
        try {
            /*KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();*/
          /*  publicKey = kp.getPublic();
            privateKey = kp.getPrivate();*/



            BigInteger modulus = new BigInteger(1,
                      Base64.decode(modulusAsString.getBytes("UTF-16"),Base64.DEFAULT));
            BigInteger exponent = new BigInteger(1,
                    Base64.decode(exponentAsString.getBytes("UTF-16"),Base64.DEFAULT));

          /*  BigInteger modulus = new BigInteger(Base64.decode(modulusAsString,Base64.DEFAULT));
            BigInteger exponent =  new BigInteger(Base64. decode(exponentAsString,Base64.DEFAULT));*/

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

              pubKey =keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            Log.e(TAG, "RSA key pair error");
        }

        // Encode the original data with RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance(RSA_mode);
            c.init(Cipher.ENCRYPT_MODE, pubKey);
            encodedBytes = c.doFinal(theTestText.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "RSA encryption error");
        }
        TextView tvencoded = (TextView)findViewById(R.id.tvencoded);
        tvencoded.setText("[ENCODED]:\n" +
                Base64.encodeToString(encodedBytes, Base64.DEFAULT) + "\n");

        // Decode the encoded data with RSA public key
        byte[] decodedBytes = null;
        try {
            BigInteger modulus = new BigInteger(1,
                    Base64.decode(modulusAsString.getBytes("UTF-16"),Base64.DEFAULT));
            BigInteger exponent = new BigInteger(1,
                    Base64.decode(exponentAsString.getBytes("UTF-16"),Base64.DEFAULT));

            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            priKey =keyFactory.generatePrivate(keySpec);
            Cipher c = Cipher.getInstance(RSA_mode);
            c.init(Cipher.DECRYPT_MODE, priKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e(TAG, "RSA decryption error");
        }
        TextView tvdecoded = (TextView)findViewById(R.id.tvdecoded);
        tvdecoded.setText("[DECODED]:\n" + new String(decodedBytes) + "\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
