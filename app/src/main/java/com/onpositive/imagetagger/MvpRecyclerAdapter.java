package com.onpositive.imagetagger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onpositive.imagetagger.presenters.BasePresenter;

import java.util.HashMap;
import java.util.Map;

public abstract class MvpRecyclerAdapter<M, P extends BasePresenter, VH extends MvpViewHolder> extends RecyclerView.Adapter<VH> {
    protected final Map<Object, P> presenters;

    protected MvpRecyclerAdapter() {
        this.presenters = new HashMap<>();
    }

    @NonNull
    protected P getPresenter(@NonNull M model) {
        return presenters.get(getModelId(model));
    }

    @NonNull
    protected abstract P createPresenter(@NonNull M model);

    @NonNull
    protected abstract Object getModelId(@NonNull M model);

    protected abstract M getItem(int position);

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        super.onViewRecycled(holder);

        holder.unbindPresenter();
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull VH holder) {
        holder.unbindPresenter();
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bindPresenter(getPresenter(getItem(position)));
    }
}
