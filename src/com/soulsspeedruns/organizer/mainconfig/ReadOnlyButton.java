package com.soulsspeedruns.organizer.mainconfig;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JLabel;

import com.soulsspeedruns.organizer.data.OrganizerManager;
import com.soulsspeedruns.organizer.games.Game;
import com.soulsspeedruns.organizer.listeners.ProfileListener;
import com.soulsspeedruns.organizer.listeners.SaveListener;
import com.soulsspeedruns.organizer.listeners.SettingsListener;
import com.soulsspeedruns.organizer.profileconfig.Profile;
import com.soulsspeedruns.organizer.savelist.Save;
import com.soulsspeedruns.organizer.savelist.SaveListEntry;


/**
 * Read Only Button.
 * <p>
 * Button to make the given file read-only and vice-versa.
 *
 * @author Kahmul (www.twitch.tv/kahmul78)
 * @date 26 Sep 2015
 */
public class ReadOnlyButton extends JLabel implements MouseListener, ProfileListener, SaveListener, SettingsListener
{

	private File file;

	/**
	 * Creates a new read only button with the given file and image.
	 * 
	 * @param file the file to associate with this button.
	 * @param isCompact whether to show the "writable/read-only" text
	 */
	public ReadOnlyButton(File file)
	{
		super(OrganizerManager.writableIcon22);

		setFile(file);
		addMouseListener(this);
		OrganizerManager.addProfileListener(this);
		OrganizerManager.addSaveListener(this);
		OrganizerManager.addSettingsListener(this);
		
		setVisible(true);
	}


	/**
	 * Returns the file that is associated with this button.
	 * 
	 * @return the file that is associated with this button
	 */
	public File getFile()
	{
		return file;
	}


	/**
	 * Sets the file to associate this button with.
	 * 
	 * @param file the file to associate this button with
	 */
	public void setFile(File file)
	{
		if (file == null || !file.exists())
		{
			this.file = null;
			setVisible(false);
			setToolTipText(null);
			return;
		}
		this.file = file;
		setVisible(true);
		refreshAppearance(false);
	}


	/**
	 * Simulates a click.
	 */
	public void doClick()
	{
		if (file == null || !file.exists() || !OrganizerManager.getSelectedGame().supportsReadOnly())
			return;
		file.setWritable(!file.canWrite());
		refreshAppearance(true);
	}
	
	
	/**
	 * Changes the image of the read-only button depending on the state of the file and whether the mouse is hovered over it.
	 * 
	 * @param isHovering
	 */
	private void refreshAppearance(boolean isHovering)
	{
		boolean isWritable = file.canWrite();
		boolean isCompact = OrganizerManager.isCompactModeEnabled();
		if(isWritable)
		{			
			setText(!isCompact && !OrganizerManager.isVersionOutdated() ? "Writable" : null);
			setIcon(isHovering ? OrganizerManager.writableIcon22Hover : OrganizerManager.writableIcon22);
			setToolTipText("Click to turn on read-only for the game's savefile.");
		}
		else
		{
			setText(!isCompact && !OrganizerManager.isVersionOutdated() ? "Read-Only" : null);
			setIcon(isHovering ? OrganizerManager.readOnlyIcon22Hover : OrganizerManager.readOnlyIcon22);
			setToolTipText("Click to turn off read-only for the game's savefile.");
		}
		
	}
	
	
	@Override
	public void setVisible(boolean flag)
	{
		super.setVisible(flag);
		
		if(flag)
		{
			if(!OrganizerManager.getSelectedGame().supportsReadOnly())
				super.setVisible(false);
			if(!OrganizerManager.isAProfileSelected())
				super.setVisible(false);
		}

	}

	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		doClick();
	}


	@Override
	public void mouseEntered(MouseEvent e)
	{
		refreshAppearance(true);
	}


	@Override
	public void mouseExited(MouseEvent e)
	{
		refreshAppearance(false);
	}


	@Override
	public void mousePressed(MouseEvent e)
	{
	}


	@Override
	public void mouseReleased(MouseEvent e)
	{
	}


	@Override
	public void profileDeleted(Profile profile)
	{
	}


	@Override
	public void profileCreated(Profile profile)
	{
	}


	@Override
	public void profileDirectoryChanged(Game game)
	{
		setFile(game.getSaveFileLocation());
		if (file == null || !file.exists() || !game.supportsReadOnly())
			setVisible(false);
		else
			setVisible(true);
	}


	@Override
	public void changedToProfile(Profile profile)
	{
	}


	@Override
	public void changedToGame(Game game)
	{
		setFile(game.getSaveFileLocation());
		if (file == null || !file.exists() || !game.supportsReadOnly())
			setVisible(false);
		else
			setVisible(true);
	}


	@Override
	public void entryCreated(SaveListEntry entry)
	{
	}


	@Override
	public void entryRenamed(SaveListEntry entry)
	{
	}


	@Override
	public void entrySelected(SaveListEntry entry)
	{
	}


	@Override
	public void saveLoadStarted(Save save)
	{
	}


	@Override
	public void saveLoadFinished(Save save)
	{
		refreshAppearance(false);
	}


	@Override
	public void gameFileWritableStateChanged(boolean writeable)
	{
		refreshAppearance(false);
	}


	@Override
	public void settingChanged(String prefsKey)
	{
		if(prefsKey.equals(OrganizerManager.PREFS_KEY_SETTING_COMPACT_MODE) || prefsKey.equals(OrganizerManager.PREFS_KEY_SETTING_CHECK_FOR_UPDATES))
			refreshAppearance(false);
	}

}
