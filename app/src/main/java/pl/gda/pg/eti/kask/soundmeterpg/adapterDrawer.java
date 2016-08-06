package pl.gda.pg.eti.kask.soundmeterpg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Daniel on 06.08.2016 at 14:41 :).
 */
public class AdapterDrawer extends ArrayAdapter<ItemDrawer> {

    private Context context;
    private int layoutId;
    private ItemDrawer displayItem[];

    public AdapterDrawer(Context mContext, int layoutId, ItemDrawer[] data) {
        super(mContext, layoutId, data);

        this.layoutId = layoutId;
        this.context = mContext;
        this.displayItem = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(layoutId, parent, false);

        ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.icon_drawer_row);
        TextView textViewName = (TextView) convertView.findViewById(R.id.text_view_drawer_row);

        ItemDrawer folder = displayItem[position];

        imageViewIcon.setImageResource(folder.iconId);
        textViewName.setText(folder.name);

        return convertView;
    }



}