package br.com.comquesamba;

import java.util.ArrayList;

import br.com.comquesamba.models.SambaBean;

public class HomeData {

    private static HomeData instance;
    private ArrayList<SambaBean> sambas;

    private HomeData(){

    }

    public static HomeData getInstance(){
        if (instance == null){
            instance = new HomeData();
        }

        return instance;
    }

    public ArrayList<SambaBean> getSambas() {
        return sambas;
    }

    public void setSambas(ArrayList<SambaBean> sambas) {
        this.sambas = sambas;
    }
}
