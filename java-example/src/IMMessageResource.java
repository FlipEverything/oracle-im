/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */
public class IMMessageResource extends java.util.ListResourceBundle 
{
  static final Object[][] contents = 
  {
    {"CANNOT_CONNECT", " Unable to establish database connection"},
    {"RETRIEVAL_FAILED", " Failed to retrieve Oracle Multimedia objects"},
    {"UPDATE_FAILED", " Failed to update Oracle Multimedia objects"},
    {"FILE_NOTFOUND", " Cannot find required file"},
    {"FILE_NORIGHT", " File permission violated"},
    {"IO_FAILED", " I/O failed"},
    {"PLAYER_IO_FAILED", " I/O failed"},
    {"PLAYER_ERR", " Player failed to play"},
    {"UNKNOWN_TYPE", " Unknown media type"},
    {"AlREADY_CONNECTED", " Connection already established"},
    {"NOT_CONNECTED", " Setup connection first"},
    {"NO_MEDIA", " Media does not exist"},
    {"SQL_FAIL", " Failed database query"},
    {"SAVE_FAIL", " Failed to save media"},
    {"MEDIA_SOURCE_ERR", " Media source information incorrect"},
    {"MEDIA_NOTRECOGNIZABLE", " Unsupported media format"},
    {"CONNECT_CLOSE_FAIL", " Failed to close database connection"},
    {"NO_TABLE", " Failed to retrieve specified table"},
    {"NO_TABLEINFO", " Failed to retrieve table information"},
    {"NO_MIME_CONF", " Failed to open mime configuration file"},
    {"NO_MIME_PLAYER", " No default player for the mime type. Do you want to specify one?"},
    {"NO_PLAYER", " Cannot find player. Check whether your path is set up correctly."},
    {"APP_ERR", " Application error"},
    {"COLNAME_ERR", " Unrecognized column name"},
    {"DEFAULT_PLAYER", " The default player is "},
    {"CHANGE_PLAYER", ". Do you want to use this player?"},
    {"MAIN_TITLE", "Oracle Multimedia Java API Sample Application"},
    {"MAIN_DESC", "Demonstration of Oracle Multimedia Java API"},
    {"MAIN_MENU_FILE", "Connection"},
    {"MAIN_MENU_CONNECT", "Open"},
    {"MAIN_MENU_CONNECT_DESC", "Open a connection to the database"},
    {"MAIN_MENU_DISCONNECT", "Close"},
    {"MAIN_MENU_DISCONNECT_DESC", "Close the connection to the database"},
    {"MAIN_MENU_EXIT", "Exit"},
    {"MAIN_MENU_EXIT_DESC", "Exit the demo"},
    {"MAIN_MENU_HELP", "Help"},
    {"MAIN_MENU_HELP_DESC", "Help information"},
    {"MAIN_MENU_ABOUT", "About"},
    {"MAIN_TBL_TITLE", "PRODUCT INFORMATION: "},
    {"MAIN_TBL_NAME", "PRODUCT INFORMATION "},
    {"MAIN_TBL_DESC", 
      "The following shows the PRODUCT INFORMATION table with each product in one row. " + 
      "There are four columns in each row. The first column is CHECK_MEDIA which if checked " +
      "pops up another window showing detailed media information for this product. " +
      "The second column shows PRODUCT ID. " +
      "The third column shows PRODUCT NAME. " +
      "The fourth column shows PRODUCT DESCRIPTION."},
    {"LOGINDIAG_TITLE", "Login"},
    {"LOGINDIAG_DESC", "Enter username, password, hostname, port, SID to login to the database"},
    {"LOGINDIAG_LOGIN_BUTTON", "LOGIN"},
    {"LOGINDIAG_LOGIN_BUTTON_DESC", "Login to the database"},
    {"LOGINDIAG_CANCEL_BUTTON", "CANCEL"},
    {"LOGINDIAG_CANCEL_BUTTON_DESC", "Cancel login"},
    {"LOGINDIAG_USERNAME", "Username:"},
    {"LOGINDIAG_PASSWD", "Password:"},
    {"LOGINDIAG_HOSTNAME", "Hostname:"},
    {"LOGINDIAG_PORT", "Port:"},
    {"LOGINDIAG_SID", "SID:"},
    {"LOGINDIAG_USERNAME_FIELD_DESC", "Enter username"},
    {"LOGINDIAG_PASSWD_FIELD_DESC", "Enter password"},
    {"LOGINDIAG_HOSTNAME_FIELD_DESC", "Enter hostname"},
    {"LOGINDIAG_PORT_FIELD_DESC", "Enter port"},
    {"LOGINDIAG_SID_FIELD_DESC", "Enter SID"},
    {"CHECK_MEDIA_NAME", "Product media"},
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
