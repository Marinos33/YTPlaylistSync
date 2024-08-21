
# YTPlaylistSync
[![Download YTPlaylistSync](https://a.fsdn.com/con/app/sf-download-button)](https://sourceforge.net/projects/ytplaylistsync/files/latest/download)

We are all listening to music at some points. You are probably using Spotify, Youtube premium or other known app to listen your favorite songs.
But what if you could use an open-source application which doesn't require any account or personal information and can also save you from using your precious mobile data.


## Usage

This app is designed to allow you to download your playlists along with their metadata from Youtube and to keep them up-to-date.

It is made of two screens with simple features.

### Playlists screen

Here, you can add your playlists and click on the refresh button to download the newly added songs (By default, the already downloaded music won't be download again.). To remove a playlist, swipe to the left and click on the trash button.

![image](https://user-images.githubusercontent.com/51862073/202209065-0bc22488-9da6-4119-a8e9-b300145aede7.png)

### Download screen

Here, you can download anything with a custom command which will be used to download the content at the inputted URL (for more informations about commands, see the technical section).

![image](https://user-images.githubusercontent.com/51862073/202208239-5ae3d69b-0604-4011-866f-abb5b661a484.png)

## Technical

This app uses [yt-dlp](https://github.com/yt-dlp/yt-dlp) to download the content and ffmpeg to execute the conversions thanks to [youtubedl-android package](https://github.com/yausername/youtubedl-android).
So about the command on the download screen, you can input anything that can be used with yt-dlp, except there is two options that are not overridable:

1. The output will always download the content in the download directory. This is because you don't want to write the full path of android folders. So don't use the command "-o"

2. Non-ascii characters will always be removed from the filename to prevent error. So the "--restrict-filenames" command will always be applied.

#### additional informations
When trying to download or update a playlist, the app will sometimes tell you that It didn't work correctly but in fact everything goes well. It's because yt-dlp is not always updated with the changes from its supported web sites and cannot handle every situation perfectly cause there is too many. By the way, this app was designed to work with Youtube. I don't know what will be the behavior with other supported site of yt-dlp, you may encounter some fortunate (or not) surprise.


## Issues
If you find any bugs or have any request about an enhancement for the software don't hesitate to add in the Issues tab and I will be glad to try to answer your request.


## License

[MIT](https://github.com/Marinos33/YTPlaylistSync/blob/main/LICENSE)


## Contributing

Contributions are always welcome!
You can open a pull request if you want to help me improve this app.
 
