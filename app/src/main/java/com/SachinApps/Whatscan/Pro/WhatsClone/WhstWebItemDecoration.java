package com.SachinApps.Whatscan.Pro.WhatsClone;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;
import android.view.View;

public class WhstWebItemDecoration extends RecyclerView.ItemDecoration {
    private int GridSize;
    private boolean NeedLeftSpacing = false;
    private int SizeGridSpacingPx;

    public WhstWebItemDecoration(int i, int i2) {
        this.SizeGridSpacingPx = i;
        this.GridSize = i2;
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
        int recyclerView1 = (recyclerView.getWidth() / this.GridSize) - ((int) ((((float) recyclerView.getWidth()) - (((float) this.SizeGridSpacingPx) * ((float) (this.GridSize - 1)))) / ((float) this.GridSize)));
        int view2 = ((LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (view2 < this.GridSize) {
            rect.top = 0;
        } else {
            rect.top = this.SizeGridSpacingPx;
        }

        if (view2 % this.GridSize == 0) {
            rect.left = 0;
            rect.right = recyclerView1;
            this.NeedLeftSpacing = true;
        } else if ((view2 + 1) % this.GridSize == 0) {
            this.NeedLeftSpacing = false;
            rect.right = 0;
            rect.left = recyclerView1;
        } else if (this.NeedLeftSpacing != false) {
            this.NeedLeftSpacing = false;
            rect.left = this.SizeGridSpacingPx - recyclerView1;
            if ((view2 + 2) % this.GridSize == 0) {
                rect.right = this.SizeGridSpacingPx - recyclerView1;
            } else {
                rect.right = this.SizeGridSpacingPx / 2;
            }
        } else if ((view2 + 2) % this.GridSize == 0) {
            this.NeedLeftSpacing = false;
            rect.left = this.SizeGridSpacingPx / 2;
            rect.right = this.SizeGridSpacingPx - recyclerView1;
        } else {
            this.NeedLeftSpacing = false;
            rect.left = this.SizeGridSpacingPx / 2;
            rect.right = this.SizeGridSpacingPx / 2;
        }
        rect.bottom = 0;
    }
}