package com.otta.library.movie.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.BDDMockito.given;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.otta.library.movie.entity.Unit;
import com.otta.library.movie.mapper.RentedMovieShowMapper;
import com.otta.library.movie.model.RentedMovieShow;
import com.otta.library.pagination.Page;
import com.otta.library.pagination.factory.PaginationStepsFactory;
import com.otta.library.pagination.model.PaginationSteps;

/**
 * Testes unitários do componente {@link RentedMovieShowPageFactory}.
 * 
 * @author Guilherme
 *
 */
@ExtendWith(MockitoExtension.class)
public class RentedMovieShowPageFactoryTest {
    private static final int CURRENT_PAGE = 0;
    private static final int TOTAL_PAGES = 10;
    private static final int NUMBER_OF_ELEMENTS = 7;
    private static final boolean HAS_PREVIOUS = Boolean.TRUE;
    private static final boolean HAS_NEXT = Boolean.TRUE;
    private static final String NEXT = "next";
    private static final String PREVIOUS = "next";

    @Mock
    private PaginationStepsFactory paginationStepsFactory;
    @Mock
    private RentedMovieShowMapper rentedMovieShowMapper;
    @InjectMocks
    private RentedMovieShowPageFactory rentedMovieShowPageFactory;

    @Mock
    org.springframework.data.domain.Page<Unit> page;
    @Mock
    private Unit unit;
    @Mock
    private RentedMovieShow rentedMovieShow;
    @Mock
    private PaginationSteps paginationSteps;

    @BeforeEach
    protected void setUp() {
        given(page.stream()).willReturn(Stream.of(unit));
        given(rentedMovieShowMapper.map(unit)).willReturn(rentedMovieShow);
        given(page.hasPrevious()).willReturn(HAS_PREVIOUS);
        given(page.hasNext()).willReturn(HAS_NEXT);
        given(page.getTotalPages()).willReturn(TOTAL_PAGES);
        given(page.getNumberOfElements()).willReturn(NUMBER_OF_ELEMENTS);
        given(paginationStepsFactory.create(CURRENT_PAGE, HAS_PREVIOUS, HAS_NEXT)).willReturn(paginationSteps);
        given(paginationSteps.getPrevious()).willReturn(PREVIOUS);
        given(paginationSteps.getNext()).willReturn(NEXT);
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyCurrentPage() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertEquals(CURRENT_PAGE, actualValue.getCurrentPage());
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyTotalPages() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertEquals(TOTAL_PAGES, actualValue.getTotalPages());
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyElementsInPage() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertEquals(NUMBER_OF_ELEMENTS, actualValue.getElementsInPage());
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyPrevious() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertEquals(PREVIOUS, actualValue.getPrevious());
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyNext() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertEquals(NEXT, actualValue.getNext());
    }

    @Test
    public void shouldCorrectlyCreateWithPropertyContent() {
        // given
        // when
        Page<RentedMovieShow> actualValue = rentedMovieShowPageFactory.create(page, CURRENT_PAGE);
        // then
        assertThat(actualValue.getContent(), contains(rentedMovieShow));
    }

}
