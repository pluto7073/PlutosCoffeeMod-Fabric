package ml.pluto7073.plutoscoffee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Utils {

    private Utils(){}

    public static <T> boolean collectionContainsOnlyAll(Collection<T> c1, Collection<T> c2) {
        List<T> l1 = new ArrayList<>(c1);
        for (T t : c2) {
            l1.remove(t);
        }
        List<T> l2 = new ArrayList<>(c2);
        for (T t : c1) {
            l2.remove(t);
        }
        return (l1.size() == 0) && (l2.size() == 0);
    }

}
