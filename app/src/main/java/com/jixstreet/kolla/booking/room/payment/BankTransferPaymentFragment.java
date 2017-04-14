package com.jixstreet.kolla.booking.room.payment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.booking.Booking;
import com.jixstreet.kolla.utility.CastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by satryaway on 3/28/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_bank_transfer_payment)
public class BankTransferPaymentFragment extends Fragment {
    private static final String BOOKING = "Booking";
    @ViewById(R.id.banking_wrapper)
    protected LinearLayout bankingWrapper;

    private OnPayOtherPayment onPayOtherPayment;
    private Booking booking;

    public static BankTransferPaymentFragment newInstance(Booking booking) {
        Bundle args = new Bundle();
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment_();
        args.putString(BOOKING, CastUtils.toString(booking));
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        booking = CastUtils.fromString(getArguments().getString(BOOKING), Booking.class);
        List<Banking> bankList = Seeder.getBankingList();
        bankingWrapper.removeAllViews();
        for (int i = 0; i < bankList.size(); i++) {
            BankingView bankingView = BankingView_.build(getContext());
            bankingView.setBanking(bankList.get(i));
            bankingWrapper.addView(bankingView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onPayOtherPayment = (OnPayOtherPayment) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException();
        }
    }

    @Click(R.id.pay_tv)
    protected void pay() {
        if (onPayOtherPayment != null && booking != null)
            onPayOtherPayment.onPay(booking);
    }
}
