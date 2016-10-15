package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.FakeLocation;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Measurement;

/**
 * Created by gierl on 28.09.2016.
 */
public class MainAdapter  extends ArrayAdapter<MeasurementDataBaseObject> implements  CompoundButton.OnCheckedChangeListener{
    private  final Context context;
    private  ArrayList<MeasurementDataBaseObject> measurementArrayList;
    public MainAdapter(Context context, ArrayList<MeasurementDataBaseObject> measurements) {
        super(context, R.layout.samples_item, measurements);
        this.measurementArrayList = measurements;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.samples_item, parent, false);
        TextView tvAVG = (TextView) rowView.findViewById(R.id.decibels);
        TextView tvLatitude = (TextView) rowView.findViewById(R.id.dateMeasurement);
        CheckBox cbStoredSample = (CheckBox) rowView.findViewById(R.id.stored_measurement);
        MeasurementDataBaseObject measurement = getItem(position);
        tvAVG.setText( String.valueOf(measurement.getAvg()));
        tvLatitude.setText(measurement.getDate());
        if(measurement.getStoredState())
            cbStoredSample.setChecked(true);
        cbStoredSample.setTag(position);
        cbStoredSample.setOnCheckedChangeListener(this);


        //convertView.setOnLongClickListener(this);
        /*convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final Measurement measurement = getItem(position);
                PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.RIGHT);
                popupMenu.getMenuInflater().inflate(R.menu.options_menu_sample, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.trash_sample:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Are you sure?");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                remove(measurement);
                                            }
                                        });
                                builder1.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                Log.i("Popup menu options", "Clicked on " + menuItem.getTitle());
                                break;
                            default:
                                return false;

                        }

                        return true;
                    }
                });

                popupMenu.show();
                Log.i("Popup menu show", "");
                return  true;
            }
        });



        /*convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                int ID = view.getId();
                if (view.getId()==R.id.listview) {
                    Log.i("ListenerOnLongClick", "Longitude value = " + 12);
                }
            }
        });*/
      /* ImageView imageView = (ImageView) convertView.findViewById(R.id.trash_sample);
        // Cache row position inside the button using `setTag`
        imageView.setTag(position);
        // Attach the click event handler
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                Sample sample = getItem(position);
                remove(sample);
            }
        });

*/

        return rowView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.i("Main Adapter", "value = " + b);
        int position = (int) compoundButton.getTag();
        MeasurementDataBaseObject measurement = measurementArrayList.get(position);
        if(measurement.getLocation().equals(new FakeLocation())) {
            createAlert();
            measurement.storedOnWebServer = false;
        }
        else {

            DataBaseHandler dataBaseHandler = new DataBaseHandler(context, context.getResources().getString(R.string.database_name));
            dataBaseHandler.changeStoreOnServerFlag(position + 1, b);
            measurementArrayList.get(position).storedOnWebServer = b;
        }
        notifyDataSetChanged();


    }

    private void createAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Cannot store measurement which not have real location !");
        dialog.create();
        dialog.show();
    }
}
