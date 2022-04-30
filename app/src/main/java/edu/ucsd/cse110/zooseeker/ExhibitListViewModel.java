package edu.ucsd.cse110.zooseeker;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExhibitListViewModel extends AndroidViewModel {
    private LiveData<List<ExhibitListItem>> exhibitListItems;

}
