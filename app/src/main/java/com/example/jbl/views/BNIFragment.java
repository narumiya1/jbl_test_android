package com.example.jbl.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.example.jbl.R;
import com.example.jbl.tools.SharedViewModel;
import com.google.android.material.tabs.TabLayout;

public class BNIFragment extends Fragment {
    com.example.jbl.databinding.FragmentThrdBinding binding;
    private SharedViewModel sharedViewModel;

    public BNIFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        binding =  com.example.jbl.databinding.FragmentThrdBinding .inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_thrd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set nilai dari ViewModel ke input field
        sharedViewModel.getMarriedCodebNI().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                binding.marriedCode.setText(fullName);
            }
        });

        sharedViewModel.getMidBNI().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                binding.mid.setText(fullName2);
            }
        });
        sharedViewModel.getTidBNI().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                binding.uid.setText(fullName2);
            }
        });
        // Simpan nilai ke ViewModel saat tombol ditekan
        // Simpan nilai ke ViewModel saat tombol ditekan
        binding.btnSendAlamat.setOnClickListener(view1 -> {

            String marriedInput = binding.marriedCode.getText().toString();
            String midInput = binding.mid.getText().toString();
            String tidInput = binding.uid.getText().toString();


            // Validasi input

            if (midInput.isEmpty() && marriedInput.isEmpty() && tidInput.isEmpty()) {
                binding.marriedCode.setError("Input tidak boleh kosong");
                binding.marriedCode.requestFocus();

                binding.mid.setError("Input tidak boleh kosong");
                binding.mid.requestFocus();

                binding.uid.setError("Input tidak boleh kosong");
                binding.uid.requestFocus();
                return;
            }

            sharedViewModel.getMarriedCodebNI().setValue(binding.marriedCode.getText().toString());
            sharedViewModel.getMidBNI().setValue(binding.mid.getText().toString());
            sharedViewModel.getTidBNI().setValue(binding.uid.getText().toString());

            // Menampilkan semua input dari FirstFragment dan SecondFragment
            String firstInput = sharedViewModel.getMarriedCodebNI().getValue();
            String firstInput2 = sharedViewModel.getMidBNI().getValue();
            String  inputFrags = sharedViewModel.getTidBNI().getValue();

            Toast.makeText(requireContext(),
                    "Input FirstFragment: " + firstInput + ", " + firstInput2 +
                            "\nInput SecondFragment: " + inputFrags,
                    Toast.LENGTH_LONG).show();

            MoveToBRIFragment();


        });
    }

    private void MoveToBRIFragment() {
        TabLayout tabLayout = requireActivity().findViewById(R.id.simpleTabLayout);
        tabLayout.getTabAt(3).select(); // Pilih tab kedua

        FragmentManager fm = getActivity().getSupportFragmentManager(); // Use getActivity() to get the Activity's FragmentManager
        FragmentTransaction ft = fm.beginTransaction();

        // Hide the current fragment (FirstFragment)
        ft.hide(BNIFragment.this); // Use FirstFragment.this to reference the current fragment instance

        // Find or create the SecondFragment
        Fragment secondFragment = fm.findFragmentByTag("BRI");
        if (secondFragment == null) {
            secondFragment = new BriFragment();
            ft.add(R.id.simpleFrameLayout, secondFragment, "BRI"); // Add if it doesn't exist
        } else {
            ft.show(secondFragment); // Show if it already exists
        }

        ft.addToBackStack(null); // Optional: Add to back stack for navigation
        ft.commit();
    }
}