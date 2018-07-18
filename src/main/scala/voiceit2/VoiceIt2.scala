package voiceit2

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ Authorization, BasicHttpCredentials }
import java.nio.file.{Files, Paths}

import scala.concurrent.Future
import scala.util.{ Failure, Success }


class VoiceIt2(val key : String, val token : String) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val auth = Authorization(BasicHttpCredentials(key, token))
  val baseUrl : String = "https://api.voiceit.io"
  val headers = RawHeader("platformId", "43")

  def getAllUsers(callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/users"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getAllUsers()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createUser(callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/users"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def checkUserExists(userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/users/" + userId
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method checkUserExist()\", \"responseCode\": \"GERR\"}")
      }
  }

  def deleteUser(userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.DELETE,
      uri = baseUrl + "/users/" + userId
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method deleteUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def getGroupsForUser(userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/users/" + userId + "/groups"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getGroupForUser()\", \"responseCode\": \"GERR\"}")
      }
  }


  def getAllGroups(callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/groups"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getAllGroups()\", \"responseCode\": \"GERR\"}")
      }
  }


  def getGroup(groupId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/groups/" + groupId
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getGroup()\", \"responseCode\": \"GERR\"}")
      }
  }

  def checkGroupExists(groupId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/groups/" + groupId + "/exists"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method checkGroupExists()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createGroup(description : String, callback : String => Unit) {

    val formData = Multipart.FormData(
    Multipart.FormData.BodyPart("description", description))

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/groups",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createGroup()\", \"responseCode\": \"GERR\"}")
      }
  }

  def addUserToGroup(groupId : String, userId : String, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("groupId", groupId),
      Multipart.FormData.BodyPart("userId", userId),
    )

    val request = HttpRequest(
      method = HttpMethods.PUT,
      uri = baseUrl + "/groups/addUser",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method addUserToGroup()\", \"responseCode\": \"GERR\"}")
      }
  }

  def removeUserFromGroup(groupId : String, userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.PUT,
      uri = baseUrl + "/groups/removeUser",
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method removeUserFromGroup()\", \"responseCode\": \"GERR\"}")
      }
  }


  def deleteGroup(groupId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.DELETE,
      uri = baseUrl + "/groups/" + groupId,
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method deleteGroup()\", \"responseCode\": \"GERR\"}")
      }
  }

  def getAllEnrollmentsForUser(userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/enrollments/" + userId,
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getAllEnrollmentsForUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def getFaceEnrollmentsForUser(userId : String, callback : String => Unit) {

    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = baseUrl + "/enrollments/face/" + userId,
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method getFaceEnrollmentsForUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createVoiceEnrollment(userId : String, lang : String, filePath : String, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("recording", HttpEntity(MediaTypes.`audio/wav`, byteArray), Map("filename" -> "recording.wav")),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/enrollments",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createVoiceEnrollment()\", \"responseCode\": \"GERR\"}")
      }
  }


  def createVoiceEnrollmentByUrl(userId : String, lang : String, url : String, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/enrollments/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createVoiceEnrollmentByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createFaceEnrollment(userId : String, filePath : String, blinkDetection : Boolean, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("video", HttpEntity(MediaTypes.`application/octet-stream`, byteArray), Map("filename" -> "video.mp4")),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/enrollments/face",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createFaceEnrollment()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createVideoEnrollment(userId : String, lang : String, filePath : String, blinkDetection : Boolean, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("video", HttpEntity(MediaTypes.`application/octet-stream`, byteArray), Map("filename" -> "video.mp4")),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/enrollments/video",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createVideoEnrollment()\", \"responseCode\": \"GERR\"}")
      }
  }

  def createVideoEnrollmentByUrl(userId : String, lang : String, url : String, blinkDetection : Boolean, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/enrollments/video/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method createVideoEnrollmentByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }

  def deleteAllEnrollmentsForUser(userId : String, callback : String => Unit) {
    val request = HttpRequest(
      method = HttpMethods.DELETE,
      uri = baseUrl + "/enrollments/" + userId + "/all"
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method deleteAllEnrollmentsForUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def deleteFaceEnrollment(userId : String, faceId : Int, callback : String => Unit) {
    val request = HttpRequest(
      method = HttpMethods.DELETE,
      uri = baseUrl + "/enrollments/face/" + userId + "/" + faceId.toString
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method deleteaceEnrollment()\", \"responseCode\": \"GERR\"}")
      }
  }

  def deleteEnrollmentForUser(userId : String, enrollmentId : Int,  callback : String => Unit) {
    val request = HttpRequest(
      method = HttpMethods.DELETE,
      uri = baseUrl + "/enrollments/" + userId + "/" + enrollmentId.toString
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method deleteEnrollmentForUser()\", \"responseCode\": \"GERR\"}")
      }
  }

  def voiceVerification(userId : String, lang : String, filePath : String, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("recording", HttpEntity(MediaTypes.`audio/wav`, byteArray), Map("filename" -> "recording.wav")),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/verification",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method voiceVerification()\", \"responseCode\": \"GERR\"}")
      }
  }

  def voiceVerificationByUrl(userId : String, lang : String, url : String, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/verification/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method voiceVerificationByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }


  def faceVerification(userId : String, filePath : String, blinkDetection : Boolean, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("video", HttpEntity(MediaTypes.`audio/wav`, byteArray), Map("filename" -> "video.mp4")),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/verification/face",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method voiceVerification()\", \"responseCode\": \"GERR\"}")
      }
  }

  def videoVerification(userId : String, lang : String, filePath : String, blinkDetection : Boolean, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("video", HttpEntity(MediaTypes.`application/octet-stream`, byteArray), Map("filename" -> "video.mp4")),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/verification/video",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method videoVerification()\", \"responseCode\": \"GERR\"}")
      }
  }

  def videoVerificationByUrl(userId : String, lang : String, url : String, blinkDetection : Boolean, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("userId", userId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/verification/video/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method videoVerificationByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }

  def voiceIdentification(groupId : String, lang : String, filePath : String, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("groupId", groupId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("recording", HttpEntity(MediaTypes.`audio/wav`, byteArray), Map("filename" -> "recording.wav")),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/identification",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method voiceIdentification()\", \"responseCode\": \"GERR\"}")
      }
  }

  def voiceIdentificationByUrl(groupId : String, lang : String, url : String, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("groupId", groupId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/identification/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method voiceIdentificationByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }

  def videoIdentification(groupId : String, lang : String, filePath : String, blinkDetection : Boolean, callback : String => Unit) {

    val byteArray = Files.readAllBytes(Paths.get(filePath))
    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("groupId", groupId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("video", HttpEntity(MediaTypes.`application/octet-stream`, byteArray), Map("filename" -> "video.mp4")),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/identification/video",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method videoIdentification()\", \"responseCode\": \"GERR\"}")
      }
  }

  def videoIdentificationByUrl(groupId : String, lang : String, url : String, blinkDetection : Boolean, callback : String => Unit) {

    val formData = Multipart.FormData(
      Multipart.FormData.BodyPart("groupId", groupId),
      Multipart.FormData.BodyPart("contentLanguage", lang),
      Multipart.FormData.BodyPart("fileUrl", url),
      Multipart.FormData.BodyPart("doBlinkDetection", String.valueOf(blinkDetection)),
    )

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = baseUrl + "/identification/video/byUrl",
      entity = formData.toEntity()
    ).withHeaders(auth, headers)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture
      .onComplete {
        case Success(response) => Unmarshal(response.entity).to[String].map {
          jsonString => callback(jsonString)
        }
        case Failure(_) => callback("{\"message\": \"Error running HTTP method videoIdenfiticationByUrl()\", \"responseCode\": \"GERR\"}")
      }
  }

}
