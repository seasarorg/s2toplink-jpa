package example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@SecondaryTable(name = "TESTE")
public class TestD {
    
    @Id
    @GeneratedValue
    private Integer id;
    
    private String named;
    
    @Column(table = "TESTE")
    private String namee;
    
    private String namef;
    
    @Version
    private Integer version;
    
    @Transient
    private String test;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getNamef() {
        return namef;
    }

    public void setNamef(String namef) {
        this.namef = namef;
    }

}
