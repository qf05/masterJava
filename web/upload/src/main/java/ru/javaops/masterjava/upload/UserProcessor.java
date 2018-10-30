package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.model.UsersFail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final UserDao dao = DBIProvider.getDao(UserDao.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public List<UsersFail> process(final InputStream is, int sizeBunch) throws Exception {
        Callable<List<UsersFail>> callable = () -> {
            List<User> users = new ArrayList<>(sizeBunch);
            int id = dao.getIdSkipChank(sizeBunch);
            Map<String, Future<List<String>>> map = new HashMap<>();
            final StaxStreamProcessor processor = new StaxStreamProcessor(is);
            JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
            while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
                ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
                final User user = new User(id++, xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
                users.add(user);
                if (users.size() == sizeBunch) {
                    List<User> finalUsers = users;
                    map.put(getRange(finalUsers), executorService.submit(() -> insert(finalUsers)));
                    id = dao.getIdSkipChank(sizeBunch);
                    users = new ArrayList<>();
                }
            }
            if (users.size()>0){
                List<User> finalUsers1 = users;
                map.put(getRange(finalUsers1), executorService.submit(() -> insert(finalUsers1)));
            }

            List<UsersFail> result = new ArrayList<>();
            map.forEach((k,v) -> {
                try {
                    List<String> u = v.get();
                    if (u.size() > 0) {
                        Map<String, String> emails = u.stream().collect(Collectors.toMap(m -> m, n -> "duplicate email"));
                        result.add(new UsersFail(k, emails));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Map<String, String> all = new HashMap<>();
                    all.put("ALL", e.getMessage());
                    result.add(new UsersFail(k, all));
                }
            });
            map.clear();
            return result;
        };
        return callable.call();
    }

    private List<String> insert(List<User> list) {
        List<String> result = new ArrayList<>();
        int insert[] = dao.insetBatch2(list);
        for (int i = 0; i < insert.length; i++) {
            if (insert[i] == 0) {
                result.add(list.get(i).getEmail());
            }
        }
        return result;
    }

    private String getRange(List<User> users){
        if (users.size()>1) {
            return users.get(0).getEmail() + " - " + users.get(users.size() - 1).getEmail();
        }else {
            return users.get(0).getEmail();
        }
    }
}
