package square.size.editor.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import square.size.editor.R;

public class ColorAdapter extends RecyclerView.Adapter {
    private List<String> colors = new ArrayList<>();
    private Context mContext;
    private OnItemColorClickListener onItemClickListener;

    public ColorAdapter(List<String> colors, OnItemColorClickListener onItemClickListener) {
        this.colors = colors;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ColorHolder colorHolder = (ColorHolder) holder;
        StateListDrawable background = (StateListDrawable) colorHolder.itemView.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) background.getConstantState();
        Drawable[] children = drawableContainerState.getChildren();
        GradientDrawable selectedItem = (GradientDrawable) children[0];
        GradientDrawable unselectedItem = (GradientDrawable) children[1];
        selectedItem.setColor(Color.parseColor(colors.get(position)));
        unselectedItem.setColor(Color.parseColor(colors.get(position)));

        colorHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemColorClick(position, colors.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public static class ColorHolder extends RecyclerView.ViewHolder {
        private ImageView bt_color;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemColorClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemColorClickListener {
        void onItemColorClick(int position, String s);
    }
}
