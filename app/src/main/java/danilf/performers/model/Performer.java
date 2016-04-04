package danilf.performers.model;

import android.support.annotation.NonNull;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DanilF on 02.04.2016.
 */
public class Performer implements Comparable<Performer> {
    private int id;
    private String name;
    private List<String> genres;
    private int tracks;
    private int albums;
    private URL link;
    private String description;
    private HashMap<String,URL> cover;

    public Performer(int id, String name, List<String> genres, int tracks, int albums, URL link, String description, HashMap<String, URL> cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, URL> getCover() {
        return cover;
    }

    public void setCover(HashMap<String, URL> cover) {
        this.cover = cover;
    }

    @Override
    public int compareTo(@NonNull Performer another) {
        return this.name.compareTo(another.name);
    }
}
