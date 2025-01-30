package com.example.jbl;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jbl.databinding.ActivityMainBinding;
import com.example.jbl.databinding.FragmentHomeBinding;
import com.mdd.aar.deviceid.AarDeviceId;
import com.mdd.aar.deviceid.DeviceEnvironment;
import com.mdd.aar.deviceid.http.responses.UnlockAarResponse;
import com.medicom.dudikov.mybanklibrary.halDriver;
import com.medicom.dudikov.mybanklibrary.nativeLib;
import com.medicom.dudikov.mybanklibrary.readerLib;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private String debugToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
binding.btnTesKoneksiFragment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // native library
        try {
            nativeLib nativeLibrary = new nativeLib(getActivity(), halDriver.USE_WISEASY_ENGGINE);
            debugToken = nativeLibrary.generateDebugCert();
            System.out.println(debugToken);


        } catch (Exception e) {
            System.out.println("Native Library Exception");

            System.out.println(e);

        }
    }
});
        String accesToken = "19be782242b4fdde4eeccb8c42e92b2b";
        try {
            AarDeviceId aarDevice = new AarDeviceId(getActivity());
            aarDevice.init(accesToken, DeviceEnvironment.UNLOCK);
            nativeLib nativeLibrary = new nativeLib(getActivity(), halDriver.USE_WEPOY_ENGGINE);
            String debugToken = nativeLibrary.generateDebugCert();
            UnlockAarResponse unlockAarResponse = aarDevice.unlockLibrary("19be782242b4fdde4eeccb8c42e92b2b", DeviceEnvironment.UNLOCK, aarDevice.getDeviceId(),
                    debugToken, "1");
            String debugResponse = unlockAarResponse.getData().getDebugResponse();

            readerLib myReader = new readerLib(getActivity(), true, halDriver.USE_WEPOY_ENGGINE);
            myReader.activateDebug(getActivity(), debugResponse);
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.d("TAG msg", msg + "");

        }

    }
}