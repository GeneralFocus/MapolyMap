package com.gaspercloud.gotozeal.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gaspercloud.gotozeal.HomeScreen;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.adapter.List_Product_Category_Adapter;
import com.gaspercloud.gotozeal.adapter.List_Product_Data_Adapter_Category;
import com.gaspercloud.gotozeal.model.Product;
import com.gaspercloud.gotozeal.model.ProductCategories;
import com.gaspercloud.gotozeal.ui.design.GridSpacingItemDecoration;
import com.gaspercloud.gotozeal.ui.store.StoreViewModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.gaspercloud.gotozeal.ui.design.GridSpacingItemDecoration.dpToPx;


public class HomeFragment extends Fragment implements HomeScreen.OnBackPressed {

    private StoreViewModel storeViewModel;
    private HomeViewModel homeViewModel;

    private Context context;

    //private RecyclerView store_subcategory_list_recycler_view;
    //private ShimmerFrameLayout parentShimmerLayout_store;

    private RecyclerView store_product_list_recycler_view;
    private ShimmerFrameLayout parentShimmerLayout_store_list;

    private List_Product_Category_Adapter sub_productCatAdapter;
    private List<ProductCategories> sub_productCatList;

    private List_Product_Data_Adapter_Category data_productAdapter;
    private List<Product> data_productList;

    boolean hide_empty;
    //private TextView text_shop;
    private RelativeLayout relativeLayout;
    boolean isLoading = false;

    //private View viewDivider;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView = null;
    private SessionManagement sessionManagement;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);//to get SubCategories

        storeViewModel =
                ViewModelProviders.of(this).get(StoreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        /*text_shop = root.findViewById(R.id.text_shop);
        storeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                text_shop.setText(s);
            }
        });*/

  /*      homeViewModel.getParentID().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                HomeFragmentOLD.parentID = integer;
                Timber.i("Store parentID " + integer);
            }
        });
*/

        //parentShimmerLayout_store = root.findViewById(R.id.parentShimmerLayout_store);
        //viewDivider = root.findViewById(R.id.viewDivider);

        //store_subcategory_list_recycler_view = root.findViewById(R.id.store_subcategory_list_recycler_view);
        relativeLayout = root.findViewById(R.id.coordinator_layout);

        parentShimmerLayout_store_list = root.findViewById(R.id.parentShimmerLayout_store_list);
        store_product_list_recycler_view = root.findViewById(R.id.store_product_list_recycler_view);

        sub_productCatList = new ArrayList<>();
        sub_productCatAdapter = new List_Product_Category_Adapter(context, sub_productCatList, false, this);
        sessionManagement = new SessionManagement(context);
        data_productList = new ArrayList<>();
        data_productAdapter = new List_Product_Data_Adapter_Category(context, data_productList, null, sessionManagement, true);

        // horizontal RecyclerView
        // keep grid_view_products_layout.xml width to `wrap_content`
        /*
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        store_subcategory_list_recycler_view.setLayoutManager(mLayoutManager);
        store_subcategory_list_recycler_view.addItemDecoration(new MyDividerItemDecoration(context, LinearLayoutManager.HORIZONTAL, 16));
        store_subcategory_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        store_subcategory_list_recycler_view.setAdapter(sub_productCatAdapter);
        */

        /*RecyclerView.LayoutManager mLayoutManager_data = new LinearLayoutManager(context);
        store_product_list_recycler_view.setLayoutManager(mLayoutManager_data);
        store_product_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        store_product_list_recycler_view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        store_product_list_recycler_view.setAdapter(data_productAdapter);*/

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        store_product_list_recycler_view.setLayoutManager(mLayoutManager);
        store_product_list_recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0, context), true));
        //home_recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, context), true));
        store_product_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        store_product_list_recycler_view.setAdapter(data_productAdapter);

        hide_empty = false;
        System.out.println("DATA DATA: ");
        //prepareProductList(HomeFragmentOLD.parentID, context, hide_empty);
        prepareProductData(-1, context, null, null);
        //initScrollListener();

        return root;
    }
