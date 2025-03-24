package effectivejava.chapter04.item21;

public interface Precious {

    default String getOrigin() {
        return Precious.class.getSimpleName();
    }

    default String getWhatIs() {
        return getClass().getSimpleName();
    }

    String getOwner();

    void setOwner(String owner);
}
