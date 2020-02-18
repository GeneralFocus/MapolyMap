package com.gaspercloud.gotozeal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.model.CartModelOLD;
import com.gaspercloud.gotozeal.model.ImageProperties;
import com.gaspercloud.gotozeal.model.ProductOLD;
import com.gaspercloud.gotozeal.ui.actionProductActivity.SingleProductActivity;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static android.widget.ImageView.ScaleType.MATRIX;
import static com.gaspercloud.gotozeal.Constants.calculatePercentage;
import static com.gaspercloud.gotozeal.Constants.get30daysDateInterval;
import static com.gaspercloud.gotozeal.Constants.getDateFromString;
import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class List_Product_Data_AdapterOLD extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private List<ProductOLD> productList, productListFiltered;
    private HomeViewModel homeViewModel;
    private SessionManagement sessionManagement;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_products_layout, parent, false);
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


    public List_Product_Data_AdapterOLD(Context mContext, List<ProductOLD> productList, Fragment fragment, SessionManagement sessionManagement) {
        this.mContext = mContext;
        this.productList = productList;
        this.productListFiltered = productList;
        db = FirebaseFirestore.getInstance();
        this.homeViewModel =
                ViewModelProviders.of(fragment).get(HomeViewModel.class);
        this.sessionManagement = sessionManagement;
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
        private TextView name, description, price, old_price, rating_count, new_signal;
        private ImageView thumbnail;
        RatingBar ratingBar_list;
        //private Button buy_products_services;
        private RelativeLayout view_foreground;

        public ItemViewHolder(View view) {
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
        final ProductOLD productView = productListFiltered.get(position);
        Date newDate = get30daysDateInterval();
        float regularprice = 0;
        float saleprice = 0;
        try {
            regularprice = Float.parseFloat(productView.getRegular_price());
            saleprice = Float.parseFloat(productView.getSale_price());
        } catch (Exception e) {
            Timber.e(e);
        }
        if (saleprice > 0) {
            //first check if there is discount on ground
            holder.new_signal.setText(mContext.getString(R.string.percent_symbol, calculatePercentage(saleprice, regularprice) + "% off"));
        } else if (newDate.before(getDateFromString(productView.getdate_created()))) {
            //check if  new date is before product creation date, product becomes old after 30 days
            holder.new_signal.setText(mContext.getString(R.string.new_product));
        } else {
            holder.new_signal.setVisibility(View.GONE);
        }
        Spanned name = Html.fromHtml(Html.fromHtml(productView.getName()).toString());
        Spanned desc = Html.fromHtml(Html.fromHtml(productView.getShort_description()).toString());
        Spanned price = Html.fromHtml(Html.fromHtml(productView.getPrice_html()).toString());
        String[] split_price = price.toString().split(" ");
        holder.name.setText(name);
        holder.description.setText(desc);
        if (split_price.length > 1 && !split_price[0].isEmpty()) {
            holder.price.setText(split_price[1]);
            holder.old_price.setText(split_price[0]);
            holder.old_price.setPaintFlags(holder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.price.setText(split_price[0]);
            holder.old_price.setVisibility(View.GONE);
        }

        holder.ratingBar_list.setRating(Float.parseFloat(productView.getAverage_rating()));
        int ratingCount = productView.getRating_count();
        if (ratingCount <= 0) {
            holder.rating_count.setText(mContext.getString(R.string.no_reviews));
        } else {
            String ratingCountplural = ratingCount > 1 ? mContext.getString(R.string.reviews_message) : mContext.getString(R.string.review_message);
            holder.rating_count.setText(mContext.getString(R.string.rating_general, ratingCount, ratingCountplural));
        }

        // loading productView cover using Glide library
        List<ImageProperties> imageProperties = productView.getImages();
        holder.thumbnail.setScaleType(MATRIX);
        if (imageProperties != null) {
            Glide.with(mContext).load(imageProperties.get(0).getSrc()).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.thumbnail);
        } else {
            Glide.with(mContext).load(R.drawable.ic_menu_gallery).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.thumbnail);
        }

        /*holder.buy_products_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "You just clicked a buy button.", Toast.LENGTH_SHORT).show();
            }
        });*/

        holder.view_foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //appBarLayout.setExpanded(false);
                homeViewModel.getParentID().setValue(productView.getId());
                nextActivity(mContext, SingleProductActivity.class, "product", productView);
            }
        });
        //to Automatically add product to purchase cart use longPress listener
        holder.view_foreground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                homeViewModel.getParentID().setValue(productView.getId());
                CartModelOLD cartModel = new CartModelOLD(1, productView);
                db.collection(mContext.getString(R.string.customers_table)).document(sessionManagement.getUsername())
                        .collection(mContext.getString(R.string.customers_cart)).document(String.valueOf(cartModel.getProduct().getId()))
                        .set(cartModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(mContext, mContext.getString(R.string.customers_cart_added, mContext.getString(R.string.customers_cart)), Toast.LENGTH_SHORT, true).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Timber.e(e);
                                Toasty.error(mContext, mContext.getString(R.string.network_error_message), Toast.LENGTH_SHORT, true).show();
                            }
                        });
                return true;
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
                    List<ProductOLD> filteredList = new ArrayList<>();
                    for (ProductOLD row : productList) {

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
                productListFiltered = (ArrayList<ProductOLD>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}