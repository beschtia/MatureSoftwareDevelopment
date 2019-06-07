package osc.androiddevacademy.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragemnt_movie_grid.*
import osc.androiddevacademy.movieapp.R
import osc.androiddevacademy.movieapp.common.displayToast
import osc.androiddevacademy.movieapp.common.showFragment
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.networking.BackendFactory
import osc.androiddevacademy.movieapp.presentation.MoviesGridPresenter
import osc.androiddevacademy.movieapp.ui.adapters.MoviesGridAdapter

class MoviesGridFragment : Fragment(), MoviesGridContract.View {

    private val SPAN_COUNT = 2

    private val gridAdapter by lazy {
        MoviesGridAdapter(
            { onMovieClicked(it) },
            { onFavoriteClicked(it) })
    }

    private val presenter: MoviesGridContract.Presenter by lazy {
        MoviesGridPresenter(
            BackendFactory.getMovieInteractor()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragemnt_movie_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesGrid.apply {
            adapter = gridAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
        if(gridAdapter.itemCount == 0) setViews()
        initListeners()

    }

    private fun setViews() {
        welcome.visibility = View.VISIBLE
        btn_start.visibility = View.VISIBLE
        nav_view.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.setView(this)
    }

    private fun initListeners() {

        val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_popular ->{
                    requestPopularMovies()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_top ->{
                    requestTopMovies()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite ->{
                    requestFavoriteMovies()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        btn_start.setOnClickListener { onButtonStartClicked() }
    }

    private fun requestFavoriteMovies() {
        presenter.onrequestFavoriteMovies()
    }

    private fun requestPopularMovies() {
        presenter.onRequestPopularMovies()
    }

    private fun requestTopMovies() {
        presenter.onRequestTopMovies()
    }

    override fun onMoviesRecieved(movies: ArrayList<Movie>) {
        gridAdapter.setMovies(movies)
    }

    private fun onButtonStartClicked() {
        nav_view.visibility = View.VISIBLE
        welcome.visibility = View.GONE
        btn_start.visibility = View.GONE
        requestPopularMovies()
    }

    override fun onResponseWentWrong() {
        activity?.displayToast(R.string.response_went_wrong)
    }

    override fun onRequestFailure() {
        activity?.displayToast(R.string.request_failure_text)
    }

    private fun onMovieClicked(movie: Movie) {
        activity?.showFragment(
            R.id.mainFragmentHolder,
            MovieDetailsFragment.getInstance(movie),
            true
        )
    }

    private fun onFavoriteClicked(movie: Movie) {
        if (movie.isFavorite)
            presenter.onFavoriteClickedSave(movie)
        else presenter.onFavoriteClickedDelete(movie)
    }

}