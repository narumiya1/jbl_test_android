package com.example.jbl.tools;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> fullName = new MutableLiveData<>();
    private MutableLiveData<String> fullName2 = new MutableLiveData<>();

    private MutableLiveData<String> mid = new MutableLiveData<>();
    private MutableLiveData<String> uId = new MutableLiveData<>();

    public MutableLiveData<String> getFullName() {
        return fullName;
    }

    public MutableLiveData<String> getFullName2() {
        return fullName2;
    }

    public MutableLiveData<String> getMid() {
        return mid;
    }

    public void setMid(MutableLiveData<String> mid) {
        this.mid = mid;
    }

    public MutableLiveData<String> getuId() {
        return uId;
    }

    public void setuId(MutableLiveData<String> uId) {
        this.uId = uId;
    }
}
