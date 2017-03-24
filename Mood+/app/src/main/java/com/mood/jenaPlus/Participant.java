package com.mood.jenaPlus;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * This is the main participant class which contains a Jest Id for elastic search,
 * a moodlist that contains their own list of moods, a following moodlist, and 2 lists for
 * who they follow and who is following them.
 * @author Cecelia
 * @author Carlo
 */
public class Participant extends User {

    private UserMoodList userMoodList = new UserMoodList();
    private MoodList followingMoodList = new MoodList();
    //private FollowList followingParticipants = new FollowList();
    //private FollowList followersParticipants = new FollowList();
    private FollowList followList = new FollowList();

    @JestId
    private String id;

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId(){
        return id;
    }

    /**
     * Set id.
     *
     * @param id the id
     */
    public void setId(String id){
        this.id = id;
    }


    /*public void addFollowingParticipant(Participant participant) {
        followingParticipants.addToFollowingList(participant);
    }*/

    public boolean hasUserName(String newName) {
        if (newName.equals(this.userName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Instantiates a new Participant.
     *
     * @param userName the user name
     */
    public Participant(String userName) {

        this.userName = userName;
    }

    /**
     * Add new.
     *
     * @param aMood the a mood
     */
    public void addNew(Mood aMood) {
        Mood mood = aMood;
        userMoodList.addUserMood(mood);
    }

    /**
     * Add new mood.
     *
     * @param text        the text
     * @param addLocation the add location
     * @param id          the id
     * @param social      the social
     * @param photo       the photo
     * @param color       the color
     */
    public void addNewMood1(String text, Boolean addLocation, Double latitude, Double longitude, String id,
                           String social, String photo, String color) {
        Mood mood = new Mood(text,addLocation,latitude,longitude,id,social,photo,color);

        mood.setText(text);
        mood.setAddLocation(addLocation);
        mood.setLatitude(latitude);
        mood.setLongitude(longitude);
        mood.setId(id);
        mood.setSocial(social);
        mood.setPhoto(photo);
        mood.setColor(color);

        userMoodList.addUserMood(mood);
    }
    public void addNewMood2(String text, Boolean addLocation, String id,
                            String social, String photo, String color) {
        Mood mood = new Mood(text,addLocation,id,social,photo,color);

        mood.setText(text);
        mood.setAddLocation(addLocation);
        mood.setId(id);
        mood.setSocial(social);
        mood.setPhoto(photo);
        mood.setColor(color);

        userMoodList.addUserMood(mood);
    }

    /**
     * Gets user mood list.
     *
     * @return the user mood list
     */
    public UserMoodList getUserMoodList() {
        return userMoodList;
    }

    /**
     * Sets user mood list.
     *
     * @param userMoodList the user mood list
     */
    public void setUserMoodList(UserMoodList userMoodList) {
        this.userMoodList = userMoodList;
    }

    /**
     * Gets following mood list.
     *
     * @return the following mood list
     */
    public MoodList getFollowingMoodList() {
        return followingMoodList;
    }

    /**
     * Sets following mood list.
     *
     * @param followingMoodList the following mood list
     */
    public void setFollowingMoodList(MoodList followingMoodList) {
        this.followingMoodList = followingMoodList;
    }


    /*public void followingParticipantsAccepted(Participant userName) {
        followingParticipants.followingAccepted(userName);
    }*/


    /*public void followingParticipantsRejected(Participant userName) {
        followingParticipants.followingRejected(userName);
    }*/


//    public void followingParticipantsRequest(Participant userName){
//        followingParticipants.followingRequest(userName);
//    }
//
//    /**
//     * Follower participants accepted.
//     *
//     * @param userName the user name
//     */
//    public void followerParticipantsAccepted(Participant userName) {
//        followersParticipants.followerAccepted(userName);
//    }
//
//    /**
//     * Follower participants rejected.
//     *
//     * @param userName the user name
//     */
//    public void followerParticipantsRejected(Participant userName) {
//        followersParticipants.followerRejected(userName);
//    }
//
//    /**
//     * Follower participants request.
//     *
//     * @param userName the user name
//     */
//    public void followerParticipantsRequest(Participant userName){
//        followersParticipants.followerRequest(userName);
//    }
//
//    /**
//     * Get pending followers array list.
//     *
//     * @return the array list
//     */
//    public ArrayList<Participant> getPendingFollowers(){
//        return followersParticipants.getPendingFollowers();
//    }
//
//    /**
//     * Get followers array list.
//     *
//     * @return the array list
//     */
//    public ArrayList<Participant> getFollowers(){
//        return followersParticipants.getFollowerList();
//    }
//
//    /**
//     * Get following array list.
//     *
//     * @return the array list
//     */
//    public ArrayList<Participant> getFollowing(){
//        return followingParticipants.getFollowingList();
//    }
//
//    /**
//     * Get pending following array list.
//     *
//     * @return the array list
//     */
//    public ArrayList<Participant> getPendingFollowing(){
//        return followingParticipants.getPendingFollowing();
//    }
//
//    /**
//     * Gets followers participants.
//     *
//     * @return the followers participants
//     */
//    public FollowList getFollowersParticipants() {
//        return followersParticipants;
//    }
//
//    /**
//     * Sets followers participants.
//     *
//     * @param followersParticipants the followers participants
//     */
//    public void setFollowersParticipants(FollowList followersParticipants) {
//        this.followersParticipants = followersParticipants;
//    }
//
//    /**
//     * Gets following participants.
//     *
//     * @return the following participants
//     */
//    public FollowList getFollowingParticipants() {
//        return followingParticipants;
//    }
//
//    /**
//     * Sets following participants.
//     *
//     * @param followingParticipants the following participants
//     */
//    public void setFollowingParticipants(FollowList followingParticipants) {
//        this.followingParticipants = followingParticipants;
//    }

    @Override public String toString() {
        return userName;
    }

}
