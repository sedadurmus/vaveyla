package com.sedadurmus.yenivavi.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sedadurmus.yenivavi.Fragments.GorevFragment;
import com.sedadurmus.yenivavi.LoginActivity;
import com.sedadurmus.yenivavi.Model.yapilabilirGorev;
import com.sedadurmus.yenivavi.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.sedadurmus.yenivavi.Fragments.DukkanFragment.dialog;
import static com.sedadurmus.yenivavi.Fragments.GorevFragment.seciliGorev;

//bu mağazanın adaptörü


public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> {
    LayoutInflater layoutInflater;
    List<yapilabilirGorev> mission_list;

    Context mContext;

    public MissionAdapter(Context context, List<yapilabilirGorev> missionList) {
        this.mContext = context;
        this.mission_list = missionList;

    }

    @NonNull
    @Override
    public MissionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_mission_two, parent, false);
        return new MissionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final yapilabilirGorev gorev = mission_list.get(position);
        holder.postTitle.setText(gorev.getGorevBasligi());
        holder.postDescription.setText(gorev.getGorevHakkinda());
        holder.point.setText("Puan: " + (int)gorev.getGorevPuani());
        Glide.with(mContext).load(gorev.getGorevResmi()).into(holder.postPicture);
        holder.postBaslangic.setText(gorev.getGorevBaslangic());
        holder.postBitis.setText(gorev.getGorevBitis());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GorevFragment.showDialog(gorev);
                dialog = new AlertDialog.Builder(v.getContext());
//                LayoutInflater factory = LayoutInflater.from(v.getContext());
                        View view1 = LayoutInflater.from(mContext).inflate(
                        R.layout.alert_dialog,
                        (ConstraintLayout)v.findViewById(R.id.layoutDialogContainer)
                        );
                        dialog.setView(view1);
                        ((TextView)view1.findViewById(R.id.textTitle)).setText("Uyarı");
                        ((TextView)view1.findViewById(R.id.textMessage)).setText(seciliGorev.getGorevBasligi() + " isimli göreve başlamak istiyor musunuz?");
                        ((Button)view1.findViewById(R.id.buttonNo)).setText(mContext.getResources().getString(R.string.alertButtonNo));
                        ((Button)view1.findViewById(R.id.buttonYes)).setText(mContext.getResources().getString(R.string.alertButtonYes));
                        ((ImageView)view1.findViewById(R.id.imageicon)).setImageResource(R.drawable.ic_info);
final AlertDialog alertDialog = dialog.create();
        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        if (seciliGorev == null) return;
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("AlinanGorevler");
        String gonderiId = veriYolu.push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gorevId", seciliGorev.getGorevId());
        hashMap.put("gonderen", LoginActivity.kullanici.getKullaniciadi()+"__"+LoginActivity.kullanici.getId());
        hashMap.put("tarih", new Date().toString());
        veriYolu.child(gonderiId).setValue(hashMap);
        Toast.makeText(mContext, "Görev başarıyla tanımlandı. Acele et süren başladı!", Toast.LENGTH_SHORT).show();
        alertDialog.cancel();
        }
        });
        view1.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Toast.makeText(mContext, "Diğer görevlere gözatmaya ne dersin?", Toast.LENGTH_SHORT).show();
        alertDialog.cancel();
        }
        });
        if (alertDialog.getWindow() != null){
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();


            }
        });





    }


    @Override
    public int getItemCount() {
        return mission_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postDescription, point, postBaslangic, postBitis;
        ImageView postPicture;
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.mission_title);
            postDescription = itemView.findViewById(R.id.mission_description);
            postPicture = itemView.findViewById(R.id.resim_mission);
            point = itemView.findViewById(R.id.mission_point);
            postBaslangic=itemView.findViewById(R.id.gorev_baslangic);
            postBitis=itemView.findViewById(R.id.gorev_bitis);
            container = itemView.findViewById(R.id.container);
        }


    }
}


