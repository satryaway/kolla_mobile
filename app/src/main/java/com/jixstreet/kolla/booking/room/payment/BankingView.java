package com.jixstreet.kolla.booking.room.payment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 4/7/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_banking_item)
public class BankingView extends LinearLayout {
    private Banking banking;

    @ViewById(R.id.banking_iv)
    protected ImageView bankingIv;

    @ViewById(R.id.banking_name_tv)
    protected TextView bankingNameTv;

    public BankingView(Context context) {
        super(context);
    }

    public void setBanking(Banking banking) {
        this.banking = banking;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadLocalImage(getContext(), bankingIv, banking.img);
        bankingNameTv.setText(banking.accountNo);
    }
}
