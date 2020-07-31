package com.tbt.bachtung.hrm_teacher;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Teacher_Adapter extends RecyclerView.Adapter<Teacher_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Teacher> teacherList;
    private Clicklistener _click;

    public Teacher_Adapter(Context context, ArrayList<Teacher> teacherList, Clicklistener _click) {
        this.context = context;
        this.teacherList = teacherList;
        this._click = _click;
    }

    @NonNull
    @Override
    public Teacher_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Teacher _teacher = teacherList.get(position);
        holder.txtTen.setText("Họ Tên: " + _teacher.getTen());
        holder.txtbomon.setText("Bộ Môn: " + _teacher.getBomon());
        holder.txtkhoa.setText("Khoa: " + _teacher.getKhoa());
        holder.rv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _click.onClick(position);
            }
        });
        holder.rv_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                _click.onLongClick(position);
                return true;
            }
        });
        Picasso.with(context).load(_teacher.getUrlImage())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.img);
    }


    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtTen, txtkhoa, txtbomon;
        RelativeLayout rv_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //ánh xạ
            txtTen = (TextView) itemView.findViewById(R.id.textviewhoten_line);
            txtkhoa = (TextView) itemView.findViewById(R.id.textviewkhoa_line);
            txtbomon = (TextView) itemView.findViewById(R.id.textviewbomon_line);
            img = (ImageView) itemView.findViewById(R.id.imageview_line);
            rv_layout = itemView.findViewById(R.id.rv_layout);

        }
    }
}
