package reflect.todolistactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import reflect.data.MoodEntryItem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.reflect.manifests.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ToDoListFragment implements the ToDoListContract.View class.
 * Populates into ToDoListActivity content frame
 */
public class MoodEntryListFragment extends Fragment implements MoodEntryListContract.View {

    // Presenter instance for view
    private MoodEntryListContract.Presenter mPresenter;
    // Inner class instance for ListView adapter
    private MoodEntryItemsAdapter mMoodEntryItemsAdapter;

    public MoodEntryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ToDoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodEntryListFragment newInstance() {
        MoodEntryListFragment fragment = new MoodEntryListFragment();
        return fragment;
    }

    /**
     * When fragment is created, create new instance of ToDoItemsAdapter with empty ArrayList and static ToDoItemsListener
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMoodEntryItemsAdapter = new MoodEntryItemsAdapter(new ArrayList<MoodEntryItem>(0), mMoodEntryItemsListener);
    }

    /**
     * start presenter during onResume
     * Ideally coupled with stopping during onPause (not needed here)
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * onCreateView inflates the fragment, finds the ListView and Button, returns the root view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mood_entry_list, container, false);

        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.rvToDoList);
        listView.setAdapter(mMoodEntryItemsAdapter);
        //Find button and set onClickMethod to add a New ToDoItem
        root.findViewById(R.id.btnNewMoodEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewMoodEntryItem();
            }
        });
        return root;
    }

    /**
     * set the presenter for this view
     *
     * @param presenter - the ToDoListContract.presenter instance
     */
    @Override
    public void setPresenter(MoodEntryListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Replace the items in the ToDoItemsAdapter
     *
     * @param moodEntryItemList - List of ToDoItems
     */
    @Override
    public void showToDoItems(List<MoodEntryItem> moodEntryItemList) {
        mMoodEntryItemsAdapter.replaceData(moodEntryItemList);

    }

    /**
     * Create intent to start ACTIVITY TO BE IMPLEMENTED!
     * Start the activity for result - callback is onActivityResult
     *
     * @param item        - Item to be added/modified
     * @param requestCode - Integer code referencing whether a ToDoItem is being added or edited
     */
    @Override
    public void showAddEditToDoItem(MoodEntryItem item, int requestCode) {
        Intent intent = new Intent(getActivity(), AddViewMoodEntryItemActivity.class);
        intent.putExtra("RequestCode", requestCode );
        intent.putExtra("MoodEntryItem", item);
        startActivityForResult(intent,requestCode);
    }

    /**
     * callback function for startActivityForResult
     * Data intent should contain a ToDoItem
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check to make sure data object has a toDoItem
        if(resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("MoodEntryItem")) {
                mPresenter.result(requestCode, resultCode, (MoodEntryItem) data.getSerializableExtra("MoodEntryItem"));
            }
        }
    }

    /**
     * instance of ToDoItemsListener with onToDoItemClick function
     */
    MoodEntryItemsListener mMoodEntryItemsListener = new MoodEntryItemsListener() {
        @Override
        public void onMoodEntryItemClick(MoodEntryItem clickedMoodEntryItem) {
            Log.d("FRAGMENT", "Open ToDoItem Details");
            //Grab item from the ListView click and pass to presenter
            mPresenter.showExistingMoodEntryItem(clickedMoodEntryItem);
        }
    };

    /**
     * Adapter for ListView to show ToDoItems
     */
    private static class MoodEntryItemsAdapter extends BaseAdapter {

        //List of all ToDoItems
        private List<MoodEntryItem> mMoodEntryItems;
        // Listener for onItemClick events
        private MoodEntryItemsListener mItemListener;

        /**
         * Constructor for the adapter
         *
         * @param moodEntryItems    - List of initial items
         * @param itemListener - onItemClick listener
         */
        public MoodEntryItemsAdapter(List<MoodEntryItem> moodEntryItems, MoodEntryItemsListener itemListener) {
            setList(moodEntryItems);
            mItemListener = itemListener;
        }

        /**
         * replace toDoItems list with new list
         *
         * @param moodEntryItems
         */
        public void replaceData(List<MoodEntryItem> moodEntryItems) {
            setList(moodEntryItems);
            notifyDataSetChanged();
        }

        private void setList(List<MoodEntryItem> moodEntryItems) {
            mMoodEntryItems = checkNotNull(moodEntryItems);
        }

        @Override
        public int getCount() {
            return mMoodEntryItems.size();
        }

        @Override
        public MoodEntryItem getItem(int i) {
            return mMoodEntryItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * Get a View based on an index and viewgroup and populate
         *
         * @param i
         * @param view
         * @param viewGroup
         * @return
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.mood_entry_item_layout, viewGroup, false);
            }

            //get the ToDoItem associated with a given view
            //used in the OnItemClick callback
            final MoodEntryItem moodEntryItem = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.tvItemColor);
            titleTV.setBackgroundResource(moodEntryItem.getColor());

            TextView contentTV = (TextView) rowView.findViewById(R.id.etMoodDescription);
            contentTV.setText(moodEntryItem.getContent());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set onItemClick listener
                    mItemListener.onMoodEntryItemClick(moodEntryItem);
                }
            });
            return rowView;
        }
    }

    public interface MoodEntryItemsListener {
        void onMoodEntryItemClick(MoodEntryItem clickedMoodEntryItem);
    }
}