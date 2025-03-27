package effectivejava.chapter05.item28;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Chooser2<T> {

    private final T[] choiceArray;

    @SuppressWarnings("unchecked")
    public Chooser2(Collection<T> choices) {
        //this.choiceArray = choices.toArray();   // compile error
        this.choiceArray = (T[]) choices.toArray(); // unchecked cast
    }

    public T choose() {
        Random random = ThreadLocalRandom.current();
        return choiceArray[random.nextInt(choiceArray.length)];
    }
}
