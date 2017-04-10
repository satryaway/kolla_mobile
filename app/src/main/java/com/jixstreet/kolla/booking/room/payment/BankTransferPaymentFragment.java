package com.jixstreet.kolla.booking.room.payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 3/28/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_bank_transfer_payment)
public class BankTransferPaymentFragment extends Fragment {

    @ViewById(R.id.banking_wrapper)
    protected LinearLayout bankingWrapper;

    public static BankTransferPaymentFragment newInstance() {
        Bundle args = new Bundle();
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        List<Banking> bankList = Seeder.getBankingList();
        bankingWrapper.removeAllViews();
        for (int i = 0; i < bankList.size(); i++) {
            BankingView bankingView = BankingView_.build(getContext());
            bankingView.setBanking(bankList.get(i));
            bankingWrapper.addView(bankingView);
        }
    }
}
