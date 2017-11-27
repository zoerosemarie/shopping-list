package com.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ait.android.shoppinglist.MainActivity;
import com.ait.android.shoppinglist.data.Item;
import com.ait.android.shoppinglist.R;
import com.ait.android.shoppinglist.touch.ItemTouchHelperAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Item> itemList;

    private Context context;
    private Realm realmItem;

    public ItemRecyclerAdapter(Context context, Realm realmItem) {
        this.context = context;
        this.realmItem = realmItem;

        itemList = new ArrayList<Item>();

        RealmResults<Item> itemResults = realmItem.where(Item.class).findAll().sort("name", Sort.ASCENDING);

        for (Item item : itemResults) {
            itemList.add(item);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View todoRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_item, parent, false);
        return new ViewHolder(todoRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item itemData = itemList.get(position);

        setHolderFields(holder, itemData);
        setHolderButtons(holder);
        setImage(itemData.getCategory(), holder);

    }

    public void setHolderButtons(ViewHolder viewHolder) {
        final ViewHolder holder = viewHolder;
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
                        itemList.get(holder.getAdapterPosition()).getItemID());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemDismiss(holder.getAdapterPosition());
            }
        });
    }

    public void setHolderFields(ViewHolder holder, Item item) {
        final Item itemData = item;
        holder.tvName.setText(itemData.getName());
        holder.tvCategory.setText(itemData.getCategory());
        holder.tvDescription.setText(itemData.getDescription());
        holder.tvPrice.setText(context.getString((R.string.dollar), itemData.getPrice()));
        holder.cbPurchased.setChecked(itemData.isPurchased());

        holder.cbPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realmItem.beginTransaction();
                itemData.setPurchased(true);
                realmItem.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        Item itemToDelete = itemList.get(position);
        realmItem.beginTransaction();
        itemToDelete.deleteFromRealm();
        realmItem.commitTransaction();

        itemList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void addItem(String itemID) {
        Item newItem = realmItem.where(Item.class).equalTo("itemID",
                itemID).findFirst();
        itemList.add(0, newItem);
        notifyItemInserted(0);
    }

    public void updateItem(String itemIDThatWasEdited, int positionToEdit) {
        Item item = realmItem.where(Item.class).
                equalTo("itemID", itemIDThatWasEdited).
                findFirst();

        itemList.set(positionToEdit, item);
        notifyItemChanged(positionToEdit);
    }

    public void deleteAll(){
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();

        itemList.clear();
        notifyDataSetChanged();
    }

    public void setImage(String category, ViewHolder viewHolder) {
        if (category != null) {
            if (category.equals(context.getString(R.string.food))) {
                viewHolder.ivCategory.setImageResource(R.mipmap.ic_food);
            } else if (category.equals(context.getString(R.string.book))) {
                viewHolder.ivCategory.setImageResource(R.mipmap.ic_book);
            } else if (category.equals(context.getString(R.string.clothing))) {
                viewHolder.ivCategory.setImageResource(R.mipmap.ic_clothing);
            } else if (category.equals(context.getString(R.string.electronics))) {
                viewHolder.ivCategory.setImageResource(R.mipmap.ic_electronics);
            } else if (category.equals(context.getString(R.string.other))) {
                viewHolder.ivCategory.setImageResource(R.mipmap.ic_other);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvCategory;
        private ImageView ivCategory;
        private TextView tvDescription;
        private TextView tvPrice;
        private CheckBox cbPurchased;
        private Button btnDelete;
        private Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ivCategory = itemView.findViewById(R.id.ivIcon);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            cbPurchased = itemView.findViewById(R.id.cbPurchased);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

    }


}
