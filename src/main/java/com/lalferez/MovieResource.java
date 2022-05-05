package com.lalferez;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Data
@Path("movies")
public class MovieResource {
    @Inject
    PgPool client;

    @PostConstruct
    void config(){
        initdb();
    }

    @GET
    public Multi<Movie> get(){
        return Movie.findAll(client);
    }

    private void initdb(){
        client.query("DROP TABLE IF EXISTS movies").execute()
                .flatMap(m -> client.query("CREATE TABLE movies (id SERIAL PRIMARY KEY," +
                        "title TEXT NOT NULL)").execute())
                .flatMap(m -> client.query("INSERT INTO movies(title) VALUES " +
                        "('STAR WARS EPISODE 3')").execute())
                .flatMap(m -> client.query("INSERT INTO movies(title) VALUES "+
                        "('INTERSTELLAR')").execute()).await().indefinitely();
    }
}
