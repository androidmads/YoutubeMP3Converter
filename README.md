# YoutubeMP3Converter
Library to Download youtube video as mp3 files
## Created By
[![API](https://img.shields.io/badge/AndroidMads-AJTS-brightgreen.svg?style=flat)](https://github.com/androidmads/YoutubeMP3Converter)
## How to Download
<b>Gradle:</b>
```groovy
compile 'com.ajts.androidmads.youtubemp3:youtubemp3:1.0.0'
```
<b>Maven:</b>
```groovy
<dependency>
  <groupId>com.ajts.androidmads.youtubemp3</groupId>
  <artifactId>youtubemp3</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
## How to use this Library:
This Library is used to download mp3 file from youtube video link.

```java
new YTubeMp3Service.Builder(MainActivity.this)
	.setDownloadUrl("https://youtu.be/nZDGC-tXCo0")
	.setFolderPath(new File(Environment.getExternalStorageDirectory(), "/YTMp3/Downloads").getPath())
	.setOnDownloadListener(new YTubeMp3Service.Builder.DownloadListener() {
		@Override
		public void onSuccess(String savedPath) {
			Log.v("exce", savedPath);
			progressDialog.dismiss();
		}

		@Override
		public void onDownloadStarted() {
		}

		@Override
		public void onError(Exception e) {
			Log.v("exce", e.getMessage());
			progressDialog.dismiss();
		}
	}).build();
```
#### License
```
MIT License

Copyright (c) 2017 AndroidMad / Mushtaq M A

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
