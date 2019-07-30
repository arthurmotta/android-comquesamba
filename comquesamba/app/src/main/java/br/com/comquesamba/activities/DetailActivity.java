package br.com.comquesamba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.comquesamba.R;
import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.models.SambaBean;

public class DetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView date;
    private TextView local;
    private TextView end1;
    private TextView end2;
    private TextView description;
    private TextView daysUntilSamba;
    private ImageView image;
    private boolean shouldBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent = getIntent();
        SambaBean samba = (SambaBean) intent.getSerializableExtra(Constants.SAMBA_EXTRA);

        shouldBackToMain = getIntent().getBooleanExtra(Constants.ADD_EVENT_EXTRA_BACK_TO_MAIN, false);

        if (samba != null){
            name = findViewById(R.id.title_event);
            name.setText(samba.getName());

            description = findViewById(R.id.descrip_event);
            description.setText(samba.getDescription());

            local = findViewById(R.id.location_event);
            local.setText(samba.getLocation());

            end1 = findViewById(R.id.detail_end1);
            end1.setText(samba.getAdressPartOne());

            end2 = findViewById(R.id.detail_end2);
            end2.setText(samba.getAdressPartTwo());


            date = findViewById(R.id.date_event);
            date.setText(samba.getSimpleDate());

            daysUntilSamba = findViewById(R.id.daysLeft_event);
            daysUntilSamba.setText(samba.daysUntilSamba());

            image = findViewById(R.id.image_event);
            int resID = getResources().getIdentifier("placeholder", "drawable", getPackageName());
            Picasso.get().load(samba.getImageUrl()).placeholder(resID).error(resID).into(image);

        } else {
            Toast.makeText(this, "Algo deu errado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (shouldBackToMain){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
