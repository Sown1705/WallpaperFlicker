package com.example.mywallpaper.other;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totlaItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (isLoading() || isLastPage()){
            return;
        }else if (firstVisibleItemPosition >=0 && visibleItemCount+ firstVisibleItemPosition >=totlaItemCount){
            loadMoreItem();
        }

    }
    //method goi khi load du lieu tiep
    public abstract  void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
