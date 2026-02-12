package test

import sys.process._
import org.apache.commons.io.FileUtils
import java.io.IOException
import java.io.PrintWriter
import java.net.URL
import java.io.File
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import voiceit3.VoiceIt3

class TestIO extends AnyFunSuite with BeforeAndAfter {

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)

    var userId : String = _
    test("createVideoEnrollment()") {
      intercept[IOException] {
        vi.createVideoEnrollment(userId, "", "", "not_a_real_file")
      }
    }

    test("createVoiceEnrollment()") {
      intercept[IOException] {
        vi.createVoiceEnrollment(userId, "", "", "not_a_real_file")
      }
    }

    test("createFaceEnrollment()") {
      intercept[IOException] {
        vi.createFaceEnrollment(userId, "not_a_real_file")
      }
    }

    test("videoVerification()") {
      intercept[IOException] {
        vi.videoVerification(userId, "", "", "not_a_real_file")
      }

    }

    test("voiceVerification()") {
      intercept[IOException] {
        vi.voiceVerification(userId, "", "", "not_a_real_file")
      }
    }

    test("faceVerification()") {
      intercept[IOException] {
        vi.faceVerification(userId, "not_a_real_file")
      }
    }

    test("videoIdentification()") {
      intercept[IOException] {
        vi.videoIdentification(userId, "", "", "not_a_real_file")
      }
    }

    test("voiceIdentification()") {
      intercept[IOException] {
        vi.voiceIdentification(userId, "", "", "not_a_real_file")
      }
    }

    test("faceIdentification()") {
      intercept[IOException] {
        vi.faceIdentification(userId, "not_a_real_file")
      }
    }
}

class TestWebhooks extends AnyFunSuite with BeforeAndAfter {
    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)

    if (sys.env("BOXFUSE_ENV") == "voiceittest") {
      new PrintWriter(sys.env("HOME") + "/platformVersion") { write(vi.getVersion()); close }
    }

    vi.addNotificationUrl("https://voiceit.io")
    assert(vi.notificationUrl === "https://voiceit.io", "Webhook URL == https://voiceit.io")
    vi.removeNotificationUrl()
    assert(vi.notificationUrl === "", "Webhook URL == ''")

}

class TestBasics extends AnyFunSuite with BeforeAndAfter {
    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    test("createUser()") {
      val ret = Json.parse(vi.createUser())
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      userId = (ret \ "userId").get.as[String]
    }

