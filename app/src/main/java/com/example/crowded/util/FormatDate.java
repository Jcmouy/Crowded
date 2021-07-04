package com.example.crowded.util;

import android.app.Activity;
import android.content.Context;

import com.example.crowded.R;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDate {
    private Context context;
    private Activity activity;

    public FormatDate(Context context, final WeakReference<Activity> mainReference) {
        this.context = context;
        this.activity = mainReference.get();
    }

    public Date formatDateFromDB(String textDate) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = inputFormat.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Date formatDateCalendar(String textDate) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = inputFormat.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public String formatDateToDB(Date dateSelected) {
        String dateString = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = inputFormat.format(dateSelected);
        return dateString;
    }

    public String dateToString(Date date) {
        String formattedDate = "";
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM");
        formattedDate = outputFormat.format(date);

        return formattedDate;
    }

    public String formatLocalDateToString (LocalDate locaDate) {
        LocalDate localDate = LocalDate.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        return localDate.format(formatter);
    }

    public String formatLocalDateTimeToString (LocalDateTime locaDateTime) {
        LocalDateTime localDateTime = LocalDateTime.now();//For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    public String messageSentDateProper(String sentDate) {
        String properDate = "";
        Calendar cal = Calendar.getInstance();
        Date todayDate = new Date();
        cal.setTime(todayDate);
        String[] dateDH = sentDate.split(" ");
        int todayMonth = cal.get(Calendar.MONTH) + 1;
        int todayDay = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        // Si date hh:mm dd/mm
        String[] date = dateDH[1].split("/");
        String[] dateHM = dateDH[0].split(":");

        if (todayMonth == Integer.parseInt(date[1]) && todayDay == Integer.parseInt(date[0])) {
            properDate = activity.getResources().getString(R.string.date_today) + " " + dateHM[0] + ":" + dateHM[1];
            // 06 11 17 12:28 AM
        } else if (todayMonth == Integer.parseInt(date[1]) && (todayDay - 1) == Integer.parseInt(date[0])) {
            properDate = activity.getResources().getString(R.string.date_yesterday) + " " + dateHM[0] + " " + dateHM[1];
        } else {
            properDate = date[0] + "/" + date[1] + "/" + year;
        }
        return properDate;
    }

    public String toCharacterMonth(int month) {
        if (month == 1) return activity.getResources().getString(R.string.date_january);
        else if (month == 2) return activity.getResources().getString(R.string.date_february);
        else if (month == 3) return activity.getResources().getString(R.string.date_march);
        else if (month == 4) return activity.getResources().getString(R.string.date_april);
        else if (month == 5) return activity.getResources().getString(R.string.date_may);
        else if (month == 6) return activity.getResources().getString(R.string.date_june);
        else if (month == 7) return activity.getResources().getString(R.string.date_july);
        else if (month == 8) return activity.getResources().getString(R.string.date_august);
        else if (month == 9) return activity.getResources().getString(R.string.date_september);
        else if (month == 10) return activity.getResources().getString(R.string.date_october);
        else if (month == 11) return activity.getResources().getString(R.string.date_november);
        else return activity.getResources().getString(R.string.date_december);
    }

    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0)
            return null;
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId );
    }
}
