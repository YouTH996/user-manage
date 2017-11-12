/**
 * 这个包里的类一开始全部都是实现Runnable接口来作为线程类，最后发现其实有些没有必要以线程的方式来实现；但有些必须要用线程
 * 比如准备倒计时环节；只要涉及通过其他线程来中断自己的线程的流程就必须通过线程来实现！
 */
/**
 *
 * @author ansatsing
 * @time 2017年10月21日 下午9:08:15
 */
package com.ansatsing.landlords.client.thread;