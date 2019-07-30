package br.com.comquesamba.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.comquesamba.R;
import br.com.comquesamba.activities.DetailActivity;
import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.models.SambaBean;
import br.com.comquesamba.services.FirebaseService;
import br.com.comquesamba.services.FirebaseStorageCallback;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static android.app.Activity.RESULT_OK;

public class AddEventFragment extends Fragment implements Validator.ValidationListener{

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText nameInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText dateInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText locationInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText streetInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText numberInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText cityInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText districtInput;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText descriptionInput;

    private EditText ticketUrlInput;

    private ImageButton uploadImageBtn;

    private FloatingTextButton confirmBtn;
    private Calendar calendar;
    private String sambaId;
    private String imageUrl;
    private boolean isUploading;
    private Validator validator;

    public AddEventFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);

        nameInput = getView().findViewById(R.id.add_event_nameID);
        dateInput = getView().findViewById(R.id.add_event_dateID);
        locationInput = getView().findViewById(R.id.add_event_placeID);
        streetInput = getView().findViewById(R.id.add_event_streetID);
        numberInput = getView().findViewById(R.id.add_event_numberID);
        cityInput = getView().findViewById(R.id.add_event_cityID);
        districtInput = getView().findViewById(R.id.add_event_areaID);
        descriptionInput = getView().findViewById(R.id.add_event_describeID);
        ticketUrlInput = getView().findViewById(R.id.add_event_buyLinkID);
        uploadImageBtn = getView().findViewById(R.id.add_event_image);
        confirmBtn = getView().findViewById(R.id.add_confirm_btn);

        confirmBtn.setOnClickListener(getCreateClickListener());
        uploadImageBtn.setOnClickListener(getUploadBtnClickListener());
        dateInput.setOnClickListener(getDateInputClickListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 200){
                uploadImage(data);
            }

        }
    }

    private void uploadImage(Intent data){

        final ContentLoadingProgressBar progressBar = getView().findViewById(R.id.add_progress_bar);

        FirebaseStorageCallback callback = new FirebaseStorageCallback() {
            @Override
            public void success(String imageUrl) {
                progressBar.hide();
                AddEventFragment.this.imageUrl = imageUrl;
                isUploading = false;
            }
        };

        FirebaseService service = new FirebaseService(callback);
        sambaId = service.getKeyFromPush();

        progressBar.show();
        isUploading = true;
        service.saveImage(data.getData(), sambaId, getContext());
    }

    private View.OnClickListener getUploadBtnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        };
    }

    public void addImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 200);
    }

    private View.OnClickListener getDateInputClickListener(){

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeKeyboard();
                new DatePickerDialog(getContext(), dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }

    private void removeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("pt","BR"));
        dateInput.setText(sdf.format(calendar.getTime()));
    }

    public View.OnClickListener getCreateClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validator.validate();
            }
        };
    }

    private SambaBean getSambaFromInput(){
        SambaBean sambaBean = new SambaBean();
        sambaBean.setName(nameInput.getText().toString());
        sambaBean.setDate(dateInput.getText().toString());
        sambaBean.setLocation(locationInput.getText().toString());
        sambaBean.setStreet(streetInput.getText().toString());
        sambaBean.setNumber(numberInput.getText().toString());
        sambaBean.setCity(cityInput.getText().toString());
        sambaBean.setDistrict(districtInput.getText().toString());
        sambaBean.setDescription(descriptionInput.getText().toString());

        if (!ticketUrlInput.getText().toString().isEmpty()){
            sambaBean.setTicketUrl(ticketUrlInput.getText().toString());
        }

        if (TextUtils.isEmpty(imageUrl)){
            sambaBean.setImageUrl(imageUrl);
        }

        return sambaBean;
    }

    @Override
    public void onValidationSucceeded() {
        FirebaseService service = new FirebaseService();

        if (isUploading){
            Toast.makeText(getContext(), "Espere finalizar o upload da imagem.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sambaId == null){
            sambaId = service.getKeyFromPush();
        }

        SambaBean sambaInput = getSambaFromInput();
        if (imageUrl != null){
            sambaInput.setImageUrl(imageUrl);
        }

        service.saveDataFromKey(sambaId, sambaInput);

        Intent intent = new Intent(getContext(), DetailActivity.class);
        getActivity().finishAffinity();
        intent.putExtra(Constants.ADD_EVENT_EXTRA_BACK_TO_MAIN, true);
        intent.putExtra(Constants.SAMBA_EXTRA, sambaInput);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors){
            View v = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            if (v instanceof EditText) {
                ((EditText) v).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
