package mobi.carton.subtitle;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mobi.carton.PageFragment;
import mobi.carton.R;
import mobi.carton.csr.ContinuousSpeechRecognition;

public class SubtitleFragment extends PageFragment
        implements
        ContinuousSpeechRecognition.OnTextListener {


    private RecyclerView mRecyclerView;
    private SubtitleAdapter mSubtitleAdapter;

    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


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

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(getContext());
        mContinuousSpeechRecognition.setOnTextListener(this);

        return rootView;
    }


    @Override
    public void onResumePage() {
        mContinuousSpeechRecognition.start();
    }


    @Override
    public void onPausePage() {
        mContinuousSpeechRecognition.stop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContinuousSpeechRecognition.destroy();
    }


    public void addSubtitle(String subtitle) {
        mSubtitleAdapter.add(subtitle);
        mRecyclerView.scrollToPosition(0);
    }


    @Override
    public void onTextMatched(ArrayList<String> matchedText) {
        addSubtitle(matchedText.get(0));
    }


    @Override
    public void onError(int error) {
    }
}
