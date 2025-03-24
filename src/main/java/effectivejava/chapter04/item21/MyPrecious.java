package effectivejava.chapter04.item21;

import java.util.Date;

public class MyPrecious extends AbstractPrecious {
    private Date acquisitionDate;

    public MyPrecious(String owner, Date acquisitionDate) {
        super(owner);
        this.acquisitionDate = acquisitionDate;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    @Override
    public String getOrigin() {
        return super.getOrigin();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof MyPrecious that))
            return false;
        return super.equals(obj) && acquisitionDate.equals(that.acquisitionDate);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + acquisitionDate.hashCode();
    }

    @Override
    public String toString() {
        return "MyPrecious{" +
                "acquisitionDate=" + acquisitionDate +
                ", owner='" + getOwner() + '\'' +
                '}';
    }
}
