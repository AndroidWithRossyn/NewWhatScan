package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities.MessegesActivity;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.userModel;

import java.util.List;

import me.fahmisdk6.avatarview.AvatarView;

public class UsersAdapter extends Adapter<ViewHolder> {
    private Context context;

    private List<userModel> list;
    private String pack;
    AvatarView avatarView5;

    public class userHolder extends ViewHolder {
        LinearLayout list;
        TextView msg;
        TextView name;
        TextView readunread;
        TextView time;

        private userHolder(@NonNull View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            list = (LinearLayout) view.findViewById(R.id.list);
            readunread = (TextView) view.findViewById(R.id.unread);
            avatarView5 = (AvatarView) view.findViewById(R.id.icon);
        }
    }

    public UsersAdapter(Context context, List<userModel> list, String str) {
        this.context = context;
        this.list = list;
        this.pack = str;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new userHolder(LayoutInflater.from(context).inflate(R.layout.users_home, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        userHolder userHolder = (userHolder) viewHolder;
        userModel userModel = (userModel) list.get(i);
        userHolder.name.setText(userModel.getName());
        userHolder.msg.setText(userModel.getLastmsg());
        userHolder.time.setText(userModel.getTime());
        userHolder.list.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, MessegesActivity.class);
                intent.putExtra("name", userModel.getName());
                intent.putExtra("pack", pack);
                context.startActivity(intent);
            }
        });
        avatarView5.bind(userModel.getName(), "");
    }

    public int getItemCount() {
        return list.size();
    }

}
