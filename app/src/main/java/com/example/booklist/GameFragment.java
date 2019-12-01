package com.example.booklist;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_game, container, false);
        GameView gameView=new GameView(getContext());
        gameView.setMinimumWidth(view.getWidth());
        gameView.setMinimumHeight(view.getHeight());

        ((FrameLayout)view).addView(gameView);//把gameView添加为子视图
        return view;
    }

}
