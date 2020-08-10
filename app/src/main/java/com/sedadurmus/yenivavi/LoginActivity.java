package com.sedadurmus.yenivavi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.Model.WriteFile;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText edt_email_Giris, edt_sifre_Giris, editText;
    Button btn_giris_yap;
    TextView txt_kayitSayfasina_git, mRecoverPassTv;

    SignInButton mGoogleLoginBtn;
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private String TAG;

    private FirebaseAuth yetki;

    private DatabaseReference yol;
    ProgressDialog pd;

    private long backPressedTime;
    private Toast backToast;

    public static Kullanici kullanici;

    public static Kullanici getKullanici() {
        return kullanici;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email_Giris = findViewById(R.id.emailEt);
        edt_sifre_Giris = findViewById(R.id.passwordEt);

        btn_giris_yap = findViewById(R.id.login_btn);

        yetki = FirebaseAuth.getInstance();
        txt_kayitSayfasina_git = findViewById(R.id.nothave_accountTv);
        mRecoverPassTv = findViewById(R.id.recoverPassTv);

//        şifre sıfırlama
        mRecoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

        //        google la oturum açma ile ilgili
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleLoginBtn = findViewById(R.id.googleLoginBtn);

        mGoogleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        txt_kayitSayfasina_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btn_giris_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Giriş Yapılıyor..");
                progressDialog.show();

                final String str_emailGiris = edt_email_Giris.getText().toString();
                final String str_sifreGiris = edt_sifre_Giris.getText().toString();

                if (TextUtils.isEmpty(str_emailGiris) || TextUtils.isEmpty(str_sifreGiris)) {
                    Toast.makeText(LoginActivity.this, "Lütfen bütün alanları doldurun..", Toast.LENGTH_SHORT).show();
                } else {
                    //Giriş yapma kodları
                    yetki.signInWithEmailAndPassword(str_emailGiris, str_sifreGiris)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                                                .child(yetki.getCurrentUser().getUid());

                                        yolGiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                progressDialog.dismiss();

                                                kullanici = dataSnapshot.getValue(Kullanici.class);
                                                if (kullanici == null) return;
                                                kullanici.setMail(str_emailGiris);
                                                WriteFile.Instance.SaveDataToFile((str_emailGiris + "-" + str_sifreGiris), getApplicationContext());
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Giriş Başarısız!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void showRecoverPasswordDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = LayoutInflater.from(LoginActivity.this).inflate(
//                R.layout.alert_dialog,
//                (ConstraintLayout)findViewById(R.id.layoutDialogContainer)
//        );
//        builder.setView(view);
//        ((TextView)view.findViewById(R.id.textTitle)).setText("Şifre Sıfırlama");
//        ((EditText)view.findViewById(R.id.textMessage)).setHint("Email");
//        ((Button)view.findViewById(R.id.buttonNo)).setText("Kapat");
//        ((Button)view.findViewById(R.id.buttonYes)).setText("Sıfırla");
//        ((ImageView)view.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
//        final AlertDialog alertDialog1 = builder.create();
//        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = ((EditText)view).getText().toString().trim();
//                beginRecovery(email);
//                finish();
//            }
//        });
//        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog1.cancel();
//            }
//        });
//        if (alertDialog1.getWindow() != null){
//            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        alertDialog1.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Şifre Sıfırlama");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Sıfırla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        builder.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dismiss dialog
                dialogInterface.dismiss();
            }
        });
// show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        yetki.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Mail gönderildi. Lütfen hesabınızı kontrol ediniz.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Hata!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            backToast.cancel();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Çıkmak için iki kere basınız", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

//        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                assert acct != null;
                firebaseAuthWithGoogle(acct);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Hata" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        yetki.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                            assert firebaseKullanici != null;
                            final String kullaniciId = firebaseKullanici.getUid();

                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                                    .child(kullaniciId);
                            yol.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Kullanici kul = dataSnapshot.getValue(Kullanici.class);
                                    if(kul==null){

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("id", kullaniciId);
                                    hashMap.put("kullaniciadi", acct.getDisplayName());
                                    hashMap.put("ad", acct.getDisplayName());
                                    hashMap.put("bio", "");
                                    hashMap.put("profilPuan", 20);
                                    hashMap.put("resimurl",acct.getPhotoUrl().toString());
                                    yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            if (task.isSuccessful()) {
//                                        pd.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    }else{
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Hataaaaaaa" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
