package diogoferreira.androidbluetootharduino;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by diogoferreira on 01/09/16.
 */
public class ListDevices extends ListActivity {

    private BluetoothAdapter myBluetoothAdapter2 = null;

    static String MAC_ADDRESS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        myBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = myBluetoothAdapter2.getBondedDevices();

        if(devices.size()>0){
            for(BluetoothDevice device: devices){
                String nameBT = device.getName();
                String macBT = device.getAddress();
                ArrayBluetooth.add(nameBT+"\n"+macBT);
            }
        }

        setListAdapter(ArrayBluetooth);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String generalinformation = ((TextView) v).getText().toString();
        //Toast.makeText(getApplicationContext(),"Info: "+generalinformation,Toast.LENGTH_LONG).show();

        String macaddress = generalinformation.substring(generalinformation.length()-17);
        //Toast.makeText(getApplicationContext(),"MAC: "+macaddress,Toast.LENGTH_LONG).show();

        Intent returnmac = new Intent();//passar informação entre actividades
        returnmac.putExtra(MAC_ADDRESS, macaddress);
        setResult(RESULT_OK,returnmac);
        finish();
    }
}
