package br.com.comquesamba.services;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import br.com.comquesamba.constants.Constants;
import br.com.comquesamba.models.SambaBean;
import br.com.comquesamba.models.SambaDataDTO;

public class FirebaseService {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private FirebaseStorageCallback storageCallback;
    private FirebaseDatabaseCallback databaseCallback;

    public FirebaseService(){
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public FirebaseService(FirebaseDatabaseCallback databaseCallback){
        this();
        this.databaseCallback = databaseCallback;
    }

    public FirebaseService(FirebaseStorageCallback storageCallback){
        this();
        this.storageCallback = storageCallback;
    }


    private DatabaseReference getUsersReference(){
        return firebaseDatabase.getReference().child(Constants.FIREBASE_SAMBAS_CHILD);
    }

    public void saveImage(Uri uri, String key, final Context context){
        final StorageReference ref = firebaseStorage.getReference().child(key);
        UploadTask uploadTask = ref.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Algo deu errado!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                storageCallback.success(downloadUrl.toString());
            }
        });
    }

//    Cria um id e salva os dados nele
    public void saveDataWithPush(SambaBean sambaBean){
        SambaDataDTO sambaDataDTO = new SambaDataDTO(sambaBean);
        getUsersReference().push().setValue(sambaDataDTO);
    }

//    Cria um id e o retorna
    public String getKeyFromPush(){
        return getUsersReference().push().getKey();
    }

//    salva os dados passando o id
    public void saveDataFromKey(String key, SambaBean sambaBean){
        SambaDataDTO sambaDataDTO = new SambaDataDTO(sambaBean);
        getUsersReference().child(key).setValue(sambaDataDTO);
    }

    public void readData(){
        final ArrayList<SambaBean> sambas = new ArrayList<>();

        getUsersReference().orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    if (databaseCallback != null){
                    }
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SambaDataDTO sambaDataDTO = snapshot.getValue(SambaDataDTO.class);
                    if (sambaDataDTO != null){

                        SambaBean sambaBean = new SambaBean(sambaDataDTO);
                        if (sambaBean.isUpToDate()){
                            sambas.add(new SambaBean(sambaDataDTO));
                        }
                    }
                }

                if (databaseCallback != null){
                    databaseCallback.success(sambas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
