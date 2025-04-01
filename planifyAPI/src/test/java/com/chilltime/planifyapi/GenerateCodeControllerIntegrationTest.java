package com.chilltime.planifyapi;

import com.chilltime.planifyapi.entity.Calendar;
import com.chilltime.planifyapi.entity.CalendarCode;
import com.chilltime.planifyapi.repository.CodigoRepository;
import com.chilltime.planifyapi.repository.CalendarRepository;
import com.chilltime.planifyapi.service.SACalendarCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "logging.level.org.springframework=DEBUG",
        "logging.level.com.chilltime=DEBUG"
})
public class GenerateCodeControllerIntegrationTest {

    @Autowired
    private SACalendarCode saCalendarCode;

    @Autowired
    private CodigoRepository calendarCodeRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    public void testCreateCode() {
        Calendar calendar = new Calendar();
        calendar.setName("Test Calendar");
        calendarRepository.save(calendar);

        CalendarCode code = new CalendarCode();
        code.setCalendar(calendar);
        TContext context = saCalendarCode.createCode(code);

        CalendarCode savedCode = (CalendarCode) context.getData();

        assertNotNull(savedCode);
        assertNotNull(savedCode.getCode());
        assertFalse(savedCode.isUsed());
        assertEquals(calendar.getId(), savedCode.getCalendar().getId());
    }
}
