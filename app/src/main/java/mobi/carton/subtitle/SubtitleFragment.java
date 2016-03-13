package mobi.carton.subtitle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import mobi.carton.R;
import mobi.carton.csr.ContinuousSpeechRecognition;

public class SubtitleFragment extends Fragment
        implements
        ContinuousSpeechRecognition.OnTextListener,
        ContinuousSpeechRecognition.OnRmsListener {


    private RecyclerView mRecyclerView;
    private SubtitleAdapter mSubtitleAdapter;

    private ProgressBar mProgressBarRms;

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

        mProgressBarRms = (ProgressBar) rootView.findViewById(R.id.progressbar_rms);

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(getContext());
        mContinuousSpeechRecognition.setOnTextListener(this);
        mContinuousSpeechRecognition.setOnRmsListener(this);

        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mContinuousSpeechRecognition.start();
        } else {
            if (mContinuousSpeechRecognition != null)
                mContinuousSpeechRecognition.stop();
        }
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


    @Override
    public void onRmsChanged(float rms) {
        mProgressBarRms.setProgress((int) (rms * 10));
    }
}
