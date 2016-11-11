package pl.gda.pg.eti.kask.soundmeterpg.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import pl.gda.pg.eti.kask.soundmeterpg.Database.DataBaseHandler;
import pl.gda.pg.eti.kask.soundmeterpg.Database.MeasurementDataBaseObject;
import pl.gda.pg.eti.kask.soundmeterpg.Dialogs.MenuMeasurement;
import pl.gda.pg.eti.kask.soundmeterpg.ViewAdapter;
import pl.gda.pg.eti.kask.soundmeterpg.R;

/**
 * Created by Daniel on 10.08.2016 at 18:3;i0 :).
 */
public class Measurements extends Fragment  implements AdapterView.OnItemLongClickListener{
ListView listView;
    ViewAdapter adapter;
    ArrayList<MeasurementDataBaseObject> arrayOfMeasurements ;
    DataBaseHandler dataBaseHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.measurements, container, false);
        listView = (ListView)relativeLayout.findViewById(R.id.measurementsListView);
        addHeaderToListView(inflater);
        listView.setOnItemLongClickListener(this);

        return  relativeLayout;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        arrayOfMeasurements = new ArrayList<>();
        Random random = new Random();
        dataBaseHandler = new DataBaseHandler(getActivity().getBaseContext(), getResources().getString(R.string.database_name));
        //MeasurementStatistics measurementStatistics = new MeasurementStatistics();
        //DO TESTOW NIE USUWAC MI TEGO!
      /*  for (int i = 0; i < 10; i++){
            MeasurementStatistics measurementStatistics = new MeasurementStatistics();
            measurementStatistics.min = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
            measurementStatistics.max = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
            measurementStatistics.avg = random.nextInt(Sample.MAX_NOISE_LEVEL - Sample.MIN_NOISE_LEVEL) + Sample.MIN_NOISE_LEVEL;
           arrayOfMeasurements.add(new Measurement(measurementStatistics, new FakeLocation(), false, new Date()));
        }
        arrayOfMeasurements.add(new Measurement(new MeasurementStatistics(), new Location(11.2, 11.4), false, new Date()));
        */
        arrayOfMeasurements = dataBaseHandler.getMeasurementArray();
        adapter = new ViewAdapter(activity, arrayOfMeasurements);;
    }


    private void addHeaderToListView(LayoutInflater inflater) {
        ViewGroup myHeader = (ViewGroup) inflater.inflate(R.layout.samples_header_item, listView, false);
        listView.addHeaderView(myHeader, null, false);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuMeasurement menuMeasurement = new MenuMeasurement(getActivity(), adapter, position);
        AlertDialog dialog = menuMeasurement.create();
        dialog.show();

        return true;
    }

}
