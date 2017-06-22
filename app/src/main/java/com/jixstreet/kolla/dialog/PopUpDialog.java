package com.jixstreet.kolla.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by satryaway on 6/22/2017.
 * satryaway@gmail.com
 */

public class PopUpDialog extends Dialog {

    private final View content;

    public PopUpDialog(View content) {
        super(content.getContext());
        setOwnerActivity((Activity) content.getContext());
        this.content = content;
        ((Inflateable) content).finishInflate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.content);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public interface Inflateable {
        void finishInflate();
    }
}
