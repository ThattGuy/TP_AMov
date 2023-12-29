package pt.isec.amov.tp.eguide.ui.screens
enum class Screens(display: String) {
    Initialization("initialization"),
    LAYOUT_BARS("Layout"),
    Login("Login"),
    REGISTER("Register"),
    MAIN("Main"),
    LIST_LOCATIONS("ListLocation"),
    REGISTER_LOCATION("RegisterLocation"),
    REGISTER_CATEGORY("RegisterCategory"),
    LIST_POINTS_OF_INTEREST("ListPointsOfInterest"),
    REGISTER_POINT_OF_INTEREST("RegisterPointOfInterest");

    val route : String
        get() = this.toString();
}