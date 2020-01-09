package local.workstation.mareu.service;

import java.util.List;

import local.workstation.mareu.model.Meeting;

/**
 * Meeting API client
 */
public interface MeetingApiService {
    /**
     * Get all meeting rooms
     * @return list of meeting rooms
     */
    List<String> getRooms();

    /**
     * Add a meeting room
     * @param room meeting room to add
     */
    void addRoom(String room);

    /**
     * Remove a meeting room
     * @param room meeting room to delete
     */
    void delRoom(String room);

    /**
     * Remove all meeting rooms
     *
     * Secure path to delete all meeting rooms
     */
    void delAllRooms();

    /**
     * Get all meetings
     * @return list of meetings
     */
    List<Meeting> getMeetings();

    /**
     * Add a meeting
     * @param meeting meeting to add
     */
    void addMeeting(Meeting meeting) throws MeetingApiServiceException;

    /**
     * Remove a meeting
     * @param idMeeting meeting to delete using the unique identifier
     */
    void delMeeting(Integer idMeeting);
}
