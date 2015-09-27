package com.example.kavitha.codepathproject2googleimagesearch.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kavitha.codepathproject2googleimagesearch.R;
import com.example.kavitha.codepathproject2googleimagesearch.models.SettingsData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kavitha on 9/26/15.
 */
public class SettingsDialog extends DialogFragment {
    SettingsData settings;
    Button btnSave;
    Button btnCancel;
    Spinner spSize;
    Spinner spType;
    Spinner spColor;
    EditText etSearchSite;

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void returnData(SettingsData settings);
    }

    public SettingsDialog() {
        ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.settings_dialog, container);


        settings = getArguments().getParcelable("settingsData");

        spSize  = (Spinner) view.findViewById(R.id.spSize);

        List<String> imgSizes = new ArrayList<String>();
        imgSizes.addAll(Arrays.asList(getResources().getStringArray(R.array.imgSizes)));
        int sizePos = settings.getImageSize() == "" ? 0 : imgSizes.indexOf(settings.getImageSize());
        spSize.setSelection(sizePos);

        spType  = (Spinner) view.findViewById(R.id.spType);
        List<String> imgTypes = new ArrayList<String>();
        imgTypes.addAll(Arrays.asList(getResources().getStringArray(R.array.imgTypes)));
        int typePos = settings.getImageType() == "" ? 0 : imgTypes.indexOf(settings.getImageType());
        spType.setSelection(typePos);

        spColor  = (Spinner) view.findViewById(R.id.spColor);
        List<String> imgColors = new ArrayList<String>();
        imgColors.addAll(Arrays.asList(getResources().getStringArray(R.array.imgColors)));
        int colorPos = settings.getImageColor() == "" ? 0 : imgColors.indexOf(settings.getImageColor());
        spColor.setSelection(colorPos);

        etSearchSite = (EditText) view.findViewById(R.id.etSearchSite);
        etSearchSite.setText(settings.getSearchWebsite());

        getDialog().setTitle(Html.fromHtml("<font color=\"#3f729b\">Advanced Filters</font>"));

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setImageSize((String) spSize.getSelectedItem());
                settings.setImageType((String) spType.getSelectedItem());
                settings.setImageColor((String) spColor.getSelectedItem());
                settings.setSearchWebsite(etSearchSite.getText().toString());

                if (mListener != null) {
                    mListener.returnData(settings);
                }
                getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void onSubmit(View view) {
        settings.setImageSize((String) this.spSize.getSelectedItem());
        settings.setImageType((String) this.spType.getSelectedItem());
        settings.setImageColor((String) this.spColor.getSelectedItem());
        settings.setSearchWebsite(this.etSearchSite.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("settingsData", settings);
        getActivity().setResult(getActivity().RESULT_OK, intent);
        getActivity().finish();
    }

    public void onCancel(View view) {
        getActivity().finish();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

}
