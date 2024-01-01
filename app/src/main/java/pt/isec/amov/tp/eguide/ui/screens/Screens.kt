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
    LIST_CATEGORIES("ListCategories"),
    REGISTER_POINT_OF_INTEREST("RegisterPointOfInterest"),
    PROFILE("Profile"),
    CONTRIBUTIONS("MyContributions"),
    CREDITS("Credits"),
    CHOOSEWHATTOREGISTER("ChooseWhatToRegister");


    val route : String
        get() = this.toString();
}