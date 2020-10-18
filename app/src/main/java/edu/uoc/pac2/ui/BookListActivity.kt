package edu.uoc.pac2.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import kotlinx.android.synthetic.main.activity_book_list.*

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

        // Add books data to Firestore [Use once for new projects with empty Firestore Database]
        // FirestoreBookData.addBooksDataToFirestoreDatabase()

        // load Ad
        adView.loadAd(AdRequest.Builder().build())
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
        adapter = BooksListAdapter(emptyList()) { book ->
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra(BookDetailFragment.ARG_ITEM_ID, book.uid)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun initRefreshBooks() {
        val swipeRefreshContainer = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list)

        swipeRefreshContainer.setOnRefreshListener {
            getBooks()
        }
    }

    // Get Books and Update UI
    private fun getBooks() {
        loadBooksFromLocalDb()

        if ((application as MyApplication).hasInternetConnection()){
            val firestoreDatabase = Firebase.firestore
            val docRef = firestoreDatabase.collection("books")

            docRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val books: List<Book> = querySnapshot.documents.mapNotNull { it.toObject(Book::class.java) }

                    saveBooksToLocalDatabase(books)

                    adapter.setBooks(books)
                    findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list).isRefreshing = false
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)

                    findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list).isRefreshing = false
                }
        } else {
            findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_book_list).isRefreshing = false
        }
    }

    // Load Books from Room
    private fun loadBooksFromLocalDb() {
        AsyncTask.execute {
            val books = (application as MyApplication).getBooksInteractor().getAllBooks()
            adapter.setBooks(books)
        }
    }

    // Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        AsyncTask.execute {
            (application as MyApplication).getBooksInteractor().saveBooks(books)
        }
    }
}