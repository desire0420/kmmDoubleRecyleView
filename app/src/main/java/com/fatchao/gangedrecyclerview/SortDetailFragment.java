package com.fatchao.gangedrecyclerview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SortDetailFragment extends Fragment implements CheckListener {
    private View mRootView;
    private RecyclerView mRv;
    private ClassifyDetailAdapter1 mAdapter;
    private GridLayoutManager mManager;
    private List<RightBean> mDatas = new ArrayList<>();
    private ItemHeaderDecoration mDecoration;
    private boolean move = false;
    private int mIndex = 0;
    private CheckListener checkListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sort_detail, container, false);
        mRv = (RecyclerView) mRootView.findViewById(R.id.rv);
        mRv.addOnScrollListener(new RecyclerViewListener());
        initPresenter();
        return mRootView;
    }


    private void initPresenter() {
        // showRightPage(1);
        mManager = new GridLayoutManager(getActivity(), 3);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mDatas.get(position).isTitle() ? 3 : 1;
            }
        });
        mRv.setLayoutManager(mManager);
        mAdapter = new ClassifyDetailAdapter1(getActivity(), mDatas);

        mRv.setAdapter(mAdapter);
        mDecoration = new ItemHeaderDecoration(getActivity(), mDatas);
        mRv.addItemDecoration(mDecoration);
        mDecoration.setCheckListener(checkListener);
        initData();
        mAdapter.setOnItemClickLitener(new ClassifyDetailAdapter1.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.w("TAG", "onItemClick");
            }

            @Override
            public void onHeadClick(View view, int position) {
                Log.w("TAG", "onHeadClick");
            }
        });
    }


    private void initData() {
        ArrayList<SortBean.CategoryOneArrayBean> rightList = getArguments().getParcelableArrayList("right");
        for (int i = 0; i < rightList.size(); i++) {
            RightBean head = new RightBean(rightList.get(i).getName());
            //头部设置为true
            head.setTitle(true);
            head.setTitleName(rightList.get(i).getName());
            head.setTag(String.valueOf(i));
            mDatas.add(head);
            List<SortBean.CategoryOneArrayBean.CategoryTwoArrayBean> categoryTwoArray = rightList.get(i).getCategoryTwoArray();
            for (int j = 0; j < categoryTwoArray.size(); j++) {
                RightBean body = new RightBean(categoryTwoArray.get(j).getName());
                body.setTag(String.valueOf(i));
                String name = rightList.get(i).getName();
                body.setTitleName(name);
                mDatas.add(body);
            }

        }

        mAdapter.notifyDataSetChanged();
        mDecoration.setData(mDatas);
    }

    public void setListener(CheckListener listener) {
        this.checkListener = listener;
    }

    public void setData(int n) {
        mIndex = n;
        mRv.stopScroll();
        smoothMoveToPosition(n);
    }


    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRv.scrollToPosition(n);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRv.getChildAt(n - firstItem).getTop();
            Log.d("top---->", String.valueOf(top));
            mRv.scrollBy(0, top);
        } else {
            mRv.scrollToPosition(n);
            move = true;
        }
    }


    @Override
    public void check(int position, boolean isScroll) {
        checkListener.check(position, isScroll);

    }


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRv.getChildCount()) {
                    int top = mRv.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRv.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRv.getChildCount()) {
                    int top = mRv.getChildAt(n).getTop();
                    mRv.scrollBy(0, top);
                }
            }
        }
    }


}
