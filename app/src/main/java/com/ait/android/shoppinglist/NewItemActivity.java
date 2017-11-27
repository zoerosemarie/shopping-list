package com.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ait.android.shoppinglist.data.Item;

import java.util.UUID;

public class NewItemActivity extends AppCompatActivity {

    private EditText etName;
    private Spinner spinnerCategory;
    private EditText etPrice;
    private EditText etDescription;
    private CheckBox cbAlreadyPurchased;

    private Item newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        initializeFields();

        Button btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    addItem();
                }
            }
        });
    }

    public void addItem() {
        ((ItemApplication)getApplication()).getRealmItem().beginTransaction();
        newItem = ((ItemApplication)getApplication()).getRealmItem().createObject(Item.class, UUID.randomUUID().toString());
        newItem.setName(etName.getText().toString());
        newItem.setCategory(spinnerCategory.getSelectedItem().toString());
        newItem.setDescription(etDescription.getText().toString());
        newItem.setPrice(etPrice.getText().toString());
        newItem.setPurchased(cbAlreadyPurchased.isChecked());
        ((ItemApplication)getApplication()).getRealmItem().commitTransaction();

        Intent intentResult = new Intent();
        intentResult.putExtra(MainActivity.KEY_ITEM_ID, newItem.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public void initializeFields() {
        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        etDescription = (EditText) findViewById(R.id.etDescription);
        cbAlreadyPurchased = (CheckBox) findViewById(R.id.cbAlreadyPurchased);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    public boolean checkEmpty() {
        if(!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText()) && !TextUtils.isEmpty(etDescription.getText())) {
            return true;
        } else {
            if (TextUtils.isEmpty(etName.getText())) {
                etName.setError("This field can not be empty");
            }
            if (TextUtils.isEmpty(etPrice.getText())) {
                etPrice.setError("This field can not be empty");
            }
            if (TextUtils.isEmpty(etDescription.getText())) {
                etDescription.setError("This field cannot be empty");
            }
            return false;
        }
    }
}
