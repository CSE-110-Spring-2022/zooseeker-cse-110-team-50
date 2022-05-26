package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaceWithTags {
    @Embedded public Place place;
    @Relation(
        parentColumn = "place_id",
        entityColumn = "tag_id",
        associateBy = @Junction(PlaceTagCrossRef.class)
    )
    public List<Tag> tags;
}
