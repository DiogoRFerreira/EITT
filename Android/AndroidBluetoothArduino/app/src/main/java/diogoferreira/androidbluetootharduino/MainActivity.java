package diogoferreira.androidbluetootharduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ACTIVATION = 1;
    private final static int REQUEST_CONNECTION = 2;


    Button btnConnect;
    Button btnAskValue;
    TextView ValueAlcohol;
    TextView ValuePrediction;
    ImageView caryes;
    ImageView carno;

    float alcoholvaluelimit;

    boolean connection = false;
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothDevice myDevice = null;
    BluetoothSocket mySocket = null;
    private static String MAC = null;
    UUID myuuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    Model modelinUse;

    float[] AlcoholValuesPermited = {0,5, 0,8, 0,8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int alcoholvalue = getIntent().getIntExtra("SpinnerInfo",0);
        alcoholvaluelimit = AlcoholValuesPermited[alcoholvalue];

        modelinUse = new Model();

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnAskValue = (Button) findViewById(R.id.btnContinue);
        ValueAlcohol = (TextView) findViewById(R.id.valueView);
        ValuePrediction = (TextView) findViewById(R.id.valueView2);
        caryes = (ImageView) findViewById(R.id.imageYescar);
        carno = (ImageView) findViewById(R.id.imageNocar);

        ValueAlcohol.setText(" ");
        ValuePrediction.setText(" ");

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myBluetoothAdapter==null){//Check if the device has bluetooth
            Toast.makeText(getApplicationContext(),"Device doesn't has Bluetooth",Toast.LENGTH_LONG).show();
        }else if(!myBluetoothAdapter.isEnabled()){//Activate Bluetooth
            Intent activate_bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activate_bluetooth, REQUEST_ACTIVATION);
        }

        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(connection){
                    //disconnect
                    try{
                        mySocket.close();
                        connection = false;
                        Toast.makeText(getApplicationContext(),"Device disconnected",Toast.LENGTH_LONG).show();
                        btnConnect.setText("Connect");
                    }catch (IOException error){
                        Toast.makeText(getApplicationContext(),"Error: "+error,Toast.LENGTH_LONG).show();

                    }
                }else{
                    //connect
                    Intent openList= new Intent(MainActivity.this,ListDevices.class);
                    startActivityForResult(openList,REQUEST_CONNECTION);
                }
            }
        });

        btnAskValue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(connection){
                    new Thread(new ConnectedThread(mySocket)).start();
                    //Toast.makeText(getApplicationContext(),"Thread Created",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_ACTIVATION:
                if(resultCode== Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(),"Bluetooth is activated",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth wasn't enabled",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_CONNECTION:
                if(resultCode ==Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ListDevices.MAC_ADDRESS);
                    //Toast.makeText(getApplicationContext(),"MAC FINAL: "+MAC,Toast.LENGTH_LONG).show();
                    myDevice = myBluetoothAdapter.getRemoteDevice(MAC);

                    try{
                        mySocket = myDevice.createRfcommSocketToServiceRecord(myuuid);
                        mySocket.connect();
                        connection = true;
                        //Toast.makeText(getApplicationContext(),"Connected to: "+MAC,Toast.LENGTH_LONG).show();
                        btnConnect.setText("Disconnect");


                    }catch(IOException error){
                        Toast.makeText(getApplicationContext(),"Error ocorred",Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Failure to obtain MAC Address",Toast.LENGTH_LONG).show();

                }
        }
    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    byte[] buffer = new byte[1024];  // buffer store for the stream
                    int bytes; // bytes returned from read()

                    try {
                        String text = "1";
                        buffer = text.getBytes();
                        mmOutStream.write(buffer);
                        //Toast.makeText(getApplicationContext(),"Send: "+text,Toast.LENGTH_LONG).show();
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        text = new String(buffer, "UTF-8");
                        //Toast.makeText(getApplicationContext(),"Received: "+text,Toast.LENGTH_LONG).show();
                        //ValueAlcohol.setText(text+" mg/L");
                        String text3 ="0,9 mg/L";
                        ValueAlcohol.setText(text3);

                        // Call the function to do the prediction a set the prediction value
                        int seconds=modelinUse.calculate(Integer.parseInt(text));
                        String text1 = String.valueOf(modelinUse.hours(seconds));
                        String text2 = String.valueOf(modelinUse.minutes(seconds));
                        ValuePrediction.setText(text1+"h"+" "+text2+"min");
                        /*if(Integer.parseInt(text)>alcoholvaluelimit){
                            caryes.setVisibility(View.INVISIBLE);
                            carno.setVisibility(View.VISIBLE);
                        }else{
                            caryes.setVisibility(View.VISIBLE);
                            carno.setVisibility(View.INVISIBLE);
                        }*/
                        if(0.9>alcoholvaluelimit){
                            caryes.setVisibility(View.INVISIBLE);
                            carno.setVisibility(View.VISIBLE);
                        }else{
                            caryes.setVisibility(View.VISIBLE);
                            carno.setVisibility(View.INVISIBLE);
                        }
                    } catch (IOException e) {

                    }
                }
                });
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
