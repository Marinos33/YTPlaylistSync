package com.example.ytplaylistsync.ui.playlist.recyclerView

import com.example.ytplaylistsync.domain.entities.PlaylistEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MockDataProvider {
    public fun getPlaylistDataList(): List<PlaylistEntity> {
        val jsonString =
            "[{\"playlist_name\":\"Andaman & Nicobar\",\"playlist_author\":\"AN\",\"playlist_last_updated\":\"AN\"}," +
                    "{\"playlist_name\":\"Andhra Pradesh\",\"playlist_author\":\"AP\",\"playlist_last_updated\":\"AP\"}," +
                    "{\"playlist_name\":\"Arunachal Pradesh\",\"playlist_author\":\"AR\",\"playlist_last_updated\":\"AR\"}," +
                    "{\"playlist_name\":\"Assam\",\"playlist_author\":\"AS\",\"playlist_last_updated\":\"AS\"}," +
                    "{\"playlist_name\":\"Bihar\",\"playlist_author\":\"BR\",\"playlist_last_updated\":\"BR\"}," +
                    "{\"playlist_name\":\"Chandigarh\",\"playlist_author\":\"CH\",\"playlist_last_updated\":\"CH\"}," +
                    "{\"playlist_name\":\"Chhattisgarh\",\"playlist_author\":\"CG\",\"playlist_last_updated\":\"CG\"}," +
                    "{\"playlist_name\":\"Dadra & Nagar Haveli\",\"playlist_author\":\"DN\",\"playlist_last_updated\":\"DN\"}," +
                    "{\"playlist_name\":\"Daman & Diu\",\"playlist_author\":\"DD\",\"playlist_last_updated\":\"DD\"}," +
                    "{\"playlist_name\":\"Delhi\",\"playlist_author\":\"DL\",\"playlist_last_updated\":\"DL\"}," +
                    "{\"playlist_name\":\"Goa\",\"playlist_author\":\"GA\",\"playlist_last_updated\":\"GA\"}," +
                    "{\"playlist_name\":\"Gujarat\",\"playlist_author\":\"GJ\",\"playlist_last_updated\":\"GJ\"}," +
                    "{\"playlist_name\":\"Haryana\",\"playlist_author\":\"HR\",\"playlist_last_updated\":\"HR\"}," +
                    "{\"playlist_name\":\"Himachal Pradesh\",\"playlist_author\":\"HP\",\"playlist_last_updated\":\"HP\"}," +
                    "{\"playlist_name\":\"Jammu & Kashmir\",\"playlist_author\":\"JK\",\"playlist_last_updated\":\"JK\"}," +
                    "{\"playlist_name\":\"Jharkhand\",\"playlist_author\":\"JH\",\"playlist_last_updated\":\"JH\"}," +
                    "{\"playlist_name\":\"Karnataka\",\"playlist_author\":\"KA\",\"playlist_last_updated\":\"KA\"}," +
                    "{\"playlist_name\":\"Kerala\",\"playlist_author\":\"KL\",\"playlist_last_updated\":\"KL\"}," +
                    "{\"playlist_name\":\"Lakshadweep\",\"playlist_author\":\"LD\",\"playlist_last_updated\":\"LD\"}," +
                    "{\"playlist_name\":\"Madhya Pradesh\",\"playlist_author\":\"MP\",\"playlist_last_updated\":\"MP\"}," +
                    "{\"playlist_name\":\"Maharashtra\",\"playlist_author\":\"MH\",\"playlist_last_updated\":\"MH\"}," +
                    "{\"playlist_name\":\"Manipur\",\"playlist_author\":\"MN\",\"playlist_last_updated\":\"MN\"}," +
                    "{\"playlist_name\":\"Meghalaya\",\"playlist_author\":\"ML\",\"playlist_last_updated\":\"ML\"}," +
                    "{\"playlist_name\":\"Mizoram\",\"playlist_author\":\"MZ\",\"playlist_last_updated\":\"MZ\"}," +
                    "{\"playlist_name\":\"Nagaland\",\"playlist_author\":\"NL\",\"playlist_last_updated\":\"NL\"}," +
                    "{\"playlist_name\":\"Odisha\",\"playlist_author\":\"OR\",\"playlist_last_updated\":\"OR\"}," +
                    "{\"playlist_name\":\"Puducherry\",\"playlist_author\":\"PY\",\"playlist_last_updated\":\"PY\"}," +
                    "{\"playlist_name\":\"Punjab\",\"playlist_author\":\"PB\",\"playlist_last_updated\":\"PB\"}," +
                    "{\"playlist_name\":\"Rajasthan\",\"playlist_author\":\"RJ\",\"playlist_last_updated\":\"RJ\"}," +
                    "{\"playlist_name\":\"Sikkim\",\"playlist_author\":\"SK\",\"playlist_last_updated\":\"SK\"}," +
                    "{\"playlist_name\":\"Tamil Nadu\",\"playlist_author\":\"TN\",\"playlist_last_updated\":\"TN\"}," +
                    "{\"playlist_name\":\"Telangana\",\"playlist_author\":\"TG\",\"playlist_last_updated\":\"TG\"}," +
                    "{\"playlist_name\":\"Tripura\",\"playlist_author\":\"TR\",\"playlist_last_updated\":\"TR\"}," +
                    "{\"playlist_name\":\"Uttar Pradesh\",\"playlist_author\":\"UP\",\"playlist_last_updated\":\"UP\"}," +
                    "{\"playlist_name\":\"Uttarakhand\",\"playlist_author\":\"UT\",\"playlist_last_updated\":\"UT\"}," +
                    "{\"playlist_name\":\"West Bengal\",\"playlist_author\":\"WB\",\"playlist_last_updated\":\"WB\"}]"
        val playlistType: Type = object :
            TypeToken<ArrayList<PlaylistEntity?>?>() {}.type
        return Gson().fromJson<ArrayList<PlaylistEntity>?>(jsonString, playlistType)
    }
}