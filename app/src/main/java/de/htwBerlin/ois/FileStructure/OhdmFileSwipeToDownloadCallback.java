package de.htwBerlin.ois.FileStructure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


public class OhdmFileSwipeToDownloadCallback extends ItemTouchHelper.SimpleCallback
{
    private final ColorDrawable redBackground;
    private final ColorDrawable greenBackground;
    private OhdmFileRecyclerAdapter mAdapter;
    private ColorDrawable actualIColor;

    public OhdmFileSwipeToDownloadCallback(OhdmFileRecyclerAdapter adapter)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        actualIColor = new ColorDrawable(Color.GREEN);
        redBackground = new ColorDrawable(Color.RED);
        greenBackground = new ColorDrawable(Color.GREEN);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT)
        {
            mAdapter.downloadTask(position);
        }
        else if (direction == ItemTouchHelper.RIGHT)
        {
            // mAdapter.deleteTask(position);
        }

    }


    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 0;


        // Swiping to the right
        if (dX > 0)
        {
            actualIColor = redBackground;
            actualIColor.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }
        // Swiping to the left
        else if (dX < 0)
        {
            actualIColor = greenBackground;
            actualIColor.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,

                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else
        {
            actualIColor.setBounds(0, 0, 0, 0);
        }
        actualIColor.draw(canvas);

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}