# Platforms Commons Application

## Overview:
The above application is an implementation for an assignment task provided in the [PDF](https://github.com/thisisvd/Platforms-Commons-App/blob/master/support/Android%20Developer%20Assignment.pdf). Used the Kotlin programming language as a default and followed the MVVM architecture pattern with clean architecture. Used Retrofit Library for making network calls and room to store user data when the network is not available, injected dependencies using Dagger-Hilt, Glide for image loading, and Flows and RxJava for asynchronous data and UI updates. To upload, use data in the background when the network is available and use Worker when the network is reconnected. Designed the application by following material guidelines and provided a basic UI theme design for both light and dark themes.

### Application: [ReleaseAPK](https://github.com/thisisvd/Platforms-Commons-App/blob/master/support/Platform-commons-app-release.apk) 

## Guiding the app components: 
You can check the application architecture, connectivity, etc. in the github project.

- **Main Screen:**
  - The first user screen shows the list of users fetched from the API with paging 3 to load other page items and displays them in the UI with name and avatar as described in the PDF, clicking to which redirects to the next movie list screen. This screen also contains a fab button to redirect to the Add New User screen.

- **Add User Screen:**
  - The user screen adds the user and makes a post-API call to the server, and a snackbar is shown once for the successful result from the API. If the network is not available, the application doesn't fire an API call; instead, it adds that user in the database, and a worker request is queued for the data saved. Once the internet connectivity is back, that work request executes successfully, and after a successful sync, it shows the snack bar, and if the application isn’t available, it still completes its work. I have used RXJava for listening to SyncWorker events and network events. Used the Timber library for debugging, so you can check the status of the worker by logging “SyncWorker” in Android Studio.
  
- **Movies Screen:**
  - The movie screen contains the paginated list of the movies from the API with a poster, title, and released date. When clicked on any movie, it redirects to the movie details screen. The images are loaded using Glide. And when scrolled down, it fetches the other page data using paging 3.

- **Movies Details Screen:**
  - The movie details screen contains the poster, title, release date, and description fetched from the API. Used navargs to pass movieId from the MoviesList screen to the MoviesDetails screen.
<br/>
The app has Dagger-Hilt implemented and has 3 modules named NetworkModule, DatabaseModule, and WorkerModule. Also contains SyncFactory and SyncWorker classes for the implementation and used flow of components between them. For further details, one can check the GitHub project and the application present in the global support folder.

## Tech-Stack:
 - `Programming Language:` Kotlin
 - `Architecture:` MVVM
 - `Dependency Injection:` Hilt/Dagger
 - `Networking:` Retrofit with best practices
 - `Local Storage:` Room Database
 - `Offline Handling:` WorkManager for syncing data when the device regains internet connectivity
 - `Pagination:` Use Paging 3 for efficient data loading
 - `Asynchronous Data Handling:` Used Kotlin Flow for reactive UI updates
 - `Image Loading:` Glide
