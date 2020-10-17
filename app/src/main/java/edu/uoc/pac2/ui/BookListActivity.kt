package edu.uoc.pac2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.data.FirestoreBookData

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // swipe refresh
        initRefreshBooks()

        // TODO: Add books data to Firestore [Use once for new projects with empty Firestore Database]
        // FirestoreBookData.addBooksDataToFirestoreDatabase()
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun initRefreshBooks() {
        val swipeRefreshContainer = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list)

        swipeRefreshContainer.setOnRefreshListener {
            getBooks()
        }
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {
        val firestoreDatabase = Firebase.firestore
        val docRef = firestoreDatabase.collection("books")

        docRef.get()
            .addOnSuccessListener { querySnapshot ->
                val books: List<Book> = querySnapshot.documents.mapNotNull { it.toObject(Book::class.java) }
                adapter = BooksListAdapter(books)
                findViewById<RecyclerView>(R.id.book_list).adapter = adapter
                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list).isRefreshing = false
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)

                findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list).isRefreshing = false
            }
    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb() {
        throw NotImplementedError()
    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        throw NotImplementedError()
    }
}