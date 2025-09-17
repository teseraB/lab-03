package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import androidx.annotation.NonNull;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void updateList();
    }

    private AddCityDialogListener listener;

    // Creates a AddCityFragment and attaches the selected City to it
    public static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city); // key = "city", value = City object

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // If editing, show the city’s current details in the text boxes
        City selectedCity;
        if (getArguments() != null) {
            selectedCity = (City) getArguments().getSerializable("city");
            if (selectedCity != null) {
                editCityName.setText(selectedCity.getName());
                editProvinceName.setText(selectedCity.getProvince());
            }
        } else {
            selectedCity = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add/edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    // Add new city or update existing one
                    if (selectedCity == null) {
                        City newCity = new City(cityName, provinceName);
                        listener.addCity(newCity);
                    } else {
                        City editedCity = selectedCity;
                        editedCity.setName(cityName);
                        editedCity.setProvince(provinceName);
                        listener.updateList();
                    }
                })
                .create();
    }
}