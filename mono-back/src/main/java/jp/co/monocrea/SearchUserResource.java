package jp.co.monocrea;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jp.co.monocrea.entity.Users;
import jp.co.monocrea.repository.*;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchUserResource {
    
    @GET
    @Path("/{limit}/{offset}")
    public List<Users> list(int limit,int offset) {
        return PanacheEntityBase.findAll(Sort.by("id",Direction.Ascending))
         .page(Page.of(--offset, limit))
         .list();
    }

    @GET
    @Path("/id/{id}")
    public Users get(Long id) {
        return PanacheEntityBase.findById(id);
    }

    @GET
    @Path("/{name}/{limit}/{offset}")
    public List<Users> get(String name,int limit,int offset) {
        return Users.findByName(name, limit, offset);
    }

    @GET
    @Path("/{id}/{name}/{limit}/{offset}")
    public List<Users> get(Long id,String name,int limit,int offset) {
        return Users.findByIdName(id,name, limit, offset);
    }

    @GET
    @Path("/check")
    public int get(){
        Users.getLastId();
        return 1;
    }

    @GET
    @Path("/create/{name}")
    @Transactional
    public int create(String name){

        Users user = new Users();
        user.name = name;
        UsersRepository usersRepository = new UsersRepository();
        usersRepository.persist(user);
        return 1;
    }

    @GET
    @Path("/update/{id}/{name}")
    @Transactional
    public int update(long id, String name){

        try{
            Users.updateNameById(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @GET
    @Path("/delete/{id}")
    @Transactional
    public int delete(long id){

        try{
            Users.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
