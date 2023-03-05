package com.example.militaryaircraft;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException ;
import android.content.Intent ;
import android.os.Bundle ;
import android.text.TextUtils ;
import android.util.Log ;
import android.util.Patterns ;
import android.view.View ;
import android.widget.Button ;
import android.widget.EditText ;
import android.widget.ProgressBar ;
import android.widget.TextView ;
import android.widget.Toast ;
import com.example.militaryaircraft.databinding.ActivityLoginPageBinding ;
import com.example.militaryaircraft.databinding.ActivityMainBinding ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import java.util.logging.LoggingMXBean;




public class Login_Page extends AppCompatActivity {

    private EditText editTextLoginEmail,editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";
    TextView register ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().setTitle("Login");
        editTextLoginEmail = findViewById(R.id.loginEmail);
        editTextLoginPwd = findViewById(R.id.loginPass);
        progressBar = findViewById(R.id.progressbBar);
        register = findViewById(R.id.register) ;
        authProfile = FirebaseAuth.getInstance();
        //login user
        Button buttonLogin = findViewById(R.id.submitButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = editTextLoginEmail.getText().toString();
                String userPass = editTextLoginPwd.getText().toString();

                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(Login_Page.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(Login_Page.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is not valid");
                    editTextLoginEmail.requestFocus();
                } else if(TextUtils.isEmpty(userPass)) {
                    Toast.makeText(Login_Page.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(userEmail,userPass);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Page.this,Register_Page.class));
            }
        });
    }
    private void loginUser(String userEmail, String userPass) {
        authProfile.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(Login_Page.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(Login_Page.this, "You are logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login_Page.this, Home_Page.class));
                    }
                    else{
                        Toast.makeText(Login_Page.this,"You have not verified your Gmail ID \n Verify Your Gmail ID to Login" , Toast.LENGTH_SHORT).show();
                    }
                } else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        editTextLoginEmail.setError("User does not exists");
                        editTextLoginEmail.requestFocus();
                    }
                    catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(Login_Page.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Not needed though ;
    private void showAlertDialog() {
        // Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Page.this) ;
        builder.setTitle("Email Not Verified") ;
        builder.setMessage("Please verify your email now , You can not login without email verification. ") ;

        // Open Email Apps is
    }

     // check if user is already logged in . In such case , straightaway take the user to the User's profile
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(authProfile.getCurrentUser()!=null) {
//            Toast.makeText(Login_Page.this,"Already Logged In ! " , Toast.LENGTH_SHORT).show() ;
//            startActivity(new Intent(Login_Page.this,Home_Page.class));
//        }
//        else {
//            Toast.makeText(Login_Page.this,"You can Login now ! " , Toast.LENGTH_SHORT).show();
//
//        }
//    }
}
