package br.com.comquesamba;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.comquesamba.models.SambaBean;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.WordViewHolder> {

    // TODO mudar nomes genericos

    private static MyClickListener myClickListener;
    private static String LOG_TAG = "MyRecyclerViewAdapter";

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SambaBean> sambas;


    public MyAdapter(Context context, ArrayList<SambaBean> sambas){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sambas = sambas;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView wordItemView_title;
        private final TextView wordItemView_date;
        private final TextView wordItemView_address;
        private final ImageView imageItemView;
        private MyAdapter wordAdapter;

        public WordViewHolder(@NonNull View itemView, MyAdapter adapter) {
            super(itemView);

            wordItemView_title = (TextView) itemView.findViewById(R.id.main_label_title);
            wordItemView_date = (TextView) itemView.findViewById(R.id.main_label_date);
            wordItemView_address = (TextView) itemView.findViewById(R.id.main_label_address);

            imageItemView = (ImageView) itemView.findViewById(R.id.imageView);
            wordAdapter = adapter;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyAdapter.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.wordlist_item, viewGroup, false);
        return new WordViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.WordViewHolder wordViewHolder, int i) {

        String oi = sambas.get(i).getName();
        String oi2 = sambas.get(i).getSimpleDate() + " - " + sambas.get(i).daysUntilSamba();
        String oi3 = sambas.get(i).getAddress();

        wordViewHolder.wordItemView_title.setText(oi);
        wordViewHolder.wordItemView_date.setText(oi2);
        wordViewHolder.wordItemView_address.setText(oi3);

        int resID = this.context.getResources().getIdentifier("placeholder", "drawable", this.context.getPackageName());

        Picasso.get().load(sambas.get(i).getImageUrl()).placeholder(resID).error(resID).into(wordViewHolder.imageItemView);
    }

    @Override
    public int getItemCount() {
        return sambas.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}