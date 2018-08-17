package voiceit2

import scalaj.http._
import java.io.File
import java.io.FileInputStream
// import java.io.BufferedOutputStream
// import java.io.FileOutputStream

class VoiceIt2(val key : String, val token : String) {
  val apikey = key
  val apitoken = token
  val baseUrl : String = "https://api.voiceit.io"
  val header = Seq("platformId" -> "43")

  def pathToByteArray(path : String) : Array[Byte] = {
    val is = new FileInputStream(path)
    val cnt = is.available
    val bytes = Array.ofDim[Byte](cnt)
    is.read(bytes)
    is.close()
    return bytes
  }

  def getAllUsers() : String = {
    return Http(baseUrl + "/users").headers(header).auth(apikey, apitoken).asString.body
  }

  def createUser() : String = {
    return Http(baseUrl + "/users").headers(header).auth(apikey, apitoken).postMulti().asString.body
  }

  def checkUserExists(userId : String) : String = {
    return Http(baseUrl + "/users/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def deleteUser(userId : String) : String = {
    return Http(baseUrl + "/users/" + userId).headers(header).auth(apikey, apitoken).method("DELETE").asString.body
  }

  def getGroupsForUser(userId : String) : String = {
    return Http(baseUrl + "/users/" + userId + "/groups").headers(header).auth(apikey, apitoken).asString.body
  }


  def getAllGroups() : String = {
    return Http(baseUrl + "/groups").headers(header).auth(apikey, apitoken).asString.body
  }

  def getGroup(groupId : String) : String = {
    return Http(baseUrl + "/groups/" + groupId).headers(header).auth(apikey, apitoken).asString.body
  }

  def checkGroupExists(groupId : String) : String = {
    return Http(baseUrl + "/groups/" + groupId + "/exists").headers(header).auth(apikey, apitoken).asString.body
  }

  def createGroup(description : String) : String = {
    return Http(baseUrl + "/groups").headers(header).auth(apikey, apitoken).postMulti(MultiPart("description", "description", "text/plain", description)).asString.body
  }

  def addUserToGroup(groupId : String, userId : String) : String = {
    return Http(baseUrl + "/groups/addUser").headers(header).auth(apikey, apitoken).postForm(Seq("groupId" -> groupId, "userId" -> userId)).method("PUT").asString.body
  }

  def removeUserFromGroup(groupId : String, userId : String) : String = {
    return Http(baseUrl + "/groups/removeUser").headers(header).auth(apikey, apitoken).postForm(Seq("groupId" -> groupId, "userId" -> userId)).method("PUT").asString.body
  }

  def deleteGroup(groupId : String) : String = {
    return Http(baseUrl + "/groups/" + groupId).headers(header).auth(apikey, apitoken).method("DELETE").asString.body
  }

  def getAllEnrollmentsForUser(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def getFaceEnrollmentsForUser(userId : String) : String = {
    return Http(baseUrl + "/enrollments/face/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def createVoiceEnrollment(userId : String, lang : String, filePath : String) : String = {
    return createVoiceEnrollment(userId, lang, pathToByteArray(filePath))
  }

  def createVoiceEnrollment(userId : String, lang : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/enrollments")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def createVoiceEnrollmentByUrl(userId : String, lang : String, url : String) : String = {
    Http(baseUrl + "/enrollments/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def createFaceEnrollment(userId : String, filePath : String) : String = {
    return createFaceEnrollment(userId, pathToByteArray(filePath), false)
  }

  def createFaceEnrollment(userId : String, file : Array[Byte]) : String = {
    return createFaceEnrollment(userId, file, false)
  }

  def createFaceEnrollment(userId : String, filePath : String, blinkDetection : Boolean) : String = {
    return createFaceEnrollment(userId, pathToByteArray(filePath), blinkDetection)
  }

  def createFaceEnrollment(userId : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/face")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "doBlinkDetection" -> String.valueOf(blinkDetection)))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def createVideoEnrollment(userId : String, lang : String, filePath : String) : String = {
    return createVideoEnrollment(userId, lang, pathToByteArray(filePath), false)
  }

  def createVideoEnrollment(userId : String, lang : String, file : Array[Byte]) : String = {
    return createVideoEnrollment(userId, lang, file, false)
  }

  def createVideoEnrollment(userId : String, lang : String, filePath : String, blinkDetection : Boolean) : String = {
    return createVideoEnrollment(userId, lang, pathToByteArray(filePath), blinkDetection)
  }

  def createVideoEnrollment(userId : String, lang : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection)))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000).asString.body
  }

