package mobi.carton.subtitle;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mobi.carton.R;


/**
 * Adapter for subtitle, very simple data-bind just a single text
 */
public class SubtitleAdapter extends RecyclerView.Adapter<SubtitleViewHolder> {


    private ArrayList<String> mSubtitles;


    public SubtitleAdapter() {
        mSubtitles = new ArrayList<>();
    }


    @Override
    public SubtitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subtitle, parent, false);
        return new SubtitleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(SubtitleViewHolder holder, int position) {
        holder.subtitle.setText(mSubtitles.get(position));
    }


    @Override
    public int getItemCount() {
        return mSubtitles.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    /**
     * Method to add (and show) a new subtitle
     * @param subtitle subtitle to add
     */
    public void add(String subtitle) {
        mSubtitles.add(0, subtitle);
        notifyItemInserted(0);
    }
}