package com.ansatsing.landlords.util;

public class ThreadUtil {
	public static Thread findThreadByName(String threadName){
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threads[i].getName().equals(threadName)) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;		
	}
}
