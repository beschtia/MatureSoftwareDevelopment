package osc.androiddevacademy.movieapp.persistance

import osc.androiddevacademy.movieapp.model.Movie


interface FavoriteMoviesRepositoryContract {
    fun save(movie: Movie)
    fun delete(movie: Movie)
    fun getMovies(): List<Movie>
}