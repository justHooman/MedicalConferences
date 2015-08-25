package vn.minhtdh.demo.model;

import java.util.ArrayList;

/**
 * Created by minhtdh on 8/23/15.
 */
public class Conference extends  Event {
    public long conferenceId;
    public String title;
    public ArrayList<Participant> participants;
    public ArrayList<Topic> topics;

    public void setConferenceId(final long pConferenceId) {
        conferenceId = pConferenceId;
        eventId = pConferenceId;
    }
}
