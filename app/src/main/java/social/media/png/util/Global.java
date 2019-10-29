package social.media.png.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

import social.media.png.User.Member;


public class Global {
    public static String current_user_email;
    public static String current_user_name;
    public static ArrayList<Integer> selected_id = new ArrayList<Integer>();
    public static ArrayList<Member> array_all_members = new ArrayList<Member>();
    public static int current_user_index;
    public static String current_user_photo;

    public static String getToday() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(currentTime);
        String sub_id = String.valueOf(currentTime.getHours())+String.valueOf(currentTime.getMinutes());
        return currentTime.toString();
    }
}
