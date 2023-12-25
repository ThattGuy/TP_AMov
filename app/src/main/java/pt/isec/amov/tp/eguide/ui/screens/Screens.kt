package pt.isec.amov.tp.eguide.ui.screens
enum class Screens(display: String) {
    Login("Login"),
    REGISTER("Register"),
    MAIN("Main"),
    LIST_LOCATIONS("ListLocation"),
    LIST_POINTS_OF_INTEREST("ListPointsOfInterest");

    val route : String
        get() = this.toString();
}