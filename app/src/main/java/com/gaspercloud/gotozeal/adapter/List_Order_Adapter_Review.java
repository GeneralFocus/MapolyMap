package com.gaspercloud.gotozeal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.ui.home.HomeViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import timber.log.Timber;

//public class List_Order_Adapter extends RecyclerView.Adapter<List_Order_Adapter.MyViewHolder> {
public class List_Order_Adapter_Review extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public List_Order_Adapter_Review(Context mContext, List<OrderModel> orderModelList, FragmentActivity fragment, boolean status) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_review_layout, parent, false);
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
        Timber.i("checking " + orderModel.toString());
        /*
        String fullname = "";
        customersDatabase pddb = customersDatabase.getAppDatabase(mContext);
        List<Customers> customersArrayList = pddb.customersDAO().getAllUsers();
        for (Customers customers : customersArrayList) {
            if (customers.getId() == orderModel.getCustomer_id()) {
                fullname = customers.getFirst_name() + " " + customers.getLast_name();
                break;
            }
        }
        */
        //holder.title_grid.setText("Tour" + orderModel.getId());
        String version = orderModel.getVersion() == null ? "General" : orderModel.getVersion();
        String order = orderModel.getOrder_key() == null ? " Comment" : " -> " + orderModel.getOrder_key();
        holder.title_grid.setText(version + " " + order);
        //holder.description_grid.setText(fullname + " said: " + orderModel.getCustomer_note());
        holder.description_grid.setText(orderModel.getCustomer_note());
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
        private TextView description_grid, title_grid;

        public ItemViewHolder(View view) {
            super(view);
            title_grid = view.findViewById(R.id.title_grid);
            description_grid = view.findViewById(R.id.description_grid);
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