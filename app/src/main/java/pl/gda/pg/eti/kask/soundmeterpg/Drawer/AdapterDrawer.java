package pl.gda.pg.eti.kask.soundmeterpg.Drawer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 06.08.2016 at 14:41 :).
 */
public class AdapterDrawer extends ArrayAdapter<ItemDrawer> {

    private Activity activity;
    private int layoutId;
    private ItemDrawer displayItem[];

    public AdapterDrawer(Activity activity, int layoutId, ItemDrawer[] data) {
        super(activity, layoutId, data);

        this.layoutId = layoutId;
        this.activity = activity;
        this.displayItem = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =  activity.getLayoutInflater();
        convertView = inflater.inflate(layoutId, parent, false);

        ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.icon_drawer_row);
        TextView textViewName = (TextView) convertView.findViewById(R.id.text_view_drawer_row);

        ItemDrawer item = displayItem[position];

        imageViewIcon.setImageResource(item.iconId);
        textViewName.setText(item.name);

        return convertView;
    }



}