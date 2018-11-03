package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao{

    public City insert(City city) {
            insertWitId(city);
        return city;
    }

    @SqlUpdate("INSERT INTO cities (ref, name) VALUES (:ref, :name) ON CONFLICT DO NOTHING")
    abstract void insertWitId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities ORDER BY name")
    public abstract List<City> get();

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();
}
