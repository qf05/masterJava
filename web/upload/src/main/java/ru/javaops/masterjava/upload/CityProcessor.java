package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class CityProcessor {
    private static CityDao dao = DBIProvider.getDao(CityDao.class);

    public List<City> process(final StaxStreamProcessor processor) throws XMLStreamException {
        while (processor.startElement("City", "Cities")) {
            City city = new City(processor.getAttribute("id"), processor.getText());
            dao.insert(city);
        }
        return dao.get();
    }
}
