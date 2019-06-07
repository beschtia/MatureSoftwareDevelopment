package osc.androiddevacademy.movieapp.ui.fragments

import osc.androiddevacademy.movieapp.model.Review

interface MovieDetailsContract {

    interface View{

        fun onReviewsRecieved(reviews: List<Review>)

        fun onGetReviewsFailed()

    }

    interface Presenter{

        fun setView(view: View)

        fun onRequestGetReviews(movieId: Int)

    }
}