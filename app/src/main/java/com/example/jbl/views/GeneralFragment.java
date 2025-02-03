package com.example.jbl.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.jbl.Config;
import com.example.jbl.R;
import com.example.jbl.databinding.FragmentGeneralsBinding;
import com.example.jbl.tools.SharedViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GeneralFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    FragmentGeneralsBinding fragmentGeneralsBinding;
    private SharedViewModel sharedViewModel;
    int gardu = 0;
    private Config cfg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGeneralsBinding = FragmentGeneralsBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Create an ArrayAdapter Kompany to populate the Spinner
        List<String> companyList = new ArrayList<>();
        companyList.add("JORR-W1");
        companyList.add("JORR-W2");
        companyList.add("JORR-W3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, companyList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_items);
        fragmentGeneralsBinding.coursesspinner.setAdapter(adapter);

        // Set listener for Spinner item selection
        fragmentGeneralsBinding.coursesspinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Set default selection to the first item in the array
        fragmentGeneralsBinding.coursesspinner.setSelection(0);
        fragmentGeneralsBinding.coursesspinner.setEnabled(false);


        // Create an ArrayAdapter Type Gardu to populate the Spinner
        List<String> typeGarduList = new ArrayList<>();
        typeGarduList.add("OPEN-MULTI");
        typeGarduList.add("SINGLE");
        ArrayAdapter<String> adapterTypeGarud = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeGarduList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTypeGarud.setDropDownViewResource(R.layout.spinner_items);
        fragmentGeneralsBinding.garduSpinner.setAdapter(adapterTypeGarud);

        // Set listener for Spinner item selection
        fragmentGeneralsBinding.garduSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Set default selection to the first item in the array
        fragmentGeneralsBinding.garduSpinner.setSelection(0);
        fragmentGeneralsBinding.garduSpinner.setEnabled(false);


        // Create an ArrayAdapter Type Gardu to populate the Spinner
        List<String> typePrintList = new ArrayList<>();
        typePrintList.add("Online");
        typePrintList.add("Manual");
        ArrayAdapter<String> adapterTypePrint = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typePrintList);
        adapterTypePrint.setDropDownViewResource(R.layout.spinner_items);
        fragmentGeneralsBinding.printSpinner.setAdapter(adapterTypePrint);

        // Set listener for Spinner item selection
        fragmentGeneralsBinding.printSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Set default selection to the first item in the array
        fragmentGeneralsBinding.printSpinner.setSelection(0);
        fragmentGeneralsBinding.printSpinner.setEnabled(false);

        cfg = Config.getInstance2(requireContext());

        int garduID = cfg.getGarduID();
        Log.i("gardu id", " "+garduID);
        if (garduID == 0) {
            fragmentGeneralsBinding.gardu.setText("0");
        } else {
            fragmentGeneralsBinding.gardu.setText(String.valueOf(garduID));
        }


        int gerbangNumber = cfg.getGerbangNumber();
        Log.i("getGerbangNumber", " "+garduID);
        if (gerbangNumber == 0) {
            fragmentGeneralsBinding.gerbang.setText("0");
        } else {
            fragmentGeneralsBinding.gerbang.setText(String.valueOf(gerbangNumber));
        }

        return fragmentGeneralsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Set nilai dari ViewModel ke input field
        sharedViewModel.getMidBRI().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                fragmentGeneralsBinding.instuitionCode.setText(fullName);
            }
        });

        sharedViewModel.getTidBRI().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                fragmentGeneralsBinding.instuitionCode.setText(fullName2);
            }
        });

        sharedViewModel.getProCodeBRI().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                fragmentGeneralsBinding.instuitionCode.setText(fullName);
            }
        });

        // Simpan nilai ke ViewModel saat tombol ditekan
        // Simpan nilai ke ViewModel saat tombol ditekan
        fragmentGeneralsBinding.btnSendGeneral.setOnClickListener(view1 -> {

            String instuitionCode = fragmentGeneralsBinding.instuitionCode.getText().toString();
            String cabang = fragmentGeneralsBinding.cabang.getText().toString();
            String gerbang = fragmentGeneralsBinding.gerbang.getText().toString();
            String gardu = fragmentGeneralsBinding.gardu.getText().toString();
            String uid = fragmentGeneralsBinding.uid.getText().toString();
            String ipPcs = fragmentGeneralsBinding.ipPcs.getText().toString();


            // Validasi input

            if (instuitionCode.isEmpty()) {
                fragmentGeneralsBinding.instuitionCode.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.instuitionCode.requestFocus();
                return;
            }
            if (cabang.isEmpty()) {
                fragmentGeneralsBinding.cabang.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.cabang.requestFocus();
                return;
            }
            if (gerbang.isEmpty()) {
                fragmentGeneralsBinding.gerbang.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.gerbang.requestFocus();
                return;
            }
            if (gardu.isEmpty()) {
                fragmentGeneralsBinding.gardu.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.gardu.requestFocus();
                return;
            }
            if (uid.isEmpty()) {
                fragmentGeneralsBinding.uid.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.uid.requestFocus();
                return;
            }
            if (ipPcs.isEmpty()) {
                fragmentGeneralsBinding.ipPcs.setError("Input tidak boleh kosong");
                fragmentGeneralsBinding.ipPcs.requestFocus();
                return;
            }
            sharedViewModel.getInstitutionCompany().setValue(kompany);
            sharedViewModel.getTipeGardu().setValue(selectedGardu);
            sharedViewModel.getTipePrint().setValue(selectedVoltage);
            sharedViewModel.getInstitutionCode().setValue(fragmentGeneralsBinding.instuitionCode.getText().toString());
            sharedViewModel.getCabang().setValue(fragmentGeneralsBinding.cabang.getText().toString());
            sharedViewModel.getGerbang().setValue(fragmentGeneralsBinding.gerbang.getText().toString());
            sharedViewModel.getGardu().setValue(fragmentGeneralsBinding.gardu.getText().toString());
            sharedViewModel.getUid().setValue(fragmentGeneralsBinding.uid.getText().toString());
            sharedViewModel.getIpPcs().setValue(fragmentGeneralsBinding.ipPcs.getText().toString());

            // Menampilkan semua input dari FirstFragment dan SecondFragment
            String institutionCodeInput = sharedViewModel.getInstitutionCode().getValue();
            String cabangInput2 = sharedViewModel.getCabang().getValue();
            String gardInput2 = sharedViewModel.getGardu().getValue();
            String selectKompany = sharedViewModel.getInstitutionCompany().getValue();
            String garduConfig =  fragmentGeneralsBinding.gardu.getText().toString();
            cfg.setGarduID(Integer.parseInt(garduConfig));
            String gerbangNumber =  fragmentGeneralsBinding.gerbang.getText().toString();
            cfg.setGerbangNumber(Integer.parseInt(gerbangNumber));
            Toast.makeText(requireContext(),
                    "Input garduConfig: " + garduConfig + ", " +
                            "\nInput gerbangNumber: " + gerbangNumber,
                    Toast.LENGTH_LONG).show();


            // Get the FragmentManager
            MoveToMandiriFragment();

        });


    }

    private void MoveToMandiriFragment() {
        TabLayout tabLayout = requireActivity().findViewById(R.id.simpleTabLayout);
        tabLayout.getTabAt(1).select(); // Pilih tab kedua

        FragmentManager fm = getActivity().getSupportFragmentManager(); // Use getActivity() to get the Activity's FragmentManager
        FragmentTransaction ft = fm.beginTransaction();

        // Hide the current fragment (FirstFragment)
        ft.hide(GeneralFragment.this); // Use FirstFragment.this to reference the current fragment instance

        // Find or create the SecondFragment
        Fragment secondFragment = fm.findFragmentByTag("MANDIRI");
        if (secondFragment == null) {
            secondFragment = new MandiriFragment();
            ft.add(R.id.simpleFrameLayout, secondFragment, "MANDIRI"); // Add if it doesn't exist
        } else {
            ft.show(secondFragment); // Show if it already exists
        }

        ft.addToBackStack(null); // Optional: Add to back stack for navigation
        ft.commit();
    }


    private String selectedGardu, selectedVoltage, kompany;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedCompany = adapterView.getItemAtPosition(i).toString();
        Log.i("", "adapterView.getId()  " + adapterView.getId());
        if (adapterView.getId() == R.id.gardu_spinner) {
            selectedGardu = adapterView.getItemAtPosition(i).toString();
            Log.i("", "selectedGardu " + selectedGardu);

        } else if (adapterView.getId() == R.id.print_spinner) {
            selectedVoltage = adapterView.getItemAtPosition(i).toString();
            Log.i("", "selectedVoltage " + selectedVoltage);
        } else {
            kompany = adapterView.getItemAtPosition(i).toString();
            Log.i("", "kompany " + kompany);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
