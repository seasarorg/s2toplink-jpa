package example.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class TestCId implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7720643743289720128L;

    private Integer id1;
    
    private Integer id2;

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id1 == null) ? 0 : id1.hashCode());
        result = PRIME * result + ((id2 == null) ? 0 : id2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TestCId other = (TestCId) obj;
        if (id1 == null) {
            if (other.id1 != null)
                return false;
        } else if (!id1.equals(other.id1))
            return false;
        if (id2 == null) {
            if (other.id2 != null)
                return false;
        } else if (!id2.equals(other.id2))
            return false;
        return true;
    }
}
