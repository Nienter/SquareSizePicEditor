package square.size.editor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import square.size.editor.R;

public class PatternAdapter extends RecyclerView.Adapter<PatternAdapter.PatternHolder> {
    private Context mContext;
    private List<Drawable> bitmaps;

    public PatternAdapter(List<Drawable> bitmaps,onPatternListener onPatternListener) {
        this.bitmaps = bitmaps;
        this.onPatternListener = onPatternListener;
    }

    @NonNull
    @Override
    public PatternHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pattern, parent, false);
        return new PatternHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatternHolder holder, int position) {
        Glide.with(mContext).load(bitmaps.get(position)).into((ImageView) holder.itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPatternListener.onPattern(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class PatternHolder extends RecyclerView.ViewHolder {

        public PatternHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private onPatternListener onPatternListener;
    public interface  onPatternListener{
        void onPattern(int pos);
    }
}
