package osc.androiddevacademy.movieapp.presentation

import osc.androiddevacademy.movieapp.common.RESPONSE_OK
import osc.androiddevacademy.movieapp.model.Movie
import osc.androiddevacademy.movieapp.model.response.MoviesResponse
import osc.androiddevacademy.movieapp.networking.interactors.MovieInteractor
import osc.androiddevacademy.movieapp.persistance.FavoriteMoviesRepository
import osc.androiddevacademy.movieapp.ui.fragments.MoviesGridContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesGridPresenter(private val interactor: MovieInteractor):
    MoviesGridContract.Presenter {

    private lateinit var view: MoviesGridContract.View

    private val repository = FavoriteMoviesRepository()

    override fun setView(view: MoviesGridContract.View) {
        this.view = view
    }

    override fun onRequestPopularMovies() {
        interactor.getPopularMovies(popularMoviesCallback())
    }

    private fun popularMoviesCallback(): Callback<MoviesResponse> = object : Callback<MoviesResponse>{
        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            view.onRequestFailure()
        }

        override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
            if (response.isSuccessful){
                when (response.code()){
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkResponse(response: Response<MoviesResponse>) {
        response.body()?.movies?.run {
            for (movie in repository.getMovies()){
                val mov = this.find { it.id == movie.id }
                mov?.isFavorite = true
            }
            view.onMoviesRecieved(this)
        }
    }

    override fun onRequestTopMovies() {
        interactor.getTopMovies(topMoviesCallback())
    }

    private fun topMoviesCallback(): Callback<MoviesResponse> = object : Callback<MoviesResponse>{
        override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            view.onRequestFailure()
        }

        override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
            if (response.isSuccessful){
                when (response.code()){
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleSomethingWentWrong() = view.onResponseWentWrong()

    override fun onrequestFavoriteMovies() {
        view.onMoviesRecieved(repository.getMovies() as ArrayList<Movie>)
    }

    override fun onFavoriteClickedSave(movie: Movie) {
        repository.save(movie)
    }

    override fun onFavoriteClickedDelete(movie: Movie) {
        repository.delete(movie)
    }
}