package com.example.crowded.database.converter;

import com.example.crowded.dto.EnumState;

import androidx.room.TypeConverter;

public class EnumConverter {
    @TypeConverter
    public static EnumState toEnumState(String enumValue) {
        return enumValue == null ? null : EnumState.valueOf(enumValue);
    }

    @TypeConverter
    public static String toString(EnumState state) {
        return state == null ? null : state.name();
    }

}
