package voiceit2

import scalaj.http._
import java.io.{File, FileInputStream, IOException}
import java.nio.file.{Paths, Files}

class VoiceIt2(val key : String, val token : String) {
  val apikey = key
  val apitoken = token
  val baseUrl : String = "https://api.voiceit.io"
  val header = Seq("platformId" -> "43")
  val connTimeoutMs = 60000
  val readTimeoutMs = 60000

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

  def getAllVoiceEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/voice/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def getAllVideoEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/video/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def getAllFaceEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/face/" + userId).headers(header).auth(apikey, apitoken).asString.body
  }

  def createVoiceEnrollment(userId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return createVoiceEnrollment(userId, lang, phrase, pathToByteArray(filePath))
  }

  def createVoiceEnrollment(userId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/enrollments/voice")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def createVoiceEnrollmentByUrl(userId : String, lang : String, phrase : String, url : String) : String = {
    Http(baseUrl + "/enrollments/voice/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def createFaceEnrollment(userId : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return createFaceEnrollment(userId, pathToByteArray(filePath), false)
  }

  def createFaceEnrollment(userId : String, file : Array[Byte]) : String = {
    return createFaceEnrollment(userId, file, false)
  }

  def createFaceEnrollment(userId : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return createFaceEnrollment(userId, pathToByteArray(filePath), blinkDetection)
  }

  def createFaceEnrollment(userId : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/face")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def createFaceEnrollmentByUrl(userId : String, url : String) : String = {
    return createFaceEnrollmentByUrl(userId, url, false)
  }

  def createFaceEnrollmentByUrl(userId : String, url : String, blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/face/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "fileUrl" -> url))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def createVideoEnrollment(userId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return createVideoEnrollment(userId, lang, phrase, pathToByteArray(filePath), false)
  }

  def createVideoEnrollment(userId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    return createVideoEnrollment(userId, lang, phrase, file, false)
  }

  def createVideoEnrollment(userId : String, lang : String, phrase : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return createVideoEnrollment(userId, lang, phrase, pathToByteArray(filePath), blinkDetection)
  }

  def createVideoEnrollment(userId : String, lang : String, phrase : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def createVideoEnrollmentByUrl(userId : String, lang : String, phrase : String, url : String) : String = {
    return createVideoEnrollmentByUrl(userId, lang, phrase, url, false)
  }

  def createVideoEnrollmentByUrl(userId : String, lang : String, phrase : String, url : String, blinkDetection : Boolean) : String = {
    Http(baseUrl + "/enrollments/video/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteAllEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/all")
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteVideoEnrollment(userId : String, enrollmentId : Int) : String = {
    return Http(baseUrl + "/enrollments/video/" + userId + "/" + String.valueOf(enrollmentId))
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteAllVideoEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/video")
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteVoiceEnrollment(userId : String, enrollmentId : Int) : String = {
    return Http(baseUrl + "/enrollments/voice/" + userId + "/" + String.valueOf(enrollmentId))
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteAllVoiceEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/voice")
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteFaceEnrollment(userId : String, enrollmentId : Int) : String = {
    return Http(baseUrl + "/enrollments/face/" + userId + "/" + String.valueOf(enrollmentId))
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def deleteAllFaceEnrollments(userId : String) : String = {
    return Http(baseUrl + "/enrollments/" + userId + "/face")
      .headers(header).auth(apikey, apitoken).method("DELETE")
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def voiceVerification(userId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return voiceVerification(userId, lang, phrase, pathToByteArray(filePath))
  }

  def voiceVerification(userId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/verification/voice")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def voiceVerificationByUrl(userId : String, lang : String, phrase : String, url : String) : String = {
    Http(baseUrl + "/verification/voice/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def faceVerification(userId : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return faceVerification(userId, pathToByteArray(filePath), false)
  }

  def faceVerification(userId : String, file : Array[Byte]) : String = {
    return faceVerification(userId, file, false)
  }

  def faceVerification(userId : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return faceVerification(userId, pathToByteArray(filePath), blinkDetection)
  }

  def faceVerification(userId : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/verification/face")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def faceVerificationByUrl(userId : String, url : String) : String = {
    return faceVerificationByUrl(userId, url, false)
  }

  def faceVerificationByUrl(userId : String, url : String, blinkDetection : Boolean) : String = {
    Http(baseUrl + "/verification/face/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "fileUrl" -> url))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def videoVerification(userId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return videoVerification(userId, lang, phrase, pathToByteArray(filePath), false)
  }

  def videoVerification(userId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    return videoVerification(userId, lang, phrase, file, false)
  }

  def videoVerification(userId : String, lang : String, phrase : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return videoVerification(userId, lang, phrase, pathToByteArray(filePath), blinkDetection)
  }

  def videoVerification(userId : String, lang : String, phrase : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/verification/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def videoVerificationByUrl(userId : String, lang : String, phrase : String, url : String) : String = {
    return videoVerificationByUrl(userId, lang, phrase, url, false)
  }

  def videoVerificationByUrl(userId : String, lang : String, phrase : String, url : String, blinkDetection : Boolean) : String = {
      Http(baseUrl + "/verification/video/byUrl")
        .headers(header).auth(apikey, apitoken)
        .postForm(Seq("userId" -> userId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
        .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
        .asString.body
  }

  def voiceIdentification(groupId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return voiceIdentification(groupId, lang, phrase, pathToByteArray(filePath))
  }

  def voiceIdentification(groupId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    Http(baseUrl + "/identification/voice")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("recording", "recording", "audio/wav", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def voiceIdentificationByUrl(groupId : String, lang : String, phrase : String, url : String) : String = {
    Http(baseUrl + "/identification/voice/byUrl")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def videoIdentification(groupId : String, lang : String, phrase : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return videoIdentification(groupId, lang, phrase, pathToByteArray(filePath), false)
  }

  def videoIdentification(groupId : String, lang : String, phrase : String, file : Array[Byte]) : String = {
    return videoIdentification(groupId, lang, phrase, file, false)
  }

  def videoIdentification(groupId : String, lang : String, phrase : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return videoIdentification(groupId, lang, phrase, pathToByteArray(filePath), blinkDetection)
  }

  def videoIdentification(groupId : String, lang : String, phrase : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/identification/video")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "phrase" -> phrase))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def videoIdentificationByUrl(groupId : String, lang : String, phrase : String, url : String) : String = {
    return videoIdentificationByUrl(groupId, lang, phrase, url, false)
  }

  def videoIdentificationByUrl(groupId : String, lang : String, phrase : String, url : String, blinkDetection : Boolean) : String = {
      Http(baseUrl + "/identification/video/byUrl")
        .headers(header).auth(apikey, apitoken).postForm(Seq("groupId" -> groupId, "contentLanguage" -> lang, "fileUrl" -> url, "phrase" -> phrase))
        .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
        .asString.body
  }

  def faceIdentification(groupId : String, filePath : String) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return faceIdentification(groupId, pathToByteArray(filePath), false)
  }

  def faceIdentification(groupId : String, file : Array[Byte]) : String = {
    return faceIdentification(groupId, file, false)
  }

  def faceIdentification(groupId : String, filePath : String, blinkDetection : Boolean) : String = {
    if (!Files.exists(Paths.get(filePath))) {
      throw new IOException("File " + filePath + " not found") 
    }
    return faceIdentification(groupId, pathToByteArray(filePath), blinkDetection)
  }

  def faceIdentification(groupId : String, file : Array[Byte], blinkDetection : Boolean) : String = {
    Http(baseUrl + "/identification/face")
      .headers(header).auth(apikey, apitoken)
      .postForm(Seq("groupId" -> groupId))
      .postMulti(MultiPart("video", "video", "video/mp4", file))
      .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
      .asString.body
  }

  def faceIdentificationByUrl(groupId : String, url : String) : String = {
    return faceIdentificationByUrl(groupId, url, false)
  }

  def faceIdentificationByUrl(groupId : String, url : String, blinkDetection : Boolean) : String = {
      Http(baseUrl + "/identification/face/byUrl")
        .headers(header).auth(apikey, apitoken).postForm(Seq("groupId" -> groupId,"fileUrl" -> url))
        .timeout(connTimeoutMs = connTimeoutMs, readTimeoutMs = readTimeoutMs)
        .asString.body
  }

  def getPhrases(contentLanguage : String) : String = {
    return Http(baseUrl + "/phrases/" + contentLanguage).headers(header).auth(apikey, apitoken).asString.body
  }

}
