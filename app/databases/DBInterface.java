package databases;

/* Contract for required DB interactions */
public interface DBInterface<T> {
    /* Return T*/
    T get(String ID);
    /* Return all T's in DB*/
    Iterable<T> getAll();
    /* True if success, False on failure */
    boolean addOrUpdate(T object);
    /* Return T being deleted */
    T delete(String ID);
    /* Delete all data*/
    T deleteAll();
    /* Export all data from the DB */
    void export();
}
