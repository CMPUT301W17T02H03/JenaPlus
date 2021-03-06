package com.mood.jenaPlus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

import java.io.Serializable;
import java.util.ArrayList;

import com.novoda.merlin.NetworkStatus;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;

import static android.app.Activity.RESULT_OK;

/**
 * Main Fragment for the user mood events activity
 * Created by carrotji on 2017-03-25.
 */

public class MoodListViewActivity extends Fragment implements MPView<MoodPlus>, Connectable, Bindable {
    protected ListView moodListView;
    private AlertDialog.Builder deleteAlertBuilder;

    private ArrayList<Mood> moodArrayList = new ArrayList<Mood>();
    protected ArrayAdapter<Mood> adapter;
    private UserMoodList myMoodList = new UserMoodList();

    private int longClickedItemIndex;
    private static final int DELETE_PERSON_RESULT_CODE = 1;
    private static final int EDIT_PERSON_RESULT_CODE = 2;
    protected MainMPController mpController;

    private MerlinsBeard merlinsBeard;
    protected Merlin merlin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.activity_mood_list,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moodListView = (ListView) getView().findViewById(R.id.listView);

        moodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewMoodActivity.class);
                intent.putExtra("aMood", (Serializable) moodListView.getItemAtPosition(position));
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });


        registerForContextMenu(moodListView);
        moodListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                longClickedItemIndex = position;
                return false;

            }
        });

        merlinsBeard = MerlinsBeard.from(getActivity());
        merlin = new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks().build(getActivity());
        merlin.registerConnectable(this);
        merlin.registerBindable(this);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_following) {
            Intent intent = new Intent(getActivity(), FollowActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(merlinsBeard.isConnected()) {
            OfflineDataController offlineController = MoodPlusApplication.getOfflineDataController();

            UserMoodList tempList = offlineController.loadSavedList(getActivity());


            if(tempList != null) {
                offlineController.SyncOfflineList(getActivity());
            }

        }
        
        mpController = MoodPlusApplication.getMainMPController();
        Participant participant = mpController.getParticipant();
        myMoodList = participant.getUserMoodList();
        moodArrayList = myMoodList.getUserMoodOrderedList();

        adapter = new MoodListAdapter(getActivity(),moodArrayList);
        moodListView.setAdapter(adapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(Menu.NONE, EDIT_PERSON_RESULT_CODE, menu.NONE, "Edit");
        menu.add(Menu.NONE, DELETE_PERSON_RESULT_CODE, menu.NONE, "Delete");
    }

    //Go to edit mood activity if long clicked item
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_PERSON_RESULT_CODE:

                Intent intent = new Intent(getActivity(), EditMoodActivity.class);
                intent.putExtra("editMood", (Serializable) moodListView.getItemAtPosition(longClickedItemIndex));
                intent.putExtra("pos", longClickedItemIndex);
                startActivity(intent);
                break;


            case DELETE_PERSON_RESULT_CODE:


                deleteAlertBuilder = new AlertDialog.Builder(getActivity());
                getActivity().setResult(RESULT_OK);

                deleteAlertBuilder.setMessage("Are you sure you want to delete this Mood Event?");

                // user selects "No" and the Mood Even long clicked will NOT be deleted.
                deleteAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // user selects "Yes" and the Mood Event long clicked will be deleted.
                deleteAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (merlinsBeard.isConnected()) {
                            mpController.deleteMoodParticipant(moodArrayList.get(longClickedItemIndex));

                            adapter.notifyDataSetChanged();


                            OfflineDataController offlineController = MoodPlusApplication.getOfflineDataController();
                            Participant offlineParticipant = offlineController.getOfflineParticipant();
                            UserMoodList offlineMoodList = offlineParticipant.getUserMoodList();


                            UserMoodList offlineList = offlineController.loadSavedList(getContext());

                            if (offlineList == null) {
                                offlineList = new UserMoodList();
                            }

                            offlineList = offlineMoodList;

                            offlineController.saveOfflineList(offlineList, getContext());

                        }
                        else{
                            OfflineDataController offlineController = MoodPlusApplication.getOfflineDataController();
                            Participant offlineParticipant = offlineController.getOfflineParticipant();
                            UserMoodList offlineMoodList = offlineParticipant.getUserMoodList();
                            offlineMoodList.deleteUserMood(moodArrayList.get(longClickedItemIndex));

                            UserMoodList offlineList = offlineController.loadSavedList(getContext());

                            if (offlineList == null) {
                                offlineList = new UserMoodList();
                            }

                            offlineList = offlineMoodList;

                            offlineController.saveOfflineList(offlineList, getContext());

                            adapter.notifyDataSetChanged();

                        }
                    }

                });


                AlertDialog alertDialog = deleteAlertBuilder.create();
                alertDialog.show();

                return false;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void update(MoodPlus moodPlus){

    }

    @Override
    public void onBind(NetworkStatus networkStatus) {
        if (networkStatus.isAvailable()) {
            onConnect();
        }
    }


    @Override
    public void onConnect() {
        Log.i("Debug", "online");

    }


}
