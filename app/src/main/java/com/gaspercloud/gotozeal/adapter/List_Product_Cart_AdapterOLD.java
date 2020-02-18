package com.gaspercloud.gotozeal.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.model.CartModel;
import com.gaspercloud.gotozeal.model.CartModelOLD;
import com.gaspercloud.gotozeal.model.ImageProperties;
import com.gaspercloud.gotozeal.ui.actionProductActivity.SingleProductActivity;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.formatnumbers;
import static com.gaspercloud.gotozeal.Constants.nextActivity;

public class List_Product_Cart_AdapterOLD extends RecyclerView.Adapter<List_Product_Cart_AdapterOLD.MyViewHolder> {

    private Context mContext;
    private List<CartModelOLD> cartProductList;
    private SessionManagement session;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    private HomeViewModel homeViewModel;
    private boolean is_Cart_Wishlist;
    //private float all_cost;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_cartlist, deletecard;
        private LinearLayout layout_item_desc;
        private TextView cart_prtitle;
        private TextView cart_prprice;
        private TextView cart_prcount;
        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            image_cartlist = view.findViewById(R.id.image_cartlist);
            deletecard = view.findViewById(R.id.deletecard);
            layout_item_desc = view.findViewById(R.id.layout_item_desc);
            cart_prtitle = view.findViewById(R.id.cart_prtitle);
            cart_prprice = view.findViewById(R.id.cart_prprice);
            cart_prcount = view.findViewById(R.id.cart_prcount);
        }
    }


    public List_Product_Cart_AdapterOLD(Context mContext, List<CartModelOLD> cartProductList, FragmentActivity fragment, boolean is_Cart_Wishlist) {
        this.mContext = mContext;
        this.cartProductList = cartProductList;
        db = FirebaseFirestore.getInstance();
        this.session = new SessionManagement(mContext);
        homeViewModel =
                ViewModelProviders.of(fragment).get(HomeViewModel.class);
        this.is_Cart_Wishlist = is_Cart_Wishlist;
        //all_cost = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CartModelOLD cartModel = cartProductList.get(position);

        Spanned name = Html.fromHtml(Html.fromHtml(cartModel.getProduct().getName()).toString());
        Spanned price = Html.fromHtml(Html.fromHtml(cartModel.getProduct().getPrice_html()).toString());
        String[] split_price = price.toString().split(" ");
        holder.cart_prtitle.setText(name);
        if (split_price.length > 1 && !split_price[0].isEmpty()) {
            holder.cart_prcount.setText(mContext.getString(R.string.cart_general_cost, split_price[1], cartModel.getNo_of_items()));
        } else {
            holder.cart_prcount.setText(mContext.getString(R.string.cart_general_cost, split_price[0], cartModel.getNo_of_items()));
        }
        final float totalCost = cartModel.getNo_of_items() * Float.parseFloat(cartModel.getProduct().getPrice());
        //String amount2decimalPlcaes = String.format(Locale.ENGLISH, "%.2f", totalCost);
        //holder.cart_prprice.setText(mContext.getString(R.string.cart_total_cost, mContext.getString(R.string.gotozeal_item), amount2decimalPlcaes));
        holder.cart_prprice.setText(mContext.getString(R.string.cart_total_cost, mContext.getString(R.string.gotozeal_item), formatnumbers(totalCost)));
        //all_cost += totalCost;

        // loading cartModel cover using Glide library
        List<ImageProperties> imageProperties = cartModel.getProduct().getImages();
        if (imageProperties != null) {
            Glide.with(mContext).load(imageProperties.get(0).getSrc()).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.image_cartlist);
        } else {
            Glide.with(mContext).load(R.drawable.ic_menu_gallery).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.image_cartlist);
        }

        final String Cart_Wishlist;
        if (is_Cart_Wishlist) {
            //means it is on cart view
            Cart_Wishlist = mContext.getString(R.string.customers_cart);
        } else {
            //on wishlist view
            Cart_Wishlist = mContext.getString(R.string.customers_wishlist);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity(mContext, SingleProductActivity.class, "product", cartModel.getProduct());
            }
        });

        holder.deletecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(mContext.getString(R.string.customers_table)).document(session.getUsername())
                        .collection(Cart_Wishlist).document(String.valueOf(cartModel.getProduct().getId()))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //use mutableLivedata to refresh layOut
                                db.collection(mContext.getString(R.string.customers_table)).document(session.getUsername())
                                        .collection(Cart_Wishlist)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && task.getResult() != null) {
                                                    //all_cost = 0;
                                                    homeViewModel.getMutableLiveCartModel().setValue(task.getResult().toObjects(CartModelOLD.class));
                                                    Toasty.info(mContext, mContext.getString(R.string.customers_cart_removed), Toast.LENGTH_SHORT, true).show();
                                                } else {
                                                    Timber.e(task.getException());
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Timber.e(e);
                                    }
                                });
                                //End Referesh Cart List
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Timber.e(e);
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartProductList != null && cartProductList.size() > 0) {
            return cartProductList.size();
        } else {
            return 0;
        }
    }

}