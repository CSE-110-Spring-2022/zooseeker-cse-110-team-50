package edu.ucsd.cse110.zooseeker.Route;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import edu.ucsd.cse110.zooseeker.R;


public class GPSSettingDialogFragment extends DialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, boolean isMock, double latitude, double longitude);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DialogListener listener;
    RadioButton realButton;
    RadioButton mockButton;
    EditText latitudeField;
    EditText longitudeField;
    boolean isMock;
    double defaultLat;
    double defaultLog;

    public GPSSettingDialogFragment(boolean isMock, double lat, double log) {
        super();
        this.isMock = isMock;
        defaultLat = lat;
        defaultLog = log;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gps_setting_dialog, null);

        realButton = view.findViewById(R.id.gps_real_button);
        mockButton = view.findViewById(R.id.gps_mock_button);
        latitudeField = view.findViewById(R.id.gps_latitude_field);
        longitudeField = view.findViewById(R.id.gps_longitude_field);

        realButton.setChecked(!isMock);
        mockButton.setChecked(isMock);
        latitudeField.setText("" + defaultLat);
        longitudeField.setText("" + defaultLog);

        realButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                isMock = !checked;
            }
        });

        mockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                isMock = checked;
            }
        });

        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        double latitude = 0;
                        double longitude = 0;
                        if (isMock) {
                            try {
                                latitude = Double.parseDouble(latitudeField.getText().toString());
                                longitude = Double.parseDouble(longitudeField.getText().toString());
                            } catch (Exception e) {
                                latitude = 0;
                                longitude = 0;
                            }
                        }

                        listener.onDialogPositiveClick(
                                GPSSettingDialogFragment.this, isMock, latitude, longitude);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(GPSSettingDialogFragment.this);
                    }
                });

        return builder.create();
    }
}