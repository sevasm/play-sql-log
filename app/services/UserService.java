package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.User;
import play.db.Database;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

public class UserService {
    private final Database database;
    private final JPAApi jpaApi;

    @Inject
    public UserService(Database database, JPAApi jpaApi) {
        this.database = database;
        this.jpaApi = jpaApi;
    }

    public List<User> getUsers(boolean useJdbc) {
        if (useJdbc) {
            return getUsersJdbc();
        } else {
            return getUsersJpa();
        }
    }

    public void insertUser(User user, boolean useJdbc) {
        if (useJdbc) {
            insertUserJdbc(user);
        } else {
            insertUserJpa(user);
        }
    }

    private List<User> getUsersJdbc() {
        return database.withConnection((Connection c) -> {
            List<User> users = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement("select id, username from users order by username");
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(rs.getInt("id"), rs.getString("username")));
                }
            } finally {
                c.close();
            }
            return users;
        });
    }

    private void insertUserJdbc(User user) {
        database.withConnection((Connection c) -> {
            try (PreparedStatement ps = c.prepareStatement("insert into users (username) values (?)")) {
                ps.setString(1, user.getUsername());
                ps.executeUpdate();
            } finally {
                c.close();
            }
        });
    }

    private List<User> getUsersJpa() {
        CriteriaBuilder cb = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.orderBy(cb.asc(root.get("username")));
        return jpaApi.em().createQuery(cq).getResultList();
    }

    private void insertUserJpa(User user) {
        JPA.em().merge(user);
    }
}
