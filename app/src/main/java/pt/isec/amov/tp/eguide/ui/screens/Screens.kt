package pt.isec.amov.tp.eguide.ui.screens
enum class Screens(display: String) {
    Login("Login"),
    REGISTER("Register"),
    MAIN("Main"),
    LIST_LOCATIONS("ListLocation");

    val route : String
        get() = this.toString();
}