package com.sedadurmus.yenivavi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sedadurmus.yenivavi.Adapter.MessageAdapter;
import com.sedadurmus.yenivavi.Fragments.APIService;
import com.sedadurmus.yenivavi.Model.Chat;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.notifications.Client;
import com.sedadurmus.yenivavi.notifications.Data;
import com.sedadurmus.yenivavi.notifications.MyReponse;
import com.sedadurmus.yenivavi.notifications.Sender;
import com.sedadurmus.yenivavi.notifications.Token;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sedadurmus.yenivavi.LoginActivity.kullanici;
import static java.security.AccessController.getContext;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    Intent intent;
    Context mContext;
    String profileId;
    FirebaseUser fuser;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;
    ImageButton btn_send;
    EditText txt_send;

    ValueEventListener seenListener;
    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar =findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
        intent = getIntent();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg =txt_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), profileId, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "Boş mesaj gönderemezsiniz...", Toast.LENGTH_SHORT).show();
                }
                txt_send.setText("");
            }
        });

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        profileId =preferences.getString("profileid", "none");

        final DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profileId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                kullanici=dataSnapshot.getValue(Kullanici.class);
                username.setText(kullanici.getKullaniciadi());

                readMessages(fuser.getUid(), profileId, kullanici.getResimurl());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

         seenMessage(profileId);
    }

    private void seenMessage(final String profileId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getRecevier().equals(fuser.getUid()) && chat.getSender().equals(profileId)){
                        HashMap<String, Object> hashMap =new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    String simdikiTarih;
    private void sendMessage (String sender, final String recevier, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Date simdi = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simdikiTarih = dateFormat.format(simdi);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("recevier", recevier);
        hashMap.put("message", message);
        hashMap.put("messageTarihi", simdikiTarih);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid())
                .child(profileId);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(profileId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
             if (notify){
             sendNotification(recevier, kullanici.getKullaniciadi(), msg);
             }
                notify =false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendNotification(final String receiver, final String kullaniciadi, final String message){
        DatabaseReference tokens= FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, "Yeni Mesaj" , kullaniciadi+":" +message,
                            profileId);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyReponse>() {
                                @Override
                                public void onResponse(Call<MyReponse> call, Response<MyReponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1 ){
                                            Toast.makeText(mContext, "Hata!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyReponse> call, Throwable t) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void readMessages(final String myid, final String kullaniciid, final String imageurl){
        mchat = new ArrayList<>();
        DatabaseReference messageYolu = FirebaseDatabase.getInstance().getReference("Chats");
        messageYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat =snapshot.getValue(Chat.class);
                    if (chat.getRecevier().equals(myid) && chat.getSender().equals(kullaniciid) || chat.getRecevier().equals(kullaniciid)
                    && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
