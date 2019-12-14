package com.otta.library.movie.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.otta.library.movie.entity.Movie;
import com.otta.library.movie.entity.Unit;
import com.otta.library.movie.factory.BorrowFactory;
import com.otta.library.movie.factory.RentedMovieShowPageFactory;
import com.otta.library.movie.get.UnitsToRentGetter;
import com.otta.library.movie.model.BorrowReturnInformation;
import com.otta.library.movie.model.MovieBorrow;
import com.otta.library.movie.model.RentedMovieShow;
import com.otta.library.movie.repository.MovieRepository;
import com.otta.library.movie.repository.UnitRepository;
import com.otta.library.movie.update.UnitBorrowReturnEndSetter;
import com.otta.library.movie.validator.UserValidator;
import com.otta.library.pagination.Page;
import com.otta.library.user.entity.User;
import com.otta.library.user.factory.LoggedUserFactory;

@Service
public class BorrowService {
    private static final int DEFAULT_ELEMENTS_QUANTITY = 10;

    private final UnitRepository unitRepository;
    private final MovieRepository movieRepository;
    private final UnitsToRentGetter unitsToRentGetter;
    private final LoggedUserFactory loggedUserFactory;
    private final RentedMovieShowPageFactory rentedMovieShowPageFactory;
    private final BorrowFactory borrowFactory;
    private final UnitBorrowReturnEndSetter unitBorrowReturnEndSetter;
    private final UserValidator userValidator;

    @Autowired
    public BorrowService(UnitRepository unitRepository, MovieRepository movieRepository,
            UnitsToRentGetter unitsToRentGetter, LoggedUserFactory loggedUserFactory,
            RentedMovieShowPageFactory rentedMovieShowPageFactory, BorrowFactory borrowFactory,
            UnitBorrowReturnEndSetter unitBorrowReturnEndSetter, UserValidator userValidator) {
        this.unitRepository = unitRepository;
        this.movieRepository = movieRepository;
        this.unitsToRentGetter = unitsToRentGetter;
        this.loggedUserFactory = loggedUserFactory;
        this.rentedMovieShowPageFactory = rentedMovieShowPageFactory;
        this.borrowFactory = borrowFactory;
        this.unitBorrowReturnEndSetter = unitBorrowReturnEndSetter;
        this.userValidator = userValidator;
    }

    @Transactional
    public MovieBorrow borrowMovie(MovieBorrow movieBorrow) {
        Movie movie = movieRepository.findById(movieBorrow.getIdMovie()).orElseThrow(IllegalArgumentException::new);
        Unit unit = unitsToRentGetter.get(movie).stream()
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        User user = loggedUserFactory.get()
                .orElseThrow(IllegalStateException::new);
        unit.addBorrow(borrowFactory.create(user, unit.getId()));

        unitRepository.save(unit);
        return movieBorrow;
    }

    @Transactional
    public BorrowReturnInformation returnMovie(BorrowReturnInformation borrowReturnInformation) {
        if (userValidator.validate(borrowReturnInformation.getUserId())) {
            Optional<Unit> optionalUnitUpdated = unitBorrowReturnEndSetter.set(borrowReturnInformation.getBorrowId());

            if (optionalUnitUpdated.isPresent()) {
                unitRepository.save(optionalUnitUpdated.get());
                return borrowReturnInformation;
            }
            throw new IllegalArgumentException("Could not find any Unit to return.");
        }
        throw new IllegalArgumentException("Could not return a Borrow for another User.");
    }

    @Transactional(readOnly = true)
    public Page<RentedMovieShow> listRentsByLoggedUser(int currentPage) {
        User user = loggedUserFactory.get()
                .orElseThrow(() -> new IllegalStateException("Could not find logged User."));
        org.springframework.data.domain.Page<Unit> unitsPage = unitRepository
                .findRentedUnitsByUser(user, PageRequest.of(currentPage, DEFAULT_ELEMENTS_QUANTITY));

        return rentedMovieShowPageFactory.create(unitsPage, currentPage);
    }
}