    test("getAllUsers()") {
      val ret = Json.parse(vi.getAllUsers)
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("checkUserExists()") {
      val ret = Json.parse(vi.checkUserExists(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    var groupId : String = _
    test("createGroup()") {
      val ret = Json.parse(vi.createGroup("Sample Group Description"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      groupId = (ret \ "groupId").get.as[String]
    }

    test("getGroup()") {
      val ret = Json.parse(vi.getGroup(groupId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("checkGroupExists()") {
      val ret = Json.parse(vi.checkGroupExists(groupId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("addUserToGroup()") {
      val ret = Json.parse(vi.addUserToGroup(groupId, userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("getGroupsForUser()") {
      val ret = Json.parse(vi.getGroupsForUser(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("removeUserFromGroup()") {
      val ret = Json.parse(vi.removeUserFromGroup(groupId, userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("createUserToken()") {
      val ret = Json.parse(vi.createUserToken(userId, 5))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("expireUserTokens()") {
      val ret = Json.parse(vi.expireUserTokens(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("deleteUser()") {
      val ret = Json.parse(vi.deleteUser(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("deleteGroup()") {
      val ret = Json.parse(vi.deleteGroup(groupId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    test("getPhrases()") {
      val ret = Json.parse(vi.getPhrases("en-US"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

}

class TestSubAccount extends AnyFunSuite with BeforeAndAfter {
    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var managedSubAccountAPIKey : String = _
    var unManagedSubAccountAPIKey : String = _

    test("createUnmanagedSubAccount()") {
      val ret = Json.parse(vi.createUnmanagedSubAccount("Test", "Scala", "", "", ""))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      unManagedSubAccountAPIKey = (ret \ "apiKey").get.as[String]
    }

    test("createManagedSubAccount()") {
      val ret = Json.parse(vi.createManagedSubAccount("Test", "Scala", "", "", ""))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      managedSubAccountAPIKey = (ret \ "apiKey").get.as[String]
    }

    test("regenerateSubAccountAPIToken()") {
      val ret = Json.parse(vi.regenerateSubAccountAPIToken(managedSubAccountAPIKey))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }



    test("deleteSubAccount1()") {
      val ret = Json.parse(vi.deleteSubAccount(managedSubAccountAPIKey))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("deleteSubAccount2()") {
      val ret = Json.parse(vi.deleteSubAccount(unManagedSubAccountAPIKey))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

}

class TestGetVideoEnrollments extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./testgetenrollmentvideoEnrollmentB1.mov")
      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testgetenrollmentvideoEnrollmentB1.mov")
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testgetenrollmentvideoEnrollmentB1.mov"))
    }


    test("getAllVideoEnrollments()") {
      val ret = Json.parse(vi.getAllVideoEnrollments(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

}

class TestGetFaceEnrollments extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./testgetenrollmentfaceEnrollmentB1.mp4")
      vi.createFaceEnrollment(userId, "./testgetenrollmentfaceEnrollmentB1.mp4")
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testgetenrollmentfaceEnrollmentB1.mp4"))
    }


    test("getAllFaceEnrollments()") {
      val ret = Json.parse(vi.getAllFaceEnrollments(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

}

class TestGetVoiceEnrollments extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./testgetenrollmentenrollmentA1.wav")
      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testgetenrollmentenrollmentA1.wav")
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testgetenrollmentenrollmentA1.wav"))
    }


    test("getAllVoiceEnrollments()") {
      val ret = Json.parse(vi.getAllVoiceEnrollments(userId))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

}

class TestDeleteEnrollment extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _
    var videoEnrollmentId : Int = _
    var faceEnrollmentId : Int = _
    var voiceEnrollmentId : Int = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./testdeleteenrollmentvideoEnrollmentB1.mov")
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB2.mov", "./testdeleteenrollmentvideoEnrollmentB2.mov")
      downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./testdeleteenrollmentenrollmentA1.wav")
      downloadFile("https://drive.voiceit.io/files/enrollmentA2.wav", "./testdeleteenrollmentenrollmentA2.wav")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./testdeleteenrollmentfaceEnrollmentB1.mp4")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB2.mp4", "./testdeleteenrollmentfaceEnrollmentB2.mp4")
      ret = Json.parse(vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentvideoEnrollmentB1.mov"))
      videoEnrollmentId = (ret \ "id").get.as[Int]
      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentvideoEnrollmentB2.mov")
      ret = Json.parse(vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentenrollmentA1.wav"))
      voiceEnrollmentId = (ret \ "id").get.as[Int]
      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentenrollmentA2.wav")
      ret = Json.parse(vi.createFaceEnrollment(userId, "./testdeleteenrollmentfaceEnrollmentB1.mp4"))
      faceEnrollmentId = (ret \ "faceEnrollmentId").get.as[Int]
      vi.createFaceEnrollment(userId, "./testdeleteenrollmentfaceEnrollmentB2.mp4")
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentvideoEnrollmentB1.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentvideoEnrollmentB2.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentfaceEnrollmentB1.mp4"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentfaceEnrollmentB2.mp4"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentenrollmentA1.wav"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentenrollmentA2.wav"))
    }
}

class TestDeleteEnrollments extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./testdeleteenrollmentsvideoEnrollmentB1.mov")
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB2.mov", "./testdeleteenrollmentsvideoEnrollmentB2.mov")
      downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./testdeleteenrollmentsenrollmentA1.wav")
      downloadFile("https://drive.voiceit.io/files/enrollmentA2.wav", "./testdeleteenrollmentsenrollmentA2.wav")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./testdeleteenrollmentsfaceEnrollmentB1.mp4")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB2.mp4", "./testdeleteenrollmentsfaceEnrollmentB2.mp4")

      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentsvideoEnrollmentB1.mov")
      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentsvideoEnrollmentB2.mov")

      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentsenrollmentA1.wav")
      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteenrollmentsenrollmentA2.wav")

      vi.createFaceEnrollment(userId, "./testdeleteenrollmentsfaceEnrollmentB1.mp4")
      vi.createFaceEnrollment(userId, "./testdeleteenrollmentsfaceEnrollmentB2.mp4")
    }

    after {
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsvideoEnrollmentB1.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsvideoEnrollmentB2.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsenrollmentA1.wav"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsenrollmentA2.wav"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsfaceEnrollmentB1.mp4"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentsfaceEnrollmentB2.mp4"))
    }

}

class TestDeleteAllEnrollments extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./testdeleteallenrollmentsvideoEnrollmentB1.mov")
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB2.mov", "./testdeleteallenrollmentsvideoEnrollmentB2.mov")
      downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./testdeleteallenrollmentsenrollmentA1.wav")
      downloadFile("https://drive.voiceit.io/files/enrollmentA2.wav", "./testdeleteallenrollmentsenrollmentA2.wav")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./testdeleteallenrollmentsfaceEnrollmentB1.mp4")
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB2.mp4", "./testdeleteallenrollmentsfaceEnrollmentB2.mp4")

      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteallenrollmentsvideoEnrollmentB1.mov")
      vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteallenrollmentsvideoEnrollmentB2.mov")

      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteallenrollmentsenrollmentA1.wav")
      vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testdeleteallenrollmentsenrollmentA2.wav")

      vi.createFaceEnrollment(userId, "./testdeleteallenrollmentsfaceEnrollmentB1.mp4")
      vi.createFaceEnrollment(userId, "./testdeleteallenrollmentsfaceEnrollmentB2.mp4")
    }

    after {
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsvideoEnrollmentB1.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsvideoEnrollmentB2.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsenrollmentA1.wav"))
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsenrollmentA2.wav"))
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsfaceEnrollmentB1.mp4"))
      FileUtils.deleteQuietly(new File("./testdeleteallenrollmentsfaceEnrollmentB2.mp4"))
    }

    // Delete All Enrollments for User
    test("deleteAllEnrollments()") {
      vi.deleteAllEnrollments(userId)
      var ret = Json.parse(vi.getAllVideoEnrollments(userId))
      var count = (ret \ "count").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(count === 0, "message: " + message)
      ret = Json.parse(vi.getAllVoiceEnrollments(userId))
      count = (ret \ "count").get.as[Int]
      assert(count === 0, "message: " + message)
      ret = Json.parse(vi.getAllFaceEnrollments(userId))
      count = (ret \ "count").get.as[Int]
      assert(count === 0, "message: " + message)
    }
}


class TestVideoEnrollments extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _


    before {
      downloadFile("https://drive.voiceit.io/files/videoEnrollmentB1.mov", "./testenrollmentvideoEnrollmentB1.mov")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentvideoEnrollmentB1.mov"))
    }

    // Create Video Enrollment
    test("createVideoEnrollment()") {
      val ret = Json.parse(vi.createVideoEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testenrollmentvideoEnrollmentB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}

class TestVideoVerificationIdentification extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      downloadFile("https://drive.voiceit.io/files/videoVerificationB1.mov", "./videoVerificationB1.mov")
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB1.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB2.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB3.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC2.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC3.mov")
    }

    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
      FileUtils.deleteQuietly(new File("./videoVerificationB1.mov"))
    }

    // Video Verification
    test("videoVerification()") {
      val ret = Json.parse(vi.videoVerification(userId1, "en-US", "never forget tomorrow is a new day", "./videoVerificationB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Video Identification
    test("videoIdentification()") {
      val ret = Json.parse(vi.videoIdentification(groupId, "en-US", "never forget tomorrow is a new day", "./videoVerificationB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}

class TestVideoEnrollmentsByUrl extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
    }

    // Create Video Enrollment
    test("createVideoEnrollmentByUrl()") {
      val ret = Json.parse(vi.createVideoEnrollmentByUrl(userId, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}
class TestVideoVerificationIdentificationByUrl extends AnyFunSuite with BeforeAndAfter {

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB1.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB2.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentB3.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC2.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoEnrollmentC3.mov")
    }


    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
    }

    // Video Verification
    test("videoVerificationByUrl()") {
      val ret = Json.parse(vi.videoVerificationByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoVerificationB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Video Identification
    test("videoIdentificationByUrl()") {
      val ret = Json.parse(vi.videoIdentificationByUrl(groupId, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/videoVerificationB1.mov"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}


class TestVoiceEnrollments extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      downloadFile("https://drive.voiceit.io/files/enrollmentA1.wav", "./testenrollmentenrollmentA1.wav")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentenrollmentA1.wav"))
    }

    // Create Voice Enrollment
    test("createVoiceEnrollment()") {
      val ret = Json.parse(vi.createVoiceEnrollment(userId, "en-US", "never forget tomorrow is a new day", "./testenrollmentenrollmentA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}

class TestVoiceEnrollmentsByUrl extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
    }

    // Create Voice Enrollment
    test("createVoiceEnrollmentByUrl()") {
      val ret = Json.parse(vi.createVoiceEnrollmentByUrl(userId, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}

class TestVoiceVerificationIdentification extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      downloadFile("https://drive.voiceit.io/files/verificationA1.wav", "./verificationA1.wav")
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA1.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA2.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA3.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC1.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC2.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC3.wav")
    }

    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
      FileUtils.deleteQuietly(new File("./verificationA1.wav"))
    }

    // Voice Verification
    test("voiceVerification()") {
      val ret = Json.parse(vi.voiceVerification(userId1, "en-US", "never forget tomorrow is a new day", "./verificationA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Voice Identification
    test("voiceIdentification()") {
      val ret = Json.parse(vi.voiceIdentification(groupId, "en-US", "never forget tomorrow is a new day", "./verificationA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}

class TestVoiceVerificationIdentificationByUrl extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA1.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA2.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentA3.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC1.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC2.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/enrollmentC3.wav")
    }

    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
    }


    // Voice Verification By Url
    test("voiceVerificationByUrl()") {
      val ret = Json.parse(vi.voiceVerificationByUrl(userId1, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/verificationA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Voice Identification By Url
    test("voiceIdentificationByUrl()") {
      val ret = Json.parse(vi.voiceIdentificationByUrl(groupId, "en-US", "never forget tomorrow is a new day", "https://drive.voiceit.io/files/verificationA1.wav"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}

class TestFaceEnrollments extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      downloadFile("https://drive.voiceit.io/files/faceEnrollmentB1.mp4", "./testenrollmentfaceEnrollmentB1.mp4")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentfaceEnrollmentB1.mp4"))
    }

    // Create Face Enrollment
    test("createFaceEnrollment()") {
      val ret = Json.parse(vi.createFaceEnrollment(userId, "./testenrollmentfaceEnrollmentB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}

class TestFaceEnrollmentsByUrl extends AnyFunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollments(userId)
      vi.deleteUser(userId)
    }

    // Create Face Enrollment
    test("createFaceEnrollmentByUrl()") {
      val ret = Json.parse(vi.createFaceEnrollmentByUrl(userId, "https://drive.voiceit.io/files/faceEnrollmentB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 201, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }
}

class TestFaceVerificationIdentification extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      downloadFile("https://drive.voiceit.io/files/faceVerificationB1.mp4", "./faceVerificationB1.mp4")
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB1.mp4")
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB2.mp4")
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB3.mp4")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC2.mov")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC3.mov")
    }

    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
      FileUtils.deleteQuietly(new File("./faceVerificationB1.mp4"))
    }

    // Face Verification
    test("faceVerification()") {
      val ret = Json.parse(vi.faceVerification(userId1, "./faceVerificationB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Face Identification
    test("faceIdentification()") {
      val ret = Json.parse(vi.faceIdentification(groupId, "./faceVerificationB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}

class TestFaceVerificationIdentificationByUrl extends AnyFunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt3(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB1.mp4")
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB2.mp4")
      vi.createFaceEnrollmentByUrl(userId1, "https://drive.voiceit.io/files/faceEnrollmentB3.mp4")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
      vi.createFaceEnrollmentByUrl(userId2, "https://drive.voiceit.io/files/videoEnrollmentC1.mov")
    }

    after {
      vi.deleteAllEnrollments(userId1)
      vi.deleteAllEnrollments(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteGroup(groupId)
    }


    // Video Verification
    test("faceVerificationByUrl()") {
      val ret = Json.parse(vi.faceVerificationByUrl(userId1, "https://drive.voiceit.io/files/faceVerificationB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
    }

    // Video Identification
    test("faceIdentificationByUrl()") {
      val ret = Json.parse(vi.faceIdentificationByUrl(groupId, "https://drive.voiceit.io/files/faceVerificationB1.mp4"))
      val status = (ret \ "status").get.as[Int]
      val message = (ret \ "message").get.as[String]
      assert(status === 200, "message: " + message)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC", "message: " + message)
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1, "message: " + message)
    }
}
