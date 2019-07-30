package br.com.comquesamba.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Objects;

import br.com.comquesamba.HomeData;
import br.com.comquesamba.MyAdapter;
import br.com.comquesamba.R;
import br.com.comquesamba.activities.DetailActivity;
import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.models.SambaBean;

public class HomeFragment extends Fragment {

    private CarouselView carouselView;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<SambaBean> sambas;
    private static final int CAROUSEL_SIZE = 3;
    private Intent intent;

    private ArrayList<SambaBean> sambasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildHomeLayout();
    }

    private void buildHomeLayout(){
        sambas = HomeData.getInstance().getSambas();

        carouselView = (CarouselView) Objects.requireNonNull(getView()).findViewById(R.id.carouselView);

        final String carouselArray[] = new String[CAROUSEL_SIZE];

        sambasAdapter = new ArrayList<>();

        for (int i=0; i < sambas.size(); i++){

            if (i < CAROUSEL_SIZE){
                carouselArray[i] = sambas.get(i).getImageUrl();
                continue;
            }

            sambasAdapter.add(sambas.get(i));
        }

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Resources resources = getContext().getResources();

                int resID = getContext().getResources().getIdentifier("placeholder", "drawable", getContext().getPackageName());
                Picasso.get().load(carouselArray[position]).placeholder(resources.getDrawable(resID)).error(resID).into(imageView);
            }
        };

        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(CAROUSEL_SIZE);

        final TextView carouselTitle = Objects.requireNonNull(getView()).findViewById(R.id.carouselTitle);
        carouselView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                carouselTitle.setText(sambas.get(i).getTileForCarousel());
            }

            @Override
            public void onPageSelected(int i) {
                final int numberOnPageSelected = i;

                carouselTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEventDetails(numberOnPageSelected - CAROUSEL_SIZE);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                openEventDetails(position - CAROUSEL_SIZE);
            }
        });

        recyclerView = (RecyclerView) Objects.requireNonNull(getView()).findViewById(R.id.recyclerView);
        adapter = new MyAdapter(getContext(), sambasAdapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((MyAdapter) adapter).setOnItemClickListener(new MyAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                openEventDetails(position);
            }
        });

    }

    private void openEventDetails(int position){
        intent = new Intent(getContext(), DetailActivity.class);
        SambaBean samba = sambas.get(position + CAROUSEL_SIZE);
        intent.putExtra(Constants.SAMBA_EXTRA, samba);
        startActivity(intent);
    }
}
