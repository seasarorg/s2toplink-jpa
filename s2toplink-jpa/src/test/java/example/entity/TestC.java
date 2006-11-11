package example.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;

@Entity(name = "Test_C")
public class TestC {

    @EmbeddedId
    private TestCId id;
    
    private String name;

    @Version
    private Integer version;


    public TestCId getId() {
        return id;
    }

    public void setId(TestCId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
}
