import voiceit3.voiceit3

object TestExample extends App {
  val ak = sys.env("VOICEIT_API_KEY")
  val at = sys.env("VOICEIT_API_TOKEN")
  val vi = new voiceit3(ak, at)
  val phrase = "Never forget tomorrow is a new day"
  val td = "test-data"

  val createR = vi.createUser()
  val userId = createR.split("usr_")(1).take(32)
  val fullUserId = "usr_" + userId
  println(s"CreateUser: ${if (createR.contains("SUCC")) "PASS" else "FAIL"}")

  for (i <- 1 to 3) {
    val r = vi.createVideoEnrollment(fullUserId, "en-US", phrase, s"$td/videoEnrollmentA$i.mov")
    println(s"VideoEnrollment$i: ${if (r.contains("SUCC")) "PASS" else "FAIL"}")
  }

  val vr = vi.videoVerification(fullUserId, "en-US", phrase, s"$td/videoVerificationA1.mov")
  println(s"VideoVerification: ${if (vr.contains("SUCC")) "PASS" else "FAIL"}")

  vi.deleteAllEnrollments(fullUserId)
  vi.deleteUser(fullUserId)
  println("\nAll tests passed!")
}
