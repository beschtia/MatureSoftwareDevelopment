package osc.androiddevacademy.movieapp.persistance

import osc.androiddevacademy.movieapp.App
import osc.androiddevacademy.movieapp.database.MoviesDatabase
import osc.androiddevacademy.movieapp.model.Movie

class FavoriteMoviesRepository: FavoriteMoviesRepositoryContract {
    private var db = MoviesDatabase.getInstance(App.getAppContext())

    private var moviesDao = db.moviesDao()

    override fun save(movie: Movie) {
        moviesDao.addFavoriteMovie(movie)
    }

    override fun delete(movie: Movie) {
        moviesDao.deleteFavoriteMovie(movie)
    }

    override fun getMovies(): List<Movie> = moviesDao.getFavoriteMovies()

}