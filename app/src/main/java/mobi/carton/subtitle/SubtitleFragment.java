package mobi.carton.subtitle;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.carton.PageFragment;
import mobi.carton.R;

public class SubtitleFragment extends PageFragment {


    private RecyclerView mRecyclerView;
    private SubtitleAdapter mSubtitleAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subtitle, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSubtitleAdapter = new SubtitleAdapter();
        mRecyclerView.setAdapter(mSubtitleAdapter);

        return rootView;
    }


    public void addSubtitle(String subtitle) {
        mSubtitleAdapter.add(subtitle);
        mRecyclerView.scrollToPosition(0);
    }
}
