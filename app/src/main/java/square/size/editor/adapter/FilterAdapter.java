package square.size.editor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import square.size.editor.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {
    private List<Bitmap> bitmaps;
   public FilterAdapter(List<Bitmap> bitmaps,OnItemClickListener onItemClickListener){
       this.bitmaps = bitmaps;
       this.onItemClickListener = onItemClickListener;
   }

    private Context mContext;
    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
        RoundedCorners roundedCorners = new RoundedCorners(10);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

        Glide.with(mContext).asBitmap().load(bitmaps.get(position)).apply(options).into((ImageView) holder.itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class FilterHolder extends RecyclerView.ViewHolder {

        public FilterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
}
