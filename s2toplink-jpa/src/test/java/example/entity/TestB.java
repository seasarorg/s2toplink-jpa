package example.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity
public class TestB {

    @Id
    @GeneratedValue
    private Integer id;
    
    private String name;
    
    @Version
    private Integer version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TestA testa;

    /**
     * @return id を戻します。
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 設定する id。
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name を戻します。
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 設定する name。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return testa を戻します。
     */
    public TestA getTesta() {
        return testa;
    }

    /**
     * @param testa 設定する testa。
     */
    public void setTesta(TestA testa) {
        this.testa = testa;
    }

    /**
     * @return version を戻します。
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version 設定する version。
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}
