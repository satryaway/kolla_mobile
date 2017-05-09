package com.jixstreet.kolla.payment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.jixstreet.kolla.BuildConfig;
import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.msaku.MSakuPreference;
import com.jixstreet.kolla.topup.CreditAmount;
import com.jixstreet.kolla.utility.CastUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;
import com.msaku.library.MSakuLib;
import com.msaku.library.MSakuListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by satryaway on 5/9/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_credit_card_payment)
public class CreditCardPaymentFragment extends Fragment implements MSakuListener {
    public static final int ERROR_NONE = 0;
    private static final String CREDIT_AMOUNT = "Credit Amount";

    @ViewById(R.id.first_name_et)
    protected EditText firstNameEt;

    @ViewById(R.id.last_name_et)
    protected EditText lastNameEt;

    @ViewById(R.id.email_et)
    protected EditText emailEt;

    @ViewById(R.id.phone_et)
    protected EditText phoneEt;

    @ViewById(R.id.card_number_et)
    protected EditText cardNumberEt;

    @ViewById(R.id.month_sp)
    protected Spinner monthSp;

    @ViewById(R.id.year_sp)
    protected Spinner yearSp;

    @ViewById(R.id.address_et)
    protected EditText addressEt;

    @ViewById(R.id.city_et)
    protected EditText cityEt;

    @ViewById(R.id.postal_code_et)
    protected EditText postalCodeEt;

    @ViewById(R.id.cvv_et)
    protected EditText cvvEt;

    @ViewById(R.id.dummy_wrapper)
    protected FrameLayout dummyWrapper;
    private CreditAmount creditAmount;

    public static CreditCardPaymentFragment newInstance(CreditAmount creditAmount) {
        Bundle args = new Bundle();

        CreditCardPaymentFragment fragment = new CreditCardPaymentFragment_();
        args.putString(CREDIT_AMOUNT, CastUtils.toString(creditAmount));
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        if (!BuildConfig.DEBUG) ViewUtils.setVisibility(dummyWrapper, View.GONE);

        creditAmount = CastUtils.fromString(getArguments().getString(CREDIT_AMOUNT), CreditAmount.class);
        initSpinners();
        MSakuLib.initlib(this, true, BuildConfig.M_SAKU_APIKEY);
    }

    private void initSpinners() {
        initSpinner(Seeder.getMonths(), monthSp, null);
        initSpinner(Seeder.getYears(), yearSp, null);
    }

    private void initSpinner(List<String> collections, Spinner spinner,
                             AdapterView.OnItemSelectedListener itemListener) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, collections);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(itemListener);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void registercb(int errorCode, String errorMessage, String session, String cardHash, String cardRsa, String s4) {
        if (errorCode == ERROR_NONE) {
            MSakuLib.paycard(getContext(),
                    R.layout.otpbrowser,R.id.webview,cardRsa, session, "jakarta@msaku.me",
                    "CC TRX INFO","1234567890123456", "MIDDEV0000001", "bni", "25000", cardHash,
                    "deec9713-6c96-4e48-aade-ecad25a0840c", true);


            MSakuLib.paycard(getContext(),
                    R.layout.otpbrowser,
                    R.id.webview,
                    cardRsa,
                    session,
                    MSakuPreference.CUSTOMER_ID,
                    generatePaymentInfo(creditAmount),
                    "",
                    MSakuPreference.OPERATOR,
                    "",
                    creditAmount.nominal,
                    cardHash,
                    MSakuPreference.CLIENT_KEY,
                    true
                    );
        } else {
            DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), errorMessage);
        }
    }

    private String generatePaymentInfo(CreditAmount creditAmount) {
        return getContext().getString(R.string.buy_s_kolla_credit, creditAmount.kolla_credit);
    }

    @Override
    public void paymentcb(int i, String s, String s1, String s2) {

    }

    @Click(R.id.set_dummy_tv)
    protected void setDummyData() {
        ViewUtils.setTextView(firstNameEt, "Budiman");
        ViewUtils.setTextView(lastNameEt, "Nugroho");
        ViewUtils.setTextView(emailEt, "Budiman.Nugroho@gmail.com");
        ViewUtils.setTextView(phoneEt, "081234567890");
        ViewUtils.setTextView(cardNumberEt, "4811111111111114");
        ViewUtils.setTextView(addressEt, "Jalan Rasuna Said kav 50");
        ViewUtils.setTextView(cityEt, "Jakarta");
        ViewUtils.setTextView(postalCodeEt, "12345");
        ViewUtils.setTextView(cvvEt, "123");
        monthSp.setSelection(11);
        yearSp.setSelection(4);
    }

    @Click(R.id.pay_tv)
    protected void pay() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Activity.TELEPHONY_SERVICE);
        String imsi = manager.getSubscriberId();

        String month = String.valueOf(monthSp.getSelectedItemPosition() + 1);
        if (month.length() < 2)
            month = "0" + month;

        String year = Seeder.getYears().get(yearSp.getSelectedItemPosition());
        year = year.substring(2, 4);

        String errorMessage = MSakuLib.regcard(getActivity(),
                imsi,
                ViewUtils.getTextFromEditText(firstNameEt),
                ViewUtils.getTextFromEditText(lastNameEt),
                ViewUtils.getTextFromEditText(emailEt),
                ViewUtils.getTextFromEditText(phoneEt),
                ViewUtils.getTextFromEditText(cardNumberEt),
                month,
                year,
                ViewUtils.getTextFromEditText(addressEt),
                ViewUtils.getTextFromEditText(cityEt),
                ViewUtils.getTextFromEditText(postalCodeEt),
                ViewUtils.getTextFromEditText(cvvEt)
        );
        if (errorMessage != null) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), errorMessage);
        }
    }
}
