package com.jixstreet.kolla.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jixstreet.kolla.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 5/17/2017.
 * satryaway@gmail.com
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private final Context context;
    private List<Event> itemList = new ArrayList<>();
    private int lastPosition = -1;
    private EventView.OnEventSelectedListener onEventSelectedListener;

    public EventListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventView view = EventView_.build(parent.getContext());
        view.setOnEventSelectedListener(onEventSelectedListener);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.getView().setItem(itemList.get(position));
        setAnimation(holder.getView(), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItems(List<Event> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearList() {
        this.itemList = new ArrayList<>();
        lastPosition = -1;
        notifyDataSetChanged();
    }

    public void setOnEventSelectedListener(EventView.OnEventSelectedListener onEventSelectedListener) {
        this.onEventSelectedListener = onEventSelectedListener;
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(EventViewHolder holder) {
        holder.getView().clearAnimation();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private EventView eventView;

        public EventViewHolder(EventView itemView) {
            super(itemView);
            this.eventView = itemView;
        }

        public EventView getView() {
            return eventView;
        }
    }
}
