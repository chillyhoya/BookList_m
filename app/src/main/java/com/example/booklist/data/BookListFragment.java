package com.example.booklist.data;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.booklist.BookListMainActivity;
import com.example.booklist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment {//由布局生成相应的视图

    private BookListMainActivity.BookAdapter bookAdapter;
    public BookListFragment(BookListMainActivity.BookAdapter bookAdapter) {
        this.bookAdapter=bookAdapter;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView listViewBooks=view.findViewById(R.id.list_view_books);
        listViewBooks.setAdapter(bookAdapter);
        this.registerForContextMenu(listViewBooks);

        return view;
    }

}
