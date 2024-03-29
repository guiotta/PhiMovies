package com.otta.movies.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.otta.movies.movie.entity.Movie;
import com.otta.movies.movie.factory.MovieShowPageFactory;
import com.otta.movies.movie.mapper.MovieMapper;
import com.otta.movies.movie.model.MovieInformation;
import com.otta.movies.movie.model.MovieSearchInformation;
import com.otta.movies.movie.model.MovieShow;
import com.otta.movies.movie.repository.MovieRepository;
import com.otta.movies.pagination.Page;
import com.otta.movies.pagination.model.PageEndpoint;

/**
 * {@link Service} contendo a lógica para manipular as operações sobre os Filmes.
 * @author Guilherme
 *
 */
@Service
public class MovieService {
    private static final int DEFAULT_ELEMENTS_QUANTITY = 10;

    private final MovieRepository movieRepository;
    private final MovieShowPageFactory movieShowPageFactory;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieShowPageFactory movieShowPageFactory,
            MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieShowPageFactory = movieShowPageFactory;
        this.movieMapper = movieMapper;
    }

    @Transactional
    public MovieInformation saveMovie(MovieInformation movieInformation) {
        Movie movie = movieMapper.map(movieInformation);

        movieRepository.save(movie);
        return movieInformation;
    }

    @Transactional(readOnly = true)
    public Page<MovieShow> listMovies(PageEndpoint pageEndpoint, int currentPage) {
        org.springframework.data.domain.Page<Movie> moviesPage =
                movieRepository.findAll(PageRequest.of(currentPage, DEFAULT_ELEMENTS_QUANTITY));

        return movieShowPageFactory.create(moviesPage, pageEndpoint, currentPage);
    }

    @Transactional(readOnly = true)
    public Page<MovieShow> listMovies(PageEndpoint pageEndpoint, MovieSearchInformation movieSearchInformation,
            int currentPage) {
        org.springframework.data.domain.Page<Movie> moviesPage =
                movieRepository.findByName(movieSearchInformation.getMovieName(),
                        PageRequest.of(currentPage, DEFAULT_ELEMENTS_QUANTITY));

        return movieShowPageFactory.create(moviesPage, pageEndpoint, currentPage);
    }

}
