package com.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ait.android.shoppinglist.data.Item;

import java.util.UUID;

public class EditItemActivity extends AppCompatActivity {

    private EditText etName;
    private Spinner spinnerCategory;
    private EditText etPrice;
    private EditText etDescription;
    private CheckBox cbAlreadyPurchased;

    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setItemToEdit();

        initializeFields();

        Button btnSave = (Button) findViewById(R.id.btnSave);

        if (itemToEdit != null) {
            setFields();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            editItem();
            }
        });
    }

    public void setItemToEdit() {
        if (getIntent().hasExtra(MainActivity.KEY_ITEM_ID)) {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_ITEM_ID);
            itemToEdit = ((ItemApplication)getApplication()).getRealmItem().where(Item.class).
                    equalTo(getString(R.string.itemID), itemID).findFirst();
        }
    }

    public void initializeFields() {
        etName = (EditText) findViewById(R.id.etName);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDescription = (EditText) findViewById(R.id.etDescription);
        cbAlreadyPurchased = (CheckBox) findViewById(R.id.cbAlreadyPurchased);
    }

    public void setFields() {
        etName.setText(itemToEdit.getName());
        setSpinnerSelected(itemToEdit.getCategory());
        etPrice.setText(itemToEdit.getPrice());
        etDescription.setText(itemToEdit.getDescription());
        cbAlreadyPurchased.setChecked(itemToEdit.isPurchased());
    }

    public void initializeItemToEdit() {
        itemToEdit.setName(etName.getText().toString());
        itemToEdit.setCategory(spinnerCategory.getSelectedItem().toString());
        itemToEdit.setDescription(etDescription.getText().toString());
        itemToEdit.setPrice(etPrice.getText().toString());
        itemToEdit.setPurchased(cbAlreadyPurchased.isChecked());
    }

    public void editItem() {
        ((ItemApplication)getApplication()).getRealmItem().beginTransaction();
        initializeItemToEdit();
        ((ItemApplication)getApplication()).getRealmItem().commitTransaction();

        Intent intentResult = new Intent();
        intentResult.putExtra(MainActivity.KEY_ITEM_ID, itemToEdit.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }

    public void setSpinnerSelected(String category) {
        if (category != null) {
            if (category.equals(getString(R.string.food))) {
                spinnerCategory.setSelection(0);
            } else if (category.equals(getString(R.string.electronics))) {
                spinnerCategory.setSelection(1);
            } else if (category.equals(getString(R.string.clothing))) {
                spinnerCategory.setSelection(2);
            } else if (category.equals(getString(R.string.book))) {
                spinnerCategory.setSelection(3);
            } else {
                spinnerCategory.setSelection(4);
            }
        }
    }
}
