package dao;

import java.util.List;
import java.util.Map;

public interface DaoWithFriendship<T> extends Dao<T> {
    List<T> getFriendsOf(T pet);

    void createFriendship(T entity1, T entity2);

    Map<Long, List<T>> getAllFriends();
}