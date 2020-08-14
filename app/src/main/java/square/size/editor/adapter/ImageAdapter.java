package square.size.editor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itheima.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import square.size.editor.Constants;
import square.size.editor.HandleActivity;
import square.size.editor.PuzzleBean;
import square.size.editor.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    private Context context;
    private List<File> imagePath;
    private OnSelectorListener onSelectorListener;

    public ImageAdapter(List<File> path, OnSelectorListener onSelectorListener) {
        this.imagePath = path;
        this.onSelectorListener = onSelectorListener;
    }

    public interface OnSelectorListener {
        void onSelectorAdd(File file);

        void onSelectorRemove(File file);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Glide.with(context).load(imagePath.get(position)).into(holder.iv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.from.equals(Constants.GALLERY)) {
                    Intent intent = new Intent(context, HandleActivity.class);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath.get(position).getPath());
                    List<Bitmap> bitmaps = new ArrayList<>();
                    bitmaps.add(bitmap);
                    Constants.setCacheBitmaps(bitmaps);
                    intent.putExtra("type", 0);
                    intent.putExtra("borderSize", 1);
                    intent.putExtra("themeId", 0);
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.finish();
                } else if (Constants.from.equals(Constants.COLLAGE)) {
                    onSelectorListener.onSelectorAdd(imagePath.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        private RoundedImageView iv;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
