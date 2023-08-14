package academy.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUsers {

    private final List<User> list = new ArrayList<>();

    public JsonUsers(List<User> sourceList, int fromIndex) {
        for (int i = fromIndex; i < sourceList.size(); i++)
            list.add(sourceList.get(i));
    }

    public List<User> getList() {
        return Collections.unmodifiableList(list);
    }
}
