package com.example.jbl.views;

import
        android.os.Bundle;
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
import com.example.jbl.databinding.FragmentFirstBinding;
import com.example.jbl.tools.SharedViewModel;
import com.google.android.material.tabs.TabLayout;


public class MandiriFragment extends Fragment {
    FragmentFirstBinding binding;
    private SharedViewModel sharedViewModel;
    public MandiriFragment() {
// Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_first, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set nilai dari ViewModel ke input field
        sharedViewModel.getFullName().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                binding.mid.setText(fullName);
            }
        });

        sharedViewModel.getFullName2().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                binding.uid.setText(fullName2);
            }
        });
        // Simpan nilai ke ViewModel saat tombol ditekan
        // Simpan nilai ke ViewModel saat tombol ditekan
        binding.btnSendAlamat.setOnClickListener(view1 -> {

            String midInput = binding.mid.getText().toString();
            String uidInput = binding.uid.getText().toString();


            // Validasi input

            if (midInput.isEmpty() && uidInput.isEmpty()) {
                binding.mid.setError("Input tidak boleh kosong");
                binding.mid.requestFocus();
                return;
            }

            sharedViewModel.getFullName().setValue(binding.mid.getText().toString());
            sharedViewModel.getFullName2().setValue(binding.uid.getText().toString());

            // Menampilkan semua input dari FirstFragment dan SecondFragment
            String firstInput = sharedViewModel.getFullName().getValue();
            String firstInput2 = sharedViewModel.getFullName2().getValue();
            String  inputFrags = sharedViewModel.getMid().getValue();

            Toast.makeText(requireContext(),
                    "Input FirstFragment: " + firstInput + ", " + firstInput2 +
                            "\nInput SecondFragment: " + inputFrags,
                    Toast.LENGTH_LONG).show();
            MoveToBNIFragment();

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

    private void MoveToBNIFragment() {
        TabLayout tabLayout = requireActivity().findViewById(R.id.simpleTabLayout);
        tabLayout.getTabAt(2).select(); // Pilih tab kedua

        FragmentManager fm = getActivity().getSupportFragmentManager(); // Use getActivity() to get the Activity's FragmentManager
        FragmentTransaction ft = fm.beginTransaction();

        // Hide the current fragment (FirstFragment)
        ft.hide(MandiriFragment.this); // Use FirstFragment.this to reference the current fragment instance

        // Find or create the SecondFragment
        Fragment secondFragment = fm.findFragmentByTag("BNI");
        if (secondFragment == null) {
            secondFragment = new BNIFragment();
            ft.add(R.id.simpleFrameLayout, secondFragment, "BNI"); // Add if it doesn't exist
        } else {
            ft.show(secondFragment); // Show if it already exists
        }

        ft.addToBackStack(null); // Optional: Add to back stack for navigation
        ft.commit();
    }
}