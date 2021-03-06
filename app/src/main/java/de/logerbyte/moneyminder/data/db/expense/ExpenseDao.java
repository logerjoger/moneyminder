package de.logerbyte.moneyminder.data.db.expense;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by logerom on 08.08.18.
 */

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM Expense")
    List<Expense> selectAll();


    // TODO-SW: select all where cashDate is in range
    @Query("SELECT * FROM Expense where cashDate")
    List<Expense> selectAllBetween();

    @Query("SELECT DISTINCT category FROM Expense")
    List<String> selectDistinctCategory();

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insert(Expense expense);

    @Query("DELETE FROM Expense where id = :entryId")
    void delete(long entryId);

    @Update
    void update(Expense expense);

    @Query("SELECT * FROM Expense WHERE category IN (:checkedCategories)")
    List<Expense> expensesWithCategories(String[] checkedCategories);
}
