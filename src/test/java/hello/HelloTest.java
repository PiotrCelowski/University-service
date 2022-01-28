package hello;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    Hello hello = new Hello();

    @Test
    public void testSum() {
        //given

        //when
        int result = hello.sumTwoNumbers(2,2);

        //then
        assertEquals(4, result);
    }

}