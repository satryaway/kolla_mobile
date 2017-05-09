package com.jixstreet.kolla.topup;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.payment.OtherPaymentActivity;
import com.jixstreet.kolla.booking.room.payment.OtherPaymentActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_topup_list)
public class TopUpListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        TopUpCreditView.OnCreditClickListener{
    public static int requestCode = ActivityUtils.getRequestCode(TopUpListActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private TopUpCreditListAdapter adapter;
    private TopUpCreditJson topUpCreditJson;

    @AfterViews
    protected void onViewsChanged() {
        ViewUtils.setToolbar(this, toolbar);
        topUpCreditJson = new TopUpCreditJson(this);
        initAdapter();
        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initAdapter() {
        adapter = new TopUpCreditListAdapter();
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(this, true);
        adapter.setOnCreditClickListener(this);
        refreshWrapper.setOnRefreshListener(this);

        listRv.setNestedScrollingEnabled(false);
        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.setAdapter(adapter);
    }

    private void getData() {
        topUpCreditJson.getList(new OnGetTopUpCreditList() {
            @Override
            public void onSuccess(TopUpCreditJson.Response response) {
                refreshWrapper.setRefreshing(false);
                adapter.setItemList(response.data);
            }

            @Override
            public void onFailure(String message) {
                refreshWrapper.setRefreshing(false);
                DialogUtils.makeSnackBar(CommonConstant.failed, TopUpListActivity.this, message);
            }
        });
    }

    @Override
    public void onRefresh() {
        adapter.clearItems();
        getData();
    }

    @Override
    public void onClick(CreditAmount creditAmount) {
        ActivityUtils.startActivityWParamAndWait(this, OtherPaymentActivity_.class,
                CreditAmount.paramKey, creditAmount, OtherPaymentActivity.requestCode);
    }
}
