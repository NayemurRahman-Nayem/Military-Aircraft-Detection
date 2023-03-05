package com.example.militaryaircraft ;
import androidx.annotation.NonNull ;
import androidx.appcompat.app.AppCompatActivity ;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog ;
import android.content.Intent ;
import android.os.Bundle ;
import android.provider.MediaStore;
import android.text.TextUtils ;
import android.util.Log;
import android.util.Patterns;
import android.view.View ;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView ;
import android.widget.Toast ;
import com.example.militaryaircraft.databinding.ActivityRegisterPageBinding ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener ;
import com.google.android.gms.tasks.OnSuccessListener ;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult ;
import com.google.firebase.auth.FirebaseAuth ;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects ;



public class Register_Page extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile, editTextRegisterPwd,
            editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    TextView userImage , captureImage  ;
    private static final String TAG="RegisterActivity";
    FirebaseFirestore firebaseFirestore ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getSupportActionBar().setTitle("Register");
        Toast.makeText(Register_Page.this, "You cann register now", Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progressbBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password) ;
        userImage = findViewById(R.id.selectImage) ;
        captureImage = findViewById(R.id.captureBtn) ;
        //Radio button for gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender) ;
        radioGroupRegisterGender.clearCheck();
        //Date Picker
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance() ;
                int day = calendar.get(Calendar.DAY_OF_MONTH) ;
                int month = calendar.get(Calendar.MONTH) ;
                int year = calendar.get(Calendar.YEAR) ;
                //Date Picker Dialog
                picker = new DatePickerDialog(Register_Page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editTextRegisterDoB.setText(dayOfMonth + "/" +(month+1) + "/" +year);
                    }
                }, year,month,day);
                picker.show();
            }
        });
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectGenderId);
                //obtain the entered data
                String userFullname = editTextRegisterFullName.getText().toString().trim();
                String userEmail = editTextRegisterEmail.getText().toString().trim();
                String userdateofBirth = editTextRegisterDoB.getText().toString();
                String userPhone = editTextRegisterMobile.getText().toString();
                String userPass = editTextRegisterPwd.getText().toString().trim();
                String userConfirmPass = editTextRegisterConfirmPwd.getText().toString().trim();
                String userGender;     //can'T get the value if gender is not selected
                // Checkeing whether any field is empty or not?
                if (TextUtils.isEmpty(userFullname)) {
                    Toast.makeText(Register_Page.this, "Please Enter Your Full Name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is Required");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(Register_Page.this, "Please Enter Your E-mail", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("E-mail is Required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(Register_Page.this, "Please Re-enter Your E-mail", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError(" Valid E-mail is Required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(userdateofBirth)) {
                    Toast.makeText(Register_Page.this, "Please Enter Your Date of Birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Birth Date is Required");
                    editTextRegisterDoB.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Register_Page.this, "Please Select Your Gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is Required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(Register_Page.this, "Please Enter Your Phone Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone Number is Required");
                    editTextRegisterMobile.requestFocus();
                } else if (userPhone.charAt(0)!='0' && userPass.charAt(1)!='1' && userPhone.length() != 11 ) {
                    Toast.makeText(Register_Page.this, "Please Re-enter Your Phone Number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone Number Should be 11 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(Register_Page.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is Required");
                    editTextRegisterPwd.requestFocus();
                } else if (userPass.length() < 6) {
                    Toast.makeText(Register_Page.this, "Password Should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Too weak password");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(userConfirmPass)) {
                    Toast.makeText(Register_Page.this, "Please Confirm Your Password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm Password is required");
                    editTextRegisterConfirmPwd.requestFocus();
                } else if (!userPass.equals(userConfirmPass)) {
                    Toast.makeText(Register_Page.this, "Password Doesn't Match", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm Password is required");
                    editTextRegisterConfirmPwd.requestFocus();
                    //clear the entered password
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                } else {
                    userGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(userFullname, userEmail, userdateofBirth, userGender, userPhone, userPass);
                }
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });
    }
    //Users regisretion
    private void registerUser(String userFullname, String userEmail, String userdateofBirth, String userGender, String userPhone, String userPass) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener((task)-> {

            if(task.isSuccessful()) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if(task1.isSuccessful())  {
                            Toast.makeText(Register_Page.this,"You have registered SuccessFully \n Please Verify your email ID ! ",Toast.LENGTH_SHORT).show();
                            // Storing Data in the Firestore Firebase
                            String userID = firebaseAuth.getCurrentUser().getUid() ;
                            firebaseFirestore = FirebaseFirestore.getInstance() ;
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID) ;
                            Map<String,Object> user = new HashMap<>() ;
                            user.put("fname" , userFullname) ;
                            user.put("email",userEmail) ;
                            user.put("dateOfBirth",userdateofBirth) ;
                            user.put("gender",userGender) ;
                            user.put("phone",userPhone) ;
                            documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG,"onSuccess: user Profile is created for "+userID) ;
                                }
                            });
                            // Storing Data in the Realtime Database
                            ReadWriteUserDetails readWriteUserDetails = new ReadWriteUserDetails(userFullname,userEmail,userdateofBirth,userGender,userPhone) ;
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users") ;
                            referenceProfile.child(firebaseAuth.getUid()).setValue(readWriteUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task1) {
                                    Log.d(TAG,"onSuccess: user Profile is created for " + userID);
                                }
                            });

                            startActivity(new Intent(Register_Page.this,Login_Page.class));
                        }
                        else {
                            Toast.makeText(Register_Page.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show(); ;
                        }
                    }
                });
            }
            else{
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e){
                    editTextRegisterPwd.setError("Your Password is too weak; Use mixed letters and numbers");
                    editTextRegisterPwd.requestFocus();
                }  catch (FirebaseAuthInvalidCredentialsException e){
                    editTextRegisterPwd.setError("Invalid email or already in use");
                    editTextRegisterPwd.requestFocus();
                }  catch (FirebaseAuthUserCollisionException e){
                    editTextRegisterPwd.setError("User already registered");
                    editTextRegisterPwd.requestFocus();
                }  catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(Register_Page.this,e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
    }

}