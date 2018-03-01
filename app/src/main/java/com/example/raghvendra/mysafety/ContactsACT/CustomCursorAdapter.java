package com.example.raghvendra.mysafety.ContactsACT;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.R;

/**
 * Created by raghvendra on 27/2/18.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    private Cursor mCursor;
    private Context mContext;



    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {




      View view= LayoutInflater.from(mContext).inflate(R.layout.task_layout,parent,false);


        return  new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(DetailsContract.GroupDetails._ID);
        int UsernameIndex = mCursor.getColumnIndex(DetailsContract.GroupDetails.USER_NAME);
        int PhoneNoIndex = mCursor.getColumnIndex(DetailsContract.GroupDetails.PHONE_NO);
        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(idIndex);
        String Username = mCursor.getString(UsernameIndex);
        String PhoneNo=mCursor.getString(PhoneNoIndex);
        holder.itemView.setTag(id);
        holder.taskDescriptionView1.setText(Username);
        holder.taskDescriptionView2.setText(PhoneNo);


    }

    @Override
    public int getItemCount()
    {
        if(mCursor==null)
           return 0;
        else {

            return mCursor.getCount();
        }
    }


    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskDescriptionView1;
        TextView taskDescriptionView2;

        public TaskViewHolder(View itemView) {
            super(itemView);
        taskDescriptionView1=(TextView) itemView.findViewById(R.id.taskDescription1);
            taskDescriptionView2=(TextView) itemView.findViewById(R.id.taskDescription2);

        }
    }

}
