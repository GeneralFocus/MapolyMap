package com.gaspercloud.gotozeal.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gaspercloud.gotozeal.HomeScreen;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.ui.store.StoreFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class List_Product_Data_Adapter_Category extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    boolean showValues;
    private Handler mHandler;

    private Context mContext;
    private List<Product> productList, productListFiltered;
    //private HomeViewModel homeViewModel;
    private SessionManagement sessionManagement;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_products_layout, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return productListFiltered == null ? 0 : productListFiltered.size();
    }


    /*public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, price, old_price, rating_count, new_signal;
        private ImageView thumbnail;
        RatingBar ratingBar_list;
        //private Button buy_products_services;
        private RelativeLayout view_foreground;

        public MyViewHolder(View view) {
            super(view);
            new_signal = view.findViewById(R.id.new_signal);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            old_price = view.findViewById(R.id.old_price);
            description = view.findViewById(R.id.description);
            rating_count = view.findViewById(R.id.rating_count);
            thumbnail = view.findViewById(R.id.thumbnail);
            ratingBar_list = view.findViewById(R.id.ratingBar_list);
            //buy_products_services = view.findViewById(R.id.buy_products_services);
            view_foreground = view.findViewById(R.id.view_foreground);
        }
    }*/


    public List_Product_Data_Adapter_Category(Context mContext, List<Product> productList, Activity fragment, SessionManagement sessionManagement, boolean showValues) {
        this.mContext = mContext;
        this.productList = productList;
        this.productListFiltered = productList;
        db = FirebaseFirestore.getInstance();
        //this.homeViewModel =
        //      ViewModelProviders.of(fragment).get(HomeViewModel.class);
        this.sessionManagement = sessionManagement;
        this.showValues = showValues;
        mHandler = new Handler();
    }


    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return productList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title_grid, description_grid;
        private ImageView icon_grid_category;
        private CardView cardview_grid;
        private View viewLayout;

        public ItemViewHolder(View view) {
            super(view);
            title_grid = view.findViewById(R.id.title_grid);
            description_grid = view.findViewById(R.id.description_grid);
            icon_grid_category = view.findViewById(R.id.icon_grid_category);
            cardview_grid = view.findViewById(R.id.cardview_grid_category);
            viewLayout = view.findViewById(R.id.view);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

        //final Product productView = productList.get(position);
        final Product productView = productListFiltered.get(position);
        holder.title_grid.setText(productView.getSlug());
        holder.description_grid.setVisibility(View.GONE);


        holder.cardview_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                HomeScreen.catname = productView.getSlug();

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
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {

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
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}