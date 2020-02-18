package com.gaspercloud.gotozeal.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.adapter.List_Product_Category_Adapter;
import com.gaspercloud.gotozeal.model.ProductCategories;
import com.gaspercloud.gotozeal.ui.design.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.gaspercloud.gotozeal.ui.design.GridSpacingItemDecoration.dpToPx;

public class HomeFragmentOLD extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView home_recycler_view;
    //private ProgressBar progressBar;
    //private TextView textView;
    private ShimmerFrameLayout parentShimmerLayout;
    //private SwipeRefreshLayout home_swiperefresh;
    private List_Product_Category_Adapter productCatAdapter;
    private List<ProductCategories> productCatList;

    Context context;
    public static int parentID;
    boolean hide_empty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getParentID().setValue(0);
        homeViewModel.getParentID().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                parentID = integer;
                Timber.i("Home parentID " + integer);
            }
        });
      /*  textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
                textView.setText(context.getString(R.string.product_loading_message, context.getString(R.string.prov_product_categories)));
            }
        });
        progressBar = root.findViewById(R.id.progressBar);
*/
        parentShimmerLayout = root.findViewById(R.id.parentShimmerLayout);
        home_recycler_view = root.findViewById(R.id.home_recycler_view);
        //home_swiperefresh = root.findViewById(R.id.home_swiperefresh);


        productCatList = new ArrayList<>();
        productCatAdapter = new List_Product_Category_Adapter(context, productCatList, true, this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        home_recycler_view.setLayoutManager(mLayoutManager);
        home_recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0, context), true));
        //home_recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, context), true));
        home_recycler_view.setItemAnimator(new DefaultItemAnimator());
        home_recycler_view.setAdapter(productCatAdapter);

        //postVparentID = 0;//pass zero to fetch main parents
        hide_empty = false;
        prepareProductList(parentID, context, hide_empty);

        /*home_swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parentID = 0;//pass zero to fetch main parents
                hide_empty = false;
                prepareProductList(parentID, context, hide_empty);
            }
        });*/
        return root;
    }

    private void prepareProductList(int parentID, Context context, boolean hide_empty) {
        //home_swiperefresh.setRefreshing(true);
        home_recycler_view.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);
        //textView.setVisibility(View.VISIBLE);
        List<Integer> exclude = new ArrayList<>();
        exclude.add(15);//remove Uncategorized Category
        homeViewModel.get_forProductCategories(parentID, context, hide_empty, exclude).observe(this, new Observer<List<ProductCategories>>() {
            @Override
            public void onChanged(List<ProductCategories> productCategories) {
                home_recycler_view.setVisibility(View.VISIBLE);
                productCatList.clear();
                productCatList.addAll(productCategories);
                productCatAdapter.notifyDataSetChanged();
                //home_swiperefresh.setRefreshing(false);
                //progressBar.setVisibility(View.GONE);
                //textView.setVisibility(View.GONE);
                // Stopping Shimmer Effect's animation after data is loaded to ListView
                parentShimmerLayout.stopShimmer();
                parentShimmerLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        parentShimmerLayout.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        parentShimmerLayout.stopShimmer();
    }
}