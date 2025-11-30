package jp.co.monocrea;


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
import jp.co.monocrea.entity.UsersInfo;
import jp.co.monocrea.repository.*;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchUserResource {
    
    @GET
    @Path("/{limit}/{offset}")
    public UsersInfo list(int limit,int offset) {
        UsersInfo result = new UsersInfo();
        result.userList = Users.findAll(Sort.by("id",Direction.Ascending))
         .page(Page.of(--offset, limit))
         .list();
        result.searchResultListsNum = Users.findAll().count();
         return result;
    }


    @GET
    @Path("/id/{id}")
    public Users get(Long id) {
        return Users.findById(id);
    }

    @GET
    @Path("/id/{id}/{limit}/{offset}")
    public UsersInfo get(Long id,int limit,int offset) {
        return Users.findById(id, limit, offset);
    }

    @GET
    @Path("/{name}/{limit}/{offset}")
    public UsersInfo get(String name,int limit,int offset) {
        return Users.findByName(name, limit, offset);
    }

    @GET
    @Path("/{id}/{name}/{limit}/{offset}")
    public UsersInfo get(Long id,String name,int limit,int offset) {
        return Users.findByIdName(id, name, limit, offset);
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
