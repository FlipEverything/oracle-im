
/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */
public interface IMConstants
{
  /**
   * Column names
   */
  public final static String ID = "PRODUCT_ID";
  public final static String PHOTO = "PRODUCT_PHOTO";
  public final static String PHOTO_THUMB = "PRODUCT_THUMBNAIL";
  public final static String AUDIO = "PRODUCT_AUDIO";
  public final static String VIDEO = "PRODUCT_VIDEO";
  public final static String TEXT = "PRODUCT_TEXT";
  public final static String DOC = "PRODUCT_TESTIMONIALS";
  public final static String CHECK = "CHECK MEDIA";
  public final static String NAME = "PRODUCT_NAME";
  public final static String DESC = "PRODUCT_DESCRIPTION";

  /**
   * Media types
   */
  final static int DEFAULT_TYPE = 0;
  final static int IMG_TYPE = 1;
  final static int AUD_TYPE = 2;
  final static int VID_TYPE = 3;
  final static int DOC_TYPE = 4;

  /**
   * Message types
   */
  final static int ERROR = 1000;
  final static int WARNING = 1001;
  final static int SUGGEST = 1002;

  /**
   * Message dialog titles
   */
  final static String ERROR_TITLE = "Hiba!";
  final static String WARNING_TITLE = "Figyelmeztetés!";
  final static String CONFIRM_TITLE = "Megerősítés";

  final static String CHAR_ENCODING = "ISO-8859-1";

  /**
   * Debug switch
   * If set to true, stack trace is printed out to standard error.
   */
  final static boolean ENABLE_STACK_TRACE = true;
  final static boolean ENABLE_STACK_GUI = true;
}
