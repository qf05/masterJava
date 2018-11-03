package ru.javaops.masterjava.persist.dao;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.ProjectTestData;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

import static ru.javaops.masterjava.persist.ProjectTestData.PROJECTS;
import static ru.javaops.masterjava.persist.ProjectTestData.TOPJAVA;

public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        ProjectTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
    }

    @Test
    public void get() {
        List<Project> cities = dao.get();
        Assert.assertEquals(PROJECTS, cities);
    }

    @Test
    public void insert() throws Exception {
        dao.clean();
        dao.insert(TOPJAVA);
        Assert.assertEquals(ImmutableList.of(TOPJAVA), dao.get());
    }
}
