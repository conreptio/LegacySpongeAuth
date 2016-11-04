package form

import javax.inject.Inject

import db.UserDBO
import external.{GitHubApi, MojangApi}
import form.api.CreateUserForm
import play.api.data.Form
import play.api.data.Forms._
import security.SpongeAuthConfig

/**
  * A collection of forms used by Sponge SSO.
  */
final class SpongeAuthForms @Inject()(override val config: SpongeAuthConfig,
                                      override val users: UserDBO,
                                      override val mojang: MojangApi,
                                      override val gitHub: GitHubApi) extends Constraints {

  val Api = new Api

  /**
    * The form submitted upon log in.
    */
  lazy val LogIn = Form(mapping(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText
  )(LogInForm.apply)(LogInForm.unapply))

  /**
    * The form submitted upon sign up.
    */
  lazy val SignUp = Form(mapping(
    "email" -> email.unique(_.email),
    "username" -> username,
    "password" -> password,
    "2fa" -> optional(boolean),
    "mc-username" -> minecraftUsername,
    "irc-nick" -> ircNick,
    "gh-username" -> gitHubUsername
  )(SignUpForm.apply)(SignUpForm.unapply))

  /**
    * The form submitted to resend a confirmation email.
    */
  lazy val ResendConfirmationEmail = Form(single("email" -> email))

  /**
    * The form submitted to verify a user with 2FA using a Time-based one
    * time password.
    */
  lazy val VerifyTotp = Form(single("totp" -> totp))

  /**
    * The form submitted to send an email where a user can reset their
    * password.
    */
  lazy val SendPasswordReset = Form(single("username" -> nonEmptyText))

  /**
    * The form submitted to reset a user's password.
    */
  lazy val ResetPassword = Form(single("password" -> password))

  final class Api {

    /**
      * The form data submitted to create a new user via the API
      */
    lazy val CreateUser = Form(mapping(
      "email" -> email.unique(_.email),
      "username" -> username,
      "password" -> password,
      "mc-username" -> minecraftUsername,
      "irc-nick" -> ircNick,
      "gh-username" -> gitHubUsername,
      "api-key" -> apiKey
    )(CreateUserForm.apply)(CreateUserForm.unapply))

  }

}
