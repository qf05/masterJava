package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {

    public static City CITY1;
    public static City CITY2;
    public static City CITY3;
    public static City CITY4;
    public static List<City> CITIES;

    public static void init() {
        CITY1 = new City("spb", "Санкт-Петербург");
        CITY2 = new City("mow", "Москва");
        CITY3 = new City("kiv", "Киев");
        CITY4 = new City("mnsk", "Минск");
        CITIES = ImmutableList.of(CITY3, CITY4, CITY2, CITY1);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.forEach(dao::insert);
        });
    }
}
