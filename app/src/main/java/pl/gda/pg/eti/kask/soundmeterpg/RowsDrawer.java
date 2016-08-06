package pl.gda.pg.eti.kask.soundmeterpg;

import android.content.Context;

import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.RowsDrawerException;

/**
 * Created by Daniel on 06.08.2016 at 14:54 :).
 */
public class RowsDrawer {
    public static AdapterDrawer createRows(Context context, int layoutId) throws RowsDrawerException {
        String[] listTitle = context.getResources().getStringArray(R.array.rows_list_drawer);
        int [] itemId = {R.mipmap.ic_politechnika,R.mipmap.ic_politechnika,R.mipmap.ic_politechnika};

        ItemDrawer[] displayItem =  new ItemDrawer[listTitle.length];
        if(listTitle.length!=itemId.length)
            throw new RowsDrawerException("Number of columns not equal number of icons");

        for(int i = 0;i<displayItem.length;i++){
            displayItem[i] =  new ItemDrawer(itemId[i],listTitle[i]);
        }

        return  new AdapterDrawer(context,layoutId,displayItem);
    }
}
