package diogoferreira.androidbluetootharduino;

/**
 * Created by diogoferreira on 19/11/16.
 */
public class Model {

    public int calculate(int valuealcohol){
        //Do the model calculus;
        return 0;
    }

    public int hours(int seconds){
        int hours = seconds/3600;
        //return hours;
        return 2;
    }
    public int minutes(int seconds){
        int hours = seconds/3600;
        int minutes = seconds/60 - hours*60;
        //return minutes;
        return 42;
    }

}
