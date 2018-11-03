package ru.javaops.masterjava.persist.dao;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

import static ru.javaops.masterjava.persist.GroupTestData.GROUP1;
import static ru.javaops.masterjava.persist.GroupTestData.GROUPS;

public class GroupDaoTest  extends AbstractDaoTest<GroupDao> {

    public GroupDaoTest() {
        super(GroupDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        GroupTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        GroupTestData.setUp();
    }

    @Test
    public void get() {
        List<Group> cities = dao.get();
        Assert.assertEquals(GROUPS, cities);
    }

    @Test
    public void insert() throws Exception {
        dao.clean();
        dao.insert(GROUP1);
        Assert.assertEquals(ImmutableList.of(GROUP1), dao.get());
    }
}
