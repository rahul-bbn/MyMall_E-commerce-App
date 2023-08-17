package com.example.mainactivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateInfoFragment extends Fragment {

    public UpdateInfoFragment() {
        // Required empty public constructor
    }
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+" ;
    private String name,email,photo;
    private EditText emailField,nameField,passwordField;
    private CircleImageView circleImageView;
    private Button changePhotoBtn,removePhotoBtn,updateUserInfoBtn,doneBtn;
    private Dialog loadingDialog,passwordDialog;
    private Uri uri;
    private boolean updatePhoto=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        circleImageView=view.findViewById(R.id.profile_photo);
        nameField=view.findViewById(R.id.name_et);
        emailField=view.findViewById(R.id.email_et);
        changePhotoBtn=view.findViewById(R.id.change_pic_btn);
        removePhotoBtn=view.findViewById(R.id.remove_pic_btn);
        updateUserInfoBtn=view.findViewById(R.id.update_info_button);

        //////////loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //////////loading dialog

        //////////password dialog

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        passwordField=passwordDialog.findViewById(R.id.enter_password);
        doneBtn=passwordDialog.findViewById(R.id.done_btn);

        //////////password dialog

        name=getArguments().getString("Name");
        email=getArguments().getString("Email");
        photo=getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);

                    }else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                }else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        removePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uri=null;
                updatePhoto=true;
                Glide.with(getContext()).load(R.drawable.profile_placeholder).into(circleImageView);
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmail();
            }
        });

        return view;
    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(emailField.getText())){
            if(!TextUtils.isEmpty(nameField.getText())){
                updateUserInfoBtn.setEnabled(true);
                updateUserInfoBtn.setTextColor(Color.rgb(255,255,255));
            }else{
                updateUserInfoBtn.setEnabled(false);
                updateUserInfoBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            updateUserInfoBtn.setEnabled(false);
            updateUserInfoBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmail() {

        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.custom_error_icon);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        if(emailField.getText().toString().matches(emailPattern)){
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())){  ///same email
                loadingDialog.show();
                updatePic(user);
            }
            else {  //update email
                passwordDialog.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userPass=passwordField.getText().toString();
                        passwordDialog.dismiss();
                        loadingDialog.show();

                        AuthCredential credential= EmailAuthProvider.getCredential(email,userPass);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    user.updateEmail(emailField.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                /// update pic
                                                updatePic(user);
                                                /// update pic
                                            }else {
                                                loadingDialog.dismiss();
                                                String error=task.getException().getMessage();
                                                Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                                    loadingDialog.dismiss();
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }else {
            emailField.setError("Invalid Email!");
        }
    }

    private void updatePic(final FirebaseUser user) {
        if(updatePhoto){
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/"+user.getUid()+".jpg");

            if(uri != null){

                Glide.with(getContext()).asBitmap().load(uri).centerCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if(task.isSuccessful()){
                                                uri=task.getResult();
                                                DBqueries.profile=task.getResult().toString();
                                                Glide.with(getContext()).load(DBqueries.profile).into(circleImageView);

                                                Map<String,Object> userdata= new HashMap<>();
                                                userdata.put("fullname",nameField.getText().toString());
                                                userdata.put("email",emailField.getText().toString());
                                                userdata.put("profile",DBqueries.profile);

                                                updateFields(user,userdata);

                                            }else {
                                                DBqueries.profile="";
                                                loadingDialog.dismiss();
                                                String error=task.getException().getMessage();
                                                Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else {
                                    loadingDialog.dismiss();
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.profile_placeholder);
                    }
                });

            }
            else {///remove pic
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DBqueries.profile="";

                            Map<String,Object> userdata= new HashMap<>();
                            userdata.put("fullname",nameField.getText().toString());
                            userdata.put("email",emailField.getText().toString());
                            userdata.put("profile","");

                            updateFields(user,userdata);
                        }else {
                            loadingDialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else {
            Map<String,Object> userdata= new HashMap<>();
            userdata.put("fullname",nameField.getText().toString());
            userdata.put("email",emailField.getText().toString());   /////my code
            updateFields(user,userdata);
        }
    }

    private void updateFields(FirebaseUser user, final Map<String, Object> userdata){
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                .update(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(userdata.size() >2){
                                DBqueries.email=emailField.getText().toString().trim();
                                DBqueries.fullname=nameField.getText().toString().trim();
                            }else {
                                DBqueries.fullname=nameField.getText().toString().trim();
                                DBqueries.email=emailField.getText().toString();  ///my code updation
                            }
                            getActivity().finish();
                            Toast.makeText(getContext(), "Successfully updated !", Toast.LENGTH_SHORT).show();
                        }else {
                            String error=task.getException().getMessage();
                            Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == getActivity().RESULT_OK){
                if(data != null){
                    uri = data.getData();
                    updatePhoto=true;
                    Glide.with(getContext()).load(uri).into(circleImageView);
                }
                else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
