package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.ui.LoginWidow;

public class QQClientApp {

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginWidow loginWidow = new LoginWidow();
			}
		});


	}

}
