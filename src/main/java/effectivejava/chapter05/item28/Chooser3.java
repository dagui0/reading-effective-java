package effectivejava.chapter05.item28;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Chooser3<T> {

    private final List<T> choiceList;

    @SafeVarargs
    public Chooser3(T... choices) {
        this.choiceList = List.of(choices);
    }
    public Chooser3(Stream<T> choices) {
        this.choiceList = choices.toList();
    }
    public Chooser3(Collection<T> choices) {
        this(choices.stream());
    }
    public Chooser3(List<T> choices) {
        this.choiceList = List.copyOf(choices);
    }

    public T choose() {
        Random random = ThreadLocalRandom.current();
        return choiceList.get(random.nextInt(choiceList.size()));
    }
}
