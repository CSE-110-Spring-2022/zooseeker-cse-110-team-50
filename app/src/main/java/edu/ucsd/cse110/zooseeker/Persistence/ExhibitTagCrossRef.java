package edu.ucsd.cse110.zooseeker.Persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"exhibitId", "tagId"}, indices = {@Index("tagId")})
public class ExhibitTagCrossRef {

    @NonNull
    public String exhibitId;

    @NonNull
    public long tagId;

    public ExhibitTagCrossRef(@NonNull String exhibitId, long tagId) {
        this.exhibitId = exhibitId;
        this.tagId = tagId;
    }
}
