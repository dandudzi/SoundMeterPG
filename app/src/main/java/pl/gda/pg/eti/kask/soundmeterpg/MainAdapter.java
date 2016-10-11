package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by gierl on 28.09.2016.
 */
public class MainAdapter  extends ArrayAdapter<Sample> {
    public MainAdapter(Context context, ArrayList<Sample> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       Sample sample = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.samples_item, parent, false);
        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final Sample sample = getItem(position);
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
                                                remove(sample);
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
        TextView tvAVG = (TextView) convertView.findViewById(R.id.decibels);
        TextView tvLatitude = (TextView) convertView.findViewById(R.id.latitude);
        TextView tvLongitude = (TextView) convertView.findViewById(R.id.longitude);
        CheckBox cbStoredSample = (CheckBox) convertView.findViewById(R.id.stored_sample);

        /*tvAVG.setText( String.valueOf(sample.getAvgNoiseLevel()));
        tvLatitude.setText(String.valueOf(sample.getLatitude()));
        tvLongitude.setText(String.valueOf(sample.getLongitude()));
        if(sample.getState())
        cbStoredSample.setChecked(true);*/
        // Return the completed view to render on screen
        return convertView;
    }
}
