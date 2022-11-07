package github.luthfipun.ytupload.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.DataStore
import com.google.api.client.util.store.FileDataStoreFactory
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

class Auth {

    val jsonFactory: JsonFactory = JacksonFactory()
    val httpTransport: HttpTransport = NetHttpTransport()

    @Throws(Exception::class)
    fun authorize(scopes: List<String>, credentialDataStore: String): Credential {

        val clientSecretReader: Reader = InputStreamReader(Auth::class.java.getResourceAsStream("/client_secret.json") as InputStream)
        val clientSecret = GoogleClientSecrets.load(jsonFactory, clientSecretReader)

        // save on desktop
        val fileDataStoreFactory = FileDataStoreFactory(File(System.getProperty("user.home") + "/" + "Desktop/yt_credentials"))
        val dataStore: DataStore<StoredCredential> = fileDataStoreFactory.getDataStore(credentialDataStore)

        val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            jsonFactory,
            clientSecret,
            scopes
        ).setCredentialDataStore(dataStore).build()

        val localServerReceiver: LocalServerReceiver = LocalServerReceiver.Builder()
            .setPort(8080).build()

        return AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user")
    }
}