package hello;

import org.springframework.stereotype.Component;

@Component
public class Hello {

    public int sumTwoNumbers(int x, int y) {
        return x+y;
    }
}
