package org.apache.isis.extensions.base.dom.services.calendar;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.applib.annotation.Programmatic;

import lombok.extern.log4j.Log4j2;

@Service
@Named("isisExtBase.calendarService")
@Order(OrderPrecedence.MIDPOINT)
@Primary
@Qualifier("Default")
@Log4j2
public class CalendarService {

    private static final int MONTHS_IN_QUARTER = 3;


    /**
     * @deprecated - use {@link org.apache.isis.applib.services.clock.ClockService#nowAsMillis()}.
     */
    @Deprecated
    @Programmatic
    public long timestamp() {
        return clockService.nowAsMillis();
    }
    
    // //////////////////////////////////////

    @Programmatic
    public LocalDate beginningOfMonth() {
        return beginningOfMonth(clockService.now());
    }

    static LocalDate beginningOfMonth(final LocalDate date) {
        final int dayOfMonth = date.getDayOfMonth();
        return date.minusDays(dayOfMonth-1);
    }

    // //////////////////////////////////////

    @Programmatic
    public LocalDate beginningOfQuarter() {
        final LocalDate date = clockService.now();
        return beginningOfQuarter(date);
    }

    @Programmatic
    public LocalDate beginningOfNextQuarter() {
        final LocalDate date = clockService.now().plusMonths(3);
        return beginningOfQuarter(date);
    }
    
    static LocalDate beginningOfQuarter(final LocalDate date) {
        final LocalDate beginningOfMonth = beginningOfMonth(date);
        final int monthOfYear = beginningOfMonth.getMonthValue();
        final int quarter = (monthOfYear-1)/MONTHS_IN_QUARTER; // 0, 1, 2, 3
        final int monthStartOfQuarter = quarter*MONTHS_IN_QUARTER+1;
        return beginningOfMonth.minusMonths(monthOfYear-monthStartOfQuarter);
    }

    @Inject
    org.apache.isis.applib.services.clock.ClockService clockService;
}
