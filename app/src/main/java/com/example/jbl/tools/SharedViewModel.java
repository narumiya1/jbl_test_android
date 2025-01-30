package com.example.jbl.tools;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    // Mandiri
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


    // BRI
    private MutableLiveData<String> midBRI = new MutableLiveData<>();
    private MutableLiveData<String> proCodeBRI = new MutableLiveData<>();
    private MutableLiveData<String> tidBRI = new MutableLiveData<>();


    public MutableLiveData<String> getMidBRI() {
        return midBRI;
    }

    public MutableLiveData<String> getProCodeBRI() {
        return proCodeBRI;
    }

    public MutableLiveData<String> getTidBRI() {
        return tidBRI;
    }

    // BNI
    private MutableLiveData<String> midBNI = new MutableLiveData<>();
    private MutableLiveData<String> marriedCodebNI = new MutableLiveData<>();
    private MutableLiveData<String> tidBNI = new MutableLiveData<>();

    public MutableLiveData<String> getMidBNI() {
        return midBNI;
    }

    public MutableLiveData<String> getMarriedCodebNI() {
        return marriedCodebNI;
    }

    public MutableLiveData<String> getTidBNI() {
        return tidBNI;
    }

    // BCA
    private MutableLiveData<String> midBca = new MutableLiveData<>();
    private MutableLiveData<String> tidBca = new MutableLiveData<>();

    public MutableLiveData<String> getMidBca() {
        return midBca;
    }

    public MutableLiveData<String> getTidBca() {
        return tidBca;
    }

    public void setuId(MutableLiveData<String> uId) {
        this.uId = uId;
    }

    // GENERAL
    private MutableLiveData<String> institutionCompany = new MutableLiveData<>();
    private MutableLiveData<String> institutionCode = new MutableLiveData<>();
    private MutableLiveData<String> cabang = new MutableLiveData<>();
    private MutableLiveData<String> gerbang = new MutableLiveData<>();
    private MutableLiveData<String> gardu = new MutableLiveData<>();
    private MutableLiveData<String> uid = new MutableLiveData<>();
    private MutableLiveData<String> ipPcs = new MutableLiveData<>();
    private MutableLiveData<String> tipeGardu = new MutableLiveData<>();
    private MutableLiveData<String> tipePrint = new MutableLiveData<>();

    public MutableLiveData<String> getInstitutionCompany() {
        return institutionCompany;
    }

    public MutableLiveData<String> getInstitutionCode() {
        return institutionCode;
    }

    public MutableLiveData<String> getCabang() {
        return cabang;
    }

    public MutableLiveData<String> getGerbang() {
        return gerbang;
    }

    public MutableLiveData<String> getGardu() {
        return gardu;
    }

    public MutableLiveData<String> getUid() {
        return uid;
    }

    public MutableLiveData<String> getIpPcs() {
        return ipPcs;
    }

    public MutableLiveData<String> getTipeGardu() {
        return tipeGardu;
    }

    public MutableLiveData<String> getTipePrint() {
        return tipePrint;
    }
}
