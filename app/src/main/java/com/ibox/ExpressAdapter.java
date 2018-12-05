package com.ibox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by lenovo on 2018/4/11.
 */

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ViewHolder> {
     private Context context;
    private List<ExpressShow>  expressList;

    public ExpressAdapter (List<ExpressShow>  expressList)
    {
        this.expressList=expressList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context==null)
        {
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.express_cardview_item,parent,false);
       final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // View headview=navigationView.getHeaderView(0);
                Intent  intent=new Intent(context,DetaiShowActivity.class);
                intent.putExtra("ordernumber",viewHolder.textView.getText().toString().substring(5));
                Log.d("IBox","hello");
                context.startActivity(intent);

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            ExpressShow express=expressList.get(position);
        holder.textView.setText(express.getOrdernumber());
        Glide.with(context).load(express.getImgnumber()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return expressList.size();
    }

    static   class ViewHolder extends RecyclerView.ViewHolder{
        CardView  cardView;
        ImageView  imageView;
        TextView   textView;
        Button   button;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            imageView=(ImageView)itemView.findViewById(R.id.express_image);
            textView=(TextView)itemView.findViewById(R.id.express_ordernumber);
             button=(Button)itemView.findViewById(R.id.look_through);
        }
    }

}
