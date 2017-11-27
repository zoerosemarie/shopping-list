package com.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ait.android.shoppinglist.adapter.ItemRecyclerAdapter;
import com.ait.android.shoppinglist.touch.ItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_ID = "KEY_ITEM_ID";
    public static final int REQUEST_CODE_EDIT = 1001;
    public static final int REQUEST_CODE_ADD = 1002;
    private int positionToEdit = -1;
    private ItemRecyclerAdapter adapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();

        ((ItemApplication)getApplication()).openRealm();

        RecyclerView recyclerViewItem = createRecyclerView();
        createTouchHelper(recyclerViewItem);
        recyclerViewItem.setAdapter(adapter);
    }

    public RecyclerView createRecyclerView() {
        RecyclerView recyclerViewItem = findViewById(R.id.recyclerItem);
        adapter = new ItemRecyclerAdapter(this, ((ItemApplication)getApplication()).getRealmItem());

        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItem.setHasFixedSize(true);

        return recyclerViewItem;
    }

    public void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void createTouchHelper(RecyclerView recyclerViewItem) {
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_item:
                openNewItemActivity();
                break;
            case R.id.action_delete_all:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        ((ItemApplication)getApplication()).closeRealm();

        super.onDestroy();
    }

    public void openEditActivity(int adapterPosition, String todoID) {
        positionToEdit = adapterPosition;

        Intent intentEdit = new Intent(this, EditItemActivity.class);
        intentEdit.putExtra(KEY_ITEM_ID, todoID);
        startActivityForResult(intentEdit, REQUEST_CODE_EDIT);
    }

    public void openNewItemActivity() {
        Intent intentNewItem = new Intent(MainActivity.this, NewItemActivity.class);
        startActivityForResult(intentNewItem, REQUEST_CODE_ADD);
    }

    public void deleteAll() {
        adapter.deleteAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            String itemIDThatWasEdited = data.getStringExtra(KEY_ITEM_ID);
            adapter.updateItem(itemIDThatWasEdited, positionToEdit);
        } else if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            String itemToAdd = data.getStringExtra(KEY_ITEM_ID);
            adapter.addItem(itemToAdd);
        } else if (requestCode == REQUEST_CODE_EDIT){
            Toast.makeText(this, R.string.cancelled_edit, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.cancelled, Toast.LENGTH_LONG).show();
        }
    }
}
