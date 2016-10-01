package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import pl.gda.pg.eti.kask.soundmeterpg.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Exceptions.OverrangeException;
import pl.gda.pg.eti.kask.soundmeterpg.MainAdapter;
import pl.gda.pg.eti.kask.soundmeterpg.R;
import pl.gda.pg.eti.kask.soundmeterpg.Sample;

import static pl.gda.pg.eti.kask.soundmeterpg.R.layout.measurements;

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
            arrayOfUsers = dataBaseHandler.getSamples();
        } catch (OverrangeException e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
        adapter = new MainAdapter(activity, arrayOfUsers);
        adapter.addAll(arrayOfUsers);

    }
}
