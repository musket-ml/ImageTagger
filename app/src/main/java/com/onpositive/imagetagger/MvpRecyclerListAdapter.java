package com.onpositive.imagetagger;

import com.onpositive.imagetagger.presenters.BasePresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class MvpRecyclerListAdapter<M extends Comparable<M>, P extends BasePresenter, VH extends MvpViewHolder<P>> extends MvpRecyclerAdapter<M, P, VH> {
    protected final List<M> models;

    public MvpRecyclerListAdapter() {
        models = new ArrayList<>();
    }

    public void clearAndAddAll(Collection<M> data) {
        models.clear();
        presenters.clear();
        for (M item : data) {
            addInternal(item);
        }
        Collections.sort(models);
        notifyDataSetChanged();
    }

    public void addAll(Collection<M> data) {
        for (M item : data) {
            addInternal(item);
        }
        Collections.sort(models);
        int addedSize = data.size();
        int oldSize = models.size() - addedSize;
        notifyItemRangeInserted(oldSize, addedSize);
    }

    public void addItem(M item) {
        addInternal(item);
        notifyItemInserted(models.size());
    }

    public void updateItem(M item) {
        Object modelId = getModelId(item);
        //Swap the model
        int position = getItemPosition(item);
        if (position >= 0) {
            models.remove(position);
            models.add(position, item);
        }
        //Swap the presenter
        P existingPresenter = presenters.get(modelId);
        if (existingPresenter != null) {
            existingPresenter.setModel(item);
        }
        if (position >= 0) {
            notifyItemChanged(position);
        }
    }

    public void removeItem(M item) {
        int position = getItemPosition(item);
        if (position > 0) {
            models.remove(item);
        }
        presenters.remove(getModelId(item));
        if (position > 0) {
            notifyItemRemoved(position); //TODO move this line to the first condition and test it.
        }
    }

    private int getItemPosition(M item) {
        Object modelId = getModelId(item);
        int position = -1;
        for (int i = 0; i < models.size(); i++) {
            M model = models.get(i);
            if (getModelId(model).equals(modelId)) {
                position = i;
                break;
            }
        }
        return position;
    }


    private void addInternal(M item) {
        models.add(item);
        presenters.put(getModelId(item), createPresenter(item));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public abstract List<M> getSelectedItems();

    @Override
    protected M getItem(int position) {
        return models.get(position);
    }
}
