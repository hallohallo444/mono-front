package jp.co.monocrea.entity;


import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Users.getByName", query = "from Users where name LIKE CONCAT('%', ?1, '%') order by id asc")
@NamedQuery(name = "Users.getByIdName", query = "from Users where id = ?1 and name LIKE CONCAT('%', ?2, '%') order by id asc")
@NamedQuery(name = "Users.countByRecords", query = "select count(*) from Users")
@NamedQuery(name = "Users.updateNameById", query = "update Users u set u.name = :name where u.id = :id")
@NamedQuery(name = "Users.deleteById", query = "delete from Users u where u.id = :id")

public class Users extends PanacheEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public static List<Users> findByName(String name, int limit, int offset){
        return find("#Users.getByName", name)
        .page(Page.of(offset - 1, limit))
        .list();
    }

    public static List<Users> findByIdName(Long id,String name, int limit, int offset){
        return find("#Users.getByIdName", id,name)
        .page(Page.of(offset - 1, limit))
        .list();
    }

    /**
     * 最後に追加されたレコードのIDを取得する
     * @return
     */
    public static long getLastId(){
        return Optional.ofNullable(find("#Users.countByRecords").firstResult())
                      .map(v -> (Long) v)
                      .orElse(0L);
    }

    /**
     * 更新
     * 
     */
    public static long updateNameById(long id, String name) {
        return update("#Users.updateNameById", Parameters.with("name", name).and("id", id));
    }

    /**
     * 削除
     * 
     */
    public static long deleteById(long id) {
        return delete("#Users.deleteById",Parameters.with("id", id));
    }
}
