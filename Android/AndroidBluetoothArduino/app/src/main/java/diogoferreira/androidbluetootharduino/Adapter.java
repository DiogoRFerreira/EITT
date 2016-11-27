package diogoferreira.androidbluetootharduino;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by diogoferreira on 22/11/16.
 */
public class Adapter extends ArrayAdapter<String> {
    Context c;
        String[] countries;
        int[] images;

    public Adapter(Context ctx,String[] countries, int[] images){
        super(ctx,R.layout.support_simple_spinner_dropdown_item,countries);
        this.c = ctx;
        this.countries=countries;
        this.images=images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.modelspinner,null);
        }

        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        //Set Data
        nameTv.setText(countries[position]);
        img.setImageResource(images[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.modelspinner,null);
        }

        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        //Set Data
        try{
            nameTv.setText(countries[position]);
            img.setImageResource(images[position]);
        }catch(NullPointerException error){

        }

        return convertView;
    }
}
