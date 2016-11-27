package diogoferreira.androidbluetootharduino;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by diogoferreira on 22/11/16.
 */
public class OpeningActivity  extends AppCompatActivity {

    ImageView imageIntro;
    Spinner sp;
    String[] countries = {"PT","UK","USA"};
    int[] images={R.mipmap.pt, R.mipmap.uk,R.mipmap.usa};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening);

        sp = (Spinner)findViewById(R.id.spinner);
        Adapter adapter = new Adapter(this,countries,images);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),countries[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageIntro = (ImageView) findViewById(R.id.imageIntro);
        imageIntro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = sp.getSelectedItemPosition();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("SpinnerInfo", position);
                startActivity(intent);
                finish();
            }
        });
    }
}
