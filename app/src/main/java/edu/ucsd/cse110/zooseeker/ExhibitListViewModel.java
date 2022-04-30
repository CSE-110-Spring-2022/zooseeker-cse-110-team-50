package edu.ucsd.cse110.zooseeker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExhibitListViewModel extends AndroidViewModel {
    private LiveData<List<ExhibitListItem>> exhibitListItems;
    private final ExhibitListItemDao exhibitListItemDao;

    public ExhibitListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitDatabase db = ExhibitDatabase.getSingleton(context);
        this.exhibitListItemDao = db.exhibitListItemDao();
    }

    public LiveData<List<ExhibitListItem>> getTodoListItems() {
        if (exhibitListItems == null) {
            loadUsers();
        }
        return this.exhibitListItems;
    }

    private void loadUsers() {
        this.exhibitListItems = this.exhibitListItemDao.getAllLive();
    }

    /**
     * A high-level operation that hides the database operation
     */
    public void toggleCompleted(ExhibitListItem todoListItem) {
        todoListItem.checked = !todoListItem.checked;
        exhibitListItemDao.update(todoListItem);
    }
}
