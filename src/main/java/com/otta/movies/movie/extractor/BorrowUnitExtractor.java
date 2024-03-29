package com.otta.movies.movie.extractor;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.otta.movies.movie.entity.Borrow;
import com.otta.movies.movie.entity.Unit;

/**
 * Componente para extrair um {@link Optional}<{@link Borrow}> contendo o item que ID passado como parâmetro.
 * @author Guilherme
 *
 */
@Component
public class BorrowUnitExtractor {

    public Optional<Borrow> extract(Unit unit, long borrowId) {
        return unit.getBorrowings().stream()
                .filter(borrow -> borrow.getId() == borrowId)
                .findFirst();
    }
}
