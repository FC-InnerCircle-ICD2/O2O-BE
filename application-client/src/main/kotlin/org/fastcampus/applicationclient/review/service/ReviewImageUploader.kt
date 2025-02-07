package org.fastcampus.applicationclient.review.service

import io.awspring.cloud.s3.ObjectMetadata
import io.awspring.cloud.s3.S3Operations
import org.fastcampus.review.exception.ReviewException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Component
class ReviewImageUploader(
    private val s3Operations: S3Operations,
) {
    @Value("\${spring.cloud.aws.s3.bucket}")
    private lateinit var bucketName: String

    private val rootFolderName = "review"

    private val allowedExtensions: List<String> = listOf("jpg", "jpeg", "png", "gif")

    private val maxFileSize: Long = 10 * 1024 * 1024 // 10MB

    fun upload(fullPath: String, imageFile: MultipartFile): String {
        this.validate(imageFile)
        val fileExtensionName = this.getFileExtension(imageFile.originalFilename ?: "")
        val imageFullPath = "$rootFolderName/$fullPath.$fileExtensionName"
        val imageUri = "https://$bucketName.s3.amazonaws.com/$imageFullPath"
        try {
            imageFile.inputStream.use { inputStream ->
                s3Operations.upload(
                    bucketName,
                    imageFullPath,
                    inputStream,
                    ObjectMetadata.builder().contentType(imageFile.contentType).build(),
                )
            }
        } catch (e: IOException) {
            log.error("fail to upload review image", e)
            throw ReviewException.ImageUploadFail(imageUri)
        }
        return imageUri
    }

    private fun validate(file: MultipartFile) {
        require(!file.isEmpty) { "파일이 비어 있습니다." }

        require(file.size <= maxFileSize) { "파일 크기는 10MB 이하만 허용됩니다." }

        val originalFilename = file.originalFilename
        require(!(originalFilename == null || !originalFilename.contains("."))) { "유효하지 않은 파일명입니다." }

        val fileExtension = getFileExtension(originalFilename)
        require(
            allowedExtensions.contains(fileExtension.lowercase(Locale.getDefault())),
        ) { "지원하지 않는 파일 형식입니다. (jpg, jpeg, png, gif 만 가능)" }

        val contentType = file.contentType ?: ""
        require(contentType.startsWith("image/") || allowedExtensions.contains(getFileExtension(file.originalFilename!!).lowercase())) {
            "이미지 파일만 업로드할 수 있습니다."
        }
    }

    private fun getFileExtension(fileName: String): String {
        val index = fileName.lastIndexOf('.')
        return if (index == -1) "" else fileName.substring(index + 1) // 점을 제외하고 반환
    }

    companion object {
        private val log = LoggerFactory.getLogger(ReviewImageUploader::class.java)
    }
}
