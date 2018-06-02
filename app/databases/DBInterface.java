package databases;

import java.lang.reflect.InvocationTargetException;

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
    default String export() {
        try {
            return new DatabaseExporter<T>().exportToCSV(this, this.getClass());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
