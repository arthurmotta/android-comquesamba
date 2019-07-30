package br.com.comquesamba.services;

import java.util.ArrayList;

import br.com.comquesamba.models.SambaBean;

public interface FirebaseDatabaseCallback {

    void success(ArrayList<SambaBean> sambas);
}
