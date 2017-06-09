package com.jixstreet.kolla.event;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.Convert;
import com.jixstreet.kolla.utility.TextUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 6/9/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.view_event_guest_input)
public class EventGuestInputView extends LinearLayout {
    @ViewById(R.id.full_name_et)
    protected EditText fullNameEt;

    @ViewById(R.id.email_et)
    protected EditText emailEt;

    @ViewById(R.id.phone_et)
    protected EditText phoneEt;

    @ViewById(R.id.guest_number_tv)
    protected TextView guestCountTv;

    public EventGuestInputView(Context context) {
        super(context);
    }

    public Guest getGuestDetail() {
        Guest guest = new Guest();
        int filledCount = 0;
        if (ViewUtils.getTextFromEditText(fullNameEt).isEmpty())
            ViewUtils.setError(fullNameEt, getContext().getString(R.string.field_required));
        else {
            guest.full_name = ViewUtils.getTextFromEditText(fullNameEt);
            filledCount++;
        }

        if (!TextUtils.isEmailValid(ViewUtils.getTextFromEditText(emailEt)))
            ViewUtils.setError(emailEt, getContext().getString(R.string.email_not_valid));
        else {
            guest.email = ViewUtils.getTextFromEditText(emailEt);
            filledCount++;
        }

        if (ViewUtils.getTextFromEditText(phoneEt).isEmpty())
            ViewUtils.setError(phoneEt, getContext().getString(R.string.field_required));
        else {
            guest.phone_no = ViewUtils.getTextFromEditText(phoneEt);
            filledCount++;
        }

        return filledCount == 3 ? guest : null;
    }

    public void setForm(int i) {
        ViewUtils.setTextView(guestCountTv, getContext().getString(R.string.guest_s, String.valueOf(i)));
    }
}
