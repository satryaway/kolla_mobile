package com.jixstreet.kolla.profile.following;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.dialog.PopUpDialog;
import com.jixstreet.kolla.model.FollowedUser;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.tools.EndlessRecyclerViewScrollListener;
import com.jixstreet.kolla.user.OnUserSelectedListener;
import com.jixstreet.kolla.user.UserListAdapter;
import com.jixstreet.kolla.user.UserPopUpDialog;
import com.jixstreet.kolla.user.UserPopUpDialog_;
import com.jixstreet.kolla.utility.CastUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_following)
public class FollowingFragment extends Fragment implements OnUserSelectedListener,
        SwipeRefreshLayout.OnRefreshListener, UserPopUpDialog.OnUserPopupClickListener {

    private static final String USER = "user";


    private static final int OFFSET = 10;
    private static final int STARTING_PAGE_INDEX = 1;
    private static final String EVENT = "event";

    @ViewById(R.id.list_rv)
    protected RecyclerView listRv;

    @ViewById(R.id.refresh_wrapper)
    protected SwipeRefreshLayout refreshWrapper;

    private EndlessRecyclerViewScrollListener scrollListener;
    private UserListAdapter adapter;
    private FollowedUserJson followedUserJson;
    private UserPopUpDialog userPopUpDialog;
    private PopUpDialog popUpDialog;

    public static FollowingFragment newInstance(UserData userData) {
        Bundle args = new Bundle();
        args.putString(USER, CastUtils.toString(userData));
        FollowingFragment fragment = new FollowingFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        UserData userData = CastUtils.fromString(getArguments().getString(USER), UserData.class);
        if (userData != null) {
            followedUserJson = new FollowedUserJson(getContext(), userData.id);
            initAdapter();
            getData(STARTING_PAGE_INDEX);
        }
    }

    private void initAdapter() {
        adapter = new UserListAdapter(getActivity());
        adapter.setOnUserSelectedListener(this);
        LinearLayoutManager layoutManager = ViewUtils.getLayoutManager(getActivity(), true);
        scrollListener = getScrollListener(layoutManager, OFFSET);
        refreshWrapper.setOnRefreshListener(this);
        ViewUtils.setRecyclerViewDivider(listRv, layoutManager);

        listRv.setClipToPadding(true);
        listRv.setLayoutManager(layoutManager);
        listRv.setItemAnimator(new DefaultItemAnimator());
        listRv.addOnScrollListener(scrollListener);

        listRv.setAdapter(adapter);
    }

    private EndlessRecyclerViewScrollListener getScrollListener(LinearLayoutManager layoutManager, int offset) {
        return new EndlessRecyclerViewScrollListener(layoutManager, offset, STARTING_PAGE_INDEX) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getData(page);
            }
        };
    }

    private void getData(final int page) {
        FollowedUserJson.Request request = new FollowedUserJson.Request();
        request.page = String.valueOf(page);
        followedUserJson.get(request, new OnGetFollowedUser() {
            @Override
            public void onSuccess(FollowedUserJson.Response response) {
                if (page == 1)
                    onGetFollowedUserDone.OnGetFollowedUser(response.data.total);
                refreshWrapper.setRefreshing(false);
                adapter.addItems(getUsers(response.data.data));
            }

            @Override
            public void onFailure(String message) {
                if (refreshWrapper != null && getActivity() != null) {
                    refreshWrapper.setRefreshing(false);
                    DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
                }
            }
        });
    }

    private List<UserData> getUsers(List<FollowedUser> data) {
        List<UserData> list = new ArrayList<>();
        for (FollowedUser followedUser : data) {
            list.add(followedUser.followed_user);
        }

        return list;
    }

    @Override
    public void onRefresh() {
        scrollListener.resetStateWithParams(STARTING_PAGE_INDEX);
        adapter.clearList();
        scrollListener.initScroll(listRv);
        getData(STARTING_PAGE_INDEX);
    }

    @Override
    public void onClick(UserData userData) {
        if (userPopUpDialog != null) userPopUpDialog.removeAllViews();

        userPopUpDialog = UserPopUpDialog_.build(getActivity());
        userPopUpDialog.setUserData(userData);
        userPopUpDialog.setOnUserPopupClickListener(this);

        popUpDialog = new PopUpDialog(userPopUpDialog);
        popUpDialog.show();
    }

    private OnGetFollowedUserDone onGetFollowedUserDone;

    @Override
    public void onViewProfile(UserData userData) {

    }

    @Override
    public void onFollow(UserData userData) {

    }

    public interface OnGetFollowedUserDone {
        void OnGetFollowedUser(String total);
    }

    public void setOnGetFollowedUserDone(OnGetFollowedUserDone onGetFollowedUserDone) {
        this.onGetFollowedUserDone = onGetFollowedUserDone;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popUpDialog != null)
            popUpDialog.dismiss();
    }
}
