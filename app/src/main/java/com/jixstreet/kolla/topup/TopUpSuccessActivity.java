package com.jixstreet.kolla.topup;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.BookingParameterView;
import com.jixstreet.kolla.booking.BookingParameterView_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_top_up_success)
public class TopUpSuccessActivity extends AppCompatActivity {
    public static int requestCode = ActivityUtils.getRequestCode(TopUpSuccessActivity.class, "1");

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.params_wrapper)
    protected LinearLayout paramsWrapper;

    @ViewById(R.id.credit_amount_tv)
    protected TextView creditAmountTv;

    @ViewById(R.id.credit_price_tv)
    protected TextView creditPriceTv;

    private TopUp topUp;

    @AfterViews
    protected void onViewsCreated() {
        ViewUtils.setToolbar(this, toolbar);

        topUp = ActivityUtils.getParam(this, TopUp.paramKey, TopUp.class);
        if (topUp != null) {
            setValue();
            makeViews(buildParams());
        }
    }

    private void setValue() {
        ViewUtils.setTextView(creditAmountTv, topUp.creditAmount.kolla_credit +
                (!topUp.creditAmount.given_bonus.equals("0") ? "+" + topUp.creditAmount.given_bonus : ""));
        ViewUtils.setTextView(creditPriceTv, FormatUtils.formatCurrency(topUp.transaction.price_final));
    }

    private ArrayList<Pair<String, String>> buildParams() {
        ArrayList<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>(getString(R.string.trx_no), topUp.transaction.trxid));
        params.add(new Pair<>(getString(R.string.trx_time), topUp.topUpSuccessInformation.transaction_time));
        params.add(new Pair<>(getString(R.string.payment_type), topUp.creditAmount.payment_type));
        params.add(new Pair<>(getString(R.string.approval_code), topUp.topUpSuccessInformation.approval_code));

        return params;
    }

    private void makeViews(ArrayList<Pair<String, String>> pairs) {
        paramsWrapper.removeAllViews();
        int i = 0;
        for (Pair<String, String> pair : pairs) {
            BookingParameterView bookingParameterView = BookingParameterView_.build(this);
            if (i == 0) bookingParameterView.addMaxLines(20);
            bookingParameterView.addValue(pair.first, pair.second);
            paramsWrapper.addView(bookingParameterView);
            i++;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Click(R.id.back_tv)
    protected void goBack() {
        onBackPressed();
    }
}
