package com.sedadurmus.yenivavi.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sedadurmus.yenivavi.Adapter.ChatAdapter;
import com.sedadurmus.yenivavi.Adapter.KullaniciAdapter;
import com.sedadurmus.yenivavi.Model.Chat;
import com.sedadurmus.yenivavi.Model.Kullanici;
import com.sedadurmus.yenivavi.R;
import com.sedadurmus.yenivavi.notifications.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter  kullaniciAdapter;
    private ChatAdapter chatAdapter;
    private List<Kullanici> mKulllanici;
    private SwipeRefreshLayout refreshLayout;

    Context  context;
    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);

        refreshLayout = view.findViewById(R.id.refresh);

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

//        reference = FirebaseDatabase.getInstance().getReference("Chats").child(fuser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    ChatList chatList = snapshot.getValue(ChatList.class);
//                    assert chatList != null;
//                    usersList.add(chatList);
//                }
//                chatList();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getSender().equals(fuser.getUid())) {
                        usersList.add(chat.getRecevier());
                    }
                    if (chat.getRecevier().equals(fuser.getUid())) {
                        usersList.add(chat.getSender());
                    }
                }
                Set<String> hashSet = new HashSet<String>(usersList);
                usersList.clear();
                usersList.addAll(hashSet);
                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mKulllanici.clear();
                readChatsRefresh();
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

//    private void chatList() {
//        mKulllanici =new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mKulllanici.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
//                    for (ChatList chatList : usersList){
//                        if (kullanici.getId().equals(chatList.getId())){
//                            mKulllanici.add(kullanici);
//                        }
//                    }
//                }
//                chatAdapter = new ChatAdapter(getContext(), mKulllanici);
//                recyclerView.setAdapter(chatAdapter);
//                chatAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void updateToken(String token){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);

    }

    private void readChats(){

        mKulllanici =new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mKulllanici.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Kullanici kullanici =snapshot.getValue(Kullanici.class);
//                        display 1 user from chats
                        for (String id:usersList){
                            assert kullanici != null;
                            if (kullanici.getId().equals(id)){
                                mKulllanici.add(kullanici);
                            }
                        }
                    }
                    chatAdapter = new ChatAdapter(getContext(), mKulllanici);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void readChatsRefresh(){
        mKulllanici =new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
        refreshLayout.setRefreshing(false);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mKulllanici.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Kullanici kullanici =snapshot.getValue(Kullanici.class);

//                        display 1 user from chats
                    for (String id : usersList){
                        assert kullanici != null;
                        if (kullanici.getId().equals(id)){
                            mKulllanici.add(kullanici);
                        }
                    }
                }
                chatAdapter = new ChatAdapter(getContext(), mKulllanici);
                recyclerView.setAdapter(chatAdapter);
                refreshLayout.setRefreshing(false);
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}