  def createVideoEnrollmentByUrl(userId : String, lang : String, url : String) : String = {
    return createVideoEnrollmentByUrl(userId, lang, url, false)
  }

  def createVideoEnrollmentByUrl(userId : String, lang : String, url : String, blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/video/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection), "fileUrl" -> url))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def deleteAllEnrollmentsForUser(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/all")
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def deleteFaceEnrollment(userId : String, faceId : Int) : String = {
    return Http(baseUrl + "/enrollments/face/" + userId + "/" + String.valueOf(faceId))
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def deleteEnrollmentForUser(userId : String, enrollmentId : Int) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/" + String.valueOf(enrollmentId))
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def voiceVerification(userId : String, lang : String, filePath : String) : String = {
    return voiceVerification(userId, lang, pathToByteArray(filePath))
  }

  def voiceVerification(userId : String, lang : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/verification")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def voiceVerificationByUrl(userId : String, lang : String, url : String) : String = {
    Http(baseUrl + "/verification/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def faceVerification(userId : String, filePath : String) : String = {
    return faceVerification(userId, pathToByteArray(filePath), false)
  }

  def faceVerification(userId : String, file : Array[Byte]) : String = {
    return faceVerification(userId, file, false)
  }

  def faceVerification(userId : String, filePath : String, blinkDetection : Boolean) : String = {
    return faceVerification(userId, pathToByteArray(filePath), blinkDetection)
  }

  def faceVerification(userId : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/verification/face")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "doBlinkDetection" -> String.valueOf(blinkDetection)))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def videoVerification(userId : String, lang : String, filePath : String) : String = {
    return videoVerification(userId, lang, pathToByteArray(filePath), false)
  }

  def videoVerification(userId : String, lang : String, file : Array[Byte]) : String = {
    return videoVerification(userId, lang, file, false)
  }

  def videoVerification(userId : String, lang : String, filePath : String, blinkDetection : Boolean) : String = {
    return videoVerification(userId, lang, pathToByteArray(filePath), blinkDetection)
  }

  def videoVerification(userId : String, lang : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/verification/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection)))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def videoVerificationByUrl(userId : String, lang : String, url : String) : String = {
    return videoVerificationByUrl(userId, lang, url, false)
  }

  def videoVerificationByUrl(userId : String, lang : String, url : String, blinkDetection : Boolean) : String = {
      Http(baseUrl + "/verification/video/byUrl")
        .headers(header).auth(apikey, apitoken)
        .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection), "fileUrl" -> url))
        .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
        .asString.body
  }

  def voiceIdentification(groupId : String, lang : String, filePath : String) : String = {
    return voiceIdentification(groupId, lang, pathToByteArray(filePath))
  }

  def voiceIdentification(groupId : String, lang : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/identification")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def voiceIdentificationByUrl(groupId : String, lang : String, url : String) : String = {
    Http(baseUrl + "/identification/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "fileUrl" -> url))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def videoIdentification(groupId : String, lang : String, filePath : String) : String = {
    return videoIdentification(groupId, lang, pathToByteArray(filePath), false)
  }

  def videoIdentification(groupId : String, lang : String, file : Array[Byte]) : String = {
    return videoIdentification(groupId, lang, file, false)
  }

  def videoIdentification(groupId : String, lang : String, filePath : String, blinkDetection : Boolean) : String = {
    return videoIdentification(groupId, lang, pathToByteArray(filePath), blinkDetection)
  }

  def videoIdentification(groupId : String, lang : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/identification/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection)))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
      .asString.body
  }

  def videoIdentificationByUrl(groupId : String, lang : String, url : String) : String = {
    return videoIdentificationByUrl(groupId, lang, url, false)
  }

  def videoIdentificationByUrl(groupId : String, lang : String, url : String, blinkDetection : Boolean) : String = {
      Http(baseUrl + "/identification/video/byUrl")
        .headers(header).auth(apikey, apitoken).postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "doBlinkDetection" -> String.valueOf(blinkDetection), "fileUrl" -> url))
        .timeout(connTimeoutMs = 1000000, readTimeoutMs = 5000000)
        .asString.body
  }

}
