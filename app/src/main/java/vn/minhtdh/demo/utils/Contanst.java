package vn.minhtdh.demo.utils;

import vn.minhtdh.demo.model.User;

/**
 * Created by minhtdh on 8/23/15.
 */
public class Contanst {
    public static class ParticipantState {
        public static final int PENDING = 0;
        public static final int DECLINE = 1;
        public static final int ACCEPT  = 2;
    }

    public static class UserRole {
        public static final String ADMIN = "admin";

        public static boolean isUserAdmin(User user) {
            return user != null && user.roles != null && user.roles.contains(ADMIN);
        }
    }
}
