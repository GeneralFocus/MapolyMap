package com.gaspercloud.gotozeal.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.model.ImageProperties;
import com.gaspercloud.gotozeal.model.ProductCategories;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.gaspercloud.gotozeal.ui.store.StoreFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class List_Product_Category_Adapter extends RecyclerView.Adapter<List_Product_Category_Adapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<ProductCategories> productListFiltered;
    private List<ProductCategories> productList;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    private boolean parent_or_sub_categories;
    private HomeViewModel homeViewModel;
    private Handler mHandler;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<ProductCategories> filteredList = new ArrayList<>();
                    for (ProductCategories row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<ProductCategories>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_grid, description_grid;
        private ImageView icon_grid_category;
        private CardView cardview_grid;
        private View viewLayout;

        public MyViewHolder(View view) {
            super(view);
            title_grid = view.findViewById(R.id.title_grid);
            description_grid = view.findViewById(R.id.description_grid);
            icon_grid_category = view.findViewById(R.id.icon_grid_category);
            cardview_grid = view.findViewById(R.id.cardview_grid_category);
            viewLayout = view.findViewById(R.id.view);
        }
    }


    public List_Product_Category_Adapter(Context mContext, List<ProductCategories> productListFiltered, boolean parent_or_sub_categories, Fragment fragment) {
        this.mContext = mContext;
        this.productList = productListFiltered;
        this.productListFiltered = productListFiltered;
        this.parent_or_sub_categories = parent_or_sub_categories;
        this.homeViewModel =
                ViewModelProviders.of(fragment).get(HomeViewModel.class);
        //init Handler
        mHandler = new Handler();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view_products_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductCategories productView = productListFiltered.get(position);
        Spanned name = Html.fromHtml(Html.fromHtml(productView.getName()).toString());
        Spanned desc = Html.fromHtml(Html.fromHtml(productView.getDescription()).toString());
        holder.title_grid.setText(name);
        holder.description_grid.setText(desc);
        Timber.i("parent_or_sub_categories " + parent_or_sub_categories);
        if (!parent_or_sub_categories) {//hide views on SubCategories
            holder.viewLayout.setVisibility(View.GONE);
            holder.description_grid.setVisibility(View.GONE);
        }

        // loading productView cover using Glide library
        ImageProperties imageProperties = productView.getImageProperties();
        if (imageProperties != null && imageProperties.getSrc() != null) {
            Glide.with(mContext).load(imageProperties.getSrc()).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.icon_grid_category);
        } else {
            Glide.with(mContext).load(R.drawable.ic_menu_gallery).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.icon_grid_category);
        }

        holder.cardview_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                homeViewModel.getParentID().setValue(productView.getId());
                //homeViewModel.getParentID().postValue(productView.getId());
                //appBarLayout.setExpanded(false);
                Toast.makeText(mContext, "Saving " + productView.getName() + " as your new preference. Yaay!!!", Toast.LENGTH_SHORT).show();

                // Sometimes, when fragment has huge data, screen seems hanging
                // when switching between navigation menus
                // So using runnable, the fragment is loaded with cross fade effect
                // This effect can be seen in GMail app
                Runnable mPendingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Fragment fragment = new StoreFragment();
                        ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out)
                                .replace(R.id.nav_host_fragment, fragment, mContext.getClass().getName())
                                // Add fragment one in back stack.So it will not be destroyed. Press back menu can pop it up from the stack.
                                .addToBackStack(null)
                                .commitAllowingStateLoss();
                    }
                };// If mPendingRunnable is not null, then add to the message queue
                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }
/*
                //saving this preference for the user, so next time he automatically goes to his last category products
                List<MetaData> metaDataList = customersHome.getMeta_data();//get old data
                Timber.i("customersHome -> " + customersHome.toString());
                MetaData metaData = new MetaData(1000, mContext.getString(R.string.customers_preference), "" + productView.getId());
                if (metaDataList.size() > 1) {
                    metaDataList.set(1, metaData);//update new MetaData
                } else {
                    metaDataList.add(metaData);//add new MetaData
                }
                customersHome.setMeta_data(metaDataList);
                db.collection(mContext.getString(R.string.customers_table)).document(customersHome.getUsername())
                        //.set(customersHome)
                        .set(customersHome, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Timber.i("Set new category preference.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //error setting category preference
                                Timber.e(e);
                            }
                        });*/
            }
        });
    }

    @Override
    public int getItemCount() {
        if (productListFiltered != null && productListFiltered.size() > 0) {
            return productListFiltered.size();
        } else {
            return 0;
        }
    }
}