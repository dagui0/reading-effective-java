package effectivejava.chapter04.item15;

import lombok.Getter;
import lombok.Setter;

/**
 * Example class demonstrating the use of access modifiers.
 * This class has private, protected, and public fields,
 * along with their respective getters and setters.
 */
@Getter
@Setter
public class DocumentExample {

    /**
     * Private field that can only be accessed within this class.
     * It is not visible to subclasses or other classes.
     */
    private int value;

    /**
     * Protected field that can be accessed by subclasses and classes
     * in the same package. It is not visible to classes in different packages.
     */
    protected int protectedValue;

    /**
     * Public field that can be accessed from anywhere.
     * It is visible to all classes, regardless of package.
     */
    public int publicValue;

}
