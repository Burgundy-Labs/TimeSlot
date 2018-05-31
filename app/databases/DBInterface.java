package databases;

import java.io.File;

/* Contract for required DB interactions */
public interface DBInterface<T> {
    /* Return T*/
    T get(String ID);
    /* Return all T's in DB*/
    Iterable<T> getAll();
    /* True if success, False on failure */
    boolean addOrUpdate(T object);
    /* Return T being deleted */
    T remove(String ID);
    /* Delete all data*/
    T removeAll();
    /* Export all data from the DB */
    File export();
}
