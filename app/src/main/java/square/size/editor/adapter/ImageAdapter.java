package square.size.editor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import square.size.editor.HandleActivity;
import square.size.editor.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    private Context context;
    private List<String> imagePath = new ArrayList<>();

    public  ImageAdapter(List<String> path) {
        this.imagePath = path;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Glide.with(context).load(imagePath.get(position)).into(holder.iv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HandleActivity.class);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView iv;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
