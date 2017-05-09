package com.jixstreet.kolla.topup;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.FormatUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 5/8/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_add_credit)
public class TopUpCreditView extends RelativeLayout {
    @ViewById(R.id.notes_tv)
    protected TextView notesTv;

    @ViewById(R.id.credit_amount_tv)
    protected TextView creditAmountTv;

    @ViewById(R.id.credit_bonus_tv)
    protected TextView creditBonusTv;

    @ViewById(R.id.credit_price_tv)
    protected TextView creditPriceTv;

    private CreditAmount item;
    private Context context;
    private OnCreditClickListener onCreditClickListener;

    public interface OnCreditClickListener {
        void onClick(CreditAmount creditAmount);
    }

    public TopUpCreditView(Context context) {
        super(context);
        this.context = context;
    }

    public void setItem(CreditAmount item) {
        this.item = item;
        setValue();
    }

    private void setValue() {
        ViewUtils.setTextView(creditAmountTv, item.kolla_credit);
        ViewUtils.setTextView(creditBonusTv, context.getString(R.string.bonus_s, item.given_bonus));
        ViewUtils.setTextView(creditPriceTv, FormatUtils.formatCurrency(item.nominal));
        ViewUtils.setTextView(notesTv, item.notes);
        ViewUtils.setVisibility(notesTv, item.notes != null ? VISIBLE : GONE);
    }

    public void setOnCreditClickListener(OnCreditClickListener onCreditClickListener) {
        this.onCreditClickListener = onCreditClickListener;
    }

    @Click(R.id.content_wrapper)
    protected void chooseItem() {
        this.onCreditClickListener.onClick(item);
    }
}
