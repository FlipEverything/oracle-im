/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import java.text.BreakIterator;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;

import javax.swing.JDialog;

/** 
 * The IMUIUtil class includes common GUI utilities.
 */
class IMUIUtil implements IMConstants
{
  /** 
   * Returns html-formatted multi-line message.
   * @param sMsg the input string
   * @param nCharInALine the max number of characters per line 
   *         for the output string
   * @return the html-formatted multi-line message if sMsg 
   *         not null; otherwise an empty string
   */
  static String createMultiLineToolTip(String sMsg, int nCharInALine)
  {
    if (sMsg == null)
      return "";

    StringBuffer sbToolTip = new StringBuffer("<html>");

    BreakIterator breakIterator = BreakIterator.getLineInstance();
    breakIterator.setText(sMsg);

    int start = breakIterator.first();
    int end = breakIterator.next();

    int lineLength = 0;
    while (end != BreakIterator.DONE)
    {
      String word = sMsg.substring(start, end);
      lineLength = lineLength + word.length();

      if (lineLength >= nCharInALine) 
      {
        sbToolTip.append("<br>");
        sbToolTip.append(word);
        lineLength = word.length();
      }
      else
        sbToolTip.append(word);

      start = end;
      end = breakIterator.next();
    }

    sbToolTip.append("</html>");

    return sbToolTip.toString();
  }

  /**
   * Sets the JDialog location to the center of the screen and adds
   * the window closing listener.
   */
  static void initJDialogHelper(JDialog jDialog)
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = jDialog.getSize();

    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    jDialog.setLocation((screenSize.width - frameSize.width) / 2, 
        (screenSize.height - frameSize.height) / 2);

    jDialog.addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent e)
          {
            e.getWindow().setVisible(false);
            e.getWindow().dispose();
          }
        });

  } 
}
