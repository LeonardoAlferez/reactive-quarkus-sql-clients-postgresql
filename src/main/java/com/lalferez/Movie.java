package com.lalferez;


import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.Data;

@Data
public class Movie {
    private Long id;
    private String title;

    public Movie(){    }

    public Movie(String title){
        this.title = title;
    }
    public Movie(Long id, String title){
        this.title = title;
        this.id = id;

    }

    public static Multi<Movie> findAll(PgPool client){
        return client.query("SELECT id, title FROM movies ORDER BY title DESC").execute()
                .onItem().transformToMulti(set ->Multi.createFrom().iterable(set))
                .onItem().transform(Movie::from);
    }
    private static Movie from(Row row){
        return new Movie(row.getLong("id"), row.getString("title"));

    }
}
