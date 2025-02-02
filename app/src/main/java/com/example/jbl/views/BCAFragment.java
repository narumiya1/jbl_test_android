package com.example.jbl.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jbl.databinding.FragmentScndBinding;
import com.example.jbl.tools.SharedViewModel;

public class BCAFragment extends Fragment {
FragmentScndBinding binding;
    private SharedViewModel sharedViewModel;

    public BCAFragment() {
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
        binding = FragmentScndBinding.inflate(inflater, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_scnd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel.getMidBca().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                binding.mid.setText(fullName);
            }
        });

        sharedViewModel.getTidBca().observe(getViewLifecycleOwner(), fullName2 -> {
            if (fullName2 != null) {
                binding.uid.setText(fullName2);
            }
        });
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

            sharedViewModel.getMidBca().setValue(binding.mid.getText().toString());
            sharedViewModel.getTidBca().setValue(binding.uid.getText().toString());

            // Menampilkan semua input dari FirstFragment dan BCAFragment
            String firstInput = sharedViewModel.getMidBca().getValue();
            String firstInput2 = sharedViewModel.getuId().getValue();
            String  inputFrags = sharedViewModel.getTidBca().getValue();

            Toast.makeText(requireContext(),
                    "Input FirstFragment: " + firstInput + ", " + firstInput2 +
                            "\nInput BCAFragment: " + inputFrags,
                    Toast.LENGTH_LONG).show();

        });

       /* binding.btnSendAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.btnSendAlamat.setOnClickListener(new View.OnClickListener() {
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
                });

            }
        });*/
    }
}