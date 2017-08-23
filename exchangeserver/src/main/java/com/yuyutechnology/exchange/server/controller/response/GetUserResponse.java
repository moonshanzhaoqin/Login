/**
 * 
 */
package com.yuyutechnology.exchange.server.controller.response;

import com.yuyutechnology.exchange.dto.UserDTO;

/**
 * @author suzan.wu
 *
 */
public class GetUserResponse extends BaseResponse {
	UserDTO userDTO;

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
}
