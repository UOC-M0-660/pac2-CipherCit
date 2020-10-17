package edu.uoc.pac2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Book Dao (Data Access Object) for accessing Book Table functions.
 */

@Dao
interface BookDao {
    @Query("SELECT * FROM Book")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM Book where uid = :id")
    fun getBookById(id: Int): Book?

    @Query("SELECT * FROM Book where title LIKE :titleBook")
    fun getBookByTitle(titleBook: String): Book?

    @Insert
    fun saveBook(book: Book): Long

    @Query("DELETE FROM Book")
    fun deleteAllBooks()
}