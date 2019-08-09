package com.example.fooddemoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddemoapp.Models.CustomUser;
import com.example.fooddemoapp.utils.Constants;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static com.example.fooddemoapp.utils.Constants.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;

    private  FirebaseAuth mAuth;

    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.editTextUserEmail);
        etPassword = (EditText) findViewById(R.id.editTextUserPassword);

        mAuth = FirebaseAuth.getInstance();

        // Linkeando con los listeners con la funcion OnClick
        findViewById(R.id.buttonLogIn).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK){
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Tambien crearemos una copia de la info en la base de datos
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
                CustomUser customUser = new CustomUser(user);

                String uid = user.getUid();

                db.child(uid).setValue(customUser);

                Toast.makeText(this, "Bienvenido " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                // No pasaremos al Main Activity sin embargo haremos que ponga sus datos de nuevo


            } else{
                if (response == null){ // La respuesta es nula si se presiono el boton hacia atras
                    Toast.makeText(this, "Se presiono el boton hacia atras", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    } // End of onActivityResult

    @Override
    public void onClick(View v) {
        switch ( v.getId() ){
            case R.id.textViewSignUp:{
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN
                );
                break;
            }
            case R.id.buttonLogIn:{
                buttonLogIn();
                break;
            }
            default:
                break;
        }
    }

    // Funcion para definir el comportamiento de este boton
    public void buttonLogIn(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validandoel Email
        if (email.isEmpty()){
            etEmail.setError("Ingrese un Email");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Porfavor entre un email valido");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etPassword.setError("Ingrese su contraseña");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            etPassword.setError("El numero minimo de caracteres es 6");
            etPassword.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();

                    // Si podemos logearnos correctamente vamos a la ventana principal
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
