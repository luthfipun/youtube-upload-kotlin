package github.luthfipun.ytupload.youtube

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.InputStreamContent
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import github.luthfipun.ytupload.youtube.VideoConstant.VIDEO_FILE_NAME
import github.luthfipun.ytupload.youtube.VideoConstant.VIDEO_FORMAT
import github.luthfipun.ytupload.youtube.VideoConstant.VIDEO_UPLOAD_SCOPES
import java.io.IOException
import java.util.*

class Upload {

    fun startUpload(){

        val auth = Auth()
        val scopes = listOf(VIDEO_UPLOAD_SCOPES)

        try {
            val credential = auth.authorize(scopes, "uploadvideo")

            val youtube = YouTube.Builder(
                auth.httpTransport,
                auth.jsonFactory,
                credential
            ).setApplicationName("upload-via-kotlin-test").build()

            println("uploading...")

            val videoMeta = Video()
            val videoStatus = VideoStatus()
            videoStatus.privacyStatus = "public"
            videoStatus.madeForKids = false
            videoMeta.status = videoStatus

            val videoSnippet = VideoSnippet()
            val cal = Calendar.getInstance()

            // add title tag #Shorts for YouTube Shorts
            videoSnippet.title = "Video upload from kotlin on ${cal.time}"
            videoSnippet.description = "Test upload video from kotlin server on ${cal.time}"

            val tags = mutableListOf<String>()
            tags.add("youtube")
            tags.add("youtube-data-api")
            tags.add("test")
            tags.add("delete me")

            videoSnippet.tags = tags
            videoMeta.snippet = videoSnippet

            val mediaContent = InputStreamContent(
                VIDEO_FORMAT,
                Upload::class.java.getResourceAsStream("/$VIDEO_FILE_NAME")
            )

            val videoInsert: YouTube.Videos.Insert = youtube.videos()
                .insert("snippet,statistics,status", videoMeta, mediaContent)

            val uploader: MediaHttpUploader = videoInsert.mediaHttpUploader
            uploader.isDirectUploadEnabled = true
            uploader.progressListener = MediaHttpUploaderProgressListener { mediaHttpUploader ->
                when(mediaHttpUploader.uploadState){
                    MediaHttpUploader.UploadState.NOT_STARTED -> {
                        println("upload not started")
                    }
                    MediaHttpUploader.UploadState.INITIATION_STARTED -> {
                        println("initialization upload")
                    }
                    MediaHttpUploader.UploadState.INITIATION_COMPLETE -> {
                        println("initialization complete")
                    }
                    MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS -> {
                        println("upload progress: ${mediaHttpUploader.numBytesUploaded}")
                    }
                    MediaHttpUploader.UploadState.MEDIA_COMPLETE -> {
                        println("upload complete")
                    }
                    else -> {
                        println("unknown")
                    }
                }
            }

            val returnedVideo: Video = videoInsert.execute()

            println("\n================== Returned Video ==================\n")
            println("  - Id: " + returnedVideo.id)
            println("  - Title: " + returnedVideo.snippet.title)
            println("  - Tags: " + returnedVideo.snippet.tags)
            println("  - Privacy Status: " + returnedVideo.status.privacyStatus)
            println("  - Video Count: " + returnedVideo.statistics.viewCount)
        }catch (e: GoogleJsonResponseException){
            println("GoogleJsonResponseException code : ${e.details.code} | ${e.details.message}")
        }catch (e: IOException){
            println("IOException : ${e.message}")
        }catch (e: Throwable){
            println("Throwable : ${e.message}")
        }
    }
}