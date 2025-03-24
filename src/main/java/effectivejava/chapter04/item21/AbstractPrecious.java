package effectivejava.chapter04.item21;

public abstract class AbstractPrecious implements Precious {
    private String owner;

    public AbstractPrecious(String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractPrecious that))
            return false;
        return owner.equals(that.owner);
    }

    public int hashCode() {
        return owner.hashCode();
    }

    public String toString() {
        return "AbstractPrecious{" +
                "owner='" + owner + '\'' +
                '}';
    }
}
