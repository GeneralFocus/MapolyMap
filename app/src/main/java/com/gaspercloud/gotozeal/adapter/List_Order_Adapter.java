package com.gaspercloud.gotozeal.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.entitiesDB.orderModelDatabase;
import com.gaspercloud.gotozeal.entitiesDB.productDatabase;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import timber.log.Timber;

import static com.gaspercloud.gotozeal.FashionAdd_gotozeal.decodeBase64;

//public class List_Order_Adapter extends RecyclerView.Adapter<List_Order_Adapter.MyViewHolder> {
public class List_Order_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OrderModel> orderModelList;
    private SessionManagement session;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    private HomeViewModel homeViewModel;
    private boolean status;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    /*
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView moreinfo;
        private TextView order_ref_no, order_time, order_title;
        private LottieAnimationView transcompleted, transprocessing;

        public MyViewHolder(View view) {
            super(view);
            moreinfo = view.findViewById(R.id.moreinfo);
            order_ref_no = view.findViewById(R.id.order_ref_no);
            order_title = view.findViewById(R.id.order_title);
            order_time = view.findViewById(R.id.order_time);
            transcompleted = view.findViewById(R.id.transcompleted);
            transprocessing = view.findViewById(R.id.transprocessing);
        }
    }
*/

    public List_Order_Adapter(Context mContext, List<OrderModel> orderModelList, FragmentActivity fragment, boolean status) {
        this.mContext = mContext;
        this.orderModelList = orderModelList;
        db = FirebaseFirestore.getInstance();
        this.status = status;
        this.session = new SessionManagement(mContext);
        homeViewModel =
                ViewModelProviders.of(fragment).get(HomeViewModel.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_orders_layout, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        /*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_orders_layout, parent, false);

        return new MyViewHolder(itemView);*/
    }

    /*@Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderModel orderModel = orderModelList.get(position);

        holder.order_title.setText(orderModel.getId());
        holder.order_ref_no.setText(orderModel.getTransaction_id());
        holder.order_time.setText(getDateFromString(getDateFromString(orderModel.getDate_created())));
        if (orderModel.getStatus().equalsIgnoreCase("completed")) {
            holder.transcompleted.setVisibility(View.VISIBLE);
            holder.transcompleted.playAnimation();
            holder.transprocessing.setVisibility(View.GONE);
        } else {
            holder.transprocessing.setVisibility(View.VISIBLE);
            holder.transprocessing.playAnimation();
            holder.transcompleted.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    private void populateItemRows(final ItemViewHolder holder, int position) {
        final OrderModel orderModel = orderModelList.get(position);
        Timber.i("checching " + orderModel.toString());

        holder.feed_call.setVisibility(View.GONE);
        holder.order_title.setText(orderModel.getId() + "_order");
        holder.order_ref_no.setText(orderModel.getTotal());
        //holder.order_time.setText(getDateFromString(getDateFromString(orderModel.getDate_created())));
        if (orderModel.getStatus().equalsIgnoreCase("false")) {
            holder.order_time.setText("waiting for designer.");
            holder.iconinfo.setImageResource(R.drawable.ic_error_red_24dp);
            holder.moreinfo.setVisibility(View.GONE);
        } else if (orderModel.getStatus().equalsIgnoreCase("true")) {
            holder.order_time.setText("Order approved.");
            holder.iconinfo.setImageResource(R.drawable.ic_check_circle_green_24dp);
            holder.moreinfo.setVisibility(View.VISIBLE);
        } else {
            holder.iconinfo.setImageResource(R.drawable.ic_info_black_24dp);
        }

        Log.i("orderModel", orderModel.getParent_id() + "");
        productDatabase pd = productDatabase.getAppDatabase(mContext);
        Product product = pd.productDAO().getProduct(orderModel.getParent_id());
        //Log.i("product", product + "");
        for (Product pro : pd.productDAO().getAllProduct()) {
            if (pro.getId() == orderModel.getParent_id()) {
                product = pro;
                break;
            }
        }
        holder.title_grid.setText(product.getName());

        Bitmap myBitmapAgain = decodeBase64(product.getImages());
        Glide.with(mContext).load(myBitmapAgain).placeholder(R.drawable.ic_menu_gallery).error(R.drawable.ic_photo_library_red_24dp).into(holder.icon_grid_category);

        holder.cardview_grid_category.setVisibility(View.GONE);
        holder.layout_item_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cardview_grid_category.isShown()) {
                    holder.cardview_grid_category.setVisibility(View.GONE);
                } else {
                    holder.cardview_grid_category.setVisibility(View.VISIBLE);
                }
            }
        });


        if (status) {//Designer
            holder.buy_now.setVisibility(View.VISIBLE);
            if (orderModel.getStatus().equalsIgnoreCase("false")) {
                holder.buy_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are you sure?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        dialog.dismiss();
                                        orderModelDatabase modelDatabase = orderModelDatabase.getAppDatabase(mContext);
                                        orderModel.setStatus("true");
                                        modelDatabase.orderModelDAO().update(orderModel);

                                        holder.buy_now.setText("ORDER APPROVED");
                                        holder.buy_now.setBackgroundColor(mContext.getResources().getColor(R.color.warningColor));
                                        holder.buy_now.setOnClickListener(null);
                                        Toast.makeText(mContext, "Order was approved.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });
                        // Create the AlertDialog object and return it
                        // create alert dialog
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();
                    }
                });
            } else {
                holder.buy_now.setText("ORDER APPROVED");
                holder.buy_now.setBackgroundColor(mContext.getResources().getColor(R.color.warningColor));
            }
        } else {
            holder.feed_call.setVisibility(View.VISIBLE);
            holder.buy_now.setVisibility(View.GONE);
            holder.make_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Use the Builder class for convenient dialog construction

                    if (orderModel.getVersion() == null || orderModel.getVersion().isEmpty()) {
                        Toast.makeText(mContext, "Sorry no number available to call.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i("TAG", orderModel.getVersion());
                    makeCall(orderModel.getVersion());
                }
            });

            holder.add_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Use the Builder class for convenient dialog construction
                    LayoutInflater li = LayoutInflater.from(mContext);
                    View promptsView = li.inflate(R.layout.alert_dialog_popup_layout, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText message = promptsView
                            .findViewById(R.id.message);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            orderModelDatabase modelDatabase = orderModelDatabase.getAppDatabase(mContext);
                                            orderModel.setCustomer_note(message.getText().toString());
                                            modelDatabase.orderModelDAO().update(orderModel);
                                            Toast.makeText(mContext, "Feedback sent successfully.", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });
        }
    }

    private void makeCall(String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mContext.startActivity(intent);
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return orderModelList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView moreinfo, iconinfo, icon_grid_category;
        private TextView order_ref_no, order_time, order_title, title_grid;
        private CardView cardview_grid_category;
        private LinearLayout layout_item_desc, feed_call;
        private TextView buy_now, make_call, add_review;

        public ItemViewHolder(View view) {
            super(view);
            moreinfo = view.findViewById(R.id.moreinfo);
            order_ref_no = view.findViewById(R.id.order_ref_no);
            order_title = view.findViewById(R.id.order_title);
            order_time = view.findViewById(R.id.order_time);
            iconinfo = view.findViewById(R.id.iconinfo);
            cardview_grid_category = view.findViewById(R.id.cardview_grid_category);
            layout_item_desc = view.findViewById(R.id.layout_item_desc);
            icon_grid_category = view.findViewById(R.id.icon_grid_category);
            title_grid = view.findViewById(R.id.title_grid);
            buy_now = view.findViewById(R.id.buy_now);
            make_call = view.findViewById(R.id.make_call);
            add_review = view.findViewById(R.id.add_review);
            feed_call = view.findViewById(R.id.feed_call);
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

    @Override
    public int getItemCount() {
        if (orderModelList != null && orderModelList.size() > 0) {
            return orderModelList.size();
        } else {
            return 0;
        }
    }

}