/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kaiyu
 */
public class DeliverySlotEntityNotFoundException extends Exception {

	/**
	 * Creates a new instance of
	 * <code>AccessoryItemEntityNotFoundException</code> without detail message.
	 */
	public DeliverySlotEntityNotFoundException() {
	}

	/**
	 * Constructs an instance of
	 * <code>AccessoryItemEntityNotFoundException</code> with the specified
	 * detail message.
	 *
	 * @param msg the detail message.
	 */
	public DeliverySlotEntityNotFoundException(String msg) {
		super(msg);
	}
}
