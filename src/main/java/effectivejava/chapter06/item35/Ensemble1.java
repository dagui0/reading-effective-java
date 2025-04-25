package effectivejava.chapter06.item35;

public enum Ensemble1 {

    SOLO, DUET, TRIO, QUARTET, QUINTET, SEXTET, SEPTET,
    OCTET, NONET, DECTET, DOUBLE_QUARTET, TRIPLE_QUARTET;

    public int numberOfMusicians() {
        return (this == DOUBLE_QUARTET)? 8: ordinal() + 1;
    }
}
