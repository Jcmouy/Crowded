package com.example.crowded.database.converter;

import com.example.crowded.util.FormatDate;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.sql.Timestamp;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Long ConvertLocalDateToTimestamp(LocalDate locadate) {
        ZoneId zoneId = ZoneId.systemDefault();
        return locadate == null ? null : locadate.atStartOfDay(zoneId).toEpochSecond();
    }

    @TypeConverter
    public static Long ConvertLocalDateTimeToTimestamp(LocalDateTime locadatetime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return locadatetime == null ? null : locadatetime.atZone(zoneId).toEpochSecond();
    }

    @TypeConverter
    public static LocalDate ConvertTimestampToLocalDate(Long timestamp) {
        LocalDateTime date = FormatDate.getDateTimeFromTimestamp(timestamp);
        return date == null ? null : date.toLocalDate();
    }

    @TypeConverter
    public static LocalDateTime ConvertTimestampToLocalDateTime(Long timestamp) {
        return FormatDate.getDateTimeFromTimestamp(timestamp);
    }

    @TypeConverter
    public static Long InstantToTimestamp(Instant instant) {
        return instant == null ? null : DateTimeUtils.toSqlTimestamp(instant).getTime();
    }

    @TypeConverter
    public static Instant TimestampToInstant(Long timestamp) {
        Timestamp timeS = Timestamp.valueOf(String.valueOf(timestamp));
        return timestamp == null ? null : DateTimeUtils.toInstant(timeS);
    }
}
