package jp.co.monocrea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/search_old")
public class SearchUser_old {

    @Inject
    //UserService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //public String search(@QueryParam("id") String id, @QueryParam("name") String name) {
    public String search() {

        //Javaオブジェクトに値をセット
    	//List<SearchUserEntity> jsonBeanList = new ArrayList<>();
    	

    	ObjectMapper mapper = new ObjectMapper();
        String resultResponseJson = "";
		/*
    	try {
    		//JavaオブジェクトからJSONに変換
    		resultResponseJson = mapper.writeValueAsString(List<SearchUserEntity>);
    	} catch (JsonProcessingException e) {
    		e.printStackTrace();
    	}*/

        System.out.println(resultResponseJson);

        return resultResponseJson;
    }

}
