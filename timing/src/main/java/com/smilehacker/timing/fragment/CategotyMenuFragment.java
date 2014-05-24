package com.smilehacker.timing.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.adapter.CategoryListAdapter;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.model.event.CategoryEvent;
import com.smilehacker.timing.model.event.DataChangeEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by kleist on 14-5-24.
 */
public class CategotyMenuFragment extends Fragment {

    private ListView mLvCategory;
    private TextView mTvAdd;
    private TextView mTvAll;
    private TextView mTvUnsigned;
    private CategoryListAdapter mCategoryListAdapter;
    private EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryListAdapter = new CategoryListAdapter(getActivity(), new ArrayList<Category>());
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_menu_category, container, false);
        mLvCategory = (ListView) view.findViewById(R.id.lv_category);
        mTvAdd = (TextView) view.findViewById(R.id.btn_add_category);
        mTvAll = (TextView) view.findViewById(R.id.btn_all);
        mTvUnsigned = (TextView) view.findViewById(R.id.btn_unassigned);

        mLvCategory.setAdapter(mCategoryListAdapter);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
    }

    private void initView() {
        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("New Category")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String categoryName = editText.getText().toString();
                                createCategory(categoryName);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

        mTvAll.setOnClickListener(new OnCategoryClickListener(CategoryEvent.ID_ALL));
        mTvUnsigned.setOnClickListener(new OnCategoryClickListener(CategoryEvent.ID_UNSIGNED));
    }

    public class OnCategoryClickListener implements View.OnClickListener {

        private int categoryId;

        public OnCategoryClickListener(int categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public void onClick(View view) {
            CategoryEvent event = new CategoryEvent();
            event.id = categoryId;
            mEventBus.post(event);
        }
    }

    private void load() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return Category.getAllCategories();
            }

            @Override
            protected void onPostExecute(List<Category> categories) {
                super.onPostExecute(categories);
                mCategoryListAdapter.flush(categories);
            }
        }.execute();
    }

    private void createCategory(String categoryName) {
        if (TextUtils.isEmpty(categoryName)) {
            return;
        }

        Category category = new Category();
        category.name = categoryName;
        category.save();
        load();
    }

    public void onEvent(DataChangeEvent event) {
        if (event.action == DataChangeEvent.ACTION_CATEGORY_DELETED) {
            load();
        }
    }
}
