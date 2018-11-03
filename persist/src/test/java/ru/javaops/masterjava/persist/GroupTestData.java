package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {
    public static Group GROUP1;
    public static Group GROUP2;
    public static Group GROUP3;
    public static Group GROUP4;
    public static List<Group> GROUPS;

    public static void init() {
        GROUP1 = new Group("topjava06", GroupType.FINISHED, 100006);
        GROUP2 = new Group("topjava07", GroupType.FINISHED, 100006);
        GROUP3 = new Group("topjava08", GroupType.CURRENT, 100006);
        GROUP4 = new Group("masterjava01", GroupType.CURRENT, 100007);
        GROUPS = ImmutableList.of(GROUP4,GROUP1,GROUP2,GROUP3);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });
    }
}
