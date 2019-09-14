package com.triple.trackme.Activity.CompletedTracks;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewPaddingDecoration extends RecyclerView.ItemDecoration {

    private final static int PADDING_IN_DIPS = 10;
    private final int padding;

    RecyclerViewPaddingDecoration(@NonNull final Context context) {
        super();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        padding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_IN_DIPS, metrics);
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view,
                               final RecyclerView parent, final RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }
        if (itemPosition == 0) {
            outRect.top = padding;
        }

        final RecyclerView.Adapter adapter = parent.getAdapter();
        if ((adapter != null) && (itemPosition == adapter.getItemCount() - 1)) {
            outRect.bottom = padding;
        }
    }
}
