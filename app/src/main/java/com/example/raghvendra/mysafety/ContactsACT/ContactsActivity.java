package com.example.raghvendra.mysafety.ContactsACT;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.raghvendra.mysafety.Data.DetailsContract;
import com.example.raghvendra.mysafety.Data.GroupdetailsDBHelper;
import com.example.raghvendra.mysafety.Data.PersonDetailsDBHelper;
import com.example.raghvendra.mysafety.MainActivity;
import com.example.raghvendra.mysafety.R;

public class ContactsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    private CustomCursorAdapter mAdapter;
    private static final int TASK_LOADER_ID = 0;
    private SQLiteDatabase mDb;
    RecyclerView mRecyclerView;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomCursorAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id= (int) viewHolder.itemView.getTag();

                String stringId=Integer.toString(id);
                int tasksDeleted;
                GroupdetailsDBHelper dbHelper = new GroupdetailsDBHelper(ContactsActivity.this);
                final SQLiteDatabase db= dbHelper.getWritableDatabase();
                tasksDeleted=db.delete(DetailsContract.GroupDetails.TABLE_NAME,"_id=?",new String[]{stringId});

                Toast.makeText(ContactsActivity.this, "ROW ID Successfully deleted" + tasksDeleted, Toast.LENGTH_LONG).show();
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null, ContactsActivity.this);




            }
        }).attachToRecyclerView(mRecyclerView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = ContactsActivity.this;

                Intent ContactformActivity = new Intent(context, ContactForm.class);
                startActivity(ContactformActivity);
                finish();
            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);

                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {

                    GroupdetailsDBHelper dbHelper = new GroupdetailsDBHelper(ContactsActivity.this);
                    mDb = dbHelper.getReadableDatabase();
                    mCursor = mDb.query(DetailsContract.GroupDetails.TABLE_NAME,
                            null, null, null, null, null, null);
                    return mCursor;
                } catch (Exception e) {

                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

            mAdapter.swapCursor(null);


    }
}




















