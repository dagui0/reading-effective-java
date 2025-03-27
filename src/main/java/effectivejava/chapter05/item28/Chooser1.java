package effectivejava.chapter05.item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser1 {

    private final Object[] choiceArray;

    public Chooser1(Collection<?> choices) {
        this.choiceArray = choices.toArray();
    }

    public Object choose() {
        Random random = ThreadLocalRandom.current();
        return choiceArray[random.nextInt(choiceArray.length)];
    }
}
