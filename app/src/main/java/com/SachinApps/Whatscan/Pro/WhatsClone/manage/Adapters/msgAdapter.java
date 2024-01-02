package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.SachinApps.Whatscan.Pro.WhatsClone.R;
import com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models.DataModel;

import java.util.List;

public class msgAdapter extends Adapter<ViewHolder> {
    private Context context;
    private List<DataModel> list;

    private class userHolder extends ViewHolder {
        LinearLayout linearLayout;
        TextView msg;
        TextView time;

        private userHolder(@NonNull View view) {
            super(view);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            linearLayout = (LinearLayout) view.findViewById(R.id.msg_parent);
        }
    }

    public msgAdapter(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new userHolder(LayoutInflater.from(context).inflate(R.layout.msg_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        userHolder userHolder = (msgAdapter.userHolder)viewHolder;
        DataModel dataModel = list.get(i);
        userHolder.msg.setText((CharSequence)dataModel.getMsg());
        userHolder.time.setText((CharSequence)dataModel.getTime());
        userHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int equals = userHolder.msg.getText().equals("\ud83d\udcf7 Photo") ? 1 : 0;
                if (equals == 0) {
                    copymsg(dataModel.getMsg());
                }

            }
        });
    }

    public int getItemCount() {
        return list.size();
    }

    public int getItemViewType(int equals) {
        return equals;
    }

    private void copymsg(String s) {
        try {
            ((ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText((CharSequence)"msg", (CharSequence)s));
            Toast.makeText(context, (CharSequence)"Message Copied", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
