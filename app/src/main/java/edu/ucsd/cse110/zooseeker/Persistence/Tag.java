package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tag", indices = {@Index(value = {"name"}, unique = true)})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    @SerializedName("tag_id")
    @NonNull
    public long tagId;

    @NonNull
    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + tagId +
                ", name='" + name + '\'' +
                '}';
    }
}
