# 2019-01 Open Source Software Project 01
# HERE
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

<img src="https://user-images.githubusercontent.com/49481548/59783238-adfc8e80-92fa-11e9-9808-35cefa8ac863.png" width="200" height="200" />

##### "Here" is photo gallery application that bring the whole photo from the gallery and displays the location of the photo on the map using the location infomation included in the photo and cluster.
## Developers
* 김 성진
* 이 도현
* 유 진선
* 강 주형
## Screen Shot
<img src="https://user-images.githubusercontent.com/49481548/59789306-a3e18c80-9308-11e9-96c4-e51bf203ce54.PNG" width="280" height="520" /> <img src="https://user-images.githubusercontent.com/49481548/59789312-a7751380-9308-11e9-8ae4-32c26ed83819.PNG" width="280" height="520" /> <img src="https://user-images.githubusercontent.com/49481548/59789315-a93ed700-9308-11e9-9c83-d8d3187fcd33.PNG" width="280" height="520" /> <img src="https://user-images.githubusercontent.com/49481548/59789321-ab089a80-9308-11e9-8d6f-1b0035df6eec.PNG" width="280" height="520" /> <img src="https://user-images.githubusercontent.com/49481548/59789325-acd25e00-9308-11e9-9533-aa3b20b11421.PNG" width="280" height="520" />

## How to build
```
Step1. Fork or download '2019-1-OSSP1-Arsonist-4' project.
Step2. Import '2019-1-OSSP1-Arsonist-4' project into android studio.
Step3. Register your package name and SHA-1 signature certificate fingerprint for Google Map use.(https://console.developers.google.com/)
Step4. Define api key to 'google_maps_api_key.xml' file.
       - /app/res/values/google_maps_api_key.xml
Step5. Build '2019-1-OSSP1-Arsonist-4' project with android studio.
```
## Project Tree
```
├─manifests
│   └─AndroidManifest.xml
├─java
│   └─com.arsonist.here
│       ├─adapters
│       │       MainAdapter.kt
│       │       PhotoRecyclerViewAdapter.kt
│       │       PopupAdapter.kt
│       │       
│       ├─fragments
│       │       GalleryFragment.kt
│       │       ImageFragment.kt
│       │       MapFragment.kt
│       │       
│       ├─Models
│       │       FetchPhotoAsyncTask.kt
│       │       MainActivity.kt
│       │       MultiDrawable.java
│       │       PhotoMetadata.kt
│       │       RenderClusterInfoWindow.kt
│       │       ViewActivity.java
│       
│   └─com.arsonist.here (androidTest)
│         ExampleInstrumentedTest.kt
│   └─com.arsonist.here (test)
│         ExampleUnitTest.kt
│       
└─res
    ├─drawable
    │      ic_launcher_background.xml
    │      ic_launcher_foreground.xml
    │     
    ├─layout
    │      activity_main.xml
    │      activity_view.xml
    │      fragment_gallery.xml
    │      fragment_image.xml
    │      fragment_map.xml
    │      info_window_layout.xml
    │      item_photo_view.xml
    │      medium_item.xml
    │      multi_photo.xml
    │      small_item.xml
    │      
    ├─mipmap
    │    └─ic_launcher
    │           ic_launcher.png (hdpi)
    │           ic_launcher.png (mdpi)
    │           ic_launcher.png (xhdpi)
    │           ic_launcher.png (xxhdpi)
    │           ic_launcher.png (xxxhdpi)
    │           ic_launcher.xml (anydpi-v26)
    │
    │    └─ic_launcher_round
    │           ic_launcher_round.png (hdpi)
    │           ic_launcher_round.png (mdpi)
    │           ic_launcher_round.png (xhdpi)
    │           ic_launcher_round.png (xxhdpi)
    │           ic_launcher_round.png (xxxhdpi)
    │           ic_launcher_round.xml (anydpi-v26)
    │
    ├─values
    │      colors.xml
    │      dimens.xml
    │      google_maps_api_key.xml
    │      strings.xml
    │      styles.xml
```
## License 
        
        
        
        
        
        
