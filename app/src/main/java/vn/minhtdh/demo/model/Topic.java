package vn.minhtdh.demo.model;

import java.util.ArrayList;

/**
 * Created by minhtdh on 8/23/15.
 */
public class Topic extends Event {
    public long topicId;
    public String title;
    public String content;
    public long conferenceId;
    public int status;
    public ArrayList<User> hosts;

    public void setTopicId(final long pTopicId) {
        topicId = pTopicId;
        eventId = pTopicId;
    }
}
