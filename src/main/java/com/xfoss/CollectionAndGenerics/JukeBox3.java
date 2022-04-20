package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;
import com.xfoss.Utils.XPlatformThings;

public class JukeBox3 {

    // 这里将这个 ArrayList 从 String 修改为了 Song 对象。
    ArrayList<Song> songList = new ArrayList<Song> ();
    String wDir = XPlatformThings.getWorkingDir("learningJava");

    public JukeBox3 () {
        getSongs();
        System.out.println(songList);

        Collections.sort(songList);
        System.out.println(songList);
    }

    public static void main(String[] args){
        new JukeBox3();
    }

    void getSongs() {
        try {
            File file = new File(String.format("%s/SongListMore.txt", wDir));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }

    void addSong(String lineToParse) {

        // 这里使用四个令牌（也就是歌曲文件中这一行的四个信息片段）创建
        // 出一个新的 Song 对象，并将该 Song 对象添加到那个清单。
        String [] tokens = lineToParse.split("/");

        Song nextSong = new Song(tokens[0], tokens[1], tokens[2], tokens[3]);
        songList.add(nextSong);
    }
}

class Song implements Comparable<Song> {

    // 这四个实例变量表示文件中的四个歌曲属性。
    private String title;
    private String artist;
    private String rating;
    private String bpm;

    // 这些变量都是在新的 Song 对象被创建时，在构造器中设置的。
    Song (String t, String a, String r, String b) {
        title = t;
        artist = a;
        rating = r;
        bpm = b;
    }

    public int compareTo(Song s) {
        return title.compareTo(s.getTitle());
    }

    // 这些是四个属性的获取器方法。
    public String getTitle () {
        return title;
    }

    public String getArtist () {
        return artist;
    }

    public String getRating () {
        return rating;
    }

    public String getBpm () {
        return bpm;
    }

    // 由于在执行 System.out.println(aSongObject)时，希望看到歌曲标题，因此
    //  这里重写了 toString() 方法。在执行 System.out.println(aListOfSongs) 
    //  时，就会调用清单中各个元素的这个 toString() 方法。
    public String toString () {
        return String.format("%s - %s", title, rating);
    }
}
