# Documentation

Implementation insert/upload video to YouTube with YouTube Data API V3.
This project use kotlin as programming language and YouTube library and Google Client Api for Authorization.

## Usage
Please create project on [Google Cloud Console](https://console.cloud.google.com) and enable YouTube Data API Library. 
Create credentials OAuth Client ID and download the client secret JSON file. Save to `resources` folder on this project with name ``client_secret.json``

If you generate OAuth Client ID as Web Application please add redirect url here :
```
http://localhost:8080/Callback&response_type=code&scope=https://www.googleapis.com/auth/youtube.upload
```

Make sure you add email tester on the OAuth Consent Screen. Start the app and it will show instruction to open the link on the browser for login and authorize the applications.

