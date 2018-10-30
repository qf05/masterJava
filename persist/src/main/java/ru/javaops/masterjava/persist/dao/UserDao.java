package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserDao implements AbstractDao {

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextVal();

    @Transaction
    public int getIdSkipChank(int skip) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('user_seq', " + (id + skip) + ")"));
        return id;
    }

    public User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWitId(user);
        }
        return user;
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag) VALUES (:fullName, :email, CAST(:flag AS user_flag)) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user);

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS user_flag)) ")
    abstract void insertWitId(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    public abstract List<User> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    public abstract void clean();

    @SqlBatch("insert into users (full_name, email, flag) values (:fullName, :email, CAST(:flag AS USER_FLAG)) ON CONFLICT DO NOTHING")
    public abstract int[] insetBatch(@BindBean List<User> users, @BatchChunkSize int chunkSize);

    @SqlBatch("insert into users (full_name, email, flag) values (:fullName, :email, CAST(:flag AS USER_FLAG)) ON CONFLICT DO NOTHING")
    public abstract int[] insetBatch2(@BindBean List<User> users);
}
