package com.ansatsing.socket.util;

import javax.swing.ImageIcon;

public class PictureUtil {

	public static ImageIcon getPicture(String name) {
		ImageIcon icon = new ImageIcon(PictureUtil.class.getClassLoader()
				.getResource("com/ansatsing/socket/client/ui/resource/images/" + name));
		return icon;
	}

}
