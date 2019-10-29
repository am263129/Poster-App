package social.media.png.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import social.media.png.MainActivity;
import social.media.png.R;
import social.media.png.Register.register;
import social.media.png.util.Global;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    Button btn_login, btn_register, btn_facebook;
    ProgressBar loading;
    CheckBox remember_me;
    public static Integer INVALIDEMAIL = 1;
    private Integer INVLAIDPASS = 2;
    private Integer EMPTYEMAIL = 3;
    private Integer EMPTYPASS = 4;
    private Integer VALID = 0;

    String TAG = "login";
    String MY_PREFS_NAME = "user_auth_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verify();
        email = (EditText)findViewById(R.id.useremail);
        password = (EditText)findViewById(R.id.password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_facebook = (Button)findViewById(R.id.btn_login_with_facebook);
        loading = (ProgressBar)findViewById(R.id.loading);
        remember_me = (CheckBox)findViewById(R.id.cbx_remember);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                switch(validate()){
                    case 1:
                        Toast.makeText(login.this,"Please input Valid Email Address!",Toast.LENGTH_LONG).show();
                        email.setText("");
                        break;
                    case 2:
                        Toast.makeText(login.this,"Please input Valid Password!",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(login.this,"Please input Email Address",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(login.this,"Please input Password",Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        loading.setVisibility(View.VISIBLE);
                        Login_with_email();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, register.class);
                startActivity(intent);
                break;
            case R.id.btn_login_with_facebook:
                break;

        }
    }
    private void verify() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", "dummy user");//"No name defined" is the default value.
        String verify = prefs.getString("verify", "FALSE");
        if (!email.equals("dummy user") && verify.equals("TRUE")) {
            Global.current_user_email = email;
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void Login_with_email() {
        String username = String.valueOf(email.getText());
        String userpassword = String.valueOf(password.getText());
        FirebaseApp.initializeApp(getApplicationContext());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, userpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Global.current_user_email = user.getEmail();
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);

                            if (remember_me.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("email", Global.current_user_email);
                                editor.putString("verify", "TRUE");
                                editor.apply();
                            }
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            loading.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private Integer validate() {
        String username = String.valueOf(email.getText());
        String userpassword = String.valueOf(password.getText());
        if(username.length()==0){
            return EMPTYEMAIL;
        }
        if(userpassword.length()==0){
            return EMPTYPASS;
        }
        if(!username.contains("@")){
            return INVALIDEMAIL;
        }
        if(userpassword.length()<6){
            return INVLAIDPASS;
        }
        return VALID;
    }
}
