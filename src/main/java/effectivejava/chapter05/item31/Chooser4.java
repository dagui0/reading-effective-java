package effectivejava.chapter05.item31;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Chooser4<T> {

    private final List<? extends T> choiceList;

    public Chooser4(Stream<? extends T> choices) {
        this.choiceList = choices.toList();
    }

    public Chooser4(Collection<? extends T> choices) {
        this(choices.stream());
    }

    public Chooser4(List<? extends T> choices) {
        this.choiceList = List.copyOf(choices);
    }

    @SafeVarargs
    public Chooser4(T... choices) {
        this.choiceList = List.of(choices);
    }

    public T choose() {
        Random random = ThreadLocalRandom.current();
        return choiceList.get(random.nextInt(choiceList.size()));
    }
}
