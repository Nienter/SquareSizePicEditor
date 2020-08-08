package square.size.editor.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import square.size.editor.Album;
import square.size.editor.ImagerActivity;
import square.size.editor.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private Context mContext;
    private List<Album> albums = new ArrayList<>();

    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_album, parent, false);
        AlbumHolder albumHolder = new AlbumHolder(view);
        return albumHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        Glide.with(mContext).load(albums.get(position).getFirstImagePath()).into(holder.iv_cover);
        holder.tv_album_name.setText(albums.get(position).getDirName());
        holder.tv_pic_count.setText(String.format("%ditems", albums.get(position).getPicNum()));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ImagerActivity.class);
            intent.putExtra("paths", albums.get(position).getDirPath());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView iv_cover;
        private AppCompatTextView tv_album_name;
        private AppCompatTextView tv_pic_count;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_album_name = itemView.findViewById(R.id.tv_album_name);
            tv_pic_count = itemView.findViewById(R.id.tv_pic_num);
        }
    }
}
