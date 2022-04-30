package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ExhibitWithTags {
    @Embedded public Exhibit exhibit;
    @Relation(
        parentColumn = "exhibitId",
        entityColumn = "tagId",
        associateBy = @Junction(ExhibitTagCrossRef.class)
    )
    public List<Tag> tags;
}
