package danilf.performers.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DanilF on 02.04.2016.
 */
public class Performer implements Comparable<Performer>, Parcelable {
    private int id;
    private String name;
    private ArrayList<String> genres;
    private int tracks;
    private int albums;
    private URL link;
    private String description;
    private HashMap<String,URL> cover;

    public Performer(int id, String name, ArrayList<String> genres, int tracks, int albums, URL link, String description, HashMap<String, URL> cover) {
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

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
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
        //fix server bug with first lowercase character
        if(description!= null && !Character.isUpperCase(description.charAt(0))) {
            description = Character.toString(description.charAt(0)).toUpperCase() +
                    description.substring(1) +
                    " "+System.getProperty("line.separator");
        }
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

    protected Performer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<String>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        tracks = in.readInt();
        albums = in.readInt();
        link = (URL) in.readValue(URL.class.getClassLoader());
        description = in.readString();
        cover = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeValue(link);
        dest.writeString(description);
        dest.writeValue(cover);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Performer> CREATOR = new Parcelable.Creator<Performer>() {
        @Override
        public Performer createFromParcel(Parcel in) {
            return new Performer(in);
        }

        @Override
        public Performer[] newArray(int size) {
            return new Performer[size];
        }
    };
}
