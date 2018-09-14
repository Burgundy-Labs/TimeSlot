package databases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class DatabaseExporter<T> {
    /*
    *  Needs to be tested - should return a CSV formatted representation of the database
    * */
    public String exportToCSV(DBInterface<T> db, Class dbClass) throws InvocationTargetException, IllegalAccessException {
        Iterable<T> objects = db.getAll();
        StringBuilder sb = new StringBuilder();
        Method[] methods = dbClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0) {
                if (m.getName().startsWith("get")) {
                    sb.append(m.getName().substring(3)).append(',');
                } else if (m.getName().startsWith("is")) {
                    sb.append(m.getName().substring(2)).append(',');
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('\n');
        for (Object o : objects) {
            for (Method m : methods) {
                if ((m.getParameterTypes().length == 0) && (m.getName().startsWith("get") || m.getName().startsWith("is"))) {
                    sb.append(m.invoke(o).toString()).append(',');
                }
            }
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
