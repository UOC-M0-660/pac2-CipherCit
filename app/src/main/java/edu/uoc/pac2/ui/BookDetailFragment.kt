package edu.uoc.pac2.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook()
    }


    // Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook() {
        arguments?.let {
            val bookId = it.getInt(ARG_ITEM_ID)

            AsyncTask.execute {
                val book = (activity!!.application as MyApplication).getBooksInteractor().getBookById(bookId)
                if (book != null){
                    activity?.runOnUiThread {
                        initUI(book)
                    }
                }
            }
        }
    }

    // Init UI with book details
    private fun initUI(book: Book) {
        view?.findViewById<TextView>(R.id.item_detail_author)?.text = book.author
        view?.findViewById<TextView>(R.id.item_detail_date)?.text = book.publicationDate
        view?.findViewById<TextView>(R.id.item_detail_description)?.text = book.description

        activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = book.title

        Picasso.get().load(book.urlImage).into(view?.findViewById(R.id.item_detail_image))
    }

    // TODO: Share Book Title and Image URL
    private fun shareContent(book: Book) {
        throw NotImplementedError()
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}