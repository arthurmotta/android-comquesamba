package br.com.comquesamba.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.comquesamba.HomeData;
import br.com.comquesamba.Locator;
import br.com.comquesamba.models.SambaBean;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private ArrayList<SambaBean> sambas;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng posInicial = catchCoordinate("Rua São José, 90 - Centro, Rio de Janeiro");
        if (posInicial != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posInicial, 17);
            googleMap.moveCamera(update);
        }

        sambas = HomeData.getInstance().getSambas();

        for (SambaBean samba : sambas){
            LatLng coordinate = catchCoordinate(samba.getAddress());
            if (coordinate != null) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(coordinate);
                marker.title(samba.getName());
                marker.snippet(samba.getDate());
                googleMap.addMarker(marker);
            }
        }

        new Locator(getContext(), googleMap);
    }

    private LatLng catchCoordinate(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if (!resultados.isEmpty()){
                LatLng position = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return position;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
