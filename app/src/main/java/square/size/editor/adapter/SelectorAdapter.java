package square.size.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itheima.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

import square.size.editor.R;

public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.SelectorImage> {
    private Context mContext;
    private List<File> selector_file;
    private ImageAdapter.OnSelectorListener onSelectorListener;

    public SelectorAdapter(List<File> files, ImageAdapter.OnSelectorListener onSelectorListener) {
        this.selector_file = files;
        this.onSelectorListener = onSelectorListener;
    }


    @NonNull
    @Override
    public SelectorImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_image, parent, false);
        return new SelectorImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorImage holder, int position) {
        Glide.with(mContext).load(selector_file.get(position)).into(holder.iv_selector_image);
        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectorListener.onSelectorRemove(selector_file.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return selector_file.size();
    }

    public static class SelectorImage extends RecyclerView.ViewHolder {
        RoundedImageView iv_selector_image;
        AppCompatImageView iv_remove;

        public SelectorImage(@NonNull View itemView) {
            super(itemView);
            iv_selector_image = itemView.findViewById(R.id.iv_selector);
            iv_remove = itemView.findViewById(R.id.iv_remove);
        }
    }


}
