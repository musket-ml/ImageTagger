package com.onpositive.imagetagger.type_converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class ImageConverter {
    @TypeConverter
    public Long fromLastModified(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public Date toLastModified(Long timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return new Date(timestamp);
        }
    }
}
