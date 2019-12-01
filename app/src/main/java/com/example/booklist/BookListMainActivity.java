package com.example.booklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booklist.data.Book;
import com.example.booklist.data.BookFragmentAdapter;
import com.example.booklist.data.BookListFragment;
import com.example.booklist.data.model.BookSaver;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_NEW = 1;
    public static final int CONTEXT_MENU_UPDATA = CONTEXT_MENU_NEW+1;
    public static final int CONTEXT_MENU_DELETE = CONTEXT_MENU_UPDATA+1;
    public static final int CONTEXT_MENU_ABOUT = CONTEXT_MENU_DELETE+1;
    public static final int REQUEST_CODE_NEW = 901;
    public static final int REQUEST_CODE_UPDATA = REQUEST_CODE_NEW+1;

    BookSaver bookSaver;
    private List<Book> listBooks=new ArrayList<>();
    BookAdapter bookAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookSaver=new BookSaver(this);
        listBooks=bookSaver.load();
        if(listBooks.size()==0)
            init();

        bookAdapter = new BookAdapter(BookListMainActivity.this, R.layout.list_view_item_book, listBooks);

        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new BookListFragment(bookAdapter));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());
        datas.add(new GameFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        titles.add("游戏");
        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==findViewById(R.id.list_view_books)) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(listBooks.get(info.position).getTitle());
            menu.add(0, CONTEXT_MENU_NEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_UPDATA, 0, "修改");
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_NEW:
                if(resultCode==RESULT_OK) {
                    String title=data.getStringExtra("title");
                    int insertPosition=data.getIntExtra("position",0);

                    listBooks.add(insertPosition,new Book(title,R.drawable.book_no_name));
                    bookAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_UPDATA:
                if(resultCode==RESULT_OK){
                    String title=data.getStringExtra("title");
                    int insertPosition=data.getIntExtra("position",0);

                    listBooks.get(insertPosition).setTitle(title);
                    bookAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_NEW:
                Intent intent=new Intent(this,EditBookActivity.class);
                intent.putExtra("title","无名书籍");
                intent.putExtra("position",((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_NEW);
                break;
            case CONTEXT_MENU_UPDATA:
                int insertPosition=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Intent intent2=new Intent(this,EditBookActivity.class);
                intent2.putExtra("title",listBooks.get(insertPosition).getTitle());
                intent2.putExtra("position",insertPosition);
                startActivityForResult(intent2, REQUEST_CODE_UPDATA);
                break;
            case CONTEXT_MENU_DELETE:
                AdapterView.AdapterContextMenuInfo info1=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int RemovePosition=info1.position;
                Dialog dialog = new AlertDialog.Builder(BookListMainActivity.this).setTitle("删除信息？").setMessage("您确定要删除这条信息吗？").setIcon(R.drawable.book_no_name).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listBooks.remove(RemovePosition);
                        bookAdapter.notifyDataSetChanged();
                        Toast.makeText(BookListMainActivity.this,"删除成功", Toast.LENGTH_LONG).show();
                    }
                }).setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
                dialog.show();
                break;
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(BookListMainActivity.this,"图书列表v3.0", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void init() {
        listBooks.add(new Book("软件项目管理案例教程（第4版）",R.drawable.book_2));
        listBooks.add(new Book("创新工程实践",R.drawable.book_no_name));
        listBooks.add(new Book("信息安全数学基础（第2版）",R.drawable.book_1));
}

    public class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

        public BookAdapter(Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Book book = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getCoverResourceID());
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getTitle());
            return view;
        }
    }
}
