package example.dao;

import org.seasar.extension.unit.S2TestCase;

import example.entity.Employee;

public class EmployeeDaoTest extends S2TestCase {

    private EmployeeDao employeeDao;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(EmployeeDaoTest.class.getSimpleName() + ".dicon");
    }
    
    public void testFindTx() {
        employeeDao.find(1);
    }
    
    public void testFindAllTx() {
        employeeDao.findAll();
    }
    
    public void testFindByNameTx() {
        employeeDao.findByName("test");
    }

    public void testFindByExampleTx() {
    
        Employee employee = new Employee();
        employee.setName("test");
        employeeDao.findByExample(employee);
    }
    
    public void testFindByNameSqlTx() {
        employeeDao.findByNameSql("test");
    }

}
