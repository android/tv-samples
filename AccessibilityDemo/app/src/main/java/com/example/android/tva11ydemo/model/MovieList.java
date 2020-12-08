package com.example.android.tva11ydemo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MovieList provides a list of {@code Movie} with categories.
 */
public final class MovieList {

    private static final String MOVIE_CATEGORIES[] = {
        "Category Zero",
        "Category One",
        "Category Two",
        "Category Three",
    };

    private static final String TITLES[] = {
        "Movie I",
        "Movie II",
        "Movie III",
        "Movie IV",
        "Movie V",
        "Movie VI",
        "Movie VII",
        "Movie VIII",
        "Movie IX",
        "Movie X",
    };

    private static List<Movie> movieList;
    private static List<String> categoryList;
    private static long count = 0;

    static {
        setupMovies();
    }

    public static List<Movie> getMovieList() {
        return movieList;
    }

    public static List<String> getCategories() {
        return categoryList;
    }

    private static void setupMovies() {
        categoryList = Arrays.asList(MOVIE_CATEGORIES);

        movieList = new ArrayList<>();

        for (int index = 0; index < TITLES.length; ++index) {
            movieList.add(buildMovieInfo(TITLES[index]));
        }
    }

    private static Movie buildMovieInfo(String title) {
        Movie movie = new Movie();
        movie.setId(count++);
        movie.setTitle(title);
        return movie;
    }
}
