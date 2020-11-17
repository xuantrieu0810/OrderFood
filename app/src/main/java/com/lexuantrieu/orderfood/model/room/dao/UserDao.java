package com.lexuantrieu.orderfood.model.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lexuantrieu.orderfood.model.room.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);
//
//    @Insert
//    void insertAll(User... users);
//
//    @Delete
//    void delete(User user);
//
//    @Update
//    void update(User user);

    @Insert(onConflict = REPLACE)
    void insertNote(User user);

    @Query("SELECT * FROM user")
    Flowable<List<User>> getListUser();

    @Query("DELETE FROM user WHERE user.id=:id")
    Single<Integer> deleteUserById(int id);

    @Query("DELETE FROM user WHERE user.id=:id")
    Single<Integer> deleteAllUser(int id);
}
