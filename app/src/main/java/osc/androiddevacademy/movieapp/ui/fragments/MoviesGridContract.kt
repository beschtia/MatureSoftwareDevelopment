package osc.androiddevacademy.movieapp.ui.fragments

import osc.androiddevacademy.movieapp.model.Movie

interface MoviesGridContract {

    interface View{

        fun onMoviesRecieved(movies: ArrayList<Movie>)

        fun onRequestFailure()

        fun onResponseWentWrong()

    }

    interface Presenter{

        fun setView(view: View)

        fun onRequestPopularMovies()

        fun onRequestTopMovies()

        fun onrequestFavoriteMovies()

        fun onFavoriteClickedSave(movie: Movie)

        fun onFavoriteClickedDelete(movie: Movie)

    }
}