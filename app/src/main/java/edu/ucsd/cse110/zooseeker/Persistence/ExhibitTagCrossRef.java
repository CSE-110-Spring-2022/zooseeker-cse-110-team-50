package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Entity;

@Entity(primaryKeys = {"exhibitId", "tagId"})
public class ExhibitTagCrossRef {
    public String exhibitId;
    public long tagId;
}
