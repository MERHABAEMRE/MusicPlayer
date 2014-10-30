/**
 * MusicInfo.java [V1.0.0]
 * classes : com.james.musicplayer.bean.MusicInfo
 * ̷���� Create at 2014-10-16 ����6:02:55
 */
package com.james.musicplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ������Ϣʵ�� com.james.musicplayer.bean.MusicInfo
 * 
 * @author ̷���� Create at 2014-10-16 ����6:02:55
 */
public class MusicInfo implements Parcelable {

    private long _id;//����ID
    private String titleString;//���ֱ���
    private String artistString;//������������
    private String durationString;//����ʱ��
    private String albumString;//��������ר��
    private String pathString;//��������·��

    public MusicInfo() {}

    public MusicInfo(Parcel source) {
        _id=source.readLong();
        titleString=source.readString();
        artistString=source.readString();
        durationString=source.readString();
        albumString=source.readString();
        pathString=source.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);   
        dest.writeString(titleString); 
        dest.writeString(artistString);
        dest.writeString(durationString);
        dest.writeString(albumString); 
        dest.writeString(pathString);
    }

    void readFromParcel(Parcel dest) {
        dest.writeLong(_id);   
        dest.writeString(titleString); 
        dest.writeString(artistString);
        dest.writeString(durationString);
        dest.writeString(albumString); 
        dest.writeString(pathString);
    }
    
    //���һ����̬��Ա,��ΪCREATOR,�ö���ʵ����Parcelable.Creator�ӿ�   
    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public String getArtistString() {
        return artistString;
    }

    public void setArtistString(String artistString) {
        this.artistString = artistString;
    }

    public String getDuration() {
        return durationString;
    }

    public void setDuration(String duration) {
        this.durationString = duration;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getAlbumString() {
        return albumString;
    }

    public void setAlbumString(String albumString) {
        this.albumString = albumString;
    }

    public String getPathString() {
        return pathString;
    }

    public void setPathString(String pathString) {
        this.pathString = pathString;
    }


}
