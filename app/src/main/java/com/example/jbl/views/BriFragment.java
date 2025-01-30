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
import com.example.jbl.databinding.FragmentBriBinding;
import com.example.jbl.databinding.FragmentFirstBinding;
import com.example.jbl.tools.SharedViewModel;
import com.google.android.material.tabs.TabLayout;

public class BriFragment extends Fragment {
    FragmentBriBinding fragmentBriBinding;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        fragmentBriBinding = FragmentBriBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return fragmentBriBinding.getRoot();
//        return inflater.inflate(R.layout.fragment_first, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set nilai dari ViewModel ke input field
        sharedViewModel.getMidBRI().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                fragmentBriBinding.midBRI.setText(fullName);
            }
        });

        sharedViewModel.getTidBRI().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                fragmentBriBinding.tidBRI.setText(fullName2);
            }
        });

        sharedViewModel.getProCodeBRI().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                fragmentBriBinding.proCodeBRI.setText(fullName);
            }
        });

        // Simpan nilai ke ViewModel saat tombol ditekan
        // Simpan nilai ke ViewModel saat tombol ditekan
        fragmentBriBinding.btnSendAlamat.setOnClickListener(view1 -> {

            String midInput = fragmentBriBinding.midBRI.getText().toString();
            String tidInput = fragmentBriBinding.tidBRI.getText().toString();
            String proCodeInput = fragmentBriBinding.proCodeBRI.getText().toString();


            // Validasi input

            if (midInput.isEmpty() ) {
                fragmentBriBinding.midBRI.setError("Input tidak boleh kosong");
                fragmentBriBinding.midBRI.requestFocus();
                return;
            }
            if (tidInput.isEmpty() ) {
                fragmentBriBinding.tidBRI.setError("Input tidak boleh kosong");
                fragmentBriBinding.tidBRI.requestFocus();
                return;
            }
            if (proCodeInput.isEmpty() ) {
                fragmentBriBinding.proCodeBRI.setError("Input tidak boleh kosong");
                fragmentBriBinding.proCodeBRI.requestFocus();
                return;
            }
            sharedViewModel.getMidBRI().setValue(fragmentBriBinding.midBRI.getText().toString());
            sharedViewModel.getTidBRI().setValue(fragmentBriBinding.tidBRI.getText().toString());
            sharedViewModel.getProCodeBRI().setValue(fragmentBriBinding.proCodeBRI.getText().toString());

            // Menampilkan semua input dari FirstFragment dan SecondFragment
            String firstInput = sharedViewModel.getMidBRI().getValue();
            String firstInput2 = sharedViewModel.getTidBRI().getValue();
            String  inputFrags = sharedViewModel.getProCodeBRI().getValue();

            Toast.makeText(requireContext(),
                    "Input FirstFragment: " + firstInput + ", " + firstInput2 +
                            "\nInput SecondFragment: " + inputFrags,
                    Toast.LENGTH_LONG).show();
            MoveToBCAFragment();

        });



    /*   binding.btnSendAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send data
                String fullName =  binding.mid.getText().toString();
                String fullName2 =  binding.uid.getText().toString();

                // Cek apakah input kosong
                if (fullName.isEmpty() && fullName2.isEmpty()) {
                    // Jika kosong, tampilkan error
                    binding.mid.setError("Nama lengkap tidak boleh kosong");
                    binding.mid.requestFocus(); // Fokuskan ke input field
                } else {
                    // Input tidak kosong, lakukan aksi lainnya
//                    sendDataToServer(fullName);
                }
            }
        });*/
    }

    private void MoveToBCAFragment() {

        TabLayout tabLayout = requireActivity().findViewById(R.id.simpleTabLayout);
        tabLayout.getTabAt(4).select(); // Pilih tab kedua

        FragmentManager fm = getActivity().getSupportFragmentManager(); // Use getActivity() to get the Activity's FragmentManager
        FragmentTransaction ft = fm.beginTransaction();

        // Hide the current fragment (FirstFragment)
        ft.hide(BriFragment.this); // Use FirstFragment.this to reference the current fragment instance

        // Find or create the SecondFragment
        Fragment secondFragment = fm.findFragmentByTag("BCA");
        if (secondFragment == null) {
            secondFragment = new BCAFragment();
            ft.add(R.id.simpleFrameLayout, secondFragment, "BCA"); // Add if it doesn't exist
        } else {
            ft.show(secondFragment); // Show if it already exists
        }

        ft.addToBackStack(null); // Optional: Add to back stack for navigation
        ft.commit();
    }

}
