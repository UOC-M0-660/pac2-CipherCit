package edu.uoc.pac2.data

import androidx.room.*

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

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveBook(book: Book): Long

    @Update
    fun updateBook(book: Book)
}