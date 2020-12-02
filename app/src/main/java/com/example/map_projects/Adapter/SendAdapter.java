package com.example.map_projects.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map_projects.R;
import com.example.map_projects.model.Demo;

import java.util.ArrayList;

public class SendAdapter extends RecyclerView.Adapter<SendAdapter.SendViewHolder> {
    Context context;
    private ArrayList<Demo> demolist;

    public SendAdapter(Context context, ArrayList<Demo> demolist) {
        this.context = context;
        this.demolist = demolist;
    }
    @NonNull
    @Override
    public SendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mynew, parent, false);
        return new SendViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SendViewHolder holder, final int position) {
        holder.Nmae.setText(demolist.get(position).getName());
        holder.flatno.setText(demolist.get(position).getFlatno());
        holder.street.setText(demolist.get(position).getStreet());
        holder.title.setText(demolist.get(position).getTitle());
    }
    @Override
    public int getItemCount() {
        return demolist.size();
    }

    public class SendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView Nmae,flatno,street,title;
       ImageView defaultbutton;

        public SendViewHolder(@NonNull final View itemView) {

            super(itemView);
            Nmae = itemView.findViewById(R.id.name_TV);
            flatno = itemView.findViewById(R.id.rollno_TV);
            street =  itemView.findViewById(R.id.address_TV);
            title=itemView.findViewById(R.id.title);
            defaultbutton =itemView.findViewById(R.id.img_selector);
        }
        @Override
        public void onClick(View view) {

            demolist.remove(getAdapterPosition());
           notifyDataSetChanged();
        }
    }
}