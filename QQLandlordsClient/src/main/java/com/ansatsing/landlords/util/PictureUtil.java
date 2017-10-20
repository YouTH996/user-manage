package com.ansatsing.landlords.util;

import javax.swing.ImageIcon;

public class PictureUtil {

	public static ImageIcon getPicture(String name) {
		ImageIcon icon = new ImageIcon(PictureUtil.class.getClassLoader()
				.getResource("com/ansatsing/landlords/client/ui/images/" + name));
		return icon;
	}

}
