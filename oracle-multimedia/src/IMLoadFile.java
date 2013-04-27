


import java.io.IOException;
import java.io.FileNotFoundException;

import java.awt.Component;

import bean.IMFileChooser;
import bean.Picture;
import bean.User;

import java.sql.SQLException;
import oracle.ord.im.OrdImage;

/** 
 * The IMLoadFile class loads a media stream
 * from a file to the database.
 */
public class IMLoadFile implements IMConstants
{
  private static IMQuery q;
  OrdImage m_img = null;
  OrdImage m_imgThumb = null;
  Object   m_obj = null;

  Component m_parent = null;
  IMFileChooser m_jFileChooser = null;
  private Object m_objUpdated;
  private int returnVal;

  /**
   * Constructs IMLoadFile to load product photo.
   * Initialize the media pointers of the photo and the thumbnail image.
   * @param parent the parent component
   * @param sLabel the dialog title
   * @param img    the media pointer to the photo object, can be null
   * @param imgThumb the media pointer to the thumbnail object, can be null
   */
  public IMLoadFile(Component parent, String sLabel, OrdImage img, 
      OrdImage imgThumb, Object updated)
  {
    m_img = img;
    m_imgThumb = imgThumb;
    m_obj = m_img;
    q = new IMQuery();
    m_objUpdated = updated;

    initFileChooser(parent, sLabel);
  }



  /**
   * Displays the file chooser dialog.
   */
  private void initFileChooser(Component parent, String sLabel)
  {
    m_jFileChooser = new IMFileChooser();
    m_jFileChooser.setDialogTitle(sLabel);
    m_jFileChooser.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("LOAD_DIAG_DESC"));
    m_parent = parent;
    returnVal = m_jFileChooser.showDialog(m_parent, "OK");
  }
  
  public int getReturnVal(){
	  return returnVal;
  }
  
  public void startUpload(){
	      try
	      {
	        loadNewMedia();
	      }
	      catch (FileNotFoundException e)
	      {
	        new IMMessage(IMConstants.ERROR, "FILE_NOTFOUND", e);
	      }
	      catch (SecurityException e)
	      {
	        new IMMessage(IMConstants.ERROR, "FILE_NORIGHT", e);
	      }
	      catch (IOException e)
	      {
	        new IMMessage(IMConstants.ERROR, "IO_FAILED", e);
	      }
	      catch (SQLException e)
	      {
	        new IMMessage(IMConstants.ERROR, "UPDATE_FAILED", e);
	      }
  }

  /**
   * This method takes care of all the preparation work, including
   * inserting a row, initializing the media object if necessary, for 
   * loading/updating a media object. <code>updateMedia</code>
   * does the real update work.
   */
  private void loadNewMedia() throws SQLException, FileNotFoundException, SecurityException, IOException {

	  if (m_obj == null)
	  {
	    q.initOrdImage(m_objUpdated, false);
	  }
	
	  // At this point, there is a row in the online_media table
	  // for this product and the desired media object is initialized.
	  // In the following, we update the media object pointer and 
	  // acquire the right to modify it by selecting again from the
	  // database.
	  //
	  m_img = q.selectOrdImage(m_objUpdated, false);
	
	  // Updates the media object.
	  updateMedia();
  }

  /**
   * Updates the media and also sets the media properties.
   */
  private void updateMedia() throws SQLException, FileNotFoundException, SecurityException, IOException
  {

    boolean isFormatSupported = false;


	m_img.loadDataFromFile(m_jFileChooser.getText());
	isFormatSupported = IMUtil.setProperties(m_img);
    boolean success = q.updateOrdImage(m_objUpdated, m_img, false);

    boolean successThumb = false;
    // Updates the thumbnail image.
    if (isFormatSupported){
    	successThumb = generateThumbnail();
    }
    
    if (m_objUpdated instanceof User){
    	((User)m_objUpdated).setProfilePicture(m_img);
    } else if (m_objUpdated instanceof Picture){
    	((Picture)m_objUpdated).setPicture(m_img);
    }
    
    if (m_objUpdated instanceof User){
      	((User)m_objUpdated).setProfilePictureThumb(m_imgThumb);
      } else if (m_objUpdated instanceof Picture){
    	((Picture)m_objUpdated).setPictureThumbnail(m_imgThumb);
      }

    //((IMImagePanel)m_parent).setMedia(m_img, m_imgThumb);
    //((IMImagePanel)m_parent).refreshPanel(isFormatSupported);
    
    if (success && successThumb){
    	new IMMessage(IMConstants.WARNING, "IMAGE_AND_THUMB_UPLOAD_SUCCESS");
    }
  }
  
  /**
   * Generates and updates the thumbnail image.
   * @param iProdId the product id
   * @param img     the media object pointer for photo. Should not be null.
   * @param imgThumb the media object pointer for the thumbnail image. 
   *                 Can be null.
   * @return imgThumb the updated media object pointer for the thumbnail image
   */
  boolean generateThumbnail() throws SQLException
  {

      if (m_imgThumb == null)
      {
        // The thumbnail media pointer is not initialized.
        // Initializes it first.
    	  q.initOrdImage(m_objUpdated, true);
        
        // Acquires the new pointer and the permission to update.
    	  m_imgThumb = q.selectOrdImage(m_objUpdated, true);

      }

      // Generates the thumbnail image.
      m_img.processCopy("maxScale="+THUMB_WIDTH+" "+THUMB_HEIGHT+", fileFormat=JPEG", m_imgThumb);

      return q.updateOrdImage(m_objUpdated, m_imgThumb, true);

  }
}
