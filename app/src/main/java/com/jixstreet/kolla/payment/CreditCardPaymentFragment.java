package com.jixstreet.kolla.payment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
import com.jixstreet.kolla.booking.BookingSuccessActivity;
import com.jixstreet.kolla.booking.BookingSuccessActivity_;
import com.jixstreet.kolla.msaku.MSakuCcData;
import com.jixstreet.kolla.msaku.MSakuSessionData;
import com.jixstreet.kolla.msaku.MSakuSessionJson;
import com.jixstreet.kolla.msaku.MSakuSuccessfulTransaction;
import com.jixstreet.kolla.msaku.OnGetMSakuSession;
import com.jixstreet.kolla.topup.CreditAmount;
import com.jixstreet.kolla.topup.TopUp;
import com.jixstreet.kolla.topup.TopUpSuccessActivity;
import com.jixstreet.kolla.topup.TopUpSuccessActivity_;
import com.jixstreet.kolla.topup.TopUpSuccessInformation;
import com.jixstreet.kolla.utility.ActivityUtils;
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
    private MSakuSessionJson sessionJson;
    private boolean isRegistered = false;

    private MSakuCcData mSakuCcData;
    private MSakuSessionData mSakuSessionData;
    private MSakuSessionJson.Response mSakuResponse;
    private TopUp topUp;

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
        creditAmount.payment_type = getContext().getString(R.string.credit_card);
        sessionJson = new MSakuSessionJson(getContext());
        topUp = new TopUp();
        topUp.creditAmount = creditAmount;

        initSpinners();
        MSakuLib.initlib(this, BuildConfig.DEBUG, BuildConfig.M_SAKU_APIKEY);
        getData();
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

    private void getData() {
        if (sessionJson == null) return;

        sessionJson.getData(new OnGetMSakuSession() {
            @Override
            public void onSuccess(MSakuSessionJson.Response response) {
                if (response.data != null) {
                    //TODO : Fill all form
                    isRegistered = true;
                    mSakuResponse = response;
                    fillForms(response);
                }
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }

    private void fillForms(MSakuSessionJson.Response response) {
        if (response == null) return;

        MSakuCcData mSakuCcData = response.data.reg_data;
        if (mSakuCcData == null) return;

        ViewUtils.setTextIntoEditText(firstNameEt, mSakuCcData.first_name);
        ViewUtils.setTextIntoEditText(lastNameEt, mSakuCcData.last_name);
        ViewUtils.setTextIntoEditText(emailEt, mSakuCcData.email);
        ViewUtils.setTextIntoEditText(phoneEt, mSakuCcData.phone);
        ViewUtils.setTextIntoEditText(cardNumberEt, mSakuCcData.cc_number);
        setMonth(mSakuCcData.exp_month);
        setYear(mSakuCcData.exp_year);
        ViewUtils.setTextIntoEditText(addressEt, mSakuCcData.address);
        ViewUtils.setTextIntoEditText(cityEt, mSakuCcData.city);
        ViewUtils.setTextIntoEditText(postalCodeEt, mSakuCcData.zip);
        ViewUtils.setTextIntoEditText(cvvEt, mSakuCcData.cvv);

        firstNameEt.setEnabled(false);
        lastNameEt.setEnabled(false);
        emailEt.setEnabled(false);
        phoneEt.setEnabled(false);
        cardNumberEt.setEnabled(false);
        monthSp.setEnabled(false);
        yearSp.setEnabled(false);
        addressEt.setEnabled(false);
        cityEt.setEnabled(false);
        postalCodeEt.setEnabled(false);
        cvvEt.setEnabled(false);
    }

    private void setYear(String exp_year) {
        List<String> years = Seeder.getYears();
        String year = "";
        for (int i = 0; i < years.size()-1; i++) {
            year =  years.get(i).substring(2, 4);
            if (exp_year.equals(year)) {
                yearSp.setSelection(i);
            }
        }
    }

    private void setMonth(String exp_month) {
        int month = Integer.valueOf(exp_month);
        monthSp.setSelection(month - 1);
    }

    private void registerCard() {
        String errorMessage = MSakuLib.regcard(getActivity(),
                getImsi(),
                ViewUtils.getTextFromEditText(firstNameEt),
                ViewUtils.getTextFromEditText(lastNameEt),
                ViewUtils.getTextFromEditText(emailEt),
                ViewUtils.getTextFromEditText(phoneEt),
                ViewUtils.getTextFromEditText(cardNumberEt),
                getMonth(),
                getYear(),
                ViewUtils.getTextFromEditText(addressEt),
                ViewUtils.getTextFromEditText(cityEt),
                ViewUtils.getTextFromEditText(postalCodeEt),
                ViewUtils.getTextFromEditText(cvvEt)
        );

        if (errorMessage != null) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), errorMessage);
        } else {
            mSakuCcData = new MSakuCcData(
                    getImsi(),
                    ViewUtils.getTextFromEditText(firstNameEt),
                    ViewUtils.getTextFromEditText(lastNameEt),
                    ViewUtils.getTextFromEditText(emailEt),
                    ViewUtils.getTextFromEditText(phoneEt),
                    ViewUtils.getTextFromEditText(cardNumberEt),
                    getMonth(),
                    getYear(),
                    ViewUtils.getTextFromEditText(addressEt),
                    ViewUtils.getTextFromEditText(cityEt),
                    ViewUtils.getTextFromEditText(postalCodeEt),
                    ViewUtils.getTextFromEditText(cvvEt)
            );
        }
    }

    private String getYear() {
        String year = Seeder.getYears().get(yearSp.getSelectedItemPosition());
        return year.substring(2, 4);
    }

    private String getImsi() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getSubscriberId();
    }

    private String getMonth() {
        String month = String.valueOf(monthSp.getSelectedItemPosition() + 1);
        if (month.length() < 2)
            month = "0" + month;

        return month;
    }

    private void payCard(MSakuSessionJson.Response mSakuResponse) {
        if (mSakuResponse == null) return;

        MSakuSessionJson.Response.Data data = mSakuResponse.data;
        MSakuLib.paycard(getContext(),
                R.layout.otpbrowser,
                R.id.webview,
                data.cc_data.card_rsa,
                data.cc_data.session,
                data.operator_data.customer_id,
                generatePaymentInfo(creditAmount),
                data.operator_data.mtrx_id,
                data.operator_data.operator_mid,
                data.operator_data.bank,
                creditAmount.nominal,
                data.cc_data.card_hash,
                data.operator_data.client_key,
                true
        );
    }

    private void saveSessionData(MSakuSessionData mSakuSessionData) {
        MSakuSessionJson.Request request = new MSakuSessionJson.Request();
        request.sessionData = mSakuSessionData;
        request.ccData = mSakuCcData;
        sessionJson.saveData(request, new OnGetMSakuSession() {
            @Override
            public void onSuccess(MSakuSessionJson.Response response) {
                if (response.data != null) {
                    mSakuResponse = response;
                    payCard(mSakuResponse);
                } else {
                    DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), response.message);
                }
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }

    private String generatePaymentInfo(CreditAmount creditAmount) {
        return getContext().getString(R.string.buy_s_kolla_credit, creditAmount.kolla_credit);
    }

    @Override
    public void registercb(int errorCode, String errorMessage, String session, String cardHash, String cardRsa, String cardBin) {
        if (errorCode == ERROR_NONE) {
            mSakuSessionData = new MSakuSessionData(session, cardHash, cardRsa, cardBin);
            saveSessionData(mSakuSessionData);
        } else {
            DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), errorMessage);
        }
    }

    @Override
    public void paymentcb(int errorCode, String errorMessage, String s1, String s2) {
        if (errorCode != ERROR_NONE) {
            DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), errorMessage);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity)getContext()).finish();
                }
            }, 1500);
        } else {
            //TODO : GO TO Payment successful page; save

            topUp.topUpSuccessInformation = CastUtils.fromString(s2, TopUpSuccessInformation.class);
            topUp.transaction = CastUtils.fromString(s1, MSakuSuccessfulTransaction.class);
            ActivityUtils.startActivityWParamAndWait(getActivity(), TopUpSuccessActivity_.class,
                    TopUp.paramKey, topUp, TopUpSuccessActivity.requestCode);
        }
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
        if (isRegistered) {
            payCard(mSakuResponse);
        } else {
            registerCard();
        }
    }
}
