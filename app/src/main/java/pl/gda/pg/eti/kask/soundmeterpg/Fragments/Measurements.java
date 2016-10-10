package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverRangeException;
import pl.gda.pg.eti.kask.soundmeterpg.MainAdapter;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.SoundMeter.Sample;

/**
 * Created by Daniel on 10.08.2016 at 18:3;i0 :).
 */
public class Measurements extends Fragment {
ListView listView;
    MainAdapter adapter;
    ArrayList<Sample> arrayOfUsers ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout lf = (LinearLayout) inflater.inflate(R.layout.measurements, container, false);
        listView = (ListView) lf.findViewById(R.id.listview);
        ViewGroup myHeader = (ViewGroup) inflater.inflate(R.layout.samples_header_item, listView, false);
        listView.addHeaderView(myHeader, null, false);
        listView.setAdapter(adapter);
        return  lf;
    }

    @Override
    public void onAttach(Activity activity) {
        arrayOfUsers = new ArrayList<>();
        DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext(), getResources().getString(R.string.database_name));
        try {
           // arrayOfUsers = dataBaseHandler.getSamples();
        } catch (OverRangeException e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
        //adapter = new MainAdapter(activity, arrayOfUsers);
      //  adapter.addAll(arrayOfUsers);

    }
}
