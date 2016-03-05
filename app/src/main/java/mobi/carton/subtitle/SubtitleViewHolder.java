package mobi.carton.subtitle;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mobi.carton.R;

public class SubtitleViewHolder extends RecyclerView.ViewHolder {


    public TextView subtitle;


    public SubtitleViewHolder(View itemView) {
        super(itemView);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle_textView);
    }
}
