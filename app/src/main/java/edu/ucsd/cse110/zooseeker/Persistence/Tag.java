package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag", indices = {@Index(value = {"name"}, unique = true)})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public long tagId;
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
