package net.flow9.android.firebasemessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.flow9.android.firebasemessage.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 06/11/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.Holder>{
    private List<User> data = new ArrayList<>();

    public void setDataAndRefresh(List<User> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User friend = data.get(position);
        holder.friend = friend;
        holder.textName.setText(friend.name);
        holder.textEmail.setText(friend.email);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        User friend;
        TextView textName;
        TextView textEmail;
        public Holder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
        }
    }
}
