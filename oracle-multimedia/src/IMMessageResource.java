

/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */
public class IMMessageResource extends java.util.ListResourceBundle 
{
  static final Object[][] contents = 
  {
	{"NO_SUCH_USER", " Hibás felhasználónév / jelszó / nincs ilyen felhasználó."},
	{"LOGIN_SUCCESS", " Sikeres bejelentkezés."},
	{"SIGNUP_SUCCESS", " Sikeres regisztráció."},
	{"SIGNUP_ERROR", " Sikertelen regisztráció."},
	{"ALREADY_RUNNING", " A program már egy példányban fut"},
    {"CANNOT_CONNECT", " Az adatbáziskapcsolat létrehozása sikertelen"},
    {"CHOOSE_SERVER", " Nincs kapcsolat! Válasszon szervert!"},
    {"CONNECTED_TO", " A kapcsolat sikeresen létrehozva: "},
    {"RETRIEVAL_FAILED", " Nem sikerült az Oracle Multimedia objektumokat letölteni"},
    {"UPDATE_FAILED", " Nem sikerült az Oracle Multimedia objektumokat frissíteni"},
    {"FILE_NOTFOUND", " Nem találom a kért fájlt"},
    {"FILE_NORIGHT", " Nincs megfelelő jogosultsága a fájlhoz hozzáférni"},
    {"IO_FAILED", " Sikertelen I/O művelet"},
    {"PLAYER_IO_FAILED", " Sikertelen I/O művelet"},
    {"PLAYER_ERR", " A lejátszó hibába ütközött"},
    {"UNKNOWN_TYPE", " Ismeretlen fájltípus"},
    {"AlREADY_CONNECTED", " Már van működő adatbáziskapcsolat"},
    {"NOT_CONNECTED", " Adja meg az adatokat"},
    {"NO_MEDIA", " A kért médiafájl nem létezik"},
    {"SQL_FAIL", " Sikertelen SQL parancsvégrejhajtás"},
    {"SAVE_FAIL", " Sikertelen fájlmentés"},
    {"MEDIA_SOURCE_ERR", " A média forrásinformációja helytelen"},
    {"MEDIA_NOTRECOGNIZABLE", " Nem támogatott fájlformátum"},
    {"CONNECT_CLOSE_FAIL", " Az adatbáziskapcsolat lezárása sikertelen"},
    {"NO_TABLE", " Nem létező tábla"},
    {"NO_TABLEINFO", " Nem létező tábla információ"},
    {"NO_MIME_CONF", " A MIME konfigurációt nem tudtam megnyitni"},
    {"NO_MIME_PLAYER", " Ehhez a fájltípushoz nincs beállítva lejátszóprogram. Beállít most egyet?"},
    {"NO_PLAYER", " Nem találom a programot. Ellenőrizze az elérési utat."},
    {"APP_ERR", " Alkalmazáshiba"},
    {"NO_RECORD_SELECTED", " Nincs kiválasztva rekord! Válasszon a listából rekordot!"},
    {"EMPTY_FIELD", " Egy vagy több mező üres! Minden mező megadása kötelező!"},
    {"PORT_MUST_BE_NUMBER", " Port csak szám lehet!"},
    {"COLNAME_ERR", " Nincs ilyen mező"},
    {"DEFAULT_PLAYER", " Az alapértelmezett lejátszó: "},
    {"CHANGE_PLAYER", ". Biztosan menti?"},
    {"MAIN_TITLE", "Oracle Multimedia 2013, példaprogram, Dobó László - SZTE-TTIK INF"},
    {"MAIN_DESC", "Szakdolgozat 2013"},
    {"MAIN_MENU_FILE", "Kapcsolat"},
    {"MAIN_MENU_USER", "Felhasználó"},
    {"MAIN_MENU_USER_DESC", ""},
    {"MAIN_MENU_LOGIN", "Bejelentkezés"},
    {"MAIN_MENU_LOGIN_DESC", ""},
    {"MAIN_MENU_LOGOUT", "Kijelentkezés"},
    {"MAIN_MENU_LOGOUT_DESC", ""},
    {"MAIN_MENU_SIGNUP", "Regisztráció"},
    {"MAIN_MENU_SIGNUP_DESC", ""},
    {"MAIN_MENU_LOSTPASS", "Elfelejtett jelszó"},
    {"MAIN_MENU_LOSTPASS_DESC", ""},
    {"MAIN_MENU_PROFILE", "Profil"},
    {"MAIN_MENU_PROFILE_DESC", ""},
    {"MAIN_MENU_SETTINGS", "Beállítások"},
    {"MAIN_MENU_SETTINGS_DESC", ""},
    {"MAIN_MENU_ALBUMS", "Albumok"},
    {"MAIN_MENU_ALBUMS_DESC", ""},
    {"MAIN_MENU_UPLOAD", "Új kép feltöltése"},
    {"MAIN_MENU_UPLOAD_DESC", ""},
    {"MAIN_MENU_ALBUM_NEW", "Új album létrehozása"},
    {"MAIN_MENU_ALBUM_NEW_DESC", ""},
    {"MAIN_MENU_GALLERY", "Galéria"},
    {"MAIN_MENU_GALLERY_DESC", ""},
    {"MAIN_MENU_PICTURES", "Képek"},
    {"MAIN_MENU_PICTURES_DESC", ""},
    {"MAIN_MENU_HOME", "Kezdőoldal"},
    {"MAIN_MENU_HOME_DESC", ""},
    {"MAIN_MENU_USERS", "Felhasználók"},
    {"MAIN_MENU_USERS_DESC", ""},
    {"MAIN_MENU_SEARCH", "Keresés"},
    {"MAIN_MENU_SEARCH_DESC", ""},
    {"MAIN_MENU_NEAR", "Közelben készült"},
    {"MAIN_MENU_NEAR_DESC", ""},
    {"MAIN_MENU_SYSTEM", "Rendszer"},
    {"MAIN_MENU_SYSTEM_DESC", ""},
    {"MAIN_MENU_ABOUT", "Névjegy"},
    {"MAIN_MENU_ABOUT_DESC", ""},
    {"MAIN_MENU_HELP", "Súgó"},
    {"MAIN_MENU_HELP_DESC", ""},
    {"MAIN_MENU_IMPORT", "Import"},
    {"MAIN_MENU_IMPORT_DESC", ""},
    {"MAIN_MENU_EXPORT", "Export"},
    {"MAIN_MENU_EXPORT_DESC", ""},
    {"MAIN_MENU_CONNECT", "Kapcsolódás"},
    {"MAIN_MENU_CONNECT_DESC", "Adatbáziskapcsolat létrehozása"},
    {"MAIN_MENU_DISCONNECT", "Kapcsolat bontása"},
    {"MAIN_MENU_DISCONNECT_DESC", "Adatbáziskapcsolat lezárása"},
    {"MAIN_MENU_EXIT", "Kilépés"},
    {"MAIN_MENU_EXIT_DESC", "Program bezárása"},
    {"MAIN_MENU_HELP", "Segítség"},
    {"MAIN_MENU_HELP_DESC", "Segítség"},
    {"MAIN_MENU_ABOUT", "Névjegy"},
    {"MAIN_TBL_TITLE", "PRODUCT INFORMATION: "},
    {"MAIN_TBL_NAME", "PRODUCT INFORMATION "},
    {"MAIN_TBL_DESC", 
      "The following shows the PRODUCT INFORMATION table with each product in one row. " + 
      "There are four columns in each row. The first column is CHECK_MEDIA which if checked " +
      "pops up another window showing detailed media information for this product. " +
      "The second column shows PRODUCT ID. " +
      "The third column shows PRODUCT NAME. " +
      "The fourth column shows PRODUCT DESCRIPTION."},
    {"LOGINLISTDIAG_TITLE", "Szerverválasztó"},
    {"LOGINDIAG_TITLE", "Új szerver adatainak felvitele"},
    {"LOGINDIAG_DESC", "Enter username, password, hostname, port, SID to login to the database"},
    {"LOGINDIAG_LOGIN_BUTTON", "Kapcsolódás"},
    {"LOGINDIAG_CREATE_BUTTON", "Felvitel"},
    {"LOGINDIAG_LOGIN_BUTTON_DESC", "Bejelentkezés az adatbázisba"},
    {"LOGINDIAG_NEW_BUTTON", "Hozzáadás"},
    {"LOGINDIAG_NEW_BUTTON_DESC", "Új szerver adatainak felvitele"},
    {"LOGINDIAG_DELETE_BUTTON", "Eltávolítás"},
    {"LOGINDIAG_DELETE_BUTTON_DESC", "Adatbázis törlése a listából"},
    {"LOGINDIAG_CANCEL_BUTTON", "Mégsem"},
    {"LOGINDIAG_CANCEL_BUTTON_DESC", "Ablak bezárása"},
    {"LOGINDIAG_EMAIL", "E-mail cím:"},
    {"LOGINDIAG_FIRSTNAME", "Keresztnév:"},
    {"LOGINDIAG_LASTNAME", "Vezetéknév:"},
    {"LOGINDIAG_USERNAME", "Felhasználónév:"},
    {"LOGINDIAG_PASSWD", "Jelszó:"},
    {"LOGINDIAG_HOSTNAME", "Hoszt:"},
    {"LOGINDIAG_PORT", "Port:"},
    {"LOGINDIAG_SID", "SID:"},
    {"LOGINDIAG_DESCRIPTION", "Megnevezés:"},
    {"LOGINDIAG_INFO", "<html><b>Válasszon a listából szervert, majd kattintson a 'Kapcsolódás' gombra!</b><br/>Szerveradatokat módosítani a mezőre való duplakattintással tud!</html>"},
    {"LOGINDIAG_USERNAME_FIELD_DESC", ""},
    {"LOGINDIAG_PASSWD_FIELD_DESC", ""},
    {"LOGINDIAG_HOSTNAME_FIELD_DESC", ""},
    {"LOGINDIAG_PORT_FIELD_DESC", ""},
    {"LOGINDIAG_SID_FIELD_DESC", ""},
    {"LOGINDIAG_DESCRIPTION_FIELD_DESC", ""},
    
    
    {"NEWALBUMDIAG_NAME", "Album neve:"},
    {"NEWALBUMDIAG_IS_PUBLIC", "Publikus-e?"},
    
    
    {"COUNTRY", "Ország"},
    {"REGION", "Megye"},
    {"CITY", "Város"},
    {"SELECT", "Válasszon!"},
    {"MODIFY_SUCCESS", "Sikeres módosítás!"},
    {"SAVE", "Mentés"},
    {"CREATE_SUCCESS", "Sikeres létrehozás!"},
    {"UPLOAD_PROFILE_PIC", "Új profilkép"},
    {"PICTURE_ORIGINAL", "Kép megtekintése eredeti méretben"},
    {"IMAGE_AND_THUMB_UPLOAD_SUCCESS", "Sikeres képfeltöltés! Sikeres bélyegkép generálás!"},
    
    
    {"CHECK_MEDIA_NAME", ""},
    {"CHECK_MEDIA_DESC", "Click mouse or tap SPACE to check media information for product"},
    {"TBL_ID_DESC", "This product ID is "},
    {"TBL_NAME_DESC", "Product name of product "},
    {"TBL_DESC_DESC", "Product description of product "},
    {"PRODDIAG_DESC", "This dialog shows detailed product information. " + 
       "First, it shows product ID, name, and description. " + 
       "Next, it shows product photo, audio, video, and testimonials from top to bottom, with " +
       "each media type taking one panel. For each media type, you can load new media, " +
       "save the media " +
       "to a file, delete the media, or play the media using a player application. " +
       "At the bottom, there are Apply and Revert buttons to commit or cancel the changes made."},
    {"IMG_LOAD_DESC", "Insert or update this product's photo"},
    {"IMG_DELETE_DESC", "Remove this product's photo"},
    {"IMG_PLAY_DESC", "Use a player application to view this product's photo"},
    {"IMG_Save_DESC", "Save this product's photo to disk"},
    {"IMG_LOAD_NAME", "Load photo"},
    {"IMG_DELETE_NAME", "Delete photo"},
    {"IMG_PLAY_NAME", "View photo"},
    {"IMG_Save_NAME", "Save photo"},
    {"AUD_LOAD_DESC", "Insert or update this product's audio"},
    {"AUD_DELETE_DESC", "Remove this product's audio"},
    {"AUD_PLAY_DESC", "Use a player application to play this product's audio"},
    {"AUD_Save_DESC", "Save this product's audio to disk"},
    {"AUD_LOAD_NAME", "Load audio"},
    {"AUD_DELETE_NAME", "Delete audio"},
    {"AUD_PLAY_NAME", "View audio"},
    {"AUD_Save_NAME", "Save audio"},
    {"VID_LOAD_DESC", "Insert or update this product's video"},
    {"VID_DELETE_DESC", "Remove this product's video"},
    {"VID_PLAY_DESC", "Use a player application to play this product's video"},
    {"VID_Save_DESC", "Save this product's video to disk"},
    {"VID_LOAD_NAME", "Load video"},
    {"VID_DELETE_NAME", "Delete video"},
    {"VID_PLAY_NAME", "View video"},
    {"VID_Save_NAME", "Save video"},
    {"DOC_LOAD_DESC", "Insert or update this product's testimonials"},
    {"DOC_DELETE_DESC", "Remove this product's testimonials"},
    {"DOC_PLAY_DESC", "Use a player application to play this product's testimonials"},
    {"DOC_Save_DESC", "Save this product's testimonials to disk"},
    {"DOC_LOAD_NAME", "Load testimonials"},
    {"DOC_DELETE_NAME", "Delete testimonials"},
    {"DOC_PLAY_NAME", "View testimonials"},
    {"DOC_Save_NAME", "Save testimonials"},
    {"SAVE_DIAG_DESC", "Save the media stream to a file"},
    {"LOAD_DIAG_DESC", "Load a media stream from a file to the database"},

    {"GMDIAG_DESC", "This dialog shows a product photo's metadata. "},
    {"GM_FAIL", " Failed to get metadata"},
    {"GMCOMBOBOX_DESC", 
      "This combobox lists types of metadata that can be retrieved from a product photo"},
    {"GMCOMBOBOX_NAME", 
      "Read a product photo's metadata"},

    {"PMDIAG_DESC", "This dialog shows inserting metadata into a product photo. "},
    {"PMDIAG_WRITE_BUTTON", "WRITE"},
    {"PMDIAG_WRITE_BUTTON_DESC", "Write the metadata to the image"},
    {"PMDIAG_CANCEL_BUTTON", "CANCEL"},
    {"PMDIAG_CANCEL_BUTTON_DESC", "Cancel write"},
    {"PM_FAIL", " Failed to write metadata"},
    {"PMCOMBOBOX_DESC", 
      "This combobox lists types of metadata that can be written into a product photo"},
    {"PMCOMBOBOX_NAME", 
      "Write metadata into a product photo"},
  };

  public Object[][] getContents() 
  {
    return contents;
  }
}
