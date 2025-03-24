package effectivejava.chapter04.item21;

public class Tresure implements Precious {
    private String name;
    private String owner;

    public Tresure(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(obj instanceof Tresure that))
            return false;
        return owner.equals(that.owner) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return owner.hashCode() * 31 + name.hashCode();
    }

    @Override
    public String toString() {
        return "Tresure{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
