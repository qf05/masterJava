package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.masterjava.persist.CityTestData.CITIES;
import static ru.javaops.masterjava.persist.CityTestData.CITY1;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }

    @Test
    public void get() {
        List<City> cities = dao.get();
        Assert.assertEquals(CITIES, cities);
    }

    @Test
    public void insert() throws Exception {
        dao.clean();
        dao.insert(CITY1);
        List<City> list = new ArrayList<>();
        list.add(CITY1);
        Assert.assertEquals(list, dao.get());
    }
}
