package test

import sys.process._
import org.apache.commons.io.FileUtils
import java.net.URL
import java.io.File
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import voiceit2.VoiceIt2

class TestBasics extends FunSuite with BeforeAndAfter {
    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    test("createUser()") {
      val ret = Json.parse(vi.createUser)
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      userId = (ret \ "userId").get.as[String]
    }

    test("getAllUsers()") {
      val ret = Json.parse(vi.getAllUsers)
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("checkUserExists()") {
      val ret = Json.parse(vi.checkUserExists(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    var groupId : String = _
    test("createGroup()") {
      val ret = Json.parse(vi.createGroup("Sample Group Description"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      groupId = (ret \ "groupId").get.as[String]
    }

    test("getAllGroups()") {
      val ret = Json.parse(vi.getAllGroups)
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("getGroup()") {
      val ret = Json.parse(vi.getGroup(groupId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("checkGroupExists()") {
      val ret = Json.parse(vi.checkGroupExists(groupId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("addUserToGroup()") {
      val ret = Json.parse(vi.addUserToGroup(groupId, userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("getGroupsForUser()") {
      val ret = Json.parse(vi.getGroupsForUser(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("removeUserFromGroup()") {
      val ret = Json.parse(vi.removeUserFromGroup(groupId, userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("deleteUser()") {
      val ret = Json.parse(vi.deleteUser(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("deleteGroup()") {
      val ret = Json.parse(vi.deleteGroup(groupId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVideoEnrollments extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _


    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov", "./testenrollmentvideoEnrollmentArmaan1.mov")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentvideoEnrollmentArmaan1.mov"))
    }

    // Create Enrollments

    test("createVideoEnrollment()") {
      val ret = Json.parse(vi.createVideoEnrollment(userId, "en-US", "./testenrollmentvideoEnrollmentArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVideoVerificationIdentification extends FunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov", "./videoVerificationArmaan1.mov")
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov")
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId1)
      vi.deleteAllEnrollmentsForUser(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteUser(groupId)
      FileUtils.deleteQuietly(new File("./videoVerificationArmaan1.mov"))
    }

    // Video Verification
    test("videoVerification()") {
      val ret = Json.parse(vi.videoVerification(userId1, "en-US", "./videoVerificationArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    // Video Identification
    test("videoIdentification()") {
      val ret = Json.parse(vi.videoIdentification(groupId, "en-US", "./videoVerificationArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1)
    }
}

class TestVideoEnrollmentsByUrl extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
    }

    // Create Enrollments

    test("createVideoEnrollmentByUrl()") {
      val ret = Json.parse(vi.createVideoEnrollmentByUrl(userId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVideoVerificationIdentificationByUrl extends FunSuite with BeforeAndAfter {

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
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
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov")
      vi.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov")
      vi.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov")
    }

    
    after {
      vi.deleteAllEnrollmentsForUser(userId1)
      vi.deleteAllEnrollmentsForUser(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteUser(groupId)
    }

    // Video Verification
    test("videoVerificationByUrl()") {
      val ret = Json.parse(vi.videoVerificationByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    // Video Identification
    test("videoIdentificationByUrl()") {
      val ret = Json.parse(vi.videoIdentificationByUrl(groupId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1)
    }
}

class TestGetEnrollments extends FunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _
    var enrollmentId : Int = _
    var faceEnrollmentId : Int = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov", "./testgetenrollmentvideoEnrollmentArmaan1.mov")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov", "./testgetenrollmentvideoEnrollmentArmaan2.mov")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./testgetenrollmentfaceEnrollmentArmaan1.mp4")
      ret = Json.parse(vi.createVideoEnrollment(userId, "en-US", "./testgetenrollmentvideoEnrollmentArmaan1.mov"))
      enrollmentId = (ret \ "id").get.as[Int]
      vi.createVideoEnrollment(userId, "en-US", "./testgetenrollmentvideoEnrollmentArmaan2.mov")
      ret = Json.parse(vi.createFaceEnrollment(userId, "./testgetenrollmentfaceEnrollmentArmaan1.mp4"))
      faceEnrollmentId = (ret \ "faceEnrollmentId").get.as[Int]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testgetenrollmentvideoEnrollmentArmaan1.mov"))
      FileUtils.deleteQuietly(new File("./testgetenrollmentvideoEnrollmentArmaan2.mov"))
      FileUtils.deleteQuietly(new File("./testgetenrollmentfaceEnrollmentArmaan1.mp4"))
    }


    test("getAllEnrollmentsForUser()") {
      val ret = Json.parse(vi.getAllEnrollmentsForUser(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("getFaceEnrollmentsForUser()") {
      val ret = Json.parse(vi.getFaceEnrollmentsForUser(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

}

class TestDeleteEnrollments extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _
    var enrollmentId : Int = _
    var faceEnrollmentId : Int = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov", "./testdeleteenrollmentvideoEnrollmentArmaan1.mov")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov", "./testdeleteenrollmentvideoEnrollmentArmaan2.mov")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./testdeleteenrollmentfaceEnrollmentArmaan1.mp4")
      ret = Json.parse(vi.createVideoEnrollment(userId, "en-US", "./testdeleteenrollmentvideoEnrollmentArmaan1.mov"))
      enrollmentId = (ret \ "id").get.as[Int]
      vi.createVideoEnrollment(userId, "en-US", "./testdeleteenrollmentvideoEnrollmentArmaan2.mov")
      ret = Json.parse(vi.createFaceEnrollment(userId, "./testdeleteenrollmentfaceEnrollmentArmaan1.mp4"))
      faceEnrollmentId = (ret \ "faceEnrollmentId").get.as[Int]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentvideoEnrollmentArmaan1.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentvideoEnrollmentArmaan2.mov"))
      FileUtils.deleteQuietly(new File("./testdeleteenrollmentfaceEnrollmentArmaan1.mp4"))
    }

    // Delete Enrollment for User
    test("deleteEnrollmentForUser()") {
      val ret = Json.parse(vi.deleteEnrollmentForUser(userId, enrollmentId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("deleteFaceEnrollment()") {
      val ret = Json.parse(vi.deleteFaceEnrollment(userId, faceEnrollmentId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    test("deleteAllEnrollmentsForUser()") {
      val ret = Json.parse(vi.deleteAllEnrollmentsForUser(userId))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVoiceEnrollments extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav", "./testenrollmentenrollmentArmaan1.wav")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentenrollmentArmaan1.wav"))
    }

    // Create Enrollments

    test("createVoiceEnrollment()") {
      val ret = Json.parse(vi.createVoiceEnrollment(userId, "en-US", "./testenrollmentenrollmentArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVoiceEnrollmentsByUrl extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    before {
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
    }

    // Create Enrollments

    test("createVoiceEnrollmentByUrl()") {
      val ret = Json.parse(vi.createVoiceEnrollmentByUrl(userId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}

class TestVoiceVerificationIdentification extends FunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId1 : String = _
    var userId2 : String = _
    var groupId : String = _

    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav", "./verificationArmaan1.wav")
      var ret = Json.parse(vi.createUser)
      userId1 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createUser)
      userId2 = (ret \ "userId").get.as[String]
      ret = Json.parse(vi.createGroup("Sample Group Description"))
      groupId = (ret \ "groupId").get.as[String]
      vi.addUserToGroup(groupId, userId1)
      vi.addUserToGroup(groupId, userId2)
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav")
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId1)
      vi.deleteAllEnrollmentsForUser(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteUser(groupId)
      FileUtils.deleteQuietly(new File("./verificationArmaan1.wav"))
    }

    // Video Verification
    test("voiceVerification()") {
      val ret = Json.parse(vi.voiceVerification(userId1, "en-US", "./verificationArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    // Video Identification
    test("voiceIdentification()") {
      val ret = Json.parse(vi.voiceIdentification(groupId, "en-US", "./verificationArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1)
    }
}

class TestVoiceVerificationIdentificationByUrl extends FunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
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
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav")
      vi.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav")
      vi.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav")
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId1)
      vi.deleteAllEnrollmentsForUser(userId2)
      vi.deleteUser(userId1)
      vi.deleteUser(userId2)
      vi.deleteUser(groupId)
    }

      
    // Video Verification
    test("voiceVerificationByUrl()") {
      val ret = Json.parse(vi.voiceVerificationByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

    // Video Identification
    test("voiceIdentificationByUrl()") {
      val ret = Json.parse(vi.voiceIdentificationByUrl(groupId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
      val userId = (ret \ "userId").get.as[String]
      assert(userId === userId1)
    }
}

class TestFaceEnrollments extends FunSuite with BeforeAndAfter {

    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./testenrollmentfaceEnrollmentArmaan1.mp4")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
    }

    after {
      vi.deleteAllEnrollmentsForUser(userId)
      vi.deleteUser(userId)
      FileUtils.deleteQuietly(new File("./testenrollmentfaceEnrollmentArmaan1.mp4"))
    }

    // Create Enrollments

    test("createFaceEnrollment()") {
      val ret = Json.parse(vi.createFaceEnrollment(userId, "./testenrollmentfaceEnrollmentArmaan1.mp4"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 201)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }

}

class TestFaceVerificationIdentification extends FunSuite with BeforeAndAfter {
    def downloadFile(source : String, path : String) {
      new URL(source) #> new File(path) !!
    }

    val viapikey = sys.env("VIAPIKEY")
    val viapitoken = sys.env("VIAPITOKEN")
    var vi = new VoiceIt2(viapikey, viapitoken)
    var userId : String = _

    before {
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./faceEnrollmentArmaan1.mp4")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan2.mp4", "./faceEnrollmentArmaan2.mp4")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan3.mp4", "./faceEnrollmentArmaan3.mp4")
      downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceVerificationArmaan1.mp4", "./faceVerificationArmaan1.mp4")
      var ret = Json.parse(vi.createUser)
      userId = (ret \ "userId").get.as[String]
      vi.createFaceEnrollment(userId, "./faceEnrollmentArmaan1.mp4")
      vi.createFaceEnrollment(userId, "./faceEnrollmentArmaan2.mp4")
      vi.createFaceEnrollment(userId, "./faceEnrollmentArmaan3.mp4")
    }

    after {
      FileUtils.deleteQuietly(new File("./faceEnrollmentArmaan1.mp4"))
      FileUtils.deleteQuietly(new File("./faceEnrollmentArmaan2.mp4"))
      FileUtils.deleteQuietly(new File("./faceEnrollmentArmaan3.mp4"))
      FileUtils.deleteQuietly(new File("./faceVerificationArmaan1.mp4"))
    }

    test("faceVerification()") {
      val ret = Json.parse(vi.faceVerification(userId, "./faceVerificationArmaan1.mp4"))
      val status = (ret \ "status").get.as[Int]
      assert(status === 200)
      val responseCode = (ret \ "responseCode").get.as[String]
      assert(responseCode === "SUCC")
    }
}