/*
    private void prepareProductList(int parentID, Context context, boolean hide_empty) {
        //home_swiperefresh.setRefreshing(true);
        store_subcategory_list_recycler_view.setVisibility(View.GONE);
        text_shop.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);
        //textView.setVisibility(View.VISIBLE);
        List<Integer> exclude = new ArrayList<>();
        exclude.add(15);//remove Uncategorized Category
        homeViewModel.get_forProductCategories(parentID, context, hide_empty, exclude).observe(this, new Observer<List<ProductCategories>>() {
            @Override
            public void onChanged(List<ProductCategories> productCategories) {
                text_shop.setVisibility(View.VISIBLE);
                store_subcategory_list_recycler_view.setVisibility(View.VISIBLE);
                sub_productCatList.clear();
                Timber.i("test Test " + productCategories.toString());
                sub_productCatList.addAll(productCategories);
                sub_productCatAdapter.notifyDataSetChanged();
                //home_swiperefresh.setRefreshing(false);
                //progressBar.setVisibility(View.GONE);
                //textView.setVisibility(View.GONE);
                // Stopping Shimmer Effect's animation after data is loaded to ListView
                //parentShimmerLayout_store.stopShimmerAnimation();
                //parentShimmerLayout_store.setVisibility(View.GONE);
                if (productCategories.size() <= 0) {
                    viewDivider.setVisibility(View.GONE);
                }
            }
        });
    }
*/

    private void prepareProductData(int category, Context context, final Integer offset, Integer per_page) {
        //home_swiperefresh.setRefreshing(true);
        store_product_list_recycler_view.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);
        //textView.setVisibility(View.VISIBLE);
        System.out.println("JUST DATA: ");

        storeViewModel.get_ProductData(category, context, null, per_page).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productData) {
                System.out.println("JUST DATA: 1");
                store_product_list_recycler_view.setVisibility(View.VISIBLE);
                data_productList.clear();
                data_productList.addAll(productData);

                for (Product p : productData) {
                    System.out.println("DATA: " + p.toString());
                }

                data_productAdapter.notifyDataSetChanged();
                if (offset != null) {
                    store_product_list_recycler_view.scrollToPosition(offset + 1);
                }
                //home_swiperefresh.setRefreshing(false);
                //progressBar.setVisibility(View.GONE);
                //textView.setVisibility(View.GONE);
                // Stopping Shimmer Effect's animation after data is loaded to ListView
                parentShimmerLayout_store_list.stopShimmer();
                parentShimmerLayout_store_list.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void initScrollListener() {
        store_product_list_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == data_productList.size() - 1) {
                        //bottom of list!
                        //loadMore();
                        data_productList.add(null);
                        data_productAdapter.notifyItemInserted(data_productList.size() - 1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    data_productList.remove(data_productList.size() - 1);
                                } catch (Exception e) {
                                    Timber.e(e);
                                }
                                int scrollPosition = data_productList.size();
                                data_productAdapter.notifyItemRemoved(scrollPosition);
                                int offset = scrollPosition;
                                int per_page = offset + 10;
                                prepareProductData(HomeFragmentOLD.parentID, context, offset, per_page);
                            }
                        }, 2000);

                        isLoading = true;
                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //parentShimmerLayout_store.startShimmer();
        parentShimmerLayout_store_list.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        //parentShimmerLayout_store.stopShimmer();
        parentShimmerLayout_store_list.stopShimmer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_screen_fragment, menu);
        /*
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Timber.i("onQueryTextChange" + newText);
                    data_productAdapter.getFilter().filter(newText);

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Timber.i("onQueryTextSubmit" + query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        */
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            //case R.id.app_bar_search:
            // Not implemented here
            //return false;
            default:
                break;
        }
        //searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onBackPressed_StoreFragment() {
        // close search view on back button pressed
        /*if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        }*/
        return false;
    }


}
